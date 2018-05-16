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
import kr.or.kpew.kieas.network.xmpp.BabblerTransmitter;

/**
 * 1개의 경보발령대와 1개의 통합게이트웨이, 그리고 5개의 경보시스템을 동시에 실행시켜주는 메인 클래스 이다.
 * 통합 테스트는 주로 이 클래스를 사용하여 이루어진다.
 * @author byun-ai
 *
 */

public class IntegratedAlertSystemMain
{
	private Profile gwProfile;

	private AlertSystemProfile aProfile;
	private AlertSystemProfile bProfile;
	private AlertSystemProfile civil;
	private AlertSystemProfile dmb;
	private AlertSystemProfile cbs;

//	private IssuerProfile kma;
	private IssuerProfile civilalertorg;

	public static int xLocation = 800; //set AS View's xLocation
	public static int yLocation = 400; //set AS View's xLocation
	public static final int xIncrement = 50; //set AS View's xLocation
	public static final int yIncrement = 30; //set AS View's yLocation
	
	enum TransmitterType
	{
		JMS, TCPIP, XMPP, AMQP
	}
	
	public IntegratedAlertSystemMain()
	{
		init(TransmitterType.JMS);
	}
	
	public void init(TransmitterType type)
	{
		gwProfile = new Profile("maingateway", "국민안전처");
		
		civilalertorg = new IssuerProfile("civilalerter", "민방위");
//		kma = new IssuerProfile("issuerkma0124@korea.kr", "기상청");
		
		aProfile = new AlertSystemProfile("townbroadcast085", "경상남도", AlertSystemType.LocalBroadcasting);
//		bProfile = new AlertSystemProfile("townbroadcast221@korea.kr", "전라남도", AlertSystemType.LocalBroadcasting);
//		civil = new AlertSystemProfile("civildef@korea.kr", "국민안전처", AlertSystemType.CivelDefense);
//		dmb = new AlertSystemProfile("dmbalert@korea.kr", "국민안전처", AlertSystemType.DmbAlertSystem);
//		cbs = new AlertSystemProfile("cbsalert@korea.kr", "국민안전처", AlertSystemType.CbsAlertSystem);
		
		GatewayManager g = new GatewayManager(createGatewayTransmitter(type), gwProfile);

		g.registAlertSystem(aProfile);
//		g.registAlertSystem(bProfile);
//		g.registAlertSystem(civil);
//		g.registAlertSystem(dmb);
//		g.registAlertSystem(cbs);

		g.registIssuer(civilalertorg);
//		g.registIssuer(kma);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		AlertSystemManager as1 = new AlertSystemManager(createAlertSystemTransmitter(type), aProfile, gwProfile);
//		AlertSystemManager as2 = new AlertSystemManager(createAlertSystemTransmitter(type), bProfile);
//		AlertSystemManager as3 = new AlertSystemManager(createAlertSystemTransmitter(type), civil);
//		AlertSystemManager as4 = new AlertSystemManager(createAlertSystemTransmitter(type), dmb);
//		AlertSystemManager as5 = new AlertSystemManager(createAlertSystemTransmitter(type), cbs);

		new IssuerManager(createIssuerTransmitter(type), civilalertorg, gwProfile);
	}
	
	public ITransmitter createGatewayTransmitter(TransmitterType type)
	{
		switch (type)
		{
		case JMS:
			return new GatewayTransmitter();
		case XMPP:
			return new BabblerTransmitter();
		default:
			break;
		}
		return null;
	}

	public ITransmitter createAlertSystemTransmitter(TransmitterType type) {
		switch (type) {
		case JMS:
			return new AlertSystemTransmitter();
		case XMPP:
			return new BabblerTransmitter();
		default:
			break;
		}
		return null;
	}

	public static ITransmitter createIssuerTransmitter(TransmitterType type) {
		switch (type) {
		case JMS:
			return new IssuerTransmitter();
		case XMPP:
			return new BabblerTransmitter();
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
