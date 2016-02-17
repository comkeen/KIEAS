
package kr.ac.uos.ai.ieas.alerter.alerterController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import kr.ac.uos.ai.ieas.alerter.alerterModel.AlertLogTableModel;
import kr.ac.uos.ai.ieas.alerter.alerterModel._AlerterModelManager;
import kr.ac.uos.ai.ieas.alerter.alerterView._AlerterTopView;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.IKieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.ITransmitter;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasAddress;


public class _AlerterController
{ 
	private static _AlerterController alerterController;
	private _AlerterTopView alerterTopView;
	private _AlerterModelManager alerterModelManager;
	private AleterViewActionListener alerterActionListener;

	private IKieasMessageBuilder kieasMessageBuilder;
	private ITransmitter transmitter;
	
	private String alerterId;
	

	public static _AlerterController getInstance()
	{
		if (alerterController == null)
		{
			alerterController = new _AlerterController(alerterController);
		}
		return alerterController;
	}
	
	
	/**
	 * Model과 View 초기화.
	 * @param alerterController 
	 */		
	private _AlerterController(_AlerterController alerterController)
	{
		
		this.alerterActionListener = new AleterViewActionListener(this);
		this.alerterModelManager = new _AlerterModelManager(this);
		this.alerterTopView = _AlerterTopView.getInstance(this, alerterActionListener);
		
		this.transmitter = new AlerterTransmitter(this);
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		init();
	}
	
	private void init()
	{
		this.setID();
		alerterTopView.setId(alerterId);
		transmitter.openConnection();
	}
	
	public void setID()
	{
		this.alerterId = "기상청";
		this.alerterId = getLocalServerIp() + ":" + new Random().nextInt(9999) + "/alerterId";
		transmitter.setQueueReceiver(alerterId);
		alerterTopView.setId(alerterId);
	}
	
	private String getLocalServerIp()
	{
		try
		{
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		    {
		        NetworkInterface intf = en.nextElement();
		        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		        {
		            InetAddress inetAddress = enumIpAddr.nextElement();
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
		            {
		            	return inetAddress.getHostAddress().toString();
		            }
		        }
		    }
		}
		catch (SocketException e)
		{		
			e.printStackTrace();
		}
		return null;
	}
	
	public void registerToGateway()
	{
		kieasMessageBuilder.setIdentifier(kieasMessageBuilder.generateKieasMessageIdentifier(alerterId));
		kieasMessageBuilder.setSender(alerterId);
		kieasMessageBuilder.setSent(kieasMessageBuilder.getDate());
		kieasMessageBuilder.setStatus(KieasMessageBuilder.SYSTEM);
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ALERT);
		kieasMessageBuilder.setRestriction(KieasMessageBuilder.RESTRICTED);
		kieasMessageBuilder.setRestriction("Alerter");
		kieasMessageBuilder.build();
		System.out.println(kieasMessageBuilder.getMessage());
		
		transmitter.sendQueueMessage(kieasMessageBuilder.getMessage(), KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);	
		
		StringBuffer log = new StringBuffer();
		log.append("(").append(alerterId).append(")").append(" Register to Gateway :");
		System.out.println(log.toString());
	}

	public void sendMessage()
	{
		alerterModelManager.addAlertTableRow();
		transmitter.sendQueueMessage(alerterModelManager.getMessage(), KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
		System.out.println("Alerter Send Message to " + "(gateway) : ");
		System.out.println();
	}

	public void sendAlert()
	{
		alerterModelManager.addAlertTableRow();
		transmitter.sendQueueMessage(alerterModelManager.getAlert(), KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
		System.out.println("Alerter Send Message to " + "(gateway) : ");
		System.out.println();
	}
	
	public void acceptMessage(String message)
	{
		try 
		{
			System.out.println("alerter acceptMessage");
			kieasMessageBuilder.setMessage(message);

//			String sender = kieasMessageBuilder.getSender();
//			String identifier = kieasMessageBuilder.getIdentifier();

			System.out.println("(Alerter)" + " Received Message From (" + "Gateway" + ") : ");
			System.out.println();

			alerterModelManager.setTextArea(message);
//			if(sender.equals(KieasConfiguration.KieasName.GATEWAY_NAME))
//			{
//				alerterModelManager.receiveGatewayAck(identifier);
//			}
//			else 
//			{
////				alerterTopView.receiveAlertSystemAck(identifier);
//			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void systemExit()
	{
		String question = "표준경보발령대 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";
		
		if (JOptionPane.showConfirmDialog(alerterTopView.getFrame(),
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
	    {
			transmitter.closeConnection();
	        System.exit(0);
	    }
		else
		{
			System.out.println("cancel exit program");
		}
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
		alerterModelManager.applyAlertElement(alerterTopView.getAlertElement());
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
		alerterModelManager.getQueryResult(KieasMessageBuilder.EVENT_CODE, alerterTopView.getQuery());		
	}

	public void loadDraft() 
	{
		alerterTopView.getQueryResult(alerterModelManager.getQueryResult("status", "Draft"));
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
	public void updateView(String view, String target, String value)
	{
		alerterTopView.updateView(view, target, value);
	}
	
	public void updateView(String view, String target, List<String> value)
	{
		alerterTopView.updateView(view, target, value);
	}

	public void insertDatabase()
	{
		alerterModelManager.insertDataBase(alerterTopView.getTextArea());
		System.out.println("insert to database : <<todo>>");
	}

	public void generateCap()
	{		
		alerterModelManager.generateCap(alerterTopView.getAlertSystemType());
	}

	public void setTextArea(String message)
	{
		alerterTopView.setTextArea(message);
	}

	public AlertLogTableModel getAlertTableModel()
	{
		return alerterModelManager.getAlertTableModel();
	}

	public String getAlertMessage(String identifier)
	{
		return alerterModelManager.getAlertMessage(identifier);
	}

	public String getId()
	{
		return alerterId;
	}
}
