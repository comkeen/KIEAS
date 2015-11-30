package kr.ac.uos.ai.ieas.gateway.gatewayController;

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


public class GatewayTransmitter 
{
	private GatewayController gateway;

	private ActiveMQConnectionFactory factory;
	public Connection connection;
	public Session session;

	private MessageProducer queueProducer;	
	private MessageProducer topicProducer;

	private String MqServerIP;


	public GatewayTransmitter(GatewayController controller)
	{
		this.gateway = controller;
		this.MqServerIP = KieasAddress.ACTIVEMQ_SERVER_IP;

		openConnection();
	}

	private void openConnection()
	{
		try
		{
			this.factory = new ActiveMQConnectionFactory(MqServerIP);
			this.connection = factory.createConnection();
			startConnection();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		setQueueListener();
	}
	
	public void startConnection()
	{
		try
		{
			this.connection.start();
			System.out.println("Gateway Connection Start");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}		
	}

	public void closeConnection()
	{
		try
		{
			if(connection != null)
			{
				connection.close();			
			}
			System.out.println("Gateway Connection Close");
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}

	public void stopConnection()
	{
		try
		{
			this.connection.stop();
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendQueueMessage(String message, String destination)
	{
		try
		{
			Destination queueDestination = this.session.createQueue(destination);
			this.queueProducer = this.session.createProducer(queueDestination);
			this.queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.queueProducer.send(textMessage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void broadcastMessage(String message) {
		try {
			Destination destination = this.session.createTopic(KieasAddress.GATEWAY_TOPIC_DESTINATION);
			this.topicProducer = this.session.createProducer(destination);
			this.topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.topicProducer.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendTopicMessage(String message, String topic)
	{
		try
		{
			Destination destination = this.session.createTopic(topic);
			this.topicProducer = this.session.createProducer(destination);
			this.topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.topicProducer.send(textMessage);			
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
			Destination alerterQueueDestination = session.createQueue(KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
			MessageConsumer alerterConsumer = session.createConsumer(alerterQueueDestination);
			Destination alertsystemQueueDestination = session.createQueue(KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
			MessageConsumer alertsystemConsumer = session.createConsumer(alertsystemQueueDestination);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;
						try 
						{
							System.out.println("gateway received message : " + textMessage.getText());
							gateway.acceptAleterMessage(textMessage.getText());
							return;
							
//							if (message.getJMSDestination().toString().equals("queue://" + KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION))
//							{
//								gateway.acceptAleterMessage(textMessage.getText());
//								return;
//							}
//							else if (message.getJMSDestination().toString().equals("queue://"  + KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION))
//							{
//								gateway.acceptAletSystemMessage(textMessage.getText());
//								return;
//							}
//							else
//							{
//								gateway.acceptAleterMessage(textMessage.getText());
//								return;
//							}
							
						}
						catch (JMSException e)
						{
							e.printStackTrace();
						}					
					}
				}
			};
			//register to eventListener
			alerterConsumer.setMessageListener(listener);
			alertsystemConsumer.setMessageListener(listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}




