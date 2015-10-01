package kr.ac.uos.ai.ieas.alerterModel;

import java.util.UUID;

import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.alerterController.AlerterController;
import kr.ac.uos.ai.ieas.resource.IeasConfiguration;
import kr.ac.uos.ai.ieas.resource.IeasMessage;

public class AlerterModel extends AbstractModel {

	private IeasMessage ieasMessage;
	
	private String alerterTextareaText;

	private String[] locationListForComboBox;
	private String locationComboboxText;
	private String eventComboboxText;
	
	private String message;
	
	private String alerterName;
	private String alerterID;
	private String senderName;
	
	private String oldString;
	
	public AlerterModel(AlerterController alerterController) {

		this.ieasMessage = new IeasMessage();
		 
		this.alerterName = IeasAlerterConfig.ALERTER_NAME;
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
		
		firePropertyChange(AlerterController.ALERT_TEXTAREA_TEXT_PROPERTY, oldString, alerterTextareaText);
	}
	
	public void setLocationComboboxText(String text){
		oldString = this.locationComboboxText;
		this.locationComboboxText = text;		

		firePropertyChange(AlerterController.ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY, oldString, locationComboboxText);
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
