package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.alertsystem.AlertSystemManager;
import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.common.Profile.AlertSystemType;
import kr.or.kpew.kieas.gateway.controller.GatewayManager;
import kr.or.kpew.kieas.issuer.controller.IssuerManager;
import kr.or.kpew.kieas.network.ITransmitter;
import kr.or.kpew.kieas.network.jms.AlertSystemTransmitter;
import kr.or.kpew.kieas.network.jms.GatewayTransmitter;
import kr.or.kpew.kieas.network.jms.IssuerTransmitter;

/**
 * 1개의 경보발령대, 1개의 통합게이트웨이, 1개의 경보시스템을 동시에 실행시켜주는 메인 클래스.
 * 각 콤포넌트간 통신을 위해 ActiveMQ를 먼저 실행하고 이 프로젝트를 실행시키자.
 *
 */

public class IntegratedAlertSystemMain
{
	
	/**
	 * 통신방법에 대한 enumeration, 이 프로젝트에서는 JMS 방식만 사용.
	 *
	 */	
	public enum TransmitterType
	{
		JMS, TCPIP, XMPP, AMQP
	}
	
	public IntegratedAlertSystemMain()
	{
		//JMS 통신방식으로 구성요소들 초기화 작업, 발령대, 게이트웨이, 경보시스템 생성
		init(TransmitterType.JMS);
	}
	
	public void init(TransmitterType type)
	{
		//게이트웨이 프로파일: 게이트웨이를 생성하기 위해 필요(식별자,이름)
		Profile gatewayProfile = new Profile("maingateway", "국민안전처");
		//발령대 프로파일: 발령대를 생성하기 위해 필요(식별자,이름)
		IssuerProfile issuerProfile = new IssuerProfile("civilalerter", "민방위");
		//경보시스템 프로파일: 수신기를 생성하기 위해 필요(식별자,지역,경보시스템타입)
		AlertSystemProfile alertSystemProfile = new AlertSystemProfile("townbroadcast085", "경상남도", AlertSystemType.LocalBroadcasting);
		
		//게이트웨이를 초기화하는 클래스 생성(통신모듈생성(통신방식),게이트웨이프로파일)
		GatewayManager gatewayManager = new GatewayManager(createGatewayTransmitter(type), gatewayProfile);
		//경보시스템을 게이트웨이를 통해 사용하기 위해 게이트웨이에 경보시스템 등록(경보시스템프로파일)  
		gatewayManager.registAlertSystem(alertSystemProfile);
		//경보발령대를 게이트웨이를 통해 사용하기 위해 게이트웨이에 경보발령대 등록(경보시스템프로파일)
		gatewayManager.registIssuer(issuerProfile);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//경보시스템을 초기화하는 클래스 생성(통신모듈생성(통신방식),경보시스템프로파일,게이트웨이프로파일)
		new AlertSystemManager(createAlertSystemTransmitter(type), alertSystemProfile, gatewayProfile);
		//발령대를 초기화하는 클래스 생성(통신모듈생성(통신방식),발령대프로파일,게이트웨이프로파일)
		new IssuerManager(createIssuerTransmitter(type), issuerProfile, gatewayProfile);
	}
	
	/**
	 * 게이트웨이 통신모듈 생성 메소드
	 * 
	 * @param 통신 타입
	 * @return 통신모듈 인터페이스
	 *
	 */	
	public ITransmitter createGatewayTransmitter(TransmitterType type)
	{
		switch (type)
		{
		case JMS:
			return new GatewayTransmitter();
		default:
			break;
		}
		return null;
	}

	/**
	 * 경보시스템 통신모듈 생성 메소드
	 * 
	 * @param 통신 타입
	 * @return 통신모듈 인터페이스
	 *
	 */	
	public ITransmitter createAlertSystemTransmitter(TransmitterType type) {
		switch (type) {
		case JMS:
			return new AlertSystemTransmitter();
		default:
			break;
		}
		return null;
	}

	/**
	 * 발령대 통신모듈 생성 메소드
	 * 
	 * @param 통신 타입
	 * @return 통신모듈 인터페이스
	 *
	 */	
	public ITransmitter createIssuerTransmitter(TransmitterType type) {
		switch (type) {
		case JMS:
			return new IssuerTransmitter();
		default:
			break;
		}
		return null;
	}


	public static void main(String[] args)
	{
		new IntegratedAlertSystemMain();
	}
}
