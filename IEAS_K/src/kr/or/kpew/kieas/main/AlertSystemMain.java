package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.Profile.AlertSystemType;


public class AlertSystemMain
{
	public AlertSystemMain()
	{
		AlertSystemProfile profile = new AlertSystemProfile("townbroadcast085@korea.kr", "경상남도", AlertSystemType.CivelDefense);
		//new AlertSystemManager(new AlertSystemTransmitter(), profile);
	}
	
	public static void main(String[] args)
	{
		new AlertSystemMain();
	}
}
