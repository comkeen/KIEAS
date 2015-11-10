
package kr.ac.uos.ai.ieas.alerter.alerterController;

import kr.ac.uos.ai.ieas.alerter.alerterModel._AlerterModelManager;
import kr.ac.uos.ai.ieas.alerter.alerterView._AlerterTopView;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _AlerterController
{ 
	private KieasMessageBuilder kieasMessage;
	private AlerterTransmitter alerterTransmitter;
	private _AlerterTopView alerterTopView;
	private _AlerterModelManager alerterModelManager;
	private AleterViewActionListener alerterActionListener;

//	public static final String ALERT_TEXTAREA_TEXT_PROPERTY = "Textarea";
//	public static final String ALERT_LOCATION_COMBOBOX_TEXT_PROPERTY = "LocationCombobox";
//	public static final String ALERT_EVENT_COMBOBOX_TEXT_PROPERTY = "EventCombobox";
//
//	public static final String DBPANEL_TEXTAREA_TEXT_PROPERTY = "DbPanelTextArea";
//	public static final String DBPANEL_TABLE_PROPERTY = "DbPanelTableModel";
//	
//	public static final String CGPANEL_TEXT_AREA_PROPERTY = "TextArea";
//	public static final String CGPANEL_LOAD_TEXT_FEILD_PROPERTY = "LoadTextField";
//	public static final String CGPANEL_SAVE_TEXT_FEILD_PROPERTY = "SaveTextField";
//	
//	public static final String CGPANEL_IDENTIFIER_PROPERTY = "Identifier";
//	public static final String CGPANEL_SENDER_PROPERTY = "Sender";
//	public static final String CGPANEL_SENT_PROPERTY = "Sent";
//	public static final String CGPANEL_STATUS_PROPERTY = "Status";
//	public static final String CGPANEL_MSG_TYPE_PROPERTY = "MsgType";
//	public static final String CGPANEL_SCOPE_PROPERTY = "Scope";
//	public static final String CGPANEL_CODE_PROPERTY = "Code";
//
//	public static final String CGPANEL_LANGUAGE_PROPERTY = "Language";
//	public static final String CGPANEL_CATEGORY_PROPERTY = "Category";
//	public static final String CGPANEL_EVENT_PROPERTY = "Event";
//	public static final String CGPANEL_URGENCY_PROPERTY = "Urgency";
//	public static final String CGPANEL_SEVERITY_PROPERTY = "Severity";
//	public static final String CGPANEL_CERTAINTY_PROPERTY = "Certainty";
//	public static final String CGPANEL_EVENT_CODE_PROPERTY = "EventCode";
//	public static final String CGPANEL_EFFECTIVE_PROPERTY = "Effective";
//	public static final String CGPANEL_SENDER_NAME_PROPERTY = "SenderName";
//	public static final String CGPANEL_HEADLINE_PROPERTY = "Headline";
//	public static final String CGPANEL_DESCRIPTION_PROPERTY = "Description";
//	public static final String CGPANEL_WEB_PROPERTY = "PanelWeb";
//	public static final String CGPANEL_CONTACT_PROPERTY = "Contact";
	
	/**
	 * Model과 View 초기화.
	 */	
	public _AlerterController()
	{
		this.alerterActionListener = new AleterViewActionListener(this);
		this.alerterTopView = _AlerterTopView.getInstance(alerterActionListener);
		this.alerterModelManager = new _AlerterModelManager(this);
	}
	
	public void sendMessage() 
	{
		sendMessageToGateway();
	}

	public void sendTextAreaMessage()
	{
//		sendTextAreaMessageToGateway(alerterModelManager.getAlerterTextAreaText());
	}

	public void sendMessageToGateway()
	{
		//		alerterView.addAlertTableRow(id, event, addresses);
//		alerterTransmitter.sendMessage(alerterModelManager.getMessage());

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

			System.out.println("(Alerter)" + " Received Message From (" + sender + ") : ");
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
		String event = kieasMessage.getEvent(0);
		String addresses = kieasMessage.getAddresses();

		alerterTopView.addAlertTableRow(id, event, addresses);
		alerterTransmitter.sendMessage(message);
	}

	public void saveCap(String capMessage)
	{
		System.out.println("saveCap");
	}

	public void connectToServer()
	{
		this.alerterTransmitter = new AlerterTransmitter(this, "Alerter");
	}

	public void loadCap()
	{
		alerterModelManager.loadCap(alerterTopView.getLoadTextField());
	}

	public void saveCap()
	{
		alerterModelManager.saveCap(alerterTopView.getSaveTextField());
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

	public void addResourceIndexPanel() 
	{
		alerterTopView.addResourceIndexPanel();
	}

	public void addAreaIndexPanel()
	{
		alerterTopView.addAreaIndexPanel();
	}
	
	public void updateView(String view, String target, String value) {
		alerterTopView.updateView(view, target, value);
	}

	public void insertDatabase() {
		System.out.println("insert to database : todo");
	}

}
