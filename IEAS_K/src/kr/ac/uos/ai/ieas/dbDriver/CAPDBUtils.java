package kr.ac.uos.ai.ieas.dbDriver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kr.ac.uos.ai.ieas.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.dbModel.CAPArea;
import kr.ac.uos.ai.ieas.dbModel.CAPInfo;
import kr.ac.uos.ai.ieas.dbModel.CAPResource;
import kr.ac.uos.ai.ieas.dbModel.DisasterEventType;

import org.apache.commons.dbutils.BeanProcessor;

public class CAPDBUtils
{
	public ArrayList<CAPAlert> getAlerts()
	{
		try
		{
			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from alert");
			BeanProcessor alertBp = new BeanProcessor();
			ArrayList<CAPAlert> list = new ArrayList<CAPAlert>();

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

	public ArrayList<CAPInfo> getInfos()
	{
		try
		{
			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from info");
			BeanProcessor alertBp = new BeanProcessor();
			ArrayList<CAPInfo> list = new ArrayList<CAPInfo>();

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

	public ArrayList<CAPResource> getResources()
	{
		try
		{
			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from resource");
			BeanProcessor alertBp = new BeanProcessor();
			ArrayList<CAPResource> list = new ArrayList<CAPResource>();

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

	public ArrayList<CAPArea> getAreas()
	{
		try
		{
			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery("select * from area");
			BeanProcessor alertBp = new BeanProcessor();
			ArrayList<CAPArea> list = new ArrayList<CAPArea>();

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

	public ArrayList<CAPAlert> searchCAPsByEventType(DisasterEventType type)
	{
		String query = "select * from info where eventCode=?";
		String ecode = "{\"valueName\":\"TTAS.KO-07.0046/R5 재난 종류 코드\",\"value\":\""+type+"\"}";
		//System.out.println("ecode = " + ecode);
		try
		{
			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, ecode);
			System.out.println("pstmt = " + pstmt.toString());
			ResultSet rs = pstmt.executeQuery();
			BeanProcessor infoBp = new BeanProcessor();
			ArrayList<CAPInfo> list = new ArrayList<CAPInfo>();
			
			while(rs.next())
			{
				list.add((CAPInfo) infoBp.toBean(rs, CAPInfo.class));
			}
			
			ArrayList<CAPAlert> result = this.searchFullCAPsByInfoElement(list);
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<CAPAlert> searchFullCAPsByInfoElement(ArrayList<CAPInfo> infoList)
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
		
		ArrayList<CAPAlert> alertList = new ArrayList<CAPAlert>();
		ArrayList<CAPResource> resList = new ArrayList<CAPResource>();
		ArrayList<CAPArea> areaList = new ArrayList<CAPArea>();
		
		String alertQuery = "SELECT * FROM alert WHERE alert_eid in " + alertIdList;
		String resQuery = "SELECT * FROM resource WHERE info_eid in " + infoIdList;
		String areaQuery = "SELECT * FROM area WHERE info_eid in " + infoIdList;
		
		try
		{
			DataTransaction transaction = new DataTransaction(true);
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
			
			ArrayList<CAPAlert> fullList = buildFullCap(alertList, infoList, resList, areaList);
			return fullList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private ArrayList<CAPAlert> buildFullCap(ArrayList<CAPAlert> alertList,	ArrayList<CAPInfo> infoList, ArrayList<CAPResource> resList, ArrayList<CAPArea> areaList)
	{

		ArrayList<CAPAlert> alertElementList = new ArrayList<CAPAlert>();
		
		for (CAPAlert alertElement : alertList) 
		{
			int alertIndex = alertElement.getAlert_eid();
			
			ArrayList<CAPInfo> infoElementList = new ArrayList<CAPInfo>();
			
			for (CAPInfo infoElement : infoList)
			{
				int infoIndex = infoElement.getInfo_eid();
				
				ArrayList<CAPResource> resElementList = new ArrayList<CAPResource>();
				ArrayList<CAPArea> areaElementList = new ArrayList<CAPArea>();
				
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
}
