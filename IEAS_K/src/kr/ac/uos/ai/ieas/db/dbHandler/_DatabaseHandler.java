package kr.ac.uos.ai.ieas.db.dbHandler;

import java.util.ArrayList;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.db.dbModel.DisasterEventType;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class _DatabaseHandler {
	
	private CAPDBUtils capDbUtils;
	private ArrayList<CAPAlert> searchResult;
	
	
	public _DatabaseHandler()
	{
		this.capDbUtils = new CAPDBUtils();
		this.searchResult = new ArrayList<CAPAlert>();
	}
	
	public ArrayList<CAPAlert> getQueryResult(String type)
	{
		for (DisasterEventType disasterEventType : DisasterEventType.values()) {
			if(disasterEventType.toString().equals(type))
			{
				searchResult = capDbUtils.searchCAPsByEventType(disasterEventType);
			}
		}

		return searchResult;
	}
}
