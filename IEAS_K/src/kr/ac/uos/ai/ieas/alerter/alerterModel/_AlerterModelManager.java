package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;


import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.db.dbHandler._DatabaseHandler;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _AlerterModelManager{

	private static _AlerterModelManager alerterModelManager;
	private _AlerterController alerterController;
	private _DatabaseHandler databaseHandler;	
	
	private AlerterAlertGeneratePanelModel alerterAlertGeneratePanelModel;
	private AlerterCapGeneratePanelModel alerterCapGeneratePanelModel;
	private AlerterDataBasePanelModel alerterDataBasePanelModel;
	
	private KieasMessageBuilder kieasMessageBuilder;
	
	private String message;
	

	public static final String EVENT_CODE = "eventCode";
	public static final String STATUS = "status";
	
	
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
	public _AlerterModelManager(_AlerterController alerterController)
	{
		this.alerterController = alerterController;
		this.databaseHandler = new _DatabaseHandler();
		this.kieasMessageBuilder = new KieasMessageBuilder();

		this.alerterAlertGeneratePanelModel = new AlerterAlertGeneratePanelModel(this);
		this.alerterCapGeneratePanelModel = new AlerterCapGeneratePanelModel(this);
		
		init();
	}
	
	private void init()
	{
		this.message = kieasMessageBuilder.buildDefaultMessage();
	}
	
//	private String generateID(String name)
//	{
//		UUID id = UUID.randomUUID();
//		String identifier = name+"-"+id;
//
//		return identifier;		
//	}
			
	public ArrayList<String> getQueryResult(String target, String query)
	{	
		ArrayList<String> result = kieasMessageBuilder.databaseObjectToCapObject(databaseHandler.getQueryResult(target, query.toUpperCase()));
		
		switch (target)
		{
		case EVENT_CODE:
			alerterDataBasePanelModel.setQueryResult(result);
			break;
		case STATUS:
			
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
		alerterController.updateView(view, target, value);
	}

	public void updateView(String view, String target, ArrayList<String> value) 
	{
		alerterController.updateView(view, target, value);
	}

	
	public String getMessage()
	{
		return message;
	}

	public void generateCap(String geoCode, String alertSystemType)
	{
		String cap = kieasMessageBuilder.buildDefaultMessage();
		kieasMessageBuilder.setMessage(cap);
		kieasMessageBuilder.setRestricion(alertSystemType);
		
		this.message = kieasMessageBuilder.getMessage();
		System.out.println("message = " + message);
		alerterController.setTextArea(message);
	}

	public String getId()
	{
		return kieasMessageBuilder.getIdentifier();
	}

	public String getEvent()
	{
		return kieasMessageBuilder.getEvent(0);
	}

	public String getAddresses()
	{
		return kieasMessageBuilder.getRestriction();
	}
}
