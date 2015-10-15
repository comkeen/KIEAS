package kr.ac.uos.ai.ieas.db.dbHandler;

import java.util.ArrayList;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.db.dbModel.DisasterEventType;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _DatabaseHandler {
	
	private CAPDBUtils capDbUtils;
	private ArrayList<CAPAlert> searchResult;
	private KieasMessageBuilder ieasMessageBuilder;
	
	
	public _DatabaseHandler()
	{
		this.capDbUtils = new CAPDBUtils();
		this.searchResult = new ArrayList<CAPAlert>();
		this.ieasMessageBuilder = new KieasMessageBuilder();
	}
	
	public ArrayList<String> getQueryResult(String type)
	{
		for (DisasterEventType disasterEventType : DisasterEventType.values()) {
			if(disasterEventType.toString().equals(type))
			{
				searchResult = capDbUtils.searchCAPsByEventType(disasterEventType);
			}
		}
		ArrayList<String> result = ieasMessageBuilder.generateCap(searchResult);

		return result;
	}
	
	public void testDatabase()
	{
		searchResult = capDbUtils.searchCAPsByEventType(DisasterEventType.HRA);

		ArrayList<String> result = ieasMessageBuilder.generateCap(searchResult);

		for (String string : result) {
			System.out.println(string);
		}
	}

}
