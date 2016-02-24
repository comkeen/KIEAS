package kr.or.kpew.kieas.alertsystem;

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

import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;

public class AlertSystemTransmitter
{
	private AlertSystemModel model;

	private Connection connection;
	private Session session;
	
	private MessageProducer producer;
	private MessageConsumer geoCodeConsumer;
	private MessageConsumer alertSystemTypeConsumer;
	private MessageConsumer queueConsumer;

	

	public AlertSystemTransmitter(AlertSystemModel alertSystem)
	{
		this.model = alertSystem;
		
		//openConnection();
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
	}
	

<<<<<<< HEAD

=======
	public void setAlertSystemType(String alertSystemType)
	{
		this.alertSystemType = alertSystemType;
		setAlertSystemTypeTopicListener(alertSystemType);
	}
>>>>>>> 5b8b750e45383dc4a0462a12d6999202edf8f6a1
	
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
		//System.out.println("send message: " + message);
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
		System.out.println("set queue listener: "+ queue);
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
							model.acceptMessage(textMessage.getText());
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
		System.out.println("set geo listener: "+ topic);

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
							model.acceptMessage(textMessage.getText());
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
		System.out.println("set listener: "+ topic);

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
							model.acceptMessage(textMessage.getText());
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
		//this.alertSystemId = id;

		setQueueListener(id);
	}
}
