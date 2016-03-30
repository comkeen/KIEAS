package kr.or.kpew.kieas.network.jms;

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
	private Destination here;

	private String mqServerIp;
	private String destination;

	
	@Override
	public void init(String id, String destination)
	{
		this.destination = destination;
		
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL);
			this.connection = factory.createConnection();
			connection.start();
			
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			here = session.createQueue(destination);
		}
		catch (Exception e)
		{
			System.out.println("AO: Could not found MQ Server : " + mqServerIp);
			return;
		}
		this.setReceiver(destination);
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
	
	public void setReceiver(String myDestination)
	{
		if(connection == null || session == null)
		{
			System.out.println("AO: Could not found JMS Connection");
			return;
		}
		
		try 
		{
			MessageConsumer consumer = session.createConsumer(here);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try
						{
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
