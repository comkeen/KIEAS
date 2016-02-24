package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.gateway.controller._GatewayController;


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
