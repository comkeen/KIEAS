package kr.ac.uos.ai.ieas.db.dbDriver;

import java.util.ArrayList;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.db.dbModel.CAPInfo;
import kr.ac.uos.ai.ieas.db.dbModel.DisasterEventType;
import kr.ac.uos.ai.ieas.resource.IeasMessageBuilder;

public class DatabaseDriver {
	
	private CAPDBUtils capDbUtils;
	private ArrayList<CAPAlert> searchResult;
	private IeasMessageBuilder ieasMessageBuilder;
	
	public DatabaseDriver()
	{
		this.capDbUtils = new CAPDBUtils();
		this.searchResult = new ArrayList<CAPAlert>();
		this.ieasMessageBuilder = new IeasMessageBuilder();
	}
	
	public ArrayList<String> getHRAResult()
	{
		searchResult = capDbUtils.searchCAPsByEventType(DisasterEventType.HRA);
		ArrayList<String> result = ieasMessageBuilder.generateCap(searchResult);

		return result;
	}
	
	public ArrayList<String> getHRWResult()
	{
		searchResult = capDbUtils.searchCAPsByEventType(DisasterEventType.HRW);
		ArrayList<String> result = ieasMessageBuilder.generateCap(searchResult);

		return result;
	}
	
	public void testDatabase()
	{
		searchResult = capDbUtils.searchCAPsByEventType(DisasterEventType.HRA);
//		for (CAPAlert capAlert : searchResult) {
//			System.out.println(capAlert.getAlert_eid());
//			System.out.println(capAlert.getStatus());
//			System.out.println(capAlert.getMsgType());
//		}
		ArrayList<String> result = ieasMessageBuilder.generateCap(searchResult);

		for (String string : result) {
			System.out.println(string);
		}
	}
	/*
	public void testDatabase()
	{
		searchResult = capDbUtils.searchCAPsByEventType(DisasterEventType.HRA);
		for (CAPAlert alert : searchResult)
		{

			System.out.println("--------------------------------------");
			System.out.println("alert counted: " + alert.getAlert_eid());
			ArrayList<CAPInfo> infoList = alert.getInfoList();
			System.out.println("this alert has " + infoList.size() + " info elements in it.");
			
			for (CAPInfo capInfo : infoList)
			{
				System.out.println("info counted: " + capInfo.getInfo_eid());
				System.out.println("this info has " + capInfo.getResList().size() + " resource elements in it.");
				System.out.println("this info has " + capInfo.getAreaList().size() + " area elements in it.");
			}
		}
	}
	*/
}
