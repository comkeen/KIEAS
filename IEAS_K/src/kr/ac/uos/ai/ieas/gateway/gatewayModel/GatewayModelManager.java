package kr.ac.uos.ai.ieas.gateway.gatewayModel;

import java.util.HashMap;

import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class GatewayModelManager
{	
	private static GatewayModelManager gatewayModelManager;
	
	private HashMap<String, String> alertMessageMap;
	private HashMap<String, String> alertElementMap;

	private GatewayAlerterInfoTableModel alerterInfoTableModel;
	private GatewayAlertSystemInfoTableModel alertSystemInfoTableModel;
	private GatewayAlertTableModel alertTableModel;
	
	private KieasMessageBuilder ieasMessage;

	
	public static GatewayModelManager getInstance()
	{
		if (gatewayModelManager == null) 
			gatewayModelManager = new GatewayModelManager();
			return gatewayModelManager;
	}
	
	private GatewayModelManager()
	{
		this.alertTableModel = new GatewayAlertTableModel();
		this.alerterInfoTableModel = new GatewayAlerterInfoTableModel();
		this.alertSystemInfoTableModel = new GatewayAlertSystemInfoTableModel();
		
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
		
		this.ieasMessage = new KieasMessageBuilder();
		
		initAlertElementMap();
	}
	
	private void initAlertElementMap()
	{
		String sender = "sender";
		String identifier = "identifier";
		String sent = "sent";
		String event = "event";
//		String addresses = "addresses";
		String ack = "ack";
		
		alertElementMap.put(sender, sender);
		alertElementMap.put(identifier, identifier);
		alertElementMap.put(sent, sent);
		alertElementMap.put(event, event);
//		alertElementMap.put(addresses, addresses);
		alertElementMap.put(ack, ack);
	}
		
	public GatewayAlertTableModel getAlertTableModel() {
		
		return alertTableModel;
	}
	
	public void receiveAck(String identifier) {
		alertTableModel.receiveAck(identifier);
	}
	
	public HashMap<String, String> getAlertElementMap(String message) {
		
		ieasMessage.setMessage(message);

		alertElementMap.replace("sender", ieasMessage.getSender());
		alertElementMap.replace("identifier", ieasMessage.getIdentifier());
		alertElementMap.replace("event", ieasMessage.getEvent(0));
//		alertElementMap.replace("addresses", ieasMessage.getAddresses());
		alertElementMap.replace("sent", ieasMessage.getSent());

		return alertElementMap;
	}


	
	public void putAlertMessageMap(String key, String message) {
		alertMessageMap.put(key, message);
	}
	
	public String getAlertMessage(String identifier) {
		return alertMessageMap.get(identifier);
	}
	
	public HashMap<String, String> getAlertMessageMap() {
		return alertMessageMap;
	}
	
	public String creatAckMessage(String message, String sender) {
		ieasMessage.setMessage(message);
		ieasMessage.setSender(sender);
		ieasMessage.setMsgType("Ack");
		ieasMessage.build();
		
		return ieasMessage.getMessage();
	}
	
	public GatewayAlerterInfoTableModel getAlerterInfoTableModel() {
		return alerterInfoTableModel;
	}
	
	public GatewayAlertSystemInfoTableModel getAlertSystemInfoTableModel() {
		return alertSystemInfoTableModel;
	}
		
	public void addAlertTableRow(String message) {
				
		alertTableModel.addTableRowData(getAlertElementMap(message));
		
		ieasMessage.setMessage(message);
		putAlertMessageMap(ieasMessage.getIdentifier(), message);
	}
	
	public void addAlerterInfoTableRow(String message) {
		alerterInfoTableModel.addTableRowData(getAlertElementMap(message));
	}
	
	public void addAlertSystemInfoTableRow(String message) {
		alertSystemInfoTableModel.addTableRowData(getAlertElementMap(message));
	}
}
