package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.alertsystem.AlertSystemManager;
import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.common.Profile.AlertSystemType;
import kr.or.kpew.kieas.network.jms.AlertSystemTransmitter;


public class AlertSystemMain
{
	public AlertSystemMain()
	{
//		new AlertSystemManager(new AlertSystemTransmitter(), new AlertSystemProfile("townbroadcast085@korea.kr", "경상남도", AlertSystemType.CivelDefense), new Profile("maingateway", "국민안전처"));
	}
	
	public static void main(String[] args)
	{
		new AlertSystemMain();
	}
}
