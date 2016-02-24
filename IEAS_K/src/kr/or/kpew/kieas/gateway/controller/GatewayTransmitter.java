package kr.or.kpew.kieas.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import kr.or.kpew.kieas.common.ITransmitter;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;


public class GatewayTransmitter implements ITransmitter
{
	private static final String QUEUE_HEADER = "queue://";
		
	private _GatewayController controller;

	private Connection connection;
	private Session session;
	
	private MessageProducer queueProducer;	
	private Map<String, MessageConsumer> messageConsumerMap;
	
	private String mqServerIp;

	
	public GatewayTransmitter(_GatewayController controller)
	{
		this.controller = controller;

		init();
	}
	
	private void init()
	{
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;		
		this.messageConsumerMap = new HashMap<String, MessageConsumer>();
		
		openConnection();		
	}
	
	@Override
	public void openConnection()
	{
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqServerIp);
			this.connection = factory.createConnection();
			connection.start();
			
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		catch (Exception ex)
		{
//			ex.printStackTrace();
			System.out.println("Could not found MQ Server : " + mqServerIp);
			return;
		}
		this.addReceiver(KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
		this.addReceiver(KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
	}
	
	@Override
	public void closeConnection()
	{
		try 
		{
			if(connection != null)
			{
				session.close();
				connection.close();			
			}
			System.out.println("Alerter Connection Close");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
		
	@Override
	public void addReceiver(String destination)
	{		
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination queueDestination = session.createQueue(destination);
			MessageConsumer consumer = session.createConsumer(queueDestination);
			
			messageConsumerMap.put(destination, consumer);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;
						try 
						{
							if (message.getJMSDestination().toString().equals(QUEUE_HEADER + KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION))
							{
								controller.acceptAleterMessage(textMessage.getText());
								return;
							}
							else if (message.getJMSDestination().toString().equals(QUEUE_HEADER  + KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION))
							{
								controller.acceptAletSystemMessage(textMessage.getText());
								return;
							}
							else
							{
								controller.acceptAleterMessage(textMessage.getText());
								return;
							}
							
						}
						catch (JMSException e)
						{
							e.printStackTrace();
						}					
					}
				}
			};
			//register to eventListener
			consumer.setMessageListener(listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void removeReceiver(String target)
	{
		try
		{
			messageConsumerMap.get(target).close();
			messageConsumerMap.remove(target);
		} 
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
		
	@Override
	public void setMqServer(String ip)
	{
		this.mqServerIp = ip;
		
		closeConnection();
		openConnection();
	}
	
	@Override
	public void sendMessage(String message, String destination) 
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination queueDestination = this.session.createQueue(destination);
			this.queueProducer = this.session.createProducer(queueDestination);
			queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			queueProducer.send(textMessage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

//	public void broadcastMessage(String message, String topic)
//	{
//		try {
//			Destination destination = this.session.createTopic(topic);
//			this.topicProducer = this.session.createProducer(destination);
//			this.topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//			TextMessage textMessage = this.session.createTextMessage(message);
//
//			this.topicProducer.send(textMessage);
//		} 
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}	
}




