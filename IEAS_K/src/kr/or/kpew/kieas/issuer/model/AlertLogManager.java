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

	public String put(String message)
	{
		System.out.println("put Log to AlertLogTable : \n" + message);
		String key = "key";
		return key;
	}
	
	public String get(String key)
	{
		System.out.println("get Log from AlertLogTable : \n" + key);
		return key;
	}
}
