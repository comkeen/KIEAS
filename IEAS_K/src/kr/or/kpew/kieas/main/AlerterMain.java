package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.issuer.controller._Controller;
import kr.or.kpew.kieas.issuer.model._Model;
import kr.or.kpew.kieas.issuer.view._View;

public class AlerterMain
{
	private static AlerterMain main;
	
	public static AlerterMain getInstance()
	{
		if(main == null)
		{
			main = new AlerterMain();
		}
		return main;
	}
	
	private AlerterMain()
	{
	
	}

	public static void main(String[] args) 
	{
		getInstance();		
	}	
}
