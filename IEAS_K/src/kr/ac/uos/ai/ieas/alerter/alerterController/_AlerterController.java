package kr.ac.uos.ai.ieas.alerter.alerterController;

import java.util.ArrayList;

import kr.ac.uos.ai.ieas.abstractClass.AbstractController;
import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.abstractClass.AbstractView;
import kr.ac.uos.ai.ieas.alerter.alerterModel.AlerterModel;
import kr.ac.uos.ai.ieas.alerter.alerterView._AlerterTopView;
import kr.ac.uos.ai.ieas.db.dbDriver.DatabaseDriver;
import kr.ac.uos.ai.ieas.resource.IeasConfiguration;
import kr.ac.uos.ai.ieas.resource.IeasMessageBuilder;

public class _AlerterController extends AbstractController{ 

	private IeasMessageBuilder ieasMessage;
	private AlerterTransmitter alerterTransmitter;
	private _AlerterTopView alerterView;
	private AlerterModel alerterModel;


	public static final String ALERT_TEXTAREA_TEXT_PROPERTY = "AlerterTextareaText";
	public static final String ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY = "AlerterLocationComboboxText";
	public static final String ALERT_EVENT_COMBOBOX_TEXT_PROPERTY = "AlerterEventComboboxText";
	
	public void changeAlerterTextareaProperty(String text){
		setModelProperty(ALERT_TEXTAREA_TEXT_PROPERTY, text);
	}
	
	public void changeLocationComboboxTextProperty(String text){
		setModelProperty(ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY, text);
	}
	
	public void changeEventComboboxTextProperty(String text){
		setModelProperty(ALERT_EVENT_COMBOBOX_TEXT_PROPERTY, text);
	}
	
	public void initAlerterController() {
				
		this.alerterModel = (AlerterModel) getRegisteredModels().get(0);
		this.alerterView = (_AlerterTopView) getRegisteredViews().get(0);
	}
	
	public ArrayList<AbstractModel> getRegisteredModels(){
		return super.registeredModels;
	}
	
	public ArrayList<AbstractView> getRegisteredViews(){
		return super.registeredViews;
	}
	

	public void sendMessage() {
		sendMessageToGateway();
	}
	
	public void sendTextAreaMessage() {
		sendTextAreaMessageToGateway(alerterModel.getAlerterTextAreaText());
	}
	
	public void sendMessageToGateway() {

//		alerterView.addAlertTableRow(id, event, addresses);
		alerterTransmitter.sendMessage(alerterModel.getMessage());

		System.out.println("Alerter Send Message to " + "(gateway) : ");
		System.out.println();
	}

	public void acceptMessage(String message) {

		try {
			ieasMessage.setMessage(message);

			String sender = ieasMessage.getSender();
			String identifier = ieasMessage.getIdentifier();

			System.out.println("(" + alerterModel.getAlerterID() + ")" + " Received Message From (" + sender + ") : ");
			System.out.println();

			if(sender.equals(IeasConfiguration.IeasName.GATEWAY_NAME)) {
				alerterView.receiveGatewayAck(identifier);
			} else {
				alerterView.receiveAlertSystemAck(identifier);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void sendTextAreaMessageToGateway(String message) {

		ieasMessage.setMessage(message);
		
		String id = ieasMessage.getIdentifier();
		String event = ieasMessage.getEvent();
		String addresses = ieasMessage.getAddresses();
		
		alerterView.addAlertTableRow(id, event, addresses);
		alerterTransmitter.sendMessage(message);
	}
	
	public void generateCap() {
		System.out.println("generate cap");
		alerterModel.buildCap();
	}

	public void saveCap(String capMessage) {
		System.out.println("saveCap");
	}

	public void connectToServer() {

		this.alerterTransmitter = new AlerterTransmitter(this, alerterModel.getAlerterID());
	}

	public void loadCapDraft()
	{
		alerterView.loadCapDraft();
	}

	public void saveCap()
	{
		alerterView.saveCap();
	}

	public void applyAlertElement() {
		alerterView.applyAlertElement();
	}

	public void selectTableEvent() {
		alerterView.selectTableEvent();
	}

	public void getHRAResult() {
		alerterView.getHRAResult();
	}
	
	public void getHRWResult() {
		alerterView.getHRWResult();
	}

//	public void saveCap(String capMessage) {
//		String fileName = "D://cap/test.xml";
//		
//		try {
//			BufferedWriter fw = new BufferedWriter(new FileWriter(fileName, true));
//			fw.write(capMessage);
//			fw.flush();
//			fw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("write comp");
//	}

}
