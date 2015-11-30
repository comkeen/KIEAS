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


	public AlertSystemTransmitter(AlertSystemController alertSystem)
	{
		this.alertSystem = alertSystem;
		this.geoCode = "";
		this.alertSystemType = "";

		openConnection();
	}

	private void openConnection()
	{
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(KieasAddress.ACTIVEMQ_SERVER_IP);
			this.connection = factory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		setGeoCodeTopicListener(geoCode);
		setAlertSystemTypeTopicListener(alertSystemType);
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
			Destination queueDestination = this.session.createQueue(destination);
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

	public void setGeoCodeTopicListener(String location)
	{
		try
		{
			Destination destination = this.session.createTopic(location);
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

	public void setAlertSystemTypeTopicListener(String alertSystemType)
	{
		try
		{
			Destination destination = this.session.createTopic(alertSystemType);
			this.alertSystemTypeConsumer = session.createConsumer(destination);
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
	
	public void selectTopic(String topic)
	{
		try 
		{
			session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("Creating session with topic: " + topic);
			setGeoCodeTopicListener(topic);
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}	
	}
}
