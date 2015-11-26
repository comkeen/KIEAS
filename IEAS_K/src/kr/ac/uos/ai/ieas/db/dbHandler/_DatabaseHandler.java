package kr.ac.uos.ai.ieas.db.dbHandler;

import java.util.ArrayList;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.db.dbModel.DisasterEventType;

public class _DatabaseHandler {

	private CAPDBUtils capDbUtils;
	private ArrayList<CAPAlert> searchResult;

	public static final String EVENT_CODE = "eventCode";
	public static final String STATUS = "status";
	/**
	 * Database에 직접 접근하는 주체.
	 * Cap 포맷과 Database 접근을 관리하는 CapDbUtils 초기화.
	 * 
	 */
	public _DatabaseHandler()
	{
		this.capDbUtils = new CAPDBUtils();
		this.searchResult = new ArrayList<CAPAlert>();		
	}

	public ArrayList<CAPAlert> getQueryResult(String target, String value)
	{
		switch (target)
		{
		case EVENT_CODE:
			for (DisasterEventType disasterEventType : DisasterEventType.values()) 
			{
				if(disasterEventType.toString().equals(value))
				{
					searchResult = capDbUtils.searchCAPsByEventType(disasterEventType);					
				}
			}
			for (CAPAlert capAlert : searchResult) {
				System.out.println(capAlert.getStatus());
			}
			break;
		case STATUS:
			searchResult = capDbUtils.searchCAPsByStatus(value);
			break;
		default:
			break;
		}		

		return searchResult;
	}	
}
