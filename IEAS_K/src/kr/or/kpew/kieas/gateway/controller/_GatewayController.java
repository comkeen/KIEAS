package kr.or.kpew.kieas.gateway.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.JOptionPane;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.ITransmitter;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasList;
import kr.or.kpew.kieas.gateway.model.GatewayAlertSystemInfoTableModel;
import kr.or.kpew.kieas.gateway.model.GatewayAlertTable;
import kr.or.kpew.kieas.gateway.model.GatewayAlerterInfoTableModel;
import kr.or.kpew.kieas.gateway.model._GatewayModel;
import kr.or.kpew.kieas.gateway.view.GatewayView;


public class _GatewayController
{
	private static final long DELAY = 1000;
	
	private static _GatewayController gatewayController;

	private IKieasMessageBuilder kieasMessageBuilder;	
	private ITransmitter transmitter;

	private GatewayActionListener gatewayActionListener;
	private GatewayView gatewayView;
	private _GatewayModel gatewayModelManager;

	private String gatewayId;


	public static _GatewayController getInstance()
	{
		if (gatewayController == null)
		{
			gatewayController = new _GatewayController();
		}
		return gatewayController;
	}

	private _GatewayController()
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		this.transmitter = new GatewayTransmitter(this);		
		
		this.gatewayActionListener = new GatewayActionListener(this);
		this.gatewayModelManager = new _GatewayModel(this);
		this.gatewayView = GatewayView.getInstance(this, gatewayActionListener);

		init();
	}
	
	private void init()
	{
		this.setID();
		
		openGateway();
	}

	public void openGateway()
	{
		transmitter.openConnection();
		
		String log = "(" + gatewayId + ")" + " Open";
		System.out.println(log);
		gatewayView.appendLog(log);
	}

	public void closeGateway()
	{
		transmitter.closeConnection();
		
		String log = "(" + gatewayId + ")" + " Close";
		System.out.println(log);
		gatewayView.appendLog(log);
	}

	public void acceptAleterMessage(String message)
	{
		System.out.println("gateway accept message : \n" + message);
		kieasMessageBuilder.setMessage(message);
		String sender = kieasMessageBuilder.getSender();
		String identifier = kieasMessageBuilder.getIdentifier();
		String status = kieasMessageBuilder.getStatus();
		System.out.println("status : " + status);
		String restriction = kieasMessageBuilder.getRestriction();
		
//		String sender = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.SENDER);
//		String identifier = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.IDENTIFIER);
//		String status = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.STATUS);

		String log = "(" + gatewayId + ")" + " Received Message From Alerter (" + sender + ") : ";
		System.out.println(log);
		gatewayView.appendLog(log + identifier);
	
		switch (status)
		{
		case "ACTUAL":
			try 
			{
				gatewayModelManager.addAlertLogTableRow(message);
				gatewayModelManager.addAlerterInfoTableRow(message);

				String ackMessage = gatewayModelManager.creatAckMessage(message, gatewayId);
				sendAcknowledge(ackMessage, sender);
				
				log = "(" + gatewayId + ")" + " Send Message To AlertSystemType For (" + restriction + ") : ";
				System.out.println(log);
				gatewayView.appendLog(log);				
				
				transmitter.sendTopicMessage(message, restriction);
//				routeMessage(message);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		case  "SYSTEM":
			try 
			{
				log = "(" + gatewayId + ")" + " Received Register request From Alerter : " + identifier;
				System.out.println(log);
				gatewayView.appendLog(log);
				
				//approveRegister();
				gatewayModelManager.addAlerterInfoTableRow(message);
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

	public void acceptAletSystemMessage(String message)
	{
		kieasMessageBuilder.setMessage(message);
		String sender = kieasMessageBuilder.getSender();
		String identifier = kieasMessageBuilder.getIdentifier();
		String status = kieasMessageBuilder.getStatus();
		
		switch (status)
		{	
		case "ACTUAL":
			try 
			{
				String log = "(" + gatewayId + ")" + " Received Message From AlertSystem : " + identifier;
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
				String log = "(" + gatewayId + ")" + " Received Register request From AlertSystem : " + identifier;
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
		transmitter.sendMessage(createAckMessage(message), destination);

		String log = "(" + gatewayId + ")" + " Send Acknowledge to ("+ destination +") : ";
		System.out.println(log);
		gatewayView.appendLog(log);
	}
	
	private String createAckMessage(String message)
	{		
		kieasMessageBuilder.setMessage(message);
		kieasMessageBuilder.setIdentifier(kieasMessageBuilder.generateKieasMessageIdentifier(gatewayId));
		kieasMessageBuilder.setSender(gatewayId);
		kieasMessageBuilder.setStatus(KieasMessageBuilder.SYSTEM);
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ACK);
		System.out.println("GW : create Ack Message");
		
		return kieasMessageBuilder.getMessage();
	}
//
//	private void broadcastMessage(String message)
//	{
//		String alertSystemType = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.RESTRICTION);
//		String event = gatewayModelManager.getAlertElementMap(message).get(GatewayModelManager.EVENT);
//		
//		for (String item : KieasList.ALERT_SYSTEM_TYPE_LIST)
//		{
//			if (item.equals(alertSystemType))
//			{
//				transmitter.sendMessage(message, alertSystemType);	
//
//				String log = "(" + gatewayId + ")" + " Broadcast Message To ("+ alertSystemType +") : ";
//				System.out.println(log);
//				gatewayView.appendLog(log + event);
//			}
//		}
//	}

	private void routeMessage(String message)
	{
		String geoCode = gatewayModelManager.getAlertElementMap(message).get(_GatewayModel.GEO_CODE);
		
		for (String item : KieasList.GEO_CODE_LIST)
		{
			if (item.equals(geoCode))
			{
				transmitter.sendMessage(message, geoCode);	

				String log = "(" + gatewayId + ")" + " Route Message To ("+ geoCode +") : ";
				System.out.println(log);
				gatewayView.appendLog(log);
			}
		}
	}

	public void clearLog()
	{
		gatewayView.clearLog();
	}

	public GatewayAlertTable getAlertTableModel()
	{
		return gatewayModelManager.getAlertTableModel();
	}

	public String getName()
	{
		return gatewayId;
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
			transmitter.closeConnection();
	        System.exit(0);
	    }
		else
		{
			System.out.println("cancel exit program");
		}
	}
	
	public void setID()
	{
		this.gatewayId = "통합게이트웨이";
		this.gatewayId = getLocalServerIp() + "/gatewayId";
		
		transmitter.addReceiver(gatewayId);
		gatewayView.setId(gatewayId);
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

	public String getId()
	{
		return this.gatewayId;
	}
}
