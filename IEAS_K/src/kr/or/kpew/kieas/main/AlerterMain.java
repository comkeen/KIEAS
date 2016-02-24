package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.issuer.controller._AlerterController;

public class AlerterMain
{
	public AlerterMain()
	{
		_AlerterController.getInstance();
	}

	public static void main(String[] args) 
	{
		new AlerterMain();
	}	
}
