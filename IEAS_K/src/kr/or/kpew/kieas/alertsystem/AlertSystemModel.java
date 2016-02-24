package kr.or.kpew.kieas.alertsystem;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Random;

import kr.or.kpew.kieas.common.KieasConfiguration;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;


public class AlertSystemModel extends Observable
{
	private AlertSystemTransmitter transmitter;
	private AlertSystemProfile profile;
	//public AlertSystemView view;
//	private AlertSystemController controller;
	private KieasMessageBuilder kieasMessageBuilder;

//	private String alertSystemID;
//	private String alertSystemType;
//	private String geoCode;
	//private String ackMessage;

	public static final String GEO_CODE = "GeoCode";
	public static final String ALERT_SYSTEM_TYPE = "AlertSystemType";
	
	public static final long DELAY = 1000;
	

	public AlertSystemModel()
	{		
		this.kieasMessageBuilder = new KieasMessageBuilder();

		this.transmitter = new AlertSystemTransmitter(this);
		this.profile = new AlertSystemProfile();
		
		//init();
	}
	
	public void init()
	{
//		this.geoCode = "1100000000";
//		this.geoCode = "5000000000";
		
//		this.alertSystemID = KieasName.STANDARD_ALERT_SYSTEM;
//		profile.setAlertSystemType(view.getSelectedAlertSystemType());
		profile.setAlertSystemType(KieasConfiguration.KieasList.ALERT_SYSTEM_TYPE_LIST[0]);
		profile.setGeoCode("1100000000");
		profile.setAlertSystemId(getLocalServerIp() + ":" + new Random().nextInt(9999) + "/" + profile.getAlertSystemType() + "/" + profile.getGeoCode());
				
//		alertSystemTransmitter.setGeoCode(alertSystemView.getSelectedGeoCode());
<<<<<<< HEAD
		//transmitter.setAlertSystemType(view.getSelectedAlertSystemType());
		transmitter.openConnection();
		
		selectTopic(AlertSystemModel.ALERT_SYSTEM_TYPE, profile.getAlertSystemType());
		selectTopic(AlertSystemModel.GEO_CODE, profile.getGeoCode());

		setID();
		
		setChanged();
		notifyObservers(profile);
=======
		transmitter.setAlertSystemType(view.getSelectedAlertSystemType());
//		transmitter.openConnection();
>>>>>>> 5b8b750e45383dc4a0462a12d6999202edf8f6a1
	}
	
	public void setID()
	{
<<<<<<< HEAD
		profile.setAlertSystemId(getLocalServerIp() + ":" + new Random().nextInt(9999) + "/" + profile.getAlertSystemType() + "/" + profile.getGeoCode());
		transmitter.setId(profile.getAlertSystemId());
		

		//view.setAlertSystemId(alertSystemID);
		
		
=======
		this.alertSystemID = getLocalServerIp() + ":" + new Random().nextInt(9999) + "/" + view.getSelectedAlertSystemType() + "/" + geoCode;
		this.alertSystemType = view.getSelectedAlertSystemType();
		transmitter.setId(alertSystemID);
		transmitter.setAlertSystemType(alertSystemType);
		view.setAlertSystemId(alertSystemID);
>>>>>>> 5b8b750e45383dc4a0462a12d6999202edf8f6a1
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
		//System.out.println("received message " + message);
		try
		{
			kieasMessageBuilder.setMessage(message);

			String sender = kieasMessageBuilder.getSender();
			
			System.out.println("(" + profile.getAlertSystemId() + ")" + " Received Message From (" + sender + ") : ");
			System.out.println();
			
			
			//view.setTextArea(message);
			setChanged();
			notifyObservers(message);
//			sendAckMessage(message, KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
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

		String ackMessage = createAckMessage(message);
		transmitter.sendMessage(ackMessage, destination);
		
		StringBuffer log = new StringBuffer();
		log.append("(")
			.append(profile.getAlertSystemId())
			.append(")")
			.append(" Send Message to Gateway :");
		System.out.println(log.toString());
	}
	
	private String createAckMessage(String message)
	{
		kieasMessageBuilder.setMessage(message);

		kieasMessageBuilder.setSender(profile.getAlertSystemId());
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ACK);
		kieasMessageBuilder.build();
		
		return kieasMessageBuilder.getMessage();
	}

	public void selectTopic(String target, String topic)
	{
		//System.out.println("select topic: " + target + ", " + topic);
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

	//public String getLocation()	{ return geoCode; }

	public void closeConnection() {	transmitter.closeConnection(); }

	//public void clear() { view.setTextArea("");	}

	public void registerToGateway()
	{
		kieasMessageBuilder.buildDefaultMessage();
		kieasMessageBuilder.setSender(profile.getAlertSystemId());
		kieasMessageBuilder.setStatus(KieasMessageBuilder.SYSTEM);
		kieasMessageBuilder.setScope(KieasMessageBuilder.RESTRICTED);
		kieasMessageBuilder.setRestriction(profile.getAlertSystemType());
		kieasMessageBuilder.setNote(profile.getGeoCode());
		kieasMessageBuilder.build();
		System.out.println(kieasMessageBuilder.getMessage());
		
		transmitter.sendMessage(kieasMessageBuilder.getMessage(), KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);	
		
		StringBuffer log = new StringBuffer();
		log.append("(")
			.append(profile.getAlertSystemId())
			.append(")")
			.append(" Register to Gateway :");
		System.out.println(log.toString());
	}

	public void readyForExit() { 
		closeConnection();
//		view.systemExit();
	}

//	public AlertSystemController getActionListener()
//	{ 
//		System.out.println("getAction");
//		return this.controller;
//	}
}
