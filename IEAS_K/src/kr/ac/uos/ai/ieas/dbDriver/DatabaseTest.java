package kr.ac.uos.ai.ieas.dbDriver;

import java.util.ArrayList;

import kr.ac.uos.ai.ieas.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.dbModel.CAPInfo;
import kr.ac.uos.ai.ieas.dbModel.DisasterEventType;

public class DatabaseTest {
	
	public static void main(String[] args)
	{
		CAPDBUtils util = new CAPDBUtils();
		ArrayList<CAPAlert> searchResult = new ArrayList<CAPAlert>();

		searchResult = util.searchCAPsByEventType(DisasterEventType.HRA);
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
}
