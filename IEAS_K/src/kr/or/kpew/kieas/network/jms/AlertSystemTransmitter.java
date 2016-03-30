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

public class AlertSystemTransmitter implements IClientTransmitter
{

	private Connection connection;
	private Session session;
	
	private MessageProducer producer;

	
	private MessageConsumer consumer;

	IOnMessageHandler handler;
	
	private Destination here;
	
	private String destination;


	@Override
	public void init(String id, String destination)
	{
		this.destination = destination;
		
		String mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqServerIp);
			this.connection = factory.createConnection();
			connection.start();
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			here = session.createQueue(this.hashCode()+"");
		}
		catch (Exception ex) 
		{
			System.out.println("AS: Could not found MQ Server : " + mqServerIp);
//			ex.printStackTrace();
			return;
		}
		
		setListener(id);
	}
	
	public void setListener(String queue)
	{
		try
		{
			System.out.println("AS: as session with : " + queue);
			Destination here = this.session.createQueue(queue);
			this.consumer = session.createConsumer(here);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try 
						{
							handler.onMessage(KieasAddress.GATEWAY_ID, textMessage.getText());
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
	public void send(String message)
	{
		try
		{
			Destination queueDestination = session.createQueue(destination);
			this.producer = this.session.createProducer(queueDestination);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			TextMessage textMessage = this.session.createTextMessage(message);
			textMessage.setJMSReplyTo(here);

			this.producer.send(textMessage);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public void setOnMessageHandler(IOnMessageHandler handler) {
		this.handler = handler;
	}

}
