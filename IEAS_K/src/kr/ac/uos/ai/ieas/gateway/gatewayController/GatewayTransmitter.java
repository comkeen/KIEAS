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
	private GatewayController controller;

	public Connection connection;
	public Session session;
	private MessageProducer queueProducer;	
	private MessageProducer topicProducer;

	private MessageConsumer alerterConsumer;
	private MessageConsumer alertsystemConsumer;
	
	private String MqServerIP;



	public GatewayTransmitter(GatewayController controller)
	{
		this.controller = controller;
		this.MqServerIP = KieasAddress.ACTIVEMQ_SERVER_IP_LOCAL;

		openConnection();
	}

	private void openConnection()
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
				session.close();
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
			System.out.println("Gateway Connection Stop");
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
			queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			queueProducer.send(textMessage);
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
			this.alerterConsumer = session.createConsumer(alerterQueueDestination);
			Destination alertsystemQueueDestination = session.createQueue(KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
			this.alertsystemConsumer = session.createConsumer(alertsystemQueueDestination);
			
			MessageListener listener = new MessageListener()
			{
				public void onMessage(Message message)
				{
					if (message instanceof TextMessage)
					{
						TextMessage textMessage = (TextMessage) message;
						try 
						{
							if (message.getJMSDestination().toString().equals("queue://" + KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION))
							{
								controller.acceptAleterMessage(textMessage.getText());
								return;
							}
							else if (message.getJMSDestination().toString().equals("queue://"  + KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION))
							{
								controller.acceptAletSystemMessage(textMessage.getText());
								return;
							}
							else
							{
								controller.acceptAleterMessage(textMessage.getText());
								return;
							}
							
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




