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
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.network.IClientTransmitter;


public class IssuerTransmitter implements IClientTransmitter
{
	private IOnMessageHandler handler;
	
	private Connection connection;
	private Session session;
	
	private MessageProducer queueProducer;	

	private String id;
	private String mqServerIp;
	private String destination;


	private Destination here;

	
	public IssuerTransmitter()
	{
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
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
		catch (Exception e)
		{
			System.out.println("AO: Could not found MQ Server : " + mqServerIp);
//			e.printStackTrace();
			return;
		}
		this.addReceiver(id);
	}
	
	@Override
	public void close()
	{
		try 
		{
			if(connection != null || session == null)
			{
				session.close();
				connection.close();			
			}
			System.out.println("AO: Connection Closed");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
		
	@Override
	public void send(String message)
	{
		if(connection == null || session == null)
		{
			System.out.println("AO: Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination queueDestination = this.session.createQueue(destination);
			this.queueProducer = this.session.createProducer(queueDestination);
			queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			System.out.println("AO: Issuer to gw dest : " + destination);
			
			TextMessage textMessage = this.session.createTextMessage(message);
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
			System.out.println("AO: Could not found JMS Connection");
			return;
		}
		
		try 
		{
//			Destination queueDestination = here;//this.session.createQueue(id);
			MessageConsumer consumer = session.createConsumer(here);
			System.out.println("AO: gw to alerter Dest : " + here);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try
						{
							System.out.println("AO: received message");
							handler.onMessage(KieasAddress.GATEWAY_ID, textMessage.getText());	//Received Message Handler
							return;
						}
						catch (JMSException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						System.out.println("AO: Received None Validate Message");
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
