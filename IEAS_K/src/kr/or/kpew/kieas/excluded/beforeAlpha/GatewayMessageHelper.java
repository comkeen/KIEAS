package kr.or.kpew.kieas.gateway.model;

import java.util.HashMap;
import java.util.Map;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;

public class GatewayMessageHelper
{	
	private IKieasMessageBuilder kieasMessageBuilder;

//	private GatewayModel controller;
//	private IssuerTable issuerTable;
//	private AlertSystemInfoTable alertSystemInfoTable;
//	private AlertMessageTable alertMessageTable;

	private Map<String, String> alertMessageMap;
	private Map<String, String> alertElementMap;


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
	
	public enum CapFields {
		No,
		Sender,
		Identifier,
		Status,
		Sent,
		Event,
		Restriction,
		GeoCode,
		Note,
	}
	
	public enum Responses {
		ACK,
		NACK,
		COMP
	}


	public GatewayMessageHelper()
	{
//		this.alertMessageTable = new AlertMessageTable();
//		this.issuerTable = new IssuerTable();
//		this.alertSystemInfoTable = new AlertSystemInfoTable();

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

//	public AlertMessageTable getAlertMessageTable()
//	{		
//		return alertMessageTable;
//	}

//	public void receiveAck(String identifier)
//	{
//		alertMessageTable.receiveAck(identifier);
//	}

//	public void addAlertMessageTableRow(String message)
//	{				
//		alertMessageTable.addTableRowData(getAlertElementMap(message));
//
//		kieasMessageBuilder.setMessage(message);
//		putAlertMessageMap(kieasMessageBuilder.getIdentifier(), message);
//	}

//	public Map<String, String> getAlertElementMap(String message)
//	{
//		kieasMessageBuilder.setMessage(message);
//
//		alertElementMap.replace(SENDER, kieasMessageBuilder.getSender());
//		alertElementMap.replace(IDENTIFIER, kieasMessageBuilder.getIdentifier());
//		alertElementMap.replace(STATUS, kieasMessageBuilder.getStatus());
//		alertElementMap.replace(SENT, kieasMessageBuilder.getSent());
//		alertElementMap.replace(EVENT, kieasMessageBuilder.getEvent(0));
//		alertElementMap.replace(NOTE, kieasMessageBuilder.getNote());
//		if(kieasMessageBuilder.getRestriction() != null)
//		{
//			alertElementMap.replace(RESTRICTION, kieasMessageBuilder.getRestriction());			
//		}
////		alertElementMap.replace(GEO_CODE, kieasMessageBuilder.getSent());
//
//		return alertElementMap;
//	}

//	public void putAlertMessageMap(String key, String message)
//	{
//		alertMessageMap.put(key, message);
//	}

//	public String getAlertMessage(String identifier)
//	{
//		return alertMessageMap.get(identifier);
//	}

//	public Map<String, String> getAlertMessageMap()
//	{
//		return alertMessageMap;
//	}

	public String creatAckMessage(String message, String sender)
	{
		kieasMessageBuilder.setMessage(message);
		kieasMessageBuilder.setSender(sender);
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ACK);
		kieasMessageBuilder.build();

		return kieasMessageBuilder.getMessage();
	}

//	public IssuerTable getAlerterInfoTableModel()
//	{
//		return issuerTable;
//	}
//
//	public AlertSystemInfoTable getAlertSystemInfoTableModel()
//	{
//		return alertSystemInfoTable;
//	}
//
//	public void addIssuerInfoTableRow(String message)
//	{
//		issuerTable.addTableRowData(getAlertElementMap(message));
//	}
//
//	public void addAlertSystemInfoTableRow(String message)
//	{
//		alertSystemInfoTable.addTableRowData(getAlertElementMap(message));
//	}
}
