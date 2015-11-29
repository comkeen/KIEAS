package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterDataBasePanelModel 
{	
	private _AlerterModelManager alerterModelManager;
	private KieasMessageBuilder kieasMessageBuilder;

	private HashMap<String, String> alertElementMap;
	private HashMap<String, String> alertMessageMap;
	
	private String textAreaText;
	private String queryTextFieldText;
	
	private String mViewName;

	
	
	public AlerterDataBasePanelModel(_AlerterModelManager _AlerterModelManager)
	{
		this.alerterModelManager = _AlerterModelManager;
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
		
		this.textAreaText = "";
		this.queryTextFieldText = "";
	}   
	
	private void init()
	{
		this.mViewName = this.getClass().getSimpleName().toString().replace("Model", "");
	}
		
	public GregorianCalendar getDateCalendar()
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		return cal;
	}

	public void setQueryResult(ArrayList<String> result)
	{
		alerterModelManager.updateView(mViewName, "alertTable", result);
		
	}	
}
