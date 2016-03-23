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
import kr.or.kpew.kieas.common.IntegratedEmergencyAlertSystem;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.network.IClientTransmitter;


public class IssuerTransmitter implements IClientTransmitter
{
	private Connection connection;
	private Session session;
	
	private MessageProducer queueProducer;
	
	private Map<String, MessageConsumer> messageConsumerMap;

	private String mqServerIp;

	private String id;
	private String destination;

	private IOnMessageHandler handler;

	private Destination here;

	public IssuerTransmitter()
	{
		System.out.println("Transmitter instantiated");
		
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
		this.messageConsumerMap = new HashMap<String, MessageConsumer>();
	}
	
	@Override
	public void init(String id ,String destination)
	{
		this.id = id;
		this.destination  = destination;
		
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqServerIp);
			this.connection = factory.createConnection();
			connection.start();
			
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			here = session.createQueue(id);
		}
		catch (Exception ex)
		{
			System.out.println("Could not found MQ Server : " + mqServerIp);
//			ex.printStackTrace();
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
			System.out.println("Issuer Connection Closed");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
		
	@Override
	public void send(byte[] message)
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination queueDestination = this.session.createQueue(destination);
			System.out.println("alerter to gw dest : " + destination);
			this.queueProducer = this.session.createProducer(queueDestination);
			queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(new String(message));
			textMessage.setJMSReplyTo(here);

			queueProducer.send(textMessage);
			queueProducer.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addReceiver(String myDestination)
	{
		this.id = myDestination;
		
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try 
		{
//			Destination queueDestination = here;//this.session.createQueue(id);
			MessageConsumer consumer = session.createConsumer(here);
			messageConsumerMap.put(myDestination, consumer);
			System.out.println("gw to alerter Dest : " + here);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try
						{
							System.out.println("alerter receive message");
							handler.onMessage(KieasAddress.GATEWAY_ID, IntegratedEmergencyAlertSystem.stringToByte(textMessage.getText()));	//Message Receive			
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
	
	public void setMqServer(String ip)
	{
		this.mqServerIp = ip;
		
		close();
	}

	@Override
	public void setOnMessageHandler(IOnMessageHandler handler) {
		this.handler = handler;
	}

}
