package kr.ac.uos.ai.ieas.resource;

import java.util.GregorianCalendar;
import java.util.List;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;


public interface IKieasMessageBuilder
{	
	public String getIdentifier();
	public String getSender();
	public String getSent();
	public String getStatus();
	public String getMsgType();
	public String getScope();
	public String getRestriction();
	public String getAddresses();
	public String getCode();
	public String getNote();

	public void setIdentifier(String text);
	public void setSender(String text);
	public void setSent(String text);
	public void setSent(GregorianCalendar cal);
	public void setStatus(String text);
	public void setMsgType(String text);
	public void setScope(String text);
	public void setRestriction(String text);
	public void setAddresses(String text);
	public void setCode(String text);
	public void setNote(String text);

	public String getMessage();
	public void setMessage(String message);
	public void build();
	public boolean validation(String message);
	
	public List<String> convertDbToCap(List<CAPAlert> alertList);
	public CAPAlert convertCapToDb(String capMessage);
}
