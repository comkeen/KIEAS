package kr.ac.uos.ai.ieas.resource;

import java.util.List;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;


public interface IKieasMessageBuilder
{	
	public String getAlertElement(String key);
	public String getInfoElement(int index, String key);
	public String getAreaElement(int index, String key);
	public String getResourceElement(int index, String key);
	
	public void setAlertElement(String key, String value);
	public void setInfoElement(int index, String key, String value);
	public void setAreaElement(int index, String key, String value);
	public void setResourceElement(int index, String key, String value);
	
	public void build();
	public boolean validation(String message);
	public void setMessage(String message);
	public String getMessage();
	
	public List<String> dbToCap(List<CAPAlert> alertList);
	public CAPAlert capToDb(String capMessage);
}
