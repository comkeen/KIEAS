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

import kr.ac.uos.ai.ieas.resource.KieasConfiguration.IeasAddress;

public class AlertSystemTransmitter {

	private AlertSystemController alertSystem;

	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;

	private String location;

	public AlertSystemTransmitter(AlertSystemController alertSystem, String location) {

		this.alertSystem = alertSystem;
		this.location = location;

		init();
	}

	private void init() {
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(IeasAddress.ACTIVEMQ_SERVER_IP);
			this.connection = factory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setTopicListener(location);
	}

	public void sendMessage(String message, String destination) {
		try {
			Destination queueDestination = this.session.createQueue(destination);
			this.producer = this.session.createProducer(queueDestination);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);

			this.producer.send(textMessage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setTopicListener(String location) {
		try {
			Destination destination = this.session.createTopic(location);
			this.consumer = session.createConsumer(destination);
			MessageListener listener = new MessageListener() {
				
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;

						try {
							alertSystem.acceptMessage(textMessage.getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			};
			consumer.setMessageListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectTopic(String topic) {
		try {
			session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("Creating session with topic: " + topic);
			setTopicListener(topic);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	
	}
}
