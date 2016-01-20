package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasAddress;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasName;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlertSystemController
{
	private AlertSystemTransmitter transmitter;
	private AlertSystemView view;
	private AlertSystemActionListener actionListener;
	private KieasMessageBuilder ieasMessage;

	private String alertSystemID;
	private String alertSystemType;
	private String geoCode;
	private String ackMessage;

	public static final String SYSTEM = "System";
	public static final String GEO_CODE = "GeoCode";
	public static final String ALERT_SYSTEM_TYPE = "AlertSystemType";
	public static final String RESTRICTED = "Restricted";
	
	public static final long DELAY = 1000;
	

	public AlertSystemController()
	{		
		this.ieasMessage = new KieasMessageBuilder();
		this.actionListener = new AlertSystemActionListener(this);
		this.view = new AlertSystemView(this);
		this.transmitter = new AlertSystemTransmitter(this);
		init();
	}
	
	private void init()
	{
//		this.alertSystemID = KieasName.STANDARD_ALERT_SYSTEM;
		setID();
				
//		alertSystemTransmitter.setGeoCode(alertSystemView.getSelectedGeoCode());
		transmitter.setAlertSystemType(view.getSelectedAlertSystemType());
		transmitter.openConnection();
	}
	
	public void setID()
	{
		this.alertSystemID = getLocalServerIp() + ":" + new Random().nextInt(9999) + "/" + view.getSelectedAlertSystemType();
		this.alertSystemType = view.getSelectedAlertSystemType();
		view.setAlertSystemId(alertSystemID);
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
		catch (SocketException ex) {}
		return null;
	}

	public void acceptMessage(String message) 
	{
		System.out.println("received message " + message);
		try
		{
			ieasMessage.setMessage(message);

			String sender = ieasMessage.getSender();
			
			System.out.println("(" + alertSystemID + ")" + " Received Message From (" + sender + ") : ");
			System.out.println();
			
			view.setTextArea(message);
			sendAckMessage(message, KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void sendAckMessage(String message, String destination)
	{		
		try
		{
			Thread.sleep(DELAY);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		ackMessage = createAckMessage(message);
		transmitter.sendMessage(ackMessage, destination);
		
		StringBuffer log = new StringBuffer();
		log.append("(")
			.append(alertSystemID)
			.append(")")
			.append(" Send Message to Gateway :");
		System.out.println(log.toString());
	}
	
	private String createAckMessage(String message)
	{
		ieasMessage.setMessage(message);
		
		ieasMessage.setAddresses(ieasMessage.getSender());
		ieasMessage.setMsgType("Ack");
		ieasMessage.setSender(alertSystemID);
		ieasMessage.build();
		
		return ieasMessage.getMessage();
	}

	public String getID()
	{		
		return this.alertSystemID;
	}

	public void selectTopic(String target, String topic)
	{
		switch (target)
		{
		case GEO_CODE:
		{
			transmitter.selectGeoCodeTopic(topic);
			break;
		}
		case ALERT_SYSTEM_TYPE:
		{
			transmitter.selectAlertSystemTypeTopic(topic);
			break;
		}
		default:
			break;
		}
	}

	public String getLocation()	{ return geoCode; }

	public void closeConnection() {	transmitter.closeConnection(); }

	public void clear() { view.setTextArea("");	}

	public void registerToGateway()
	{
		ieasMessage.buildDefaultMessage();
		ieasMessage.setSender(alertSystemID);
		ieasMessage.setStatus(SYSTEM);
		ieasMessage.setScope(RESTRICTED);
		ieasMessage.setRestricion(alertSystemType);
		ieasMessage.build();
		System.out.println(ieasMessage.getMessage());
		
		transmitter.sendMessage(ieasMessage.getMessage(), KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);	
		
		StringBuffer log = new StringBuffer();
		log.append("(")
			.append(alertSystemID)
			.append(")")
			.append(" Register to Gateway :");
		System.out.println(log.toString());
	}

	public void exit() { view.systemExit();	}

	public AlertSystemActionListener getActionListener() { 
		System.out.println("getaction");
		return this.actionListener; }
}
