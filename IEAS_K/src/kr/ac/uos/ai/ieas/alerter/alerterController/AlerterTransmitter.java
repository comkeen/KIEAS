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

import kr.ac.uos.ai.ieas.resource.KieasConfiguration.IeasAddress;

import org.apache.activemq.ActiveMQConnectionFactory;

public class AlerterTransmitter {
	

	private _AlerterController alerter;

	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;

	private String alerterID;
	
	private String MqServerIP;
	

	public AlerterTransmitter(_AlerterController alerter, String alerterID) {

		this.alerter = alerter;
		this.alerterID = alerterID;
		
		this.MqServerIP = IeasAddress.ACTIVEMQ_SERVER_IP;

		init();
	}

	private void init() {
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(MqServerIP);
			this.connection = factory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setQueueListener(alerterID);
	}

	//sender
	public void sendMessage(String message) {
		try {
			Destination queueDestination = this.session.createQueue(IeasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
			this.producer = this.session.createProducer(queueDestination);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage textMessage = this.session.createTextMessage(message);
			
			this.producer.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setQueueListener(String id) {
		try {
			Destination destination = this.session.createQueue(id);
			this.consumer = session.createConsumer(destination);
			MessageListener listener = new MessageListener() {
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;

						try {
							alerter.acceptMessage(textMessage.getText());					
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
	
	/*
	private void setTopicListener() {
		try {
			Destination destination = this.session.createTopic(GATEWAY_TOPIC_DESTINATION);
			MessageConsumer consumer = session.createConsumer(destination);
			MessageListener listener = new MessageListener() {
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;

						try {
							alerter.acceptMessage(textMessage.getText());							
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
	*/
}
