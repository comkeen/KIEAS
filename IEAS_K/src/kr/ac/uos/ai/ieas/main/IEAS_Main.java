package kr.ac.uos.ai.ieas.main;

import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;

public class IEAS_Main
{
	public IEAS_Main()
	{
		new _AlerterController();
//		GatewayController.getInstance();
	}

	public static void main(String[] args) 
	{
		new IEAS_Main();
	}	
}
