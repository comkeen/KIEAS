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

	private String id;

	private String mqServerIp;


	public AlerterTransmitter(_AlerterController controller)
	{
		this.controller = controller;
		
		init();
	}
	
	private void init()
	{
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
		this.id = KieasAddress.GATEWAY_TO_ALERTER_QUEUE_DESTINATION;	//default
		
		createConnection(mqServerIp);
	}
	
	private void createConnection(String mqServerIp)
	{
		try
		{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqServerIp);
			this.connection = factory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		createConsumer(id);
	}
	
	private void destroyConnection()
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
	
	private void createProducer(String message, String destination)
	{
		try
		{			
			Destination queueDestination = this.session.createQueue(KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
			System.out.println("alerter to gw dest : " + queueDestination);
			this.producer = this.session.createProducer(queueDestination);
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
	
	private void createConsumer(String queueName)
	{
		try 
		{
			Destination destination = this.session.createQueue(queueName);
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
	public void openConnection()
	{
		createConnection(mqServerIp);
	}
	
	@Override
	public void closeConnection()
	{
		destroyConnection();
	}
	
	@Override
	public void sendMessage(String message, String destination) 
	{
		createProducer(message, destination);
	}
	
	@Override
	public void setReceiver(String id)
	{
		this.id = id;
		createConsumer(id);
	}
	
	@Override
	public void setMqServer(String ip)
	{
		this.mqServerIp = ip;
		
		destroyConnection();
		createConnection(ip);
	}
}
