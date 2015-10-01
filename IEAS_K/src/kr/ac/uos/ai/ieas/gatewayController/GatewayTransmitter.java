package kr.ac.uos.ai.ieas.gatewayController;

import java.util.Date;

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

import kr.ac.uos.ai.ieas.resource.IeasConfiguration.IeasAddress;


public class GatewayTransmitter {

	private GatewayController gateway;

	private ActiveMQConnectionFactory factory;
	public Connection connection;
	public Session session;

	private MessageProducer queueProducer;	
	private MessageProducer topicProducer;


	public GatewayTransmitter(GatewayController gateway) {

		this.gateway = gateway;

		init();
	}

	private void init() {
		try {
			this.factory = new ActiveMQConnectionFactory(IeasAddress.ACTIVEMQ_SERVER_IP);
			this.connection = factory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setQueueMessageListener();

		//		new GatewayTransmitterRestarterThread().run();
	}

	public void sendQueueMessage(String message, String destination) {
		try {
			Destination queueDestination = this.session.createQueue(destination);
			this.queueProducer = this.session.createProducer(queueDestination);
			this.queueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.queueProducer.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void broadcastMessage(String message) {
		try {
			Destination destination = this.session.createTopic(IeasAddress.GATEWAY_TOPIC_DESTINATION);
			this.topicProducer = this.session.createProducer(destination);
			this.topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.topicProducer.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendTopicMessage(String message, String topic) {
		try {
			Destination destination = this.session.createTopic(topic);
			this.topicProducer = this.session.createProducer(destination);
			this.topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.topicProducer.send(textMessage);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setQueueMessageListener() {
		try {
			Destination alerterQueueDestination = session.createQueue(IeasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
			MessageConsumer alerterConsumer = session.createConsumer(alerterQueueDestination);
			Destination alertsystemQueueDestination = session.createQueue(IeasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
			MessageConsumer alertsystemConsumer = session.createConsumer(alertsystemQueueDestination);

			MessageListener listener = new MessageListener() {
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;

						try {
							if (message.getJMSDestination().toString().equals("queue://"+IeasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION)) {

								gateway.acceptAleterMessage(textMessage.getText());
							} else if (message.getJMSDestination().toString().equals("queue://"+IeasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION)) {
								gateway.acceptAletSystemMessage(textMessage.getText());
							}
						} catch (JMSException e) {
							e.printStackTrace();
						}					
					}
				}
			};
			//register to eventListener
			alerterConsumer.setMessageListener(listener);
			alertsystemConsumer.setMessageListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		try {

			this.connection.start();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void close() {
		try {

			this.connection.stop();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class GatewayTransmitterRestarterThread extends Thread{
		public void run() {
			try {

				while(true) {
					Thread.sleep(600000);
					System.out.println(new Date().toString() + " : create new connection");
					factory = new ActiveMQConnectionFactory(IeasAddress.ACTIVEMQ_SERVER_IP);
					connection = factory.createConnection();
					connection.start();
					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}	
		}
	}

	public class GatewayTransmitterRestarter implements Runnable{

		@Override
		public void run() {
			try {

				while(true) {
					Thread.sleep(600000);
					System.out.println(new Date().toString() + " : create new connection");
					factory = new ActiveMQConnectionFactory(IeasAddress.ACTIVEMQ_SERVER_IP);
					connection = factory.createConnection();
					connection.start();
					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}			
		}
	}
}




