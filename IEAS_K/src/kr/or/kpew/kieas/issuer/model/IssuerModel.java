package kr.or.kpew.kieas.issuer.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.common.IntegratedEmergencyAlertSystem;
import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.Item;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.gateway.view.AlertMessageTable.Responses;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.issuer.model.AlertLogger.MessageAckPair;
import kr.or.kpew.kieas.issuer.view.IssuerView;
import kr.or.kpew.kieas.network.IClientTransmitter;

public class IssuerModel extends IntegratedEmergencyAlertSystem implements IOnMessageHandler
{
	private IKieasMessageBuilder kieasMessageBuilder;
	private IClientTransmitter transmitter;

	private XmlReaderAndWriter xmlReaderAndWriter;
	private AlertLogger alertLogger;
	private IssuerProfile profile;

	private String mAlertMessage;

	/**
	 * AlerterModel들을 관리한다.
	 * CAP 메시지 처리를 위한 KieasMessageBuilder 초기화.
	 * Database 접근을 위한 DatabaseHandler 초기화.
	 * @param transmitter 
	 * @param alerterController Controller
	 */
	public IssuerModel(IClientTransmitter transmitter, IssuerProfile profile)
	{
		super(profile);
		this.transmitter = transmitter;
		transmitter.setOnMessageHandler(this);

		this.alertLogger = new AlertLogger();
		alertLogger.addModel(this);
		this.xmlReaderAndWriter = new XmlReaderAndWriter();
		this.kieasMessageBuilder = new KieasMessageBuilder();

		init();
	}

	private void init()
	{	
		transmitter.init(profile.getSender(), KieasAddress.GATEWAY_ID);
		transmitter.setOnMessageHandler(this);
	}

	/**
	 * 현재 시스템의 IP 주소를 구하기 위한 메소드이다.
	 * 통합경보시스템 고유의 기능은 아니다. 참조모델 구현 시 시스템 ID를 IP 주소를 이용하여 생성하므로 필요하다. 
	 * @return 현재 시스템의 IP 주소(ex: 123.123.123.123)
	 */
	protected String getLocalServerIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	public void loadCap(String path)
	{
		kieasMessageBuilder.parse(xmlReaderAndWriter.loadXml(path));
		kieasMessageBuilder.setIdentifier(createMessageId());
		String message = kieasMessageBuilder.getMessage();

		setAlertMessage(message);

		notifyItemToObservers(IssuerView.CAP_ELEMENT_PANEL, message);
	}	

	public void sendMessage()
	{	
		kieasMessageBuilder.parse(mAlertMessage);

		kieasMessageBuilder.setIdentifier(createMessageId());
		kieasMessageBuilder.setSent();

		String message = kieasMessageBuilder.getMessage();

		setAlertMessage(message);
		
		System.out.println("AO: Save Alert Log");	
		alertLogger.saveAlertLog(message);

		System.out.println("AO: Send Message to " + "Gateway : ");		
		transmitter.send(stringToByte(message));	
		
		updateIdentifier(message);				
	}

	@Override
	public void onMessage(String senderAddress, byte[] data)
	{
		System.out.println("AO: received from GW: " + senderAddress);
		String message = new String(data);

		kieasMessageBuilder.parse(message);
		String msgType = kieasMessageBuilder.getMsgType().toString();
		String status = kieasMessageBuilder.getStatus().toString();
		String references = kieasMessageBuilder.getReferences();

		String[] tokens = references.split(",");
		String sender = tokens[0];		
		String identifier = tokens[1];
		String sent = tokens[2];
		
		switch (msgType)
		{
		case "ACK":
			alertLogger.saveAckLog(message);
			if(alertLogger.loadAlertLogState(identifier).equals(Responses.COMP.toString()))
			{
				notifyItemToObservers(IssuerView.ACK, identifier);				
			}
			break;
		default:
			break;
		}
	}


	private void updateIdentifier(String message)
	{
		notifyItemToObservers(IssuerView.IDENTIFIER, message);
	}

	public void updateTable(MessageAckPair pair)
	{
		notifyItemToObservers(IssuerView.TABLE, pair);
	}

	private void notifyItemToObservers(String target, Object value)
	{
		setChanged();
		if(value instanceof String)
		{
			notifyObservers(new Item(target, (String) value));		
		}
		else if(value instanceof MessageAckPair)
		{
			notifyObservers((MessageAckPair) value);
		}
	}

	public void writeCap(String path, String message)
	{ 
		xmlReaderAndWriter.writerXml(path, message);
	}

	public void setAlertMessage(String message)
	{
		this.mAlertMessage = message;
		notifyItemToObservers(IssuerView.TEXT_AREA, message);
	}	

	public void closeConnection()
	{
		transmitter.close();
	}

	@Override
	public void setProfile(Profile profile)
	{
		this.profile = (IssuerProfile)profile;
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public void onRegister(String sender, String address) {
		// TODO Auto-generated method stub

	}

	public void setSelectedAlertLog(String identifier)
	{
		notifyItemToObservers(IssuerView.TEXT_AREA, alertLogger.loadAlertLog(identifier));
	}
}
