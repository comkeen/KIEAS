package kr.or.kpew.kieas.issuer.model;

import java.util.HashMap;
import java.util.Map;

public class AlertLogManager
{
	private Map<String, String> alertMessageMap;

	public AlertLogManager()
	{
		this.alertMessageMap = new HashMap<String, String>();		
	}

	public void put(String key, String message)
	{
		System.out.println("put Log to AlertLogTable : \n" + message);
		alertMessageMap.put(key, message);
	}
	
	public String get(String key)
	{
		System.out.println("get Log from AlertLogTable : \n" + key);
		return alertMessageMap.get(key);
	}
}
