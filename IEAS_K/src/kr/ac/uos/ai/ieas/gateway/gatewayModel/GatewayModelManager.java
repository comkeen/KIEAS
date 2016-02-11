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
	public static final String STATUS = "Status";
	public static final String SENT = "Sent";
	public static final String EVENT = "Event";
	public static final String RESTRICTION = "Restriction";
	public static final String GEO_CODE = "GeoCode";
	public static final String NOTE = "Note";

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
//		String ack = "ack";

		alertElementMap.put(SENDER, SENDER);
		alertElementMap.put(IDENTIFIER, IDENTIFIER);
		alertElementMap.put(SENT, SENT);
		alertElementMap.put(STATUS, STATUS);
		alertElementMap.put(EVENT, EVENT);
		alertElementMap.put(RESTRICTION, RESTRICTION);
		alertElementMap.put(GEO_CODE, GEO_CODE);
		alertElementMap.put(NOTE, NOTE);
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
		putAlertMessageMap(kieasMessageBuilder.getAlertElement(KieasMessageBuilder.IDENTIFIER), message);
	}

	public HashMap<String, String> getAlertElementMap(String message)
	{
		kieasMessageBuilder.setMessage(message);

		alertElementMap.replace(SENDER, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.SENDER));
		alertElementMap.replace(IDENTIFIER, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.IDENTIFIER));
		alertElementMap.replace(STATUS, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.STATUS));
		alertElementMap.replace(SENT, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.SENT));
		alertElementMap.replace(RESTRICTION, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.RESTRICTION));
		alertElementMap.replace(EVENT, kieasMessageBuilder.getInfoElement(0, KieasMessageBuilder.EVENT));
		alertElementMap.replace(NOTE, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.NOTE));
		if(kieasMessageBuilder.getAlertElement(KieasMessageBuilder.RESTRICTION) != null)
		{
			alertElementMap.replace(RESTRICTION, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.RESTRICTION));			
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
		kieasMessageBuilder.setAlertElement(KieasMessageBuilder.SENDER, sender);
		kieasMessageBuilder.setAlertElement(KieasMessageBuilder.MSG_TYPE, "Ack");
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
