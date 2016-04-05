package kr.or.kpew.kieas.gateway.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;

public class AckAggregator
{
	private Map<String, List<Profile>> beTracedProfileMap;
	private List<String[]> senderList;
	
	
	public AckAggregator()
	{
		init();
	}
	
	public void init()
	{
		this.beTracedProfileMap = new HashMap<>();
		this.senderList = new LinkedList<>();
	}
	
	public void addProfileTracing(String message, List<Profile> profiles)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		
		beTracedProfileMap.put(kieasMessageBuilder.getIdentifier(), profiles);
		String[] items = new String[3];
		items[0] = kieasMessageBuilder.getIdentifier();
		items[1] = kieasMessageBuilder.getSender();
		items[2] = kieasMessageBuilder.getMessage();
		senderList.add(items);
	}
	
	public void removeProfileTracing(String messageIdentifier)
	{
		beTracedProfileMap.remove(messageIdentifier);
	}
	
	public String[] checkAck(String sender, String messageIdentifier)
	{		
		List<Profile> profiles = beTracedProfileMap.get(messageIdentifier);
		if(profiles != null && profiles.size() > 0)
		{
			for (Profile profile : profiles)
			{
				if(profile.getSender().equals(sender))
				{
					profiles.remove(profile);
					break;
				}
			}
		}		
		if(profiles != null && profiles.size() == 0)
		{
			beTracedProfileMap.remove(messageIdentifier);
			for (int i = 0; i < senderList.size(); i++) 
			{
				if(messageIdentifier.equals(senderList.get(i)[0]))
				{
					return senderList.get(i);
				}
			}
		}
		return null;
	}
}
