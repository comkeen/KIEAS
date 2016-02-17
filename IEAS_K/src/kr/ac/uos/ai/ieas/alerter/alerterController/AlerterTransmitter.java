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

import kr.ac.uos.ai.ieas.resource.ITransmitter;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasAddress;


public class AlerterTransmitter implements ITransmitter
{
	private _AlerterController controller;

	private Connection connection;
	private Session session;
	
	private MessageProducer producer;
	private MessageConsumer consumer;

	private String mqServerIp;
	private String id;


	public AlerterTransmitter(_AlerterController controller)
	{
		this.controller = controller;
		
		init();
	}
	
	private void init()
	{
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
		this.id = KieasAddress.GATEWAY_TO_ALERTER_QUEUE_DESTINATION;	//default
		
		openConnection();
	}	
	
	@Override
	public void openConnection()
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
//			ex.printStackTrace();
			System.out.println("Could not found MQ Server : " + mqServerIp);
			return;
		}
		this.setQueueReceiver(id);
	}
	
	@Override
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
	
	@Override
	public void stopConnection()
	{
		try
		{
			this.connection.stop();
			System.out.println("Gateway Connection Stop");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendQueueMessage(String message, String queueDestination) 
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination destination = this.session.createQueue(queueDestination);
			System.out.println("alerter to gw dest : " + destination);
			this.producer = this.session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			producer.send(textMessage);
			producer.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void sendTopicMessage(String message, String topicDestination)
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination destination = this.session.createQueue(topicDestination);
			System.out.println("alerter to gw dest : " + destination);
			this.producer = this.session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			producer.send(textMessage);
			producer.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void setQueueReceiver(String id)
	{
		this.id = id;
		
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
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
							controller.acceptMessage(textMessage.getText());	//Message Receive			
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
	public void setTopicReceiver(String topicDestination)
	{		
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try 
		{
			Destination destination = this.session.createTopic(topicDestination);
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
							System.out.println("(" + id + ") receive topic message");
							controller.acceptMessage(textMessage.getText());	//Message Receive			
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
	public void setMqServer(String ip)
	{
		this.mqServerIp = ip;
		
		closeConnection();
		openConnection();
	}

}
