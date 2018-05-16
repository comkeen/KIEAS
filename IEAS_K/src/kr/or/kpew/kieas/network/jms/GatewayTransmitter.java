package kr.or.kpew.kieas.network.jms;

import javax.jms.*;

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.network.ITransmitter;

import org.apache.activemq.ActiveMQConnectionFactory;


public class GatewayTransmitter implements ITransmitter
{
	private IOnMessageHandler handler;

	private Connection connection;
	private Session session;

	private MessageProducer queueProducer;	

	private String mqServerIp;
	
	
	@Override
	public void init(String destination)
	{
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
		
		open();
		addReceiver(destination);
	}
	
	@Override
	public void open()
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
	}
	
	@Override
	public void close()
	{
		try 
		{
			if(connection != null && session != null)
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
	
	@Override
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
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;
						try 
						{
							handler.onMessage(textMessage.getText());
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
			if(address != null)
			{
				Destination queueDestination = this.session.createQueue(address);
				this.queueProducer = this.session.createProducer(queueDestination);
				queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				TextMessage textMessage = this.session.createTextMessage(message);

				queueProducer.send(textMessage);
			}
			else
			{
				System.out.println("GW: Could not connect to system");
			}
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
		open();
	}

	@Override
	public void setOnMessageHandler(IOnMessageHandler handler) 
	{
		this.handler = handler;
	}
}