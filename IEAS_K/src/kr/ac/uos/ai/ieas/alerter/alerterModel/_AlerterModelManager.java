package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.alerter.alerterView._AlerterTopView;
import kr.ac.uos.ai.ieas.db.dbHandler._DatabaseHandler;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _AlerterModelManager{

	private static _AlerterModelManager alerterModelManager;
	private _AlerterController alerterController;
	private _DatabaseHandler databaseHandler;	
	private AlerterCapGeneratePanelModel alerterCapGeneratePanelModel;
	
	private KieasMessageBuilder kieasMessageBuilder;
	
	public static _AlerterModelManager getInstance(_AlerterController alerterController)
	{
		if (alerterModelManager == null)
		{
			alerterModelManager = new _AlerterModelManager(alerterController);
		}
		return alerterModelManager;
	}
	
	/**
	 * AlerterModel을 관리한다.
	 * Cap메시지 처리를 위한 KieasMessageBuilder 초기화.
	 * Database 접근을 위한 DatabaseHandler 초기화.
	 * 
	 *  
	 * @param alerterController
	 */
	public _AlerterModelManager(_AlerterController alerterController)
	{
		this.alerterController = alerterController;
		this.databaseHandler = new _DatabaseHandler();
		this.alerterCapGeneratePanelModel = new AlerterCapGeneratePanelModel(this);
		this.kieasMessageBuilder = new KieasMessageBuilder();
	}
	
	private String generateID(String name)
	{
		UUID id = UUID.randomUUID();
		String identifier = name+"-"+id;

		return identifier;		
	}
			
	public GregorianCalendar getDateCalendar()
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		return cal;
	}
			
	public ArrayList<String> getQueryResult(String query)
	{	
		ArrayList<String> result = kieasMessageBuilder.databaseObjectToCapLibraryObject(databaseHandler.getQueryResult(query.toUpperCase()));
		return result;
	}
	

	public void capLoader() {
		alerterCapGeneratePanelModel.capLoader();
	}

	public void updateView(String view, String target, String value) {
		alerterController.updateView(view, target, value);
	}
}
