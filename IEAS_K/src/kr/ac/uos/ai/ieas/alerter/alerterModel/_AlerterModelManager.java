package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;

import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.db.dbHandler._DatabaseHandler;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _AlerterModelManager extends AbstractModel {
	
	private KieasMessageBuilder kieasMessageBuilder;
	private _DatabaseHandler databaseHandler;
	
	private String alerterTextareaText;
	private String[] locationListForComboBox;
	private String locationComboboxText;
	private String eventComboboxText;
	
	private String message;
	
	private String alerterName;
	private String alerterID;
	
	private String oldString;
	private AlerterCapGeneratePanelModel alerterCapGeneratePanelModel;
	
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
		this.kieasMessageBuilder = new KieasMessageBuilder();
		this.databaseHandler = new _DatabaseHandler();
		
		this.alerterName = "기상청";
		this.alerterID = generateID(alerterName);
		
		this.locationComboboxText = "locate";
		this.eventComboboxText = "event";
	}
    
	public void initDefault()
	{
		setAlerterTextArea(" ");
	}

	public String buildCap() 
	{
		String str = createMessage(eventComboboxText);
		setAlerterTextArea(str);
		return message;
	}
		
	private String generateID(String name)
	{
		UUID id = UUID.randomUUID();
		String identifier = name+"-"+id;

		return identifier;		
	}
	
	private String createMessage(String event) 
	{
		kieasMessageBuilder.setIdentifier(alerterID);
		kieasMessageBuilder.setSender(alerterID);
		kieasMessageBuilder.setSent(getDateCalendar());
		kieasMessageBuilder.setMsgType("Alert");
		kieasMessageBuilder.setEvent(event);
		kieasMessageBuilder.build();

		return kieasMessageBuilder.getMessage();
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
		ArrayList<String> result = kieasMessageBuilder.databaseObjectToCapLibraryObject(databaseHandler.getQueryResult(query.toUpperCase()));
		return result;
	}
}
