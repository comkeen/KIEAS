package kr.ac.uos.ai.ieas.main;

import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;

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
