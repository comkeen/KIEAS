package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.alertsystem.AlertSystemManager;
import kr.or.kpew.kieas.alertsystem.AlertSystemModel;


public class AlertSystemMain
{
	
	public static void main(String[] args)
	{
		AlertSystemManager manager = new AlertSystemManager();
		manager.run();
	}
}
