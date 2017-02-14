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
import kr.or.kpew.kieas.common.KieasConfiguration.KieasConstant;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.gateway.view.AlertMessageTable.Responses;
import kr.or.kpew.kieas.issuer.model.AlertLogger.MessageAckPair;
import kr.or.kpew.kieas.issuer.view.IssuerView;
import kr.or.kpew.kieas.network.ITransmitter;

public class IssuerModel extends IntegratedEmergencyAlertSystem implements IOnMessageHandler
{
	private IKieasMessageBuilder kieasMessageBuilder;
	private ITransmitter transmitter;
	private IssuerProfile profile;

	private XmlReaderAndWriter xmlReaderAndWriter;
	private AlertLogger alertLogger;

	private String mAlertMessage;
	private String gatewayName;

	/**
	 * 경보발령대의 주요 기능들을 관리한다.
	 * CAP 메시지 처리를 위한 KieasMessageBuilder 초기화.
	 * Database 접근을 위한 DatabaseHandler 초기화.
	 * 
	 * @param transmitter 발령대에서 사용할 통신방식을 규정한다. 통신방식은 TCP/IP와 JMS 두가지 방법을 활용할 수 있게 고안되었으나 현재는 JMS만을 사용한다.
	 * @param profile 경보발령대에서 사용하는 발령대프로필은 프로필은 상속하여 구현되어있다. 프로필은 통신에서 라우팅 기능을 위해 사용된다. 프로필에 대한 정보는 kr.or.kpew.kieas.common.Profile을 참고하라.
	 */
	public IssuerModel(ITransmitter transmitter, IssuerProfile profile)
	{
		super(profile);
		this.transmitter = transmitter;
		transmitter.setOnMessageHandler(this);
		System.out.println("AO: set issuer sender : " + profile.getSender());
		transmitter.init(profile.getSender());
		
		//발령한 경보와 수신한 수신응답 메시지를 저장한다.
		this.alertLogger = new AlertLogger();
		alertLogger.addModel(this);
		
		//cap 형식의 xml파일을 읽거나 쓰는 기능을 수행한다.
		this.xmlReaderAndWriter = new XmlReaderAndWriter();
		
		//cap 형식의 메시지를 파싱하기 위해 사용된다.
		this.kieasMessageBuilder = new KieasMessageBuilder();
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

	/**
	 * alertLogger에 발령하는 메시지를 저장하고 transmitter에 지정되있는 대상으로 메시지를 송신한다.
	 */
	public void sendMessage()
	{	
		kieasMessageBuilder.parse(mAlertMessage);

		kieasMessageBuilder.setProfile(profile);
		kieasMessageBuilder.setIdentifier(createMessageId());
		kieasMessageBuilder.setSent();

		String message = kieasMessageBuilder.getMessage();

		setAlertMessage(message);
		
		alertLogger.saveAlertLog(message);

		System.out.println("AO: Send Message to GW : ");		
		transmitter.sendTo(gatewayName, message);
		
		updateIdentifier(message);
	}

	/**
	 * transmitter에서 메시지를 수신했을 때 이 메소드로 처리한다.
	 */
	@Override
	public void onMessage(String message)
	{
		System.out.println("AO: Received Message from GW");

		kieasMessageBuilder.parse(message);
		String msgType = kieasMessageBuilder.getMsgType().toString();
		String references = kieasMessageBuilder.getReferences();

		String[] parsedReferences = kieasMessageBuilder.parseReferences(references);
//		String sender = parsedReferences[0];
		String identifier = parsedReferences[1];
//		String sent = parsedReferences[2];
		
		switch (msgType)
		{
		case KieasConstant.ACK:
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

	/**
	 * 이미 발령된 경보메시지 identifier를 갱신하기위해 사용되는 메소드.
	 * @param message
	 */
	private void updateIdentifier(String message)
	{
		notifyItemToObservers(IssuerView.IDENTIFIER, message);
	}

	/**
	 * 경보발령과 수신응답 메시지 수신에 따른 경보로그 테이블의 정보를 갱신하기 위해 사용되는 메소드.
	 * @param pair
	 */
	public void updateTable(MessageAckPair pair)
	{
		notifyItemToObservers(IssuerView.TABLE, pair);
	}

	/**
	 * 경보발령대의 Model과 View는 Observer Patter을 적용하여 연결되어있다.
	 * @param target 갱신되어야 할 View의 대상을 지정한다. 
	 * @param value 갱신되는 새로운 정보를 지정한다.
	 */
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

	/**
	 * 프로그램에서 작성되어 있는 cap형식을 xml 파일로 쓰기 위해 사용되는 메소드.
	 * @param path 파일로 저장되어질 이름을 의미한다.
	 * @param message xml 파일로 쓰여질 내용을 의미한다.
	 */
	public void writeCap(String path, String message)
	{ 
		xmlReaderAndWriter.writerXml(path, message);
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
		kieasMessageBuilder.setSender(profile.getSender());
		String message = kieasMessageBuilder.getMessage();
		
		setAlertMessage(message);
		
		notifyItemToObservers(IssuerView.CAP_ELEMENT_PANEL, message);
	}	

	/**
	 * 현재 로드되어 있는 cap 경보 메시지를 지정한다.
	 * @param message 현재 로드되어 있는 cap 경보 메시지.
	 */
	public void setAlertMessage(String message)
	{
		this.mAlertMessage = message;
		notifyItemToObservers(IssuerView.TEXT_AREA, message);
	}	

	/**
	 * transmitter의 커넥션을 닫는다.
	 */
	public void closeConnection()
	{
		transmitter.close();
	}

	/**
	 * 경보발령대의 프로필을 지정한다.
	 */
	@Override
	public void setProfile(Profile profile)
	{
		this.profile = (IssuerProfile)profile;
	}

	/**
	 * 경보발령대의 프로필을 가져온다.
	 */
	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public void onRegister(String sender, String address)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * 현재 선택된 경보로그 테이블의 행의 identifier를 식별하여 대상이되는 경보로그를 TextArea에 갱신하는 메소드.
	 * @param identifier
	 */
	public void setSelectedAlertLog(String identifier)
	{
		notifyItemToObservers(IssuerView.TEXT_AREA, alertLogger.loadAlertLog(identifier));
	}

	public void setGatewayName(String gateway) {
		this.gatewayName = gateway;
		
	}
}
