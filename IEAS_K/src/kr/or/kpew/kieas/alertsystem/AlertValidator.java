package kr.or.kpew.kieas.alertsystem;

import java.util.HashMap;
import java.util.Map;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;

public class AlertValidator
{
	public enum State
	{
		New, Duplication, HasGeoCode, NoGeoCode
	}
	
	private Map<String, String> receivedAlertMap;
	
	
	public AlertValidator()
	{
		this.receivedAlertMap = new HashMap<>();
	}
	
	public State validateMessage(String message)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		String identifier = kieasMessageBuilder.getIdentifier();
		if(receivedAlertMap.get(identifier) == null)
		{
			receivedAlertMap.put(identifier, message);
			return State.New;
		}
		else
		{
			return State.Duplication;
		}
	}
	
	public String hasGeoCode(String message)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		int infoCount = kieasMessageBuilder.getInfoCount();
		if(infoCount > 0)
		{
			for(int i = 0; i < infoCount; i++)
			{
				int areaCount = kieasMessageBuilder.getAreaCount(i);
				if(areaCount > 0)
				{
					for(int j = 0; j < areaCount; j++)
					{
						String geoCode = kieasMessageBuilder.getGeoCode(i, j);
						if(geoCode.length() > 0)
						{
							return geoCode;
						}
					}
				}
			}
		}
		return "";
	}
}
