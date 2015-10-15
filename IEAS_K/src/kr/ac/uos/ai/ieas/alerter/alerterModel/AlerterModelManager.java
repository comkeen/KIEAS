package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.CapUtil;

import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.db.dbHandler._DatabaseHandler;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class AlerterModelManager extends AbstractModel {
	
	private KieasMessageBuilder kieasMessage;
	private _DatabaseHandler databaseHandler;
	
	private String alerterTextareaText;
	private String[] locationListForComboBox;
	private String locationComboboxText;
	private String eventComboboxText;
	
	private String message;
	
	private String alerterName;
	private String alerterID;
	private String senderName;
	
	private String oldString;
	
	
	public AlerterModelManager(_AlerterController alerterController)
	{
		this.kieasMessage = new KieasMessageBuilder();
		this.databaseHandler = new _DatabaseHandler();
		 
		this.alerterName = "기상청";
		this.alerterID = generateID(alerterName);
		this.senderName = "";
		
		this.locationComboboxText = "locate";
		this.eventComboboxText = "event";
	}
    
	public void initDefault()
	{
		setAlerterTextArea(" ");
		setLocationComboBoxList(KieasConfiguration.IEAS_List.LOCATION_LIST);
	}

	private void setLocationComboBoxList(String[] locationList)
	{
		this.locationListForComboBox = locationList;
	}

	public String buildCap() 
	{
		String str = createMessage(locationComboboxText, eventComboboxText);
		setAlerterTextArea(str);
		return message;
	}
		
	private String generateID(String name)
	{
		UUID id = UUID.randomUUID();
		String identifier = name+"-"+id;

		return identifier;		
	}
	
	private String createMessage(String addresses, String event) 
	{
		kieasMessage.setIdentifier(alerterID);
		kieasMessage.setSender(alerterID);
		kieasMessage.setSent(getDateCalendar());
		kieasMessage.setMsgType("Alert");
		kieasMessage.setEvent(event);
		kieasMessage.build();

		return kieasMessage.getMessage();
	}
		
	public GregorianCalendar getDateCalendar()
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		return cal;
	}
	
	public void setAlerterTextArea(String text)
	{
		oldString = this.alerterTextareaText;
		this.alerterTextareaText = text;
		
		firePropertyChange(_AlerterController.ALERT_TEXTAREA_TEXT_PROPERTY, oldString, alerterTextareaText);
	}
	
	public void setLocationComboboxText(String text)
	{
		oldString = this.locationComboboxText;
		this.locationComboboxText = text;

		firePropertyChange(_AlerterController.ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY, oldString, locationComboboxText);
	}
	
	public String getAlerterTextAreaText()
	{
		return alerterTextareaText;
	}

	public String getAlerterID() 
	{
		return alerterID;
	}

	public String getMessage() 
	{
		return message;
	}
	
	public ArrayList<String> getQueryResult(String query)
	{	
		return databaseHandler.getQueryResult(query.toUpperCase());
	}
}
