package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import java.util.UUID;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.CapUtil;

import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.db.dbHandler._DatabaseHandler;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class AlerterDataBasePanelModel extends AbstractModel {
	
	private KieasMessageBuilder kieasMessage;

	private HashMap<String, String> alertElementMap;
	private HashMap<String, String> alertMessageMap;
	
	private String textAreaText;
	private String queryTextFieldText;
	
	
	public AlerterDataBasePanelModel(_AlerterController alerterController)
	{
		this.kieasMessage = new KieasMessageBuilder();
		
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
		
		this.textAreaText = "";
		this.queryTextFieldText = "";
	}
   
		
	public GregorianCalendar getDateCalendar()
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		return cal;
	}
	
}