package kr.ac.uos.ai.ieas.gateway.gatewayController;

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

import kr.ac.uos.ai.ieas.resource.ITransmitter;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasAddress;


public class GatewayTransmitter implements ITransmitter
{
	private static final String QUEUE_HEADER = "queue://";
	
	
	private GatewayController controller;

	private Connection connection;
	private Session session;
	
	private MessageProducer queueProducer;	
	private MessageProducer topicProducer;
	
	private Map<String, MessageConsumer> messageConsumerMap;
//	private MessageConsumer alerterConsumer;
//	private MessageConsumer alertsystemConsumer;
	
	private String mqServerIp;

	
	


	public GatewayTransmitter(GatewayController controller)
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
		this.setQueueReceiver(KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
		this.setQueueReceiver(KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
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
	public void setQueueReceiver(String queueDestination)
	{		
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination destination = session.createQueue(queueDestination);
			MessageConsumer consumer = session.createConsumer(destination);
			
			messageConsumerMap.put(queueDestination, consumer);
			
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
	public void setTopicReceiver(String topicDestination)
	{		
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try 
		{
			Destination destination = this.session.createTopic(topicDestination);
			System.out.println("gw to alerter Dest : " + destination);
			MessageConsumer consumer = session.createConsumer(destination);
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						try
						{
							TextMessage textMessage = (TextMessage) message;
							String text = textMessage.getText();
						
							System.out.println("GateWay receive topic message : " + text);
//							controller.acceptMessage(textMessage.getText());	//Message Receive			
						}
						catch (JMSException e)
						{
							e.printStackTrace();
						}
					}
				}
			};
			consumer.setMessageListener(listener);
		}
		catch (Exception e)
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
	public void stopConnection()
	{
		try
		{
			this.connection.stop();
			System.out.println("Gateway Connection Stop");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendQueueMessage(String message, String queueDestination) 
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination destination = this.session.createQueue(queueDestination);
			this.queueProducer = this.session.createProducer(destination);
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
	
	@Override
	public void sendTopicMessage(String message, String topicDestination)
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination destination = this.session.createTopic(topicDestination);
			this.topicProducer = this.session.createProducer(destination);
			this.topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.topicProducer.send(textMessage);			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}




