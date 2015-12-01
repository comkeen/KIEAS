package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasAddress;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasName;


public class AlertSystemController
{
	private KieasMessageBuilder ieasMessage;
	private AlertSystemTransmitter alertSystemTransmitter;
	private AlertSystemView alertSystemView;

	private String alertSystemID;
	private String geoCode;
	private String ackMessage;
	
	public static final String GEO_CODE = "GeoCode";
	public static final String ALERT_SYSTEM_TYPE = "AlertSystemType";
	
	public static final long DELAY = 1000;
	

	public AlertSystemController()
	{		
		this.ieasMessage = new KieasMessageBuilder();
		this.alertSystemView = new AlertSystemView(this);
		this.alertSystemTransmitter = new AlertSystemTransmitter(this);
		
		init();
	}
	
	private void init()
	{
		this.alertSystemID = KieasName.STANDARD_ALERT_SYSTEM;
		
		alertSystemView.setAlertSystemId(alertSystemID);	
		
//		alertSystemTransmitter.setGeoCode(alertSystemView.getSelectedGeoCode());
		alertSystemTransmitter.setAlertSystemType(alertSystemView.getSelectedAlertSystemType());
		alertSystemTransmitter.openConnection();
	}

	public void acceptMessage(String message) 
	{
		try
		{
			ieasMessage.setMessage(message);

			String sender = ieasMessage.getSender();
			
			System.out.println("(" + alertSystemID + ")" + " Received Message From (" + sender + ") : ");
			System.out.println();
			
			alertSystemView.setTextArea(message);
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
		alertSystemTransmitter.sendMessage(ackMessage, destination);

		System.out.println("(" + alertSystemID + ")" + " Send Message to Gateway :");
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
			alertSystemTransmitter.selectGeoCodeTopic(topic);
			break;
		}
		case ALERT_SYSTEM_TYPE:
		{
			alertSystemTransmitter.selectAlertSystemTypeTopic(topic);
			break;
		}
		default:
			break;
		}
	}

	public String getLocation()
	{
		return geoCode;
	}

	public void closeConnection()
	{
		alertSystemTransmitter.closeConnection();
	}
}
