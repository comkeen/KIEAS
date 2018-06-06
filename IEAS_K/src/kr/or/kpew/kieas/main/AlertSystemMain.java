package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.alertsystem.AlertSystemManager;
import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.common.Profile.AlertSystemType;
import kr.or.kpew.kieas.main.IntegratedAlertSystemMain.TransmitterType;
import kr.or.kpew.kieas.network.jms.AlertSystemTransmitter;


public class AlertSystemMain
{
	public AlertSystemMain()
	{
		//게이트웨이 프로파일: 게이트웨이를 생성하기 위해 필요(식별자,이름)
		Profile gatewayProfile = new Profile("maingateway", "국민안전처");
		//발령대 프로파일: 발령대를 생성하기 위해 필요(식별자,이름)
		IssuerProfile issuerProfile = new IssuerProfile("civilalerter", "민방위");
		//경보시스템 프로파일: 수신기를 생성하기 위해 필요(식별자,지역,경보시스템타입)
		AlertSystemProfile alertSystemProfile = new AlertSystemProfile("townbroadcast085", "경상남도", AlertSystemType.LocalBroadcasting);
		alertSystemProfile.setLanguage("us-EN");
		alertSystemProfile.setGeoCode("1100000000");
		alertSystemProfile.setCapability("그림");
		new AlertSystemManager(new AlertSystemTransmitter(), alertSystemProfile, gatewayProfile);
	}
	
	public static void main(String[] args)
	{
		new AlertSystemMain();
	}
}
