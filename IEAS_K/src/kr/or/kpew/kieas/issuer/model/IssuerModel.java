package kr.or.kpew.kieas.issuer.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.ITransmitter;
import kr.or.kpew.kieas.common.Item;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.issuer.view.IssuerView;

public class IssuerModel extends Observable
{
	private IKieasMessageBuilder kieasMessageBuilder;
	private ITransmitter transmitter;
	
	private XmlReaderAndWriter xmlReaderAndWriter;
	private AlertLogger alertLogger;
	private IssuerProfile componentProfile;
	
	private String mAlertMessage;
	
		
	/**
	 * AlerterModel들을 관리한다.
	 * CAP 메시지 처리를 위한 KieasMessageBuilder 초기화.
	 * Database 접근을 위한 DatabaseHandler 초기화.
	 * @param alerterController Controller
	 */
	public IssuerModel()
	{
		this.transmitter = new Transmitter(this);
		this.alertLogger = new AlertLogger();
		this.xmlReaderAndWriter = new XmlReaderAndWriter();
		this.componentProfile = new IssuerProfile();

		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		init();
	}
	
	private void init()
	{	
		String id = generateAndSetID();
		componentProfile.setId(id);
		transmitter.addReceiver(id);
	}
	
	public String generateAndSetID()
	{
		Integer randomIntegerer = new Integer(new Random().nextInt(9999));
		String str = randomIntegerer.toString();
		if(str.length() < 4)
		{
			for(int i = 0; i < 4 - str.length(); i++)
			{
				str = "0" + str;
			}
		}
		String alerterId = getLocalServerIp() + ":" + Integer.parseInt(str) + "/alerterId";		
		
		notifyObservers(alerterId);
		
		return alerterId;
	}
	

	public void registerToGateway()
	{
		String id = generateAndSetID();
		
		kieasMessageBuilder.buildDefaultMessage();

		kieasMessageBuilder.setIdentifier(kieasMessageBuilder.generateKieasMessageIdentifier(id));
		kieasMessageBuilder.setSender(id);
		kieasMessageBuilder.setSent(kieasMessageBuilder.getDate());
		kieasMessageBuilder.setStatus(KieasMessageBuilder.SYSTEM);
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ALERT);
		kieasMessageBuilder.setRestriction(KieasMessageBuilder.RESTRICTED);
		kieasMessageBuilder.setRestriction("Alerter");
		kieasMessageBuilder.build();
		
		transmitter.sendMessage(kieasMessageBuilder.getMessage(), KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);	
		
		String log = "(" + id + ")" + " Register to Gateway :";
		System.out.println(log);
	}

	public void sendMessage()
	{
		String message = mAlertMessage;
		
		transmitter.sendMessage(message, KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);

		kieasMessageBuilder.setMessage(message);
		String identifier = kieasMessageBuilder.getIdentifier();
		alertLogger.saveAlertLog(identifier, message);
		
		System.out.println("Send Message to " + "Gateway : ");		
	}
	
	public void acceptMessage(String message)
	{
		try 
		{
			System.out.println("Message Received");
			kieasMessageBuilder.setMessage(message);
			alertLogger.saveAlertLog(kieasMessageBuilder.getIdentifier(), message);
//			String sender = kieasMessageBuilder.getSender();
//			String identifier = kieasMessageBuilder.getIdentifier();

			System.out.println();
//			if(sender.equals(KieasConfiguration.KieasName.GATEWAY_NAME))
//			{
//				alerterModelManager.receiveGatewayAck(identifier);
//			}
//			else 
//			{
//				alerterTopView.receiveAlertSystemAck(identifier);
//			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadCap(String path)
	{
		this.setAlertMessage(xmlReaderAndWriter.loadXml(path));
		
		String target = IssuerView.TEXT_AREA;
		String value = mAlertMessage;
		
		setChanged();
		notifyObservers(new Item(target, value));
	}	
	
	public void writeCap(String path, String message)
	{ 
		xmlReaderAndWriter.writerXml(path, message);
	}
	
	public void receiveGatewayAck(String identifier)
	{
		alertLogger.receiveAck(identifier, AlertLogger.ACK);
	}
	
	
	public void updateView(String view, String target, String value) 
	{
//		controller.updateView(view, target, value);
	}
	
	public void updateView(String view, String target, List<String> value) 
	{
//		controller.updateView(view, target, value);
	}


	
//	public List<String> getQueryResult(String target, String query)
//	{	
//		System.out.println("getQuery target, query : " + target + " " + query);
//		List<String> result = kieasMessageBuilder.convertDbToCap(databaseHandler.getQueryResult(target, query.toUpperCase()));
//		for (String string : result)
//		{
//			System.out.println("query result = " + string);
//		}
//		switch (target)
//		{
//		case KieasMessageBuilder.EVENT_CODE:
//			alerterDataBasePanelModel.setQueryResult(result);
//			break;
//		case KieasMessageBuilder.STATUS:
//			
//			break;	
//		default:
//			break;
//		}
//		return result;	
//	}	
	
//	public void insertDataBase(String message)
//	{
//		databaseHandler.insertCap(kieasMessageBuilder.convertCapToDb(message));
//	}

	public void setAlertMessage(String message)
	{
		this.mAlertMessage = message;
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

	public void closeConnection()
	{
		transmitter.closeConnection();
	}
}
