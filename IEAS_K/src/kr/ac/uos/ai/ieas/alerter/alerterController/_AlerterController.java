
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

	/**
	 * CapGeneratePanel에 Cap 메시지를 불러올 때 호출됨.
	 */
	public void loadCap()
	{
		alerterModelManager.loadCap(alerterTopView.getLoadTextField());
	}
	
	/**
	 * CapGeneratePanel에서 작성된 Cap 메시지를 파일로 저장할 때 호출됨.
	 */
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
	 * View 콤포넌트인 DatabasePanel Class의 "Query"버튼에 의해 호출된다.
	 * QueryTextField에 기재된 이벤트코드에 의해 데이터베이스에서 해당하는 결과값들을 가져온다.
	 */
	public void getQueryResult()
	{
		alerterTopView.getQueryResult(alerterModelManager.getQueryResult(alerterTopView.getQuery()));
	}

	/**
	 * View 클래스인 CapGeneratePanel의 InfoPanel에 포함된 "Add Info"버튼에 의해 호출된다.
	 * InfoPanel의 Tab이 하나 추가 된다.
	 */
	public void addInfoIndexPanel() 
	{
		alerterTopView.addInfoIndexPanel();
	}

	/**
	 * View 클래스인 CapGeneratePanel의 ResourcePanel에 포함된 "Add Resource"버튼에 의해 호출된다.
	 * ResourcePanel의 Tab이 하나 추가 된다.
	 */
	public void addResourceIndexPanel() 
	{
		alerterTopView.addResourceIndexPanel();
	}
	
	/**
	 * View 클래스인 CapGeneratePanel의 AreaPanel에 포함된 "Add Area"버튼에 의해 호출된다.
	 * AreaPanel의 Tab이 하나 추가 된다.
	 */
	public void addAreaIndexPanel()
	{
		alerterTopView.addAreaIndexPanel();
	}
	
	/**
	 * Model의 데이터 값이 바뀌었을 경우 View 갱신을 위해 Model에 의해 호출된다.
	 * @param view 갱신되어야 하는 View 클래스 이름
	 * @param target 값이 표시되는 Component의 이름
	 * @param value 표시되는 값
	 */
	public void updateView(String view, String target, String value) {
		alerterTopView.updateView(view, target, value);
	}

	public void insertDatabase() {
		System.out.println("insert to database : todo");
	}

}
