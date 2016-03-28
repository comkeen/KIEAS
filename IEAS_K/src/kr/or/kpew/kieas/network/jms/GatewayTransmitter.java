package kr.or.kpew.kieas.network.jms;

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

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.common.KieasConfiguration;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.network.IServerTransmitter;


public class GatewayTransmitter implements IServerTransmitter
{
	private static final String QUEUE_HEADER = "queue://";

	private Connection connection;
	private Session session;
	
	private MessageProducer queueProducer;	
	private MessageProducer topicProducer;	
	private Map<String, MessageConsumer> messageConsumerMap;

	private String id;
	private String mqServerIp;

	private IOnMessageHandler handler;
	
	
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
			System.out.println("GW: Could not found MQ Server : " + mqServerIp);
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
			System.out.println("GW: Connection Close");
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
			System.out.println("GW: Could not found JMS Connection");
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
							handler.onMessage(sender,textMessage.getText());
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
	public void sendTo(String address, String message) 
	{
		if(connection == null || session == null)
		{
			System.out.println("GW: Could not found JMS Connection");
			return;
		}

		try
		{
			if(address == null)
			{
				System.out.println("GW: not connected system: " + address);
			}
			Destination queueDestination = this.session.createQueue(address);
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
	
	public void setMqServer(String ip)
	{
		this.mqServerIp = ip;
		
		close();
		openConnection();
	}

	@Override
	public void setOnReceiveHandler(IOnMessageHandler handler) 
	{
		this.handler = handler;
	}

	@Override
	public void waitForClient()
	{
		addReceiver(KieasAddress.GATEWAY_TOPIC_DESTINATION);		
	}
}




