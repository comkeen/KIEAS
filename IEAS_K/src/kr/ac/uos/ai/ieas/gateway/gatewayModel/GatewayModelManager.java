package kr.ac.uos.ai.ieas.gateway.gatewayModel;

import java.util.HashMap;

import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class GatewayModelManager
{	
	private static GatewayModelManager gatewayModelManager;
	private KieasMessageBuilder kieasMessageBuilder;

	private GatewayAlerterInfoTableModel alerterInfoTableModel;
	private GatewayAlertSystemInfoTableModel alertSystemInfoTableModel;
	private GatewayAlertTableModel alertTableModel;

	private HashMap<String, String> alertMessageMap;
	private HashMap<String, String> alertElementMap;


	public static final String NO = "No.";
	public static final String SENDER = "Sender";
	public static final String IDENTIFIER = "Identifier";
	public static final String SENT = "Sent";
	public static final String EVENT = "Event";
	public static final String RESTRICTION = "Restriction";
	public static final String GEO_CODE = "GeoCode";

	public static final String ACK = "ACK";
	public static final String NACK = "NACK";
	public static final String COMP = "COMP";


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

		this.kieasMessageBuilder = new KieasMessageBuilder();

		initAlertElementMap();
	}

	private void initAlertElementMap()
	{		
		String sender = "sender";
		String identifier = "identifier";
		String sent = "sent";
		String event = "event";
		String restriction = "restriction";
//		String geoCode = "geoCode";
//		String ack = "ack";

		alertElementMap.put(SENDER, sender);
		alertElementMap.put(IDENTIFIER, identifier);
		alertElementMap.put(SENT, sent);
		alertElementMap.put(EVENT, event);
		alertElementMap.put(RESTRICTION, restriction);
//		alertElementMap.put(GEO_CODE, geoCode);
//		alertElementMap.put(ACK, ack);
	}

	public GatewayAlertTableModel getAlertTableModel()
	{		
		return alertTableModel;
	}

	public void receiveAck(String identifier)
	{
		alertTableModel.receiveAck(identifier);
	}

	public void addAlertTableRow(String message)
	{				
		alertTableModel.addTableRowData(getAlertElementMap(message));

		kieasMessageBuilder.setMessage(message);
		putAlertMessageMap(kieasMessageBuilder.getIdentifier(), message);
	}

	public HashMap<String, String> getAlertElementMap(String message)
	{
		kieasMessageBuilder.setMessage(message);

		alertElementMap.replace(SENDER, kieasMessageBuilder.getSender());
		alertElementMap.replace(IDENTIFIER, kieasMessageBuilder.getIdentifier());
		alertElementMap.replace(SENT, kieasMessageBuilder.getSent());
		alertElementMap.replace(EVENT, kieasMessageBuilder.getEvent(0));
		if(kieasMessageBuilder.getRestriction() != null)
		{
			alertElementMap.replace(RESTRICTION, kieasMessageBuilder.getRestriction());			
		}
//		alertElementMap.replace(GEO_CODE, kieasMessageBuilder.getSent());

		return alertElementMap;
	}

	public void putAlertMessageMap(String key, String message)
	{
		alertMessageMap.put(key, message);
	}

	public String getAlertMessage(String identifier)
	{
		return alertMessageMap.get(identifier);
	}

	public HashMap<String, String> getAlertMessageMap()
	{
		return alertMessageMap;
	}

	public String creatAckMessage(String message, String sender)
	{
		kieasMessageBuilder.setMessage(message);
		kieasMessageBuilder.setSender(sender);
		kieasMessageBuilder.setMsgType("Ack");
		kieasMessageBuilder.build();

		return kieasMessageBuilder.getMessage();
	}

	public GatewayAlerterInfoTableModel getAlerterInfoTableModel()
	{
		return alerterInfoTableModel;
	}

	public GatewayAlertSystemInfoTableModel getAlertSystemInfoTableModel()
	{
		return alertSystemInfoTableModel;
	}

	public void addAlerterInfoTableRow(String message)
	{
		alerterInfoTableModel.addTableRowData(getAlertElementMap(message));
	}

	public void addAlertSystemInfoTableRow(String message)
	{
		alertSystemInfoTableModel.addTableRowData(getAlertElementMap(message));
	}
}
