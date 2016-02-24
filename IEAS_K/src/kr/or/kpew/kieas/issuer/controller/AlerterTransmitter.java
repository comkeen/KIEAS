package kr.or.kpew.kieas.issuer.controller;

import java.util.HashMap;
import java.util.Map;

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

import kr.or.kpew.kieas.common.ITransmitter;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.issuer.model._AlerterModel;


public class AlerterTransmitter implements ITransmitter
{
	private _AlerterModel modelManager;

	private Connection connection;
	private Session session;
	
	private MessageProducer queueProducer;
	
	private Map<String, MessageConsumer> messageConsumerMap;

	private String mqServerIp;
	private String id;


	public AlerterTransmitter(_AlerterModel modelManager)
	{
		this.modelManager = modelManager;
		
		init();
	}
	
	private void init()
	{
		this.mqServerIp = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;
		this.id = KieasAddress.GATEWAY_TO_ALERTER_QUEUE_DESTINATION;	//default
		this.messageConsumerMap = new HashMap<String, MessageConsumer>();
		
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
		this.addReceiver(id);
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
	public void sendMessage(String message, String destination)
	{
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try
		{
			Destination queueDestination = this.session.createQueue(destination);
			System.out.println("alerter to gw dest : " + destination);
			this.queueProducer = this.session.createProducer(queueDestination);
			queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			queueProducer.send(textMessage);
			queueProducer.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void addReceiver(String myDestination)
	{
		this.id = myDestination;
		
		if(connection == null || session == null)
		{
			System.out.println("Could not found JMS Connection");
			return;
		}
		
		try 
		{
			Destination queueDestination = this.session.createQueue(id);
			MessageConsumer consumer = session.createConsumer(queueDestination);
			messageConsumerMap.put(myDestination, consumer);
			System.out.println("gw to alerter Dest : " + queueDestination);
			
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
							modelManager.acceptMessage(textMessage.getText());	//Message Receive			
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
	public void removeReceiver(String target)
	{
		try
		{
			messageConsumerMap.get(target).close();
			messageConsumerMap.remove(target);
		} 
		catch (JMSException e)
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

	@Override
	public void sendTopicMessage(String message, String topic)
	{
		try {
			Destination destination = this.session.createTopic(topic);
			MessageProducer topicProducer = this.session.createProducer(destination);
			topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			topicProducer.send(textMessage);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
}
