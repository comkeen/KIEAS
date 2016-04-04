package kr.or.kpew.kieas.gateway.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;

public class AckAggregator
{
	private Map<String, List<Profile>> beTracedProfilesMap;
	private List<String[]> senderList;
	
	
	public AckAggregator()
	{
		init();
	}
	
	public void init()
	{
		this.beTracedProfilesMap = new HashMap<>();
		this.senderList = new LinkedList<>();
	}
	
	public void addProfileTracing(String senderAddress, IKieasMessageBuilder kieasMessageBuilder, List<Profile> profiles)
	{
		beTracedProfilesMap.put(kieasMessageBuilder.getIdentifier(), profiles);
		String[] items = new String[3];
		items[0] = kieasMessageBuilder.getIdentifier();
		items[1] = senderAddress;
		items[2] = kieasMessageBuilder.getMessage();
		senderList.add(items);
	}
	
	public void removeProfileTracing(String messageIdentifier)
	{
		beTracedProfilesMap.remove(messageIdentifier);
	}
	
	public String[] checkAck(String senderAddress, String messageId)
	{		
		System.out.println("AckAggregator: check " + messageId);
		List<Profile> profiles = beTracedProfilesMap.get(messageId);
		if(profiles != null && profiles.size() > 0)
		{
			for (Profile profile : profiles)
			{
				if(profile.getSender().equals(senderAddress))
				{
					profiles.remove(profile);
					System.out.println("AckAggregator: Remain profiles size:" + profiles.size());
					break;
				}
			}
		}		
		if(profiles != null && profiles.size() == 0)
		{
			System.out.println("AckAggregator: Remove profiles");
			beTracedProfilesMap.remove(messageId);
			for (int i = 0; i < senderList.size(); i++) 
			{
				if(messageId.equals(senderList.get(i)[0]))
				{
					return senderList.get(i);
				}
			}
		}
		return null;
	}
}
