package kr.or.kpew.kieas.network.jms;

import javax.jms.*;

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.network.ITransmitter;

import org.apache.activemq.ActiveMQConnectionFactory;

public class AlertSystemTransmitter implements ITransmitter
{
	private IOnMessageHandler handler;
	
	private Connection connection;
	private Session session;
	
	private MessageProducer producer;
	private Destination here;
	
	private String mqServerIp;
	
	

	@Override
	public void init(String destination)
	{
		mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
		
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
			here = session.createQueue(this.hashCode()+"");
		}
		catch (Exception e) 
		{
			System.out.println("AS: Could not found MQ Server : " + mqServerIp);
			return;
		}
	}
	
	@Override
	public void close()
	{
		try
		{
			if(connection != null)
			{
				connection.close();			
			}
			System.out.println("AS: Connection Close");
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
			System.out.println("AS: As session with : " + destination);
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
	public void sendTo(String target, String message)
	{
		try
		{
			Destination queueDestination = session.createQueue(target);
			this.producer = this.session.createProducer(queueDestination);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			TextMessage textMessage = this.session.createTextMessage(message);
			textMessage.setJMSReplyTo(here);

			this.producer.send(textMessage);
			producer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public void setOnMessageHandler(IOnMessageHandler handler)
	{
		this.handler = handler;
	}
}
