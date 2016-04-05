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
import kr.or.kpew.kieas.network.ITransmitter;


public class IssuerTransmitter implements ITransmitter
{
	private IOnMessageHandler handler;

	private Connection connection;
	private Session session;

	private MessageProducer queueProducer;	
	private Destination queueConsumerDestination;

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
	public void addReceiver(String myDestination)
	{
		if(connection == null || session == null)
		{
			System.out.println("AO: Could not found JMS Connection");
			return;
		}

		try 
		{
			this.queueConsumerDestination = session.createQueue(myDestination);
			MessageConsumer queueConsumer = session.createConsumer(queueConsumerDestination);

			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try
						{
							handler.onMessage(textMessage.getText());	//Received Message Handler
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
			queueConsumer.setMessageListener(listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void sendTo(String target, String message)
	{
		if(connection == null || session == null)
		{
			System.out.println("AO: Could not found JMS Connection");
			return;
		}

		try
		{
			Destination queueProducerDestination = this.session.createQueue(target);
			this.queueProducer = this.session.createProducer(queueProducerDestination);
			
			queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			TextMessage textMessage = this.session.createTextMessage(message);
			textMessage.setJMSReplyTo(queueProducerDestination);

			queueProducer.send(textMessage);
			queueProducer.close();
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
	public void setOnMessageHandler(IOnMessageHandler handler)
	{
		this.handler = handler;
	}
}
