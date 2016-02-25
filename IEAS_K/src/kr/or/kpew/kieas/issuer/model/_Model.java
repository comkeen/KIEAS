package kr.or.kpew.kieas.issuer.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.ITransmitter;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.issuer.controller._Controller;
import kr.or.kpew.kieas.issuer.view.resource.AlertLogTableModel;

public class _Model extends Observable
{
	private _Controller controller;
	
	private IKieasMessageBuilder kieasMessageBuilder;
	private ITransmitter transmitter;
//	private _DatabaseHandler databaseHandler;	
	
	private AlertGenerator alerterCapGeneratePanelModel;
	private AlertLogManager alertLogManager;
//	private AlerterDataBasePanelModel alerterDataBasePanelModel;
//	private AlerterAlertGeneratePanelModel alerterAlertGeneratePanelModel;
	
	private AlertLogTableModel alertLogTableModel;
	private Map<String, String> alertMessageMap;
	private Map<String, String> alertElementMap;
	
	private String message;

	private String alerterId;
		
	/**
	 * AlerterModel들을 관리한다.
	 * CAP 메시지 처리를 위한 KieasMessageBuilder 초기화.
	 * Database 접근을 위한 DatabaseHandler 초기화.
	 * @param alerterController Controller
	 */
	public _Model()
	{
//		this.controller = controller;
		this.kieasMessageBuilder = new KieasMessageBuilder();

		this.alerterCapGeneratePanelModel = new AlertGenerator(this);
		this.alertLogManager = new AlertLogManager();
		this.transmitter = new Transmitter(this);
		
		init();
	}
	
	private void init()
	{
		this.alertElementMap = new HashMap<String, String>();
		
		this.alertLogTableModel = new AlertLogTableModel();
		this.alerterId = "기상청";
		
		transmitter.addReceiver(alerterId);
		
		initAlertElementMap();
	}
	
	public void setID()
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
		this.alerterId = getLocalServerIp() + ":" + Integer.parseInt(str) + "/alerterId";

		controller.setId(alerterId);
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
		
		transmitter.sendMessage(kieasMessageBuilder.getMessage(), KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);	
		
		StringBuffer log = new StringBuffer();
		log.append("(").append(alerterId).append(")").append(" Register to Gateway :");
		System.out.println(log.toString());
	}

	public void sendMessage()
	{
		String message = alerterCapGeneratePanelModel.getMessage();
//		alerterModelManager.addAlertTableRow();
		transmitter.sendMessage(message, KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
		System.out.println("Alerter Send Message to " + "(gateway) : ");
//		System.out.println(message);
		alertLogManager.put(kieasMessageBuilder.setMessage(message).getIdentifier(), message);
		controller.updateView("AlertLogManager", "Table", message);
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

			alerterCapGeneratePanelModel.setModelProperty("TextArea", message);
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
	
	private void initAlertElementMap()
	{
		alertElementMap.put(KieasMessageBuilder.SENDER, KieasMessageBuilder.SENDER);
		alertElementMap.put(KieasMessageBuilder.IDENTIFIER, KieasMessageBuilder.IDENTIFIER);
		alertElementMap.put(KieasMessageBuilder.SENT, KieasMessageBuilder.SENT);
		alertElementMap.put(KieasMessageBuilder.EVENT, KieasMessageBuilder.EVENT);
		alertElementMap.put(KieasMessageBuilder.RESTRICTION, KieasMessageBuilder.RESTRICTION);
		alertElementMap.put(KieasMessageBuilder.GEO_CODE, KieasMessageBuilder.GEO_CODE);
//		alertElementMap.put(ACK, ack);
	}
	


	public String getMessage() { return message; }
	public void loadCap(String path) { alerterCapGeneratePanelModel.loadCap(path); }	
	public void saveCap(String path) { alerterCapGeneratePanelModel.saveCap(path); }
	
	public void updateView(String view, String target, String value) 
	{
		controller.updateView(view, target, value);
	}
	
	public void updateView(String view, String target, List<String> value) 
	{
		controller.updateView(view, target, value);
	}
	

	public void generateCap(String alertSystemType)
	{		
		kieasMessageBuilder.buildDefaultMessage();
		kieasMessageBuilder.setIdentifier(kieasMessageBuilder.generateKieasMessageIdentifier(alerterId));
		kieasMessageBuilder.setRestriction(alertSystemType);
	
		this.message = kieasMessageBuilder.getMessage();
		controller.setTextArea(message);
	}

	public String getId()
	{
		return kieasMessageBuilder.getIdentifier();
	}
	
	public String getRestriction()
	{
		return kieasMessageBuilder.getRestriction();
	}
	
	public String getEvent()
	{
		return kieasMessageBuilder.getEvent(0);
	}
	
	public String getGeoCode()
	{
		return kieasMessageBuilder.getGeoCode(0, 0);
	}

	public void addAlertTableRow()
	{
		kieasMessageBuilder.setMessage(message);
		alertLogTableModel.addTableRowData(getAlertElementMap(message));
		
		putAlertMessageMap(kieasMessageBuilder.getIdentifier(), message);
	}
	
	public Map<String, String> getAlertElementMap(String message)
	{
		kieasMessageBuilder.setMessage(message);

		alertElementMap.replace(KieasMessageBuilder.SENDER, kieasMessageBuilder.getSender());
		alertElementMap.replace(KieasMessageBuilder.IDENTIFIER, kieasMessageBuilder.getIdentifier());
		alertElementMap.replace(KieasMessageBuilder.SENT, kieasMessageBuilder.getSent());
		alertElementMap.replace(KieasMessageBuilder.EVENT, kieasMessageBuilder.getEvent(0));
		
		if(kieasMessageBuilder.getRestriction() != null)
		{
			alertElementMap.replace(KieasMessageBuilder.RESTRICTION, kieasMessageBuilder.getRestriction());
		}
//		alertElementMap.replace(GEO_CODE, kieasMessageBuilder.getSent());

		return alertElementMap;
	}
	
	public void putAlertMessageMap(String key, String message)
	{
		alertMessageMap.put(key, message);
	}

	public String getAlertMessage(String identifier)
	{
		return alertMessageMap.get(identifier);
	}
	
	public Map<String, String> getAlertMessageMap()
	{
		return alertMessageMap;
	}

	public AlertLogTableModel getAlertTableModel()
	{
		return alertLogTableModel;
	}

	public void receiveGatewayAck(String identifier)
	{
		alertLogTableModel.receiveAck(identifier);
	}

	public void applyAlertElement(Map<String, String> alertElement)
	{
		alerterCapGeneratePanelModel.applyAlertElement(alertElement);
	}

	public String getAlert()
	{
		return alerterCapGeneratePanelModel.getMessage();
	}

	public void setTextArea(String message)
	{
		alerterCapGeneratePanelModel.setModelProperty("TextArea", message);
	}

	public void closeConnection()
	{
		transmitter.closeConnection();
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
		
	//model Push - send counter as part of the message
	public void setValue(int value)
	{
		int counter = value;
		System.out.println("Model init: counter = " + counter);
		setChanged();
		//model Push - send counter as part of the message
		notifyObservers(counter);
		//if using Model Pull, then can use notifyObservers()
		//notifyObservers()

	}
}
