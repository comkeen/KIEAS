package kr.ac.uos.ai.ieas.alerterModel;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.CapUtil;

import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.resource.IeasConfiguration;
import kr.ac.uos.ai.ieas.resource.IeasMessageBuilder;

public class AlerterModel extends AbstractModel {

	public static final String ALERTER_NAME = "국민안전처";
	
	private IeasMessageBuilder ieasMessage;
	
	private String alerterTextareaText;
	private String[] locationListForComboBox;
	private String locationComboboxText;
	private String eventComboboxText;
	
	private String message;
	
	private String alerterName;
	private String alerterID;
	private String senderName;
	
	private String oldString;
	
	public AlerterModel(_AlerterController alerterController) {

		this.ieasMessage = new IeasMessageBuilder();
		 
		this.alerterName = ALERTER_NAME;
		this.alerterID = generateID(alerterName);
		this.senderName = "";
		
		this.locationComboboxText = "locate";
		this.eventComboboxText = "event";
	}
    
	public void initDefault(){
		setAlerterTextArea(" ");
		setLocationComboBoxList(IeasConfiguration.IEAS_List.LOCATION_LIST);
	}

	private void setLocationComboBoxList(String[] locationList) {
		this.locationListForComboBox = locationList;
	}

	public String buildCap() {
		String str = createMessage(locationComboboxText, eventComboboxText);
		setAlerterTextArea(str);
		return message;
	}
		
	private String generateID(String name){
		UUID id = UUID.randomUUID();
		String identifier = name+"-"+id;

		return identifier;		
	}
	
	public GregorianCalendar setSent() {
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		return cal;
	}
	
	private String createMessage(String addresses, String event) {

		ieasMessage.setIdentifier(alerterID);
		ieasMessage.setSender(alerterID);
		ieasMessage.setSent();
		ieasMessage.setScopeToRestricted();
		ieasMessage.setAddresses(addresses);
		ieasMessage.setMsgTypeToAlert();
		ieasMessage.setEvent(event);
		ieasMessage.build();

		return ieasMessage.getMessage();
	}
	
	public void setAlerterTextArea(String text) {
		oldString = this.alerterTextareaText;
		this.alerterTextareaText = text;
		
		firePropertyChange(_AlerterController.ALERT_TEXTAREA_TEXT_PROPERTY, oldString, alerterTextareaText);
	}
	
	public void setLocationComboboxText(String text){
		oldString = this.locationComboboxText;
		this.locationComboboxText = text;

		firePropertyChange(_AlerterController.ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY, oldString, locationComboboxText);
	}
	
	public String getAlerterTextAreaText(){
		return alerterTextareaText;
	}

	public String getAlerterID() {
		return alerterID;
	}

	public String getMessage() {
		return message;
	}
}
