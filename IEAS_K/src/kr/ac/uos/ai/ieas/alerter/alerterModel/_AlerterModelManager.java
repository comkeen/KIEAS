package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.alerter.alerterView.AlerterDataBasePanel;
import kr.ac.uos.ai.ieas.db.dbHandler._DatabaseHandler;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _AlerterModelManager
{
	private static _AlerterModelManager alerterModelManager;
	private _AlerterController controller;
	private _DatabaseHandler databaseHandler;	
	private KieasMessageBuilder kieasMessageBuilder;
	
	private AlerterAlertGeneratePanelModel alerterAlertGeneratePanelModel;
	private AlerterCapGeneratePanelModel alerterCapGeneratePanelModel;
	private AlerterDataBasePanelModel alerterDataBasePanelModel;
	

	private AlertLogTableModel alertLogTableModel;
	private HashMap<String, String> alertMessageMap;
	private HashMap<String, String> alertElementMap;
	
	private String message;
	private String identifier;
	

	public static final String EVENT_CODE = "eventCode";
	public static final String STATUS = "status";
	
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
	
	
	public static _AlerterModelManager getInstance(_AlerterController alerterController)
	{
		if (alerterModelManager == null)
		{
			alerterModelManager = new _AlerterModelManager(alerterController);
		}
		return alerterModelManager;
	}
	
	/**
	 * AlerterModel들을 관리한다.
	 * CAP 메시지 처리를 위한 KieasMessageBuilder 초기화.
	 * Database 접근을 위한 DatabaseHandler 초기화.
	 * @param alerterController Controller
	 */
	public _AlerterModelManager(_AlerterController controller)
	{
		this.controller = controller;
		this.databaseHandler = new _DatabaseHandler();
		this.kieasMessageBuilder = new KieasMessageBuilder();

		this.alerterAlertGeneratePanelModel = new AlerterAlertGeneratePanelModel(this);
		this.alerterCapGeneratePanelModel = new AlerterCapGeneratePanelModel(this);
		this.alerterDataBasePanelModel = new AlerterDataBasePanelModel(this);
		
		init();
	}
	
	private void init()
	{
		this.alertLogTableModel = new AlertLogTableModel();
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
		
		this.message = kieasMessageBuilder.buildDefaultMessage();
		initAlertElementMap();
	}
	
	private void initAlertElementMap()
	{		
		String sender = "sender";
		String identifier = "identifier";
		String sent = "sent";
		String event = "event";
		String restriction = "restriction";
		String geoCode = "geoCode";
//		String ack = "ack";
		
		alertElementMap.put(SENDER, sender);
		alertElementMap.put(IDENTIFIER, identifier);
		alertElementMap.put(SENT, sent);
		alertElementMap.put(EVENT, event);
		alertElementMap.put(RESTRICTION, restriction);
		alertElementMap.put(GEO_CODE, geoCode);
//		alertElementMap.put(ACK, ack);
	}
			
	public ArrayList<String> getQueryResult(String target, String query)
	{	
		System.out.println("getQuery target, query : " + target + " " + query);
		ArrayList<String> result = kieasMessageBuilder.databaseObjectToCapObject(databaseHandler.getQueryResult(target, query.toUpperCase()));
		for (String string : result)
		{
			System.out.println("query result = " + string);
		}
		switch (target)
		{
		case KieasMessageBuilder.EVENT_CODE:
			alerterDataBasePanelModel.setQueryResult(result);
			break;
		case KieasMessageBuilder.STATUS:
			
			break;	
		default:
			break;
		}
		return result;	
	}	

	public void loadCap(String path)
	{
		alerterCapGeneratePanelModel.loadCap(path);
	}
	
	public void saveCap(String path)
	{
		alerterCapGeneratePanelModel.saveCap(path);		
	}

	public void updateView(String view, String target, String value) 
	{
		controller.updateView(view, target, value);
	}

	public void updateView(String view, String target, ArrayList<String> value) 
	{
		controller.updateView(view, target, value);
	}
	
	public String getMessage()
	{
		return message;
	}

	public void generateCap(String alertSystemType)
	{
		this.identifier = generateIdentifier();
		
		String cap = kieasMessageBuilder.buildDefaultMessage();
		kieasMessageBuilder.setMessage(cap);
		kieasMessageBuilder.setIdentifier(identifier);
		kieasMessageBuilder.setRestricion(alertSystemType);
		this.message = kieasMessageBuilder.getMessage();
		controller.setTextArea(message);
	}

	public String getId()
	{
		return kieasMessageBuilder.getIdentifier();
	}
	
	public String generateIdentifier()
	{
		String alerterId = controller.getAlerterId();
		String idNum = Double.toString(Math.random());		
		
		String identifier = alerterId + idNum.substring(2, 12);
		System.out.println("generate id = " + identifier);
		return identifier;
	}

	public String getEvent()
	{
		return kieasMessageBuilder.getEvent(0);
	}

	public String getRestriction()
	{
		return kieasMessageBuilder.getRestriction();
	}

	public String getGeoCode()
	{
		return kieasMessageBuilder.getGeoCode(0, 0);
	}

	public void addAlertTableRow()
	{
		kieasMessageBuilder.setMessage(message);
		alertLogTableModel.addTableRowData(getAlertElementMap(message));
		
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
	
	public void insertDataBase(String message)
	{
		databaseHandler.insertCap(kieasMessageBuilder.capObjectToDatabaseObject(message));
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

	public AlertLogTableModel getAlertTableModel()
	{
		return alertLogTableModel;
	}

	public void receiveGatewayAck(String identifier)
	{
		alertLogTableModel.receiveAck(identifier);
	}

	public void applyAlertElement(HashMap<String, String> alertElement)
	{
		alerterCapGeneratePanelModel.applyAlertElement(alertElement);
	}

	public String getAlert()
	{
		return alerterCapGeneratePanelModel.getMessage();
	}
}
