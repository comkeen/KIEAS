package kr.ac.uos.ai.ieas.main;

import kr.ac.uos.ai.ieas.gateway.gatewayController._GatewayController;


public class GatewayMain
{
	public GatewayMain()
	{
		_GatewayController.getInstance();
	}
	
	public static void main(String[] args)
	{
		new GatewayMain();
	}
}
