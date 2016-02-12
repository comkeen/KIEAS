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
	
	public String getLanguage(int infoIndex);
	public String getCategory(int infoIndex);
	public String getResponseType(int infoIndex);
	public String getEvent(int infoIndex);
	public String getUrgency(int infoIndex);
	public String getSeverity(int infoIndex);
	public String getCertainty(int infoIndex);
	public String getAudience(int infoIndex);
	public String getEventCode(int infoIndex);
	public String getEffective(int infoIndex);
	public String getExpires(int infoIndex);
	public String getSenderName(int infoIndex);
	public String getHeadline(int infoIndex);
	public String getDescription(int infoIndex);
	public String getInstruction(int infoIndex);
	public String getWeb(int infoIndex);
	public String getContact(int infoIndex);
	
	public String getResourceDesc(int infoIndex);
	public String getMimeType(int infoIndex);
	public String getUri(int infoIndex);	

	public String getAreaDesc(int infoIndex);
	public String getGeoCode(int infoIndex);
	
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

	public void setLanguage(int infoIndex, String text);
	public void setCategory(int infoIndex, String text);
	public void setResponseType(int infoIndex, String text);
	public void setEvent(int infoIndex, String text);
	public void setUrgency(int infoIndex, String text);
	public void setSeverity(int infoIndex, String text);
	public void setCertainty(int infoIndex, String text);
	public void setAudience(int infoIndex, String text);
	public void setEventCode(int infoIndex, String text);
	public void setEffective(int infoIndex, String text);
	public void setExpires(int infoIndex, String text);
	public void setSenderName(int infoIndex, String text);
	public void setHeadline(int infoIndex, String text);
	public void setDescription(int infoIndex, String text);
	public void setInstruction(int infoIndex, String text);
	public void setWeb(int infoIndex, String text);
	public void setContact(int infoIndex, String text);
	
	public void setResourceDesc(int infoIndex, String text);
	public void setMimeType(int infoIndex, String text);
	public void setUri(int infoIndex, String text);	

	public void setAreaDesc(int infoIndex, String text);
	public void setGeoCode(int infoIndex, String text);
	
	public String build();
	public String getMessage();
	public void setMessage(String message);
	public boolean validation(String message);
	
	public List<String> convertDbToCap(List<CAPAlert> alertList);
	public CAPAlert convertCapToDb(String capMessage);
}
