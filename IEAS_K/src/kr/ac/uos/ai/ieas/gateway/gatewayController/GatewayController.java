package kr.ac.uos.ai.ieas.gateway.gatewayController;

import javax.swing.JOptionPane;

import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertSystemInfoTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlerterInfoTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayModelManager;
import kr.ac.uos.ai.ieas.gateway.gatewayView.GatewayView;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasList;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasName;

public class GatewayController {


	private static GatewayController gatewayController;

	private GatewayActionListener gatewayActionListener;
	private GatewayTransmitter gatewayTransmitter;
	private GatewayView gatewayView;
	private GatewayModelManager gatewayModelManager;

	private String gatewayID;

	private String ackMessage;

	private String sender;
	private String identifier;
	private String status;
	private String alertSystemType;
	private String event;
	private String geoCode;
	private String note;



	private static final long DELAY = 1000;

	public static GatewayController getInstance()
	{
		if (gatewayController == null)
		{
			gatewayController = new GatewayController();
		}
		return gatewayController;
	}

	private GatewayController()
	{
		this.gatewayID = KieasName.GATEWAY_NAME;
		this.gatewayActionListener = new GatewayActionListener(this);
		this.gatewayModelManager = GatewayModelManager.getInstance();
		this.gatewayView = GatewayView.getInstance(this, gatewayActionListener);
		this.gatewayTransmitter = new GatewayTransmitter(this);		
		gatewayView.appendLog("(" + gatewayID + ")" + " Open");

		this.sender = "";
		this.identifier = "";
		this.event = "";
		this.alertSystemType = "";
	}

	public void openGateway()
	{
		gatewayTransmitter.startConnection();
		
		String log = "(" + gatewayID + ")" + " Open";
		System.out.println(log);
		gatewayView.appendLog(log);
	}

	public void closeGateway()
	{
		gatewayTransmitter.stopConnection();
		
		String log = "(" + gatewayID + ")" + " Close";
		System.out.println(log);
		gatewayView.appendLog(log);
	}

	public void acceptAleterMessage(String message)
	{
		sender = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.SENDER);
		identifier = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.IDENTIFIER);
		
		String log = "(" + gatewayID + ")" + " Received Message From Alerter (" + sender + ") : ";
		System.out.println(log);
		gatewayView.appendLog(log + identifier);

		try 
		{
			gatewayModelManager.addAlertTableRow(message);
			gatewayModelManager.addAlerterInfoTableRow(message);

			this.ackMessage = gatewayModelManager.creatAckMessage(message, gatewayID);
			sendAcknowledge(ackMessage, sender);

			broadcastMessage(message);
			routeMessage(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void acceptAletSystemMessage(String message)
	{
		sender = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.SENDER);	
		identifier = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.IDENTIFIER);
		status = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.STATUS);

		switch (status)
		{	
		case "ACTUAL":
			try 
			{
				String log = "(" + gatewayID + ")" + " Received Message From AlertSystem : " + identifier;
				System.out.println(log);
				gatewayView.appendLog(log);

				gatewayModelManager.addAlertSystemInfoTableRow(message);
				
				sendAcknowledge(message, sender);
				gatewayModelManager.receiveAck(identifier);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		case "SYSTEM":
			try 
			{
				note = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.NOTE);
				
				String log = "(" + gatewayID + ")" + " Received Register request From AlertSystem : " + identifier;
				System.out.println(log);
				gatewayView.appendLog(log);
				
				//approveRegister();
				//
				gatewayModelManager.addAlertSystemInfoTableRow(message);
				sendAcknowledge(message, sender);
				gatewayModelManager.receiveAck(identifier);		
				//
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		default:
			break;
		}		
	}

	private void sendAcknowledge(String message, String destination)
	{
		try
		{
			Thread.sleep(DELAY);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		gatewayTransmitter.sendQueueMessage(message, destination);

		String log = "(" + gatewayID + ")" + " Send Acknowledge to ("+ destination +") : ";
		System.out.println(log);
		gatewayView.appendLog(log);
	}

	private void broadcastMessage(String message)
	{
		alertSystemType = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.RESTRICTION);
		event = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.EVENT);
		
		for (String item : KieasList.ALERT_SYSTEM_TYPE_LIST)
		{
			if (item.equals(alertSystemType))
			{
				gatewayTransmitter.sendTopicMessage(message, alertSystemType);	

				String log = "(" + gatewayID + ")" + " Broadcast Message To ("+ alertSystemType +") : ";
				System.out.println(log);
				gatewayView.appendLog(log + event);
			}
		}
	}

	private void routeMessage(String message)
	{
		geoCode = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.GEO_CODE);
		
		for (String item : KieasList.GEO_CODE_LIST)
		{
			if (item.equals(geoCode))
			{
				gatewayTransmitter.sendQueueMessage(message, geoCode);	

				String log = "(" + gatewayID + ")" + " Route Message To ("+ geoCode +") : ";
				System.out.println(log);
				gatewayView.appendLog(log);
			}
		}
	}

	public void clearLog()
	{
		gatewayView.clearLog();
	}

	public GatewayAlertTableModel getAlertTableModel()
	{
		return gatewayModelManager.getAlertTableModel();
	}

	public String getName()
	{
		return gatewayID;
	}

	public String getAlertMessage(String identifier)
	{
		return gatewayModelManager.getAlertMessage(identifier);
	}

	public void selectTableEvent()
	{
		gatewayView.selectTableEvent();
	}

	public GatewayAlerterInfoTableModel getAlerterInfoTableModel()
	{
		return gatewayModelManager.getAlerterInfoTableModel();
	}
	
	public GatewayAlertSystemInfoTableModel getAlertSystemInfoTableModel()
	{
		return gatewayModelManager.getAlertSystemInfoTableModel();
	}

	public void systemExit()
	{
		String question = "통합게이트웨이 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";
		
		if (JOptionPane.showConfirmDialog(gatewayView.getFrame(),
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
	    {	
			gatewayTransmitter.closeConnection();
	        System.exit(0);
	    }
		else
		{
			System.out.println("cancel exit program");
		}
	}
}
