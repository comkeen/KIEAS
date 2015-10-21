package kr.ac.uos.ai.ieas.alerter.alerterController;

import java.util.ArrayList;

import kr.ac.uos.ai.ieas.abstractClass.AbstractController;
import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.abstractClass.AbstractView;
import kr.ac.uos.ai.ieas.alerter.alerterModel.AlerterCapGeneratePanelModel;
import kr.ac.uos.ai.ieas.alerter.alerterModel._AlerterModelManager;
import kr.ac.uos.ai.ieas.alerter.alerterView._AlerterTopView;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _AlerterController extends AbstractController
{ 
	private KieasMessageBuilder kieasMessage;
	private AlerterTransmitter alerterTransmitter;
	private _AlerterTopView alerterTopView;
	private _AlerterModelManager alerterModelManager;
	private AlerterCapGeneratePanelModel alerterCapGeneratePanelModel;

	public static final String ALERT_TEXTAREA_TEXT_PROPERTY = "AlerterTextareaText";
	public static final String ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY = "AlerterLocationComboboxText";
	public static final String ALERT_EVENT_COMBOBOX_TEXT_PROPERTY = "AlerterEventComboboxText";

	public static final String ALERTER_DB_PANEL_TEXTAREA_TEXT_PROPERTY = "AlerterDbPanelTextAreaText";
	public static final String ALERTER_DB_PANEL_TABLE_PROPERTY = "AlerterDbPanelTableModel";
	public static final String ALERTER_CAPGENERATEPANEL_TEXTAREA_PROPERTY = "AlerterCapGenerateTextAreaText";
	public static final String ALERTER_IDENTIFIER_PROPERTY = "CapGeneratePanelAlertIdentifierText";
	public static final String ALERTER_SENDER_PROPERTY = "CapGeneratePanelAlertSenderText";
	public static final String ALERTER_STATUS_PROPERTY = "CapGeneratePanelAlertStatusText";

	
	/**
	 * Model과 View 초기화.
	 */	
	public _AlerterController()
	{
		this.addModel(new _AlerterModelManager(this));
		this.addModel(new AlerterCapGeneratePanelModel(this));
		this.addView(new _AlerterTopView(this));
		
		this.alerterModelManager = (_AlerterModelManager) getRegisteredModels().get(0);
		this.alerterCapGeneratePanelModel = (AlerterCapGeneratePanelModel) getRegisteredModels().get(1);
		this.alerterTopView = (_AlerterTopView) getRegisteredViews().get(0);
	}
	
	public void changeAlerterTextareaProperty(String text)
	{
		setModelProperty(ALERT_TEXTAREA_TEXT_PROPERTY, text);
	}

	public void changeLocationComboboxTextProperty(String text)
	{
		setModelProperty(ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY, text);
	}

	public void changeEventComboboxTextProperty(String text)
	{
		setModelProperty(ALERT_EVENT_COMBOBOX_TEXT_PROPERTY, text);
	}

	public ArrayList<AbstractModel> getRegisteredModels()
	{
		return super.registeredModels;
	}

	public ArrayList<AbstractView> getRegisteredViews()
	{
		return super.registeredViews;
	}	

	public void sendMessage() 
	{
		sendMessageToGateway();
	}

	public void sendTextAreaMessage()
	{
		sendTextAreaMessageToGateway(alerterModelManager.getAlerterTextAreaText());
	}

	public void sendMessageToGateway()
	{
		//		alerterView.addAlertTableRow(id, event, addresses);
		alerterTransmitter.sendMessage(alerterModelManager.getMessage());

		System.out.println("Alerter Send Message to " + "(gateway) : ");
		System.out.println();
	}

	public void acceptMessage(String message)
	{
		try 
		{
			kieasMessage.setMessage(message);

			String sender = kieasMessage.getSender();
			String identifier = kieasMessage.getIdentifier();

			System.out.println("(" + alerterModelManager.getAlerterID() + ")" + " Received Message From (" + sender + ") : ");
			System.out.println();

			if(sender.equals(KieasConfiguration.IeasName.GATEWAY_NAME))
			{
				alerterTopView.receiveGatewayAck(identifier);
			}
			else 
			{
				alerterTopView.receiveAlertSystemAck(identifier);
			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public void sendTextAreaMessageToGateway(String message)
	{
		kieasMessage.setMessage(message);

		String id = kieasMessage.getIdentifier();
		String event = kieasMessage.getEvent();
		String addresses = kieasMessage.getAddresses();

		alerterTopView.addAlertTableRow(id, event, addresses);
		alerterTransmitter.sendMessage(message);
	}

	public void generateCap() 
	{
		System.out.println("generate cap");
		alerterModelManager.buildCap();
	}

	public void saveCap(String capMessage)
	{
		System.out.println("saveCap");
	}

	public void connectToServer()
	{

		this.alerterTransmitter = new AlerterTransmitter(this, alerterModelManager.getAlerterID());
	}

	public void loadCapDraft()
	{
		alerterCapGeneratePanelModel.capLoader();
	}

	public void saveCap()
	{
//		alerterView.saveCap();
	}

	public void applyAlertElement()
	{
		alerterTopView.applyAlertElement();
	}

	public void selectTableEvent()
	{
		alerterTopView.selectTableEvent();
	}
	
	/**
	 * 데이터베이스에서 이벤트코드에 맞는 결과값들을 가져온다.
	 */
	public void getQueryResult()
	{
		alerterTopView.getQueryResult(alerterModelManager.getQueryResult(alerterTopView.getQuery()));
	}

	public void addInfoIndexPanel() 
	{
		alerterTopView.addInfoIndexPanel();
	}
}
