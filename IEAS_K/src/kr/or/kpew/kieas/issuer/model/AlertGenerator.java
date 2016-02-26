package kr.or.kpew.kieas.issuer.model;

import kr.or.kpew.kieas.common.KieasMessageBuilder;


public class AlertGenerator
{	
	private KieasMessageBuilder kieasMessageBuilder;
	
	/**
	 * CAP 메시지를 다루기 위해 사용되는 KieasMessageBuilder 객체 생성.
	 * @param _AlerterModelManager Model들을 관리하는 ModelManager. 
	 */
	public AlertGenerator()
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		System.out.println("AlertGenerator instantiated");
	}
	
	public String getAlertMessage()
	{
		return kieasMessageBuilder.getMessage();
	}

	public void setAlertMessage(String message)
	{
		this.kieasMessageBuilder.setMessage(message);
//		System.out.println("AlertGenerator generate Message : \n" + message);
	}
}
