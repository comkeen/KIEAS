package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterCapGeneratePanelModel
{	
	private KieasMessageBuilder kieasMessage;

	private HashMap<String, String> alertElementMap;
	private HashMap<String, String> alertMessageMap;
	
	private String textAreaText;
	private String queryTextFieldText;
	
	
	public AlerterCapGeneratePanelModel(_AlerterController alerterController)
	{
		this.kieasMessage = new KieasMessageBuilder();
		
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
		
		this.textAreaText = "";
		this.queryTextFieldText = "";
	}
}
