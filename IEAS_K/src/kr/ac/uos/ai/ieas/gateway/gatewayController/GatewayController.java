package kr.ac.uos.ai.ieas.gateway.gatewayController;

import javax.swing.JOptionPane;

import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertSystemInfoTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlerterInfoTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayModelManager;
import kr.ac.uos.ai.ieas.gateway.gatewayView.GatewayView;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.IeasName;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder.Item;

public class GatewayController {

	private static GatewayController gatewayController;

	private GatewayActionListener gatewayActionListener;
	private GatewayTransmitter gatewayTransmitter;
	private GatewayView gatewayView;
	private GatewayModelManager gatewayModelManager;
	private KieasMessageBuilder kieasMessageBuilder;

	private String gatewayID;

	private String ackMessage;

	private String sender;
	private String identifier;
	private String alertSystemType;
	private String event;


	public static GatewayController getInstance()
	{
		if (gatewayController == null)
		{
			gatewayController = new GatewayController();
		}
		return gatewayController;
	}

	private GatewayController()
	{
		this.gatewayID = KieasConfiguration.IeasName.GATEWAY_NAME;
		this.gatewayActionListener = new GatewayActionListener(this);
		this.gatewayModelManager = GatewayModelManager.getInstance();
		this.gatewayView = GatewayView.getInstance(this, gatewayActionListener);
		this.gatewayTransmitter = new GatewayTransmitter(this);
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		gatewayView.appendLog("(" + gatewayID + ")" + " Open");

		this.sender = "";
		this.identifier = "";
		this.alertSystemType = "";
		this.event = "";
	}

	public void openGateway()
	{
		gatewayTransmitter.startConnection();
		System.out.println("(" + gatewayID + ")" + " Open");
		gatewayView.appendLog("(" + gatewayID + ")" + " Open");
	}

	public void closeGateway()
	{
		gatewayTransmitter.stopConnection();
		System.out.println("(" + gatewayID + ")" + " Close");
		gatewayView.appendLog("(" + gatewayID + ")" + " Close");
	}

	public void acceptAleterMessage(String message)
	{
		sender = gatewayModelManager.getAlertElementMap(message).get("Sender");
		identifier = gatewayModelManager.getAlertElementMap(message).get("Identifier");
		System.out.println("(" + gatewayID + ")" + " Received Message From (" + sender + ") : ");
		System.out.println();
		gatewayView.appendLog("(" + gatewayID + ")" + " Received Message From (" + sender + ") : "+ identifier);

		try 
		{
			gatewayModelManager.addAlertTableRow(message);
			gatewayModelManager.addAlerterInfoTableRow(message);

			this.ackMessage = gatewayModelManager.creatAckMessage(message, gatewayID);
			sendAcknowledge(ackMessage, sender);

			broadcastMessage(message);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

	private void sendAcknowledge(String message, String destination)
	{
//		gatewayTransmitter.sendQueueMessage(message, destination);

		System.out.println("(" + gatewayID + ")" + " Send Acknowledge to ("+ destination +") : ");
		gatewayView.appendLog("(" + gatewayID + ")" + " Send Acknowledge to ("+ destination +") : ");
	}

	private void broadcastMessage(String message)
	{
		alertSystemType = gatewayModelManager.getAlertElementMap(message).get("Restriction");
		event = gatewayModelManager.getAlertElementMap(message).get("Event");

		for (Item item : kieasMessageBuilder.getCapEnumMap().get(KieasMessageBuilder.EVENT_CODE))
		{
			if (alertSystemType.equals(item.getValue()))
			{
				gatewayTransmitter.sendTopicMessage(message, alertSystemType);	

				System.out.println("(" + gatewayID + ")" + " Broadcast Message To ("+ alertSystemType +") : ");
				gatewayView.appendLog("(" + gatewayID + ")" + " Broadcast Message To ("+ alertSystemType +") : "+event);
			}
		}
	}

	public void acceptAletSystemMessage(String message) {

		sender = gatewayModelManager.getAlertElementMap(message).get("sender");
		identifier = gatewayModelManager.getAlertElementMap(message).get("identifier");
		alertSystemType = gatewayModelManager.getAlertElementMap(message).get("addresses");

		try {
			System.out.println("(" + gatewayID + ")" + " Received Message From (" + sender + ") : "+ identifier);
			gatewayView.appendLog("(" + gatewayID + ")" + " Received Message From (" + sender + ") : "+ identifier);
			System.out.println();

			gatewayModelManager.addAlertSystemInfoTableRow(message);
			
			sendAcknowledge(message, alertSystemType);
			gatewayModelManager.receiveAck(identifier);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearLog() {
		gatewayView.clearLog();
	}

	public GatewayAlertTableModel getAlertTableModel() {

		return gatewayModelManager.getAlertTableModel();
	}

	public String getName() {
		return gatewayID;
	}

	public String getAlertMessage(String identifier) {
		return gatewayModelManager.getAlertMessage(identifier);
	}

	public void selectTableEvent() {
		gatewayView.selectTableEvent();
	}

	public GatewayAlerterInfoTableModel getAlerterInfoTableModel() {
		return gatewayModelManager.getAlerterInfoTableModel();
	}
	
	public GatewayAlertSystemInfoTableModel getAlertSystemInfoTableModel() {
		return gatewayModelManager.getAlertSystemInfoTableModel();
	}

	public void systemExit()
	{
		String question = "통합게이트웨이 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";
		
		if (JOptionPane.showConfirmDialog(gatewayView.getFrame(),
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
	    {	
			gatewayTransmitter.closeConnection();
	        System.exit(0);
	    }
		else
		{
			System.out.println("cancel exit program");
		}
	}
}
