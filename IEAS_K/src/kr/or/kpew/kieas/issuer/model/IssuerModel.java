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
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.gateway.view.AlertMessageTable.Responses;
import kr.or.kpew.kieas.issuer.model.AlertLogger.MessageAckPair;
import kr.or.kpew.kieas.issuer.view.IssuerView;
import kr.or.kpew.kieas.network.IClientTransmitter;

public class IssuerModel extends IntegratedEmergencyAlertSystem implements IOnMessageHandler
{
	private IKieasMessageBuilder kieasMessageBuilder;
	private IClientTransmitter transmitter;
	private IssuerProfile profile;

	private XmlReaderAndWriter xmlReaderAndWriter;
	private AlertLogger alertLogger;

	private String mAlertMessage;

	/**
	 * 경보발령대의 주요 기능들을 관리한다.
	 * CAP 메시지 처리를 위한 KieasMessageBuilder 초기화.
	 * Database 접근을 위한 DatabaseHandler 초기화.
	 * 
	 * @param transmitter 발령대에서 사용할 통신방식을 규정한다. 통신방식은 TCP/IP와 JMS 두가지 방법을 활용할 수 있게 고안되었으나 현재는 JMS만을 사용한다.
	 * @param profile 경보발령대에서 사용하는 발령대프로필은 프로필은 상속하여 구현되어있다. 프로필은 통신에서 라우팅 기능을 위해 사용된다. 프로필에 대한 정보는 kr.or.kpew.kieas.common.Profile을 참고하라.
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
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
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
		transmitter.send(message);
		
		updateIdentifier(message);
	}

	@Override
	public void onMessage(String senderAddress, String message)
	{
		System.out.println("AO: Received from GW: " + senderAddress);

		kieasMessageBuilder.parse(message);
		String msgType = kieasMessageBuilder.getMsgType().toString();
		String references = kieasMessageBuilder.getReferences();

		String[] parsedReferences = kieasMessageBuilder.parseReferences(references);
//		String sender = parsedReferences[0];
//		String identifier = parsedReferences[1];
//		String sent = parsedReferences[2];
		
		switch (msgType)
		{
		case "ACK":
			alertLogger.saveAckLog(message);
			if(alertLogger.loadAlertLogState(parsedReferences[1]).equals(Responses.COMP.toString()))
			{
				notifyItemToObservers(IssuerView.ACK, parsedReferences[1]);				
			}
			break;
		default:
			System.out.println("AO: Received Message msgType " + msgType);
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
	public void onRegister(String sender, String address)
	{
		// TODO Auto-generated method stub
	}

	public void setSelectedAlertLog(String identifier)
	{
		notifyItemToObservers(IssuerView.TEXT_AREA, alertLogger.loadAlertLog(identifier));
	}
	
	/**
	 * 지정한 경로의 cap형식의 xml 파일을 읽어오는 메소드.
	 * 현재는 사용되지 않는다.
	 * @param path (ex:"/cap/cap.xml")
	 * 
	 */
	public void loadCap(String path)
	{
		kieasMessageBuilder.parse(xmlReaderAndWriter.loadXml(path));
		kieasMessageBuilder.setIdentifier(createMessageId());
		String message = kieasMessageBuilder.getMessage();
		
		setAlertMessage(message);
		
		notifyItemToObservers(IssuerView.CAP_ELEMENT_PANEL, message);
	}	
}
