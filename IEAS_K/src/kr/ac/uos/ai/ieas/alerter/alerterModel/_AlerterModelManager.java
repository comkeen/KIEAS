package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _AlerterModelManager
{
	private static _AlerterModelManager alerterModelManager;
	private _AlerterController controller;
	private KieasMessageBuilder kieasMessageBuilder;
//	private _DatabaseHandler databaseHandler;	
	
	private AlerterCapGeneratePanelModel alerterCapGeneratePanelModel;
//	private AlerterDataBasePanelModel alerterDataBasePanelModel;
//	private AlerterAlertGeneratePanelModel alerterAlertGeneratePanelModel;
	

	private AlertLogTableModel alertLogTableModel;
	private Map<String, String> alertMessageMap;
	private Map<String, String> alertElementMap;
	
	private String message;
	
	
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
		this.kieasMessageBuilder = new KieasMessageBuilder();
//		this.databaseHandler = new _DatabaseHandler();

		this.alerterCapGeneratePanelModel = new AlerterCapGeneratePanelModel(this);
//		this.alerterAlertGeneratePanelModel = new AlerterAlertGeneratePanelModel(this);
//		this.alerterDataBasePanelModel = new AlerterDataBasePanelModel(this);
		
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
		alertElementMap.put(KieasMessageBuilder.SENDER, KieasMessageBuilder.SENDER);
		alertElementMap.put(KieasMessageBuilder.IDENTIFIER, KieasMessageBuilder.IDENTIFIER);
		alertElementMap.put(KieasMessageBuilder.SENT, KieasMessageBuilder.SENT);
		alertElementMap.put(KieasMessageBuilder.EVENT, KieasMessageBuilder.EVENT);
		alertElementMap.put(KieasMessageBuilder.RESTRICTION, KieasMessageBuilder.RESTRICTION);
		alertElementMap.put(KieasMessageBuilder.GEO_CODE, KieasMessageBuilder.GEO_CODE);
//		alertElementMap.put(ACK, ack);
	}
	


	public String getMessage() { return message; }
	public void loadCap(String path) { alerterCapGeneratePanelModel.loadCap(path); }	
	public void saveCap(String path) { alerterCapGeneratePanelModel.saveCap(path); }
	
	public void updateView(String view, String target, String value) 
	{
		controller.updateView(view, target, value);
	}
	
	public void updateView(String view, String target, List<String> value) 
	{
		controller.updateView(view, target, value);
	}
	

	public void generateCap(String alertSystemType)
	{		
		kieasMessageBuilder.buildDefaultMessage();
		kieasMessageBuilder.setIdentifier(kieasMessageBuilder.generateKieasMessageIdentifier(controller.getId()));
		kieasMessageBuilder.setRestriction(alertSystemType);
	
		this.message = kieasMessageBuilder.getMessage();
		controller.setTextArea(message);
	}

	public String getId()
	{
		return kieasMessageBuilder.getIdentifier();
	}
	
	public String getRestriction()
	{
		return kieasMessageBuilder.getRestriction();
	}
	
	public String getEvent()
	{
		return kieasMessageBuilder.getEvent(0);
	}
	
	public String getGeoCode()
	{
		return kieasMessageBuilder.getGeoCode(0);
	}

	public void addAlertTableRow()
	{
		kieasMessageBuilder.setMessage(message);
		alertLogTableModel.addTableRowData(getAlertElementMap(message));
		
		putAlertMessageMap(kieasMessageBuilder.getIdentifier(), message);
	}
	
	public Map<String, String> getAlertElementMap(String message)
	{
		kieasMessageBuilder.setMessage(message);

		alertElementMap.replace(KieasMessageBuilder.SENDER, kieasMessageBuilder.getSender());
		alertElementMap.replace(KieasMessageBuilder.IDENTIFIER, kieasMessageBuilder.getIdentifier());
		alertElementMap.replace(KieasMessageBuilder.SENT, kieasMessageBuilder.getSent());
		alertElementMap.replace(KieasMessageBuilder.EVENT, kieasMessageBuilder.getEvent(0));
		
		if(kieasMessageBuilder.getRestriction() != null)
		{
			alertElementMap.replace(KieasMessageBuilder.RESTRICTION, kieasMessageBuilder.getRestriction());
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
	
	public Map<String, String> getAlertMessageMap()
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

	public void applyAlertElement(Map<String, String> alertElement)
	{
		alerterCapGeneratePanelModel.applyAlertElement(alertElement);
	}

	public String getAlert()
	{
		return alerterCapGeneratePanelModel.getMessage();
	}

	public void setTextArea(String message)
	{
		alerterCapGeneratePanelModel.setModelProperty("TextArea", message);
	}
	
//	public List<String> getQueryResult(String target, String query)
//	{	
//		System.out.println("getQuery target, query : " + target + " " + query);
//		List<String> result = kieasMessageBuilder.convertDbToCap(databaseHandler.getQueryResult(target, query.toUpperCase()));
//		for (String string : result)
//		{
//			System.out.println("query result = " + string);
//		}
//		switch (target)
//		{
//		case KieasMessageBuilder.EVENT_CODE:
//			alerterDataBasePanelModel.setQueryResult(result);
//			break;
//		case KieasMessageBuilder.STATUS:
//			
//			break;	
//		default:
//			break;
//		}
//		return result;	
//	}	
	
//	public void insertDataBase(String message)
//	{
//		databaseHandler.insertCap(kieasMessageBuilder.convertCapToDb(message));
//	}
}
