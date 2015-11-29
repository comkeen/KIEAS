package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlertSystemController {

	private KieasMessageBuilder ieasMessage;
	private AlertSystemTransmitter alertSystemTransmitter;
	private AlertSystemView alertSystemView;

	private String alertSystemID;
	private String location;
	private String ackMessage;

	public AlertSystemController()  {		

		this.alertSystemID = "alertSystem-Jeju-V2";
		this.location = "Jeju";
		
		this.ieasMessage = new KieasMessageBuilder();
		this.alertSystemTransmitter = new AlertSystemTransmitter(this, location);		
		this.alertSystemView = new AlertSystemView(this, alertSystemID);
	}

	public void sendAckMessage(String message, String destination) {
		ackMessage = createAckMessage(message);
		alertSystemTransmitter.sendMessage(ackMessage, destination);

		System.out.println("(" + alertSystemID + ")" + " Send Message to " + "(gateway) : ");
	}

	public void acceptMessage(String message) {
		try {
			ieasMessage.setMessage(message);

			String sender = ieasMessage.getSender();
			
			System.out.println("(" + alertSystemID + ")" + " Received Message From (" + sender + ") : ");
			System.out.println();
			
			alertSystemView.setTextArea(message);
			Thread.sleep(1000);
			sendAckMessage(message, KieasConfiguration.KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private String createAckMessage(String message) {
		ieasMessage.setMessage(message);
		
		ieasMessage.setAddresses(ieasMessage.getSender());
		ieasMessage.setMsgType("Ack");
		ieasMessage.setSender(alertSystemID);
		ieasMessage.build();
		
		return ieasMessage.getMessage();
	}

	public String getID() {		
		return this.alertSystemID;
	}

	public void selectTopic(String topic) {
		alertSystemTransmitter.selectTopic(topic);		
	}

	public String getLocation() {
		return location;
	}
}
