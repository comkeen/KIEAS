package kr.or.kpew.kieas.network.jms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.common.IntegratedEmergencyAlertSystem;
import kr.or.kpew.kieas.common.Item;
import kr.or.kpew.kieas.common.KieasConfiguration;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.network.IServerTransmitter;


public class GatewayTransmitter implements IServerTransmitter
{
	private static final String QUEUE_HEADER = "queue://";

	private String id;

	private Connection connection;
	private Session session;
	
	private MessageProducer queueProducer;	
	private MessageProducer topicProducer;	
	private Map<String, MessageConsumer> messageConsumerMap;
	
	private String mqServerIp;

	private IOnMessageHandler handler;
	
	List<Item> alertsystems = new ArrayList<>();
	
	public GatewayTransmitter()
	{
	}
	
	@Override
	public void init(String id)
	{
		this.id = id;
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;		
		this.messageConsumerMap = new HashMap<String, MessageConsumer>();
		
		openConnection();		

		
	}
	
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
			System.out.println("Could not found MQ Server : " + mqServerIp);
			return;
		}
		
		this.addReceiver(id);
	}
	
	@Override
	public void close()
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
							String queue = message.getJMSReplyTo().toString();
							String sender = queue.replace(QUEUE_HEADER, "");
							handler.onMessage(sender, IntegratedEmergencyAlertSystem.stringToByte(textMessage.getText()));
							/*
							if (message.getJMSDestination().toString().equals(QUEUE_HEADER + KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION))
							{
								
								handler.onMessage(sender, IntegratedEmergencyAlertSystem.stringToByte(textMessage.getText()));
//								model.onMessageFromIssuer(textMessage.getText());
								return;
							}
							else if (message.getJMSDestination().toString().equals(QUEUE_HEADER  + KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION))
							{
								System.out.println("g: msg from a");
								handler.onMessage(KieasTcpProtocol.SenderType.AlertSystem.toString(), IntegratedEmergencyAlertSystem.stringToByte(textMessage.getText()));
								return;
							}
							else
							{
								System.out.println("unknown sender");
								handler.onMessage(KieasTcpProtocol.SenderType.Issuer.toString(), IntegratedEmergencyAlertSystem.stringToByte(textMessage.getText()));
								return;
							}
							*/
							
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
	public void sendTo(String id, byte[] message) 
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}

		try
		{
			String address = getAddress(id); 
			if(address == null) {
				System.out.println("not connected system: " + id);
			}
			Destination queueDestination = this.session.createQueue(address);
			this.queueProducer = this.session.createProducer(queueDestination);
			queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(new String(message));

			queueProducer.send(textMessage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void broadcast(byte[] message)
	{
		String topic = KieasConfiguration.KieasAddress.GATEWAY_TOPIC_DESTINATION;
		System.out.println("gateway topic send : " + topic);
		try
		{
			Destination destination = this.session.createTopic(topic);
			this.topicProducer = this.session.createProducer(destination);
			topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(new String(message));

			topicProducer.send(textMessage);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getAddress(String id) {
		return id;
		
//		for (Item item : alertsystems) {
//			if(item.getKey().equals(id))
//				return item.getValue();
//		}
//		return null;
	}
	
	public void setMqServer(String ip)
	{
		this.mqServerIp = ip;
		
		close();
		openConnection();
	}

	@Override
	public void setOnReceiveHandler(IOnMessageHandler handler) {
		this.handler = handler;
	}

	@Override
	public void waitForClient() {
		addReceiver(KieasAddress.GATEWAY_TOPIC_DESTINATION);		
	}
	
	

}




