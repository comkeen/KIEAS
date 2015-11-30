package kr.ac.uos.ai.ieas.alerter.alerterController;

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


public class AlerterTransmitter 
{
	private _AlerterController controller;

	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;

	private String id;

	private String MqServerIP;


	public AlerterTransmitter(_AlerterController controller)
	{
		this.controller = controller;
		this.MqServerIP = KieasAddress.ACTIVEMQ_SERVER_IP;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public void openConnection()
	{
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(MqServerIP);
			this.connection = factory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		setQueueListener();
	}

	public void closeConnection()
	{
		try 
		{
			if(connection != null)
			{
				session.close();
				connection.close();			
			}
			System.out.println("Alerter Connection Close");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) 
	{
		try
		{			
			Destination queueDestination = this.session.createQueue(KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
			System.out.println("alerter to gw dest : " + queueDestination);
			this.producer = this.session.createProducer(queueDestination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			producer.send(textMessage);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void setQueueListener()
	{
		try 
		{
			Destination destination = this.session.createQueue(id);
			System.out.println("gw to alerter Dest : " + destination);
			this.consumer = session.createConsumer(destination);
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
							controller.acceptMessage(textMessage.getText());				
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

	/*
	private void setTopicListener()
	{
		try {
			Destination destination = this.session.createTopic(GATEWAY_TOPIC_DESTINATION);
			MessageConsumer consumer = session.createConsumer(destination);
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;

						try
						{
							alerter.acceptMessage(textMessage.getText());							
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
	*/
}
