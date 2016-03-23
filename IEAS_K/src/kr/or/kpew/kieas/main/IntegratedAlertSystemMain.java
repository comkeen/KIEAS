package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.alertsystem.AlertSystemManager;
import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.common.Profile.AlertSystemType;
import kr.or.kpew.kieas.gateway.controller.GatewayManager;
import kr.or.kpew.kieas.issuer.controller.IssuerManager;
import kr.or.kpew.kieas.network.IClientTransmitter;
import kr.or.kpew.kieas.network.IServerTransmitter;
import kr.or.kpew.kieas.network.jms.AlertSystemTransmitter;
import kr.or.kpew.kieas.network.jms.GatewayTransmitter;
import kr.or.kpew.kieas.network.jms.IssuerTransmitter;

public class IntegratedAlertSystemMain {
	static Profile gProfile = new Profile("maingateway@korea.kr", "국민안전처");

	static AlertSystemProfile aProfile = new AlertSystemProfile("townbroadcast085@korea.kr", "경상남도",
			AlertSystemType.LocalBroadcasting);
	static AlertSystemProfile bProfile = new AlertSystemProfile("townbroadcast221@korea.kr", "전라남도",
			AlertSystemType.LocalBroadcasting);
	static AlertSystemProfile cProfile = new AlertSystemProfile("townbroadcast761@korea.kr", "제주도",
			AlertSystemType.LocalBroadcasting);

	static AlertSystemProfile civil = new AlertSystemProfile("civildef@korea.kr", "국민안전처",
			AlertSystemType.CivelDefense);
	static AlertSystemProfile dmb = new AlertSystemProfile("dmbalert@korea.kr", "국민안전처",
			AlertSystemType.DmbAlertSystem);
	static AlertSystemProfile cbs = new AlertSystemProfile("cbsalert@korea.kr", "국민안전처",
			AlertSystemType.CbsAlertSystem);

	static IssuerProfile kma = new IssuerProfile("issuerkma0124@korea.kr", "기상청");
	static IssuerProfile civilalertorg = new IssuerProfile("civilalerter@korea.kr", "민방위");

	enum TransmitterType {
		JMS,
		TCPIP
	}

	public static IServerTransmitter createGatewayTransmitter(TransmitterType type) {
		switch (type) {
		case JMS:
			return new GatewayTransmitter();
		}
		return null;
	}

	public static IClientTransmitter createAlertSystemTransmitter(TransmitterType type) {
		switch (type) {
		case JMS:
			return new AlertSystemTransmitter();
		}
		return null;
	}

	public static IClientTransmitter createIssuerTransmitter(TransmitterType type) {
		switch (type) {
		case JMS:
			return new IssuerTransmitter();
		}
		return null;
	}

	@SuppressWarnings("unused")
	public static void run(TransmitterType type) throws Exception {
		GatewayManager g = new GatewayManager(createGatewayTransmitter(type), gProfile);

		g.registAlertSystem(aProfile);
		g.registAlertSystem(bProfile);
		g.registAlertSystem(cProfile);

		g.registAlertSystem(civil);
		g.registAlertSystem(dmb);
		g.registAlertSystem(cbs);

		g.registIssuer(kma);
		g.registIssuer(civilalertorg);

		Thread.sleep(500);

		AlertSystemManager a = new AlertSystemManager(createAlertSystemTransmitter(type), aProfile);
		AlertSystemManager b = new AlertSystemManager(createAlertSystemTransmitter(type), bProfile);
		AlertSystemManager c = new AlertSystemManager(createAlertSystemTransmitter(type), cProfile);

		AlertSystemManager d = new AlertSystemManager(createAlertSystemTransmitter(type), civil);
		AlertSystemManager e = new AlertSystemManager(createAlertSystemTransmitter(type), dmb);
		AlertSystemManager f = new AlertSystemManager(createAlertSystemTransmitter(type), cbs);

		IssuerManager i = new IssuerManager(createIssuerTransmitter(type), civilalertorg);

	}

	@SuppressWarnings("unused")
	public static void runJms() throws Exception {

		GatewayManager g = new GatewayManager(new GatewayTransmitter(), gProfile);

		g.registAlertSystem(aProfile);
		g.registAlertSystem(bProfile);
		g.registAlertSystem(cProfile);

		g.registAlertSystem(civil);
		g.registAlertSystem(dmb);
		g.registAlertSystem(cbs);

		g.registIssuer(kma);
		g.registIssuer(civilalertorg);

		Thread.sleep(500);

		AlertSystemManager a = new AlertSystemManager(new AlertSystemTransmitter(), aProfile);
		AlertSystemManager b = new AlertSystemManager(new AlertSystemTransmitter(), bProfile);
		AlertSystemManager c = new AlertSystemManager(new AlertSystemTransmitter(), cProfile);

		AlertSystemManager d = new AlertSystemManager(new AlertSystemTransmitter(), civil);
		AlertSystemManager e = new AlertSystemManager(new AlertSystemTransmitter(), dmb);
		AlertSystemManager f = new AlertSystemManager(new AlertSystemTransmitter(), cbs);

		IssuerManager i = new IssuerManager(new IssuerTransmitter(), civilalertorg);

	}


	public static void main(String[] args) throws Exception {
		run(TransmitterType.JMS);
	}
}
