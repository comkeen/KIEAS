package kr.or.kpew.kieas.excluded;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterDataBasePanelModel 
{	
	private _AlerterModelManager alerterModelManager;
	private KieasMessageBuilder kieasMessageBuilder;

	private Map<String, String> alertElementMap;
	private Map<String, String> alertMessageMap;
	
	private String textAreaText;
	private String queryTextFieldText;
	
	private String mViewName;

	
	
	public AlerterDataBasePanelModel(_AlerterModelManager _AlerterModelManager)
	{
		this.alerterModelManager = _AlerterModelManager;
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
				
		init();
	}   
	
	private void init()
	{
		this.mViewName = this.getClass().getSimpleName().toString().replace("Model", "");		

		this.textAreaText = "";
		this.queryTextFieldText = "";
	}

	public void setQueryResult(List<String> result)
	{
		System.out.println("databasemodel setQueryResult");
		alerterModelManager.updateView(mViewName, "alertTable", result);
		
	}	
}
