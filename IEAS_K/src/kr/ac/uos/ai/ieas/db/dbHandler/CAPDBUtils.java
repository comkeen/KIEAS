package kr.ac.uos.ai.ieas.db.dbHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.db.dbModel.CAPArea;
import kr.ac.uos.ai.ieas.db.dbModel.CAPInfo;
import kr.ac.uos.ai.ieas.db.dbModel.CAPResource;
import kr.ac.uos.ai.ieas.db.dbModel.DisasterEventType;


public class CAPDBUtils
{
	private DataTransaction transaction;
	
	
	private static String SEARCH_ALERT_EID = "SELECT `alert_eid` from `alert` WHERE "
			+ "(identifier=? and sender=? and sent=?);";
	
	public CAPDBUtils()
	{
		this.transaction = new DataTransaction(this);
	}	
	
	public List<CAPAlert> getAlerts()
	{
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from alert");
			BeanProcessor alertBp = new BeanProcessor();
			List<CAPAlert> list = new ArrayList<CAPAlert>();

			while (rs.next())
			{
				list.add((CAPAlert) alertBp.toBean(rs, CAPAlert.class));
			}

			conn.close();
			return list;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<CAPInfo> getInfos()
	{
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from info");
			BeanProcessor alertBp = new BeanProcessor();
			List<CAPInfo> list = new ArrayList<CAPInfo>();

			while (rs.next())
			{
				list.add((CAPInfo) alertBp.toBean(rs, CAPInfo.class));
			}

			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<CAPResource> getResources()
	{
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from resource");
			BeanProcessor alertBp = new BeanProcessor();
			List<CAPResource> list = new ArrayList<CAPResource>();

			while (rs.next())
			{
				list.add((CAPResource) alertBp.toBean(rs, CAPResource.class));
			}

			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<CAPArea> getAreas()
	{
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from area");
			BeanProcessor alertBp = new BeanProcessor();
			List<CAPArea> list = new ArrayList<CAPArea>();

			while (rs.next())
			{
				list.add((CAPArea) alertBp.toBean(rs, CAPArea.class));
			}

			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CAPAlert> searchCAPsByEventType(DisasterEventType type)
	{
		String query = "select * from info where eventCode=?";
		String ecode = "{\"valueName\":\"TTAS.KO-07.0046/R5 재난 종류 코드\",\"value\":\""+type+"\"}";
		//System.out.println("ecode = " + ecode);
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, ecode);
			System.out.println("pstmt = " + pstmt.toString());
			ResultSet rs = pstmt.executeQuery();
			BeanProcessor infoBp = new BeanProcessor();
			List<CAPInfo> list = new ArrayList<CAPInfo>();
			
			while(rs.next())
			{
				list.add((CAPInfo) infoBp.toBean(rs, CAPInfo.class));
			}
			
			List<CAPAlert> result = this.searchFullCAPsByInfoElement(list);
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CAPAlert> searchCAPsByStatus(String status)
	{
		String query = "select * from alert where status=?";
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, status);
			System.out.println("pstmt = " + pstmt.toString());
			ResultSet rs = pstmt.executeQuery();
			BeanProcessor alertBp = new BeanProcessor();
			List<CAPAlert> list = new ArrayList<CAPAlert>();
			
			while(rs.next())
			{
				list.add((CAPAlert) alertBp.toBean(rs, CAPAlert.class));
			}
			
			List<CAPAlert> result = this.searchFullCAPsByAlertElement(list);
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
		
	public List<CAPAlert> searchFullCAPsByInfoElement(List<CAPInfo> infoList)
	{
		String alertIdList = "";
		String infoIdList = "";
		
		for (int i=0; i<infoList.size(); i++) 
		{
			alertIdList += infoList.get(i).getAlert_eid();
			infoIdList += infoList.get(i).getInfo_eid();
			if(i != infoList.size() - 1)
			{
				alertIdList += ", ";
				infoIdList += ", ";
			}
		}
		
		alertIdList = "(" + alertIdList + ");";
		infoIdList = "(" + infoIdList + ");";
		
		List<CAPAlert> alertList = new ArrayList<CAPAlert>();
		List<CAPResource> resList = new ArrayList<CAPResource>();
		List<CAPArea> areaList = new ArrayList<CAPArea>();
		
		String alertQuery = "SELECT * FROM alert WHERE alert_eid in " + alertIdList; // + "and type="actual"
		String resQuery = "SELECT * FROM resource WHERE info_eid in " + infoIdList;
		String areaQuery = "SELECT * FROM area WHERE info_eid in " + infoIdList;
		
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
						
			Statement stmtAlert = conn.createStatement();
			ResultSet rsAlert = stmtAlert.executeQuery(alertQuery);
			BeanProcessor alertBp = new BeanProcessor();
			
			while(rsAlert.next())
			{
				alertList.add((CAPAlert) alertBp.toBean(rsAlert, CAPAlert.class));
			}
			
//			System.out.println("alertList 완료");
			
			Statement stmtRes = conn.createStatement();
			ResultSet rsRes = stmtRes.executeQuery(resQuery);
			BeanProcessor resBp = new BeanProcessor();
			
			while(rsRes.next())
			{
				resList.add((CAPResource) resBp.toBean(rsRes, CAPResource.class));
			}
			
//			System.out.println("resList 완료");
			
			Statement stmtArea = conn.createStatement();
			ResultSet rsArea = stmtArea.executeQuery(areaQuery);
			BeanProcessor areaBp = new BeanProcessor();
			
			while(rsArea.next()) 
			{
				areaList.add((CAPArea) areaBp.toBean(rsArea, CAPArea.class));
			}
			
//			System.out.println("areaList 완료");
			
			List<CAPAlert> fullList = buildFullCap(alertList, infoList, resList, areaList);
			return fullList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CAPAlert> searchFullCAPsByAlertElement(List<CAPAlert> alertList)
	{
		String alertIdList = "";
		String infoIdList = "";
		
		for (int i=0; i < alertList.size(); i++) 
		{
			alertIdList += alertList.get(i).getAlert_eid();
			if(i != alertList.size() - 1)
			{
				alertIdList += ", ";
			}
		}
		
		alertIdList = "(" + alertIdList + ");";
		
		List<CAPInfo> infoList = new ArrayList<CAPInfo>();
		List<CAPResource> resList = new ArrayList<CAPResource>();
		List<CAPArea> areaList = new ArrayList<CAPArea>();
		
		String infoQuery = "SELECT * FROM info WHERE info_eid in " + alertIdList;
		System.out.println(infoQuery);
		
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			
			Statement stmtInfo = conn.createStatement();
			ResultSet rsInfo = stmtInfo.executeQuery(infoQuery);
			BeanProcessor infoBp = new BeanProcessor();
			
			while(rsInfo.next())
			{
				infoList.add((CAPInfo) infoBp.toBean(rsInfo, CAPInfo.class));
			}
			
			for (int i=0; i < infoList.size(); i++) 
			{
				infoIdList += infoList.get(i).getInfo_eid();
				if(i != infoList.size() - 1)
				{
					infoIdList += ", ";
				}
			}
			infoIdList = "(" + infoIdList + ");";

			String resQuery = "SELECT * FROM resource WHERE info_eid in " + infoIdList;
			String areaQuery = "SELECT * FROM area WHERE info_eid in " + infoIdList;
			
			Statement stmtRes = conn.createStatement();
			ResultSet rsRes = stmtRes.executeQuery(resQuery);
			BeanProcessor resBp = new BeanProcessor();
			
			while(rsRes.next())
			{
				resList.add((CAPResource) resBp.toBean(rsRes, CAPResource.class));
			}
			
//			System.out.println("resList 완료");
			
			Statement stmtArea = conn.createStatement();
			ResultSet rsArea = stmtArea.executeQuery(areaQuery);
			BeanProcessor areaBp = new BeanProcessor();
			
			while(rsArea.next()) 
			{
				areaList.add((CAPArea) areaBp.toBean(rsArea, CAPArea.class));
			}
			
//			System.out.println("areaList 완료");
			
			List<CAPAlert> fullList = buildFullCap(alertList, infoList, resList, areaList);
			return fullList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<CAPAlert> getCAPDrafts() {
		
		List<CAPAlert> draftList = new ArrayList<CAPAlert>();
		List<CAPInfo> infoList = new ArrayList<CAPInfo>();
		String queryAlert = "SELECT * from alert WHERE status='Draft'"; 
		String queryInfo = "SELECT * from info WHERE alert_eid in ";
		String alertIdList = "";
//		String infoIdList = "";
		
		try
		{
//			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			
			Statement stmtAlert = conn.createStatement();
			ResultSet rsAlert = stmtAlert.executeQuery(queryAlert);
			BeanProcessor bpAlert = new BeanProcessor();
			
			while(rsAlert.next()){
				draftList.add((CAPAlert) bpAlert.toBean(rsAlert, CAPAlert.class));
			}
			
			for (int i=0; i<draftList.size(); i++) 
			{
				alertIdList += draftList.get(i).getAlert_eid();
//				infoIdList += infoList.get(i).getInfo_eid();
				if(i != draftList.size() - 1)
				{
					alertIdList += ", ";
//					infoIdList += ", ";
				}
			}
			
			Statement stmtInfo = conn.createStatement();
			queryInfo += "(" + alertIdList + ")";
			System.out.println(queryInfo);
			
			ResultSet rsInfo = stmtInfo.executeQuery(queryInfo);
			BeanProcessor bpInfo = new BeanProcessor();
			System.out.println(alertIdList);
			
			while (rsInfo.next()) {
				infoList.add((CAPInfo) bpInfo.toBean(rsInfo, CAPInfo.class));
			}
			
			draftList.get(0).setInfoList(infoList);
			return draftList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	protected int getAlertEidByObject(CAPAlert alert)
	{
//		DataTransaction transaction = new DataTransaction(true);
		Connection conn = transaction.connection;
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(CAPDBUtils.SEARCH_ALERT_EID);
			pstmt.setString(1, alert.getIdentifier());
			pstmt.setString(2, alert.getSender());
			pstmt.setString(3, DataFormatUtils.convertDateObjectType(alert.getSent()));
			
			ResultSet rs = pstmt.executeQuery();
			BeanProcessor bpAlert = new BeanProcessor();
			while(rs.next())
			{
				CAPAlert result = bpAlert.toBean(rs, CAPAlert.class);
				return result.getAlert_eid();
			};
			
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		return 0;
	}
	
	private List<CAPAlert> buildFullCap(List<CAPAlert> alertList,	List<CAPInfo> infoList, List<CAPResource> resList, List<CAPArea> areaList)
	{
		List<CAPAlert> alertElementList = new ArrayList<CAPAlert>();
		
		for (CAPAlert alertElement : alertList) 
		{
			int alertIndex = alertElement.getAlert_eid();
			
			List<CAPInfo> infoElementList = new ArrayList<CAPInfo>();
			
			for (CAPInfo infoElement : infoList)
			{
				int infoIndex = infoElement.getInfo_eid();
				
				List<CAPResource> resElementList = new ArrayList<CAPResource>();
				List<CAPArea> areaElementList = new ArrayList<CAPArea>();
				
				if(infoElement.getAlert_eid() == alertIndex) 
				{
					
					for (CAPResource resElement : resList)
					{
						if(resElement.getInfo_eid() == infoIndex) 
						{
							resElementList.add(resElement);
						}
					}
					
					for (CAPArea areaElement : areaList)
					{
						if(areaElement.getInfo_eid() == infoIndex)
						{
							areaElementList.add(areaElement);
						}
					}					
					infoElement.setAreaList(areaElementList);
					infoElement.setResList(resElementList);
					infoElementList.add(infoElement);
				}				
			}			
			alertElement.setInfoList(infoElementList);
			alertElementList.add(alertElement);
		}		
		return alertElementList;
	}
//	
//	public static void main(String[] args) {
//		ArrayList<CAPAlert> drafts = new ArrayList<CAPAlert>();
//		CAPDBUtils util = new CAPDBUtils();
//		drafts = util.getCAPDrafts();
//		System.out.println("size: " + drafts.size());
//		System.out.println("하위 info size: " + drafts.get(0).getInfoList().size());
//	}

	public void connectionFail() {
		// TODO Auto-generated method stub
		
	}
}


