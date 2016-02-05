package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

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

import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasAddress;

public class AlertSystemTransmitter
{
	private AlertSystemController alertSystem;

	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer geoCodeConsumer;
	private MessageConsumer alertSystemTypeConsumer;

	private String geoCode;
	private String alertSystemType;
	private String alertSystemId;

	private MessageConsumer queueConsumer;


	public AlertSystemTransmitter(AlertSystemController alertSystem)
	{
		this.alertSystem = alertSystem;
		
		openConnection();
	}

	public void openConnection()
	{
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL);
			this.connection = factory.createConnection();
			connection.start();
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
//		setGeoCodeTopicListener(geoCode);
//		setAlertSystemTypeTopicListener(alertSystemType);
	}
	
	public void setGeoCode(String geoCode)
	{
		this.geoCode = geoCode;
	}

	public void setAlertSystemType(String alertSystemType)
	{
		this.alertSystemType = alertSystemType;
	}

	
	public void closeConnection()
	{
		try
		{
			if(connection != null)
			{
				connection.close();			
			}
			System.out.println("AlertSystem Connection Close");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message, String destination)
	{
		try
		{
			Destination queueDestination = session.createQueue(destination);
			this.producer = this.session.createProducer(queueDestination);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.producer.send(textMessage);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setQueueListener(String queue)
	{
		try
		{
			System.out.println("queue destination : " + queue);
			Destination destination = session.createQueue(queue);
			this.queueConsumer = session.createConsumer(destination);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try 
						{
							alertSystem.acceptMessage(textMessage.getText());
						}
						catch (JMSException e)
						{
							e.printStackTrace();
						}
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
	
	public void setGeoCodeTopicListener(String topic)
	{
		try
		{
			Destination destination = session.createTopic(topic);
			this.geoCodeConsumer = session.createConsumer(destination);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try 
						{
							alertSystem.acceptMessage(textMessage.getText());
						}
						catch (JMSException e)
						{
							e.printStackTrace();
						}
					}
				}
			};
			geoCodeConsumer.setMessageListener(listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setAlertSystemTypeTopicListener(String topic)
	{
		try
		{
			System.out.println("Creating session with topic : " + topic);
			Destination alertSystemTypeDestination = this.session.createTopic(topic);
			this.alertSystemTypeConsumer = session.createConsumer(alertSystemTypeDestination);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try 
						{
							alertSystem.acceptMessage(textMessage.getText());
						}
						catch (JMSException e)
						{
							e.printStackTrace();
						}
					}
				}
			};
			alertSystemTypeConsumer.setMessageListener(listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void selectGeoCodeTopic(String topic)
	{	
		try
		{
			session.close();
			session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			setGeoCodeTopicListener(topic);	
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}		
	}
	
	public void selectAlertSystemTypeTopic(String topic)
	{	
		try
		{
			session.close();
			session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			setAlertSystemTypeTopicListener(topic);	
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}		
	}

	public void setId(String id)
	{
		this.alertSystemId = id;

		setQueueListener(alertSystemId);
	}
}
