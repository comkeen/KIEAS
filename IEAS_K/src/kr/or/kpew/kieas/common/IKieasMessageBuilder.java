package kr.or.kpew.kieas.common;

import java.util.List;
import java.util.Map;

import com.google.publicalerts.cap.Alert.MsgType;
import com.google.publicalerts.cap.Alert.Scope;
import com.google.publicalerts.cap.Alert.Status;
import com.google.publicalerts.cap.Group.Builder;
import com.google.publicalerts.cap.Info.Category;

import kr.or.kpew.kieas.common.AlertValidator.AckCode;

import com.google.publicalerts.cap.Point;

/**
 * CAP 메시지빌더 인터페이스
 * @author comkeen
 *
 */
public interface IKieasMessageBuilder
{	
	public String getIdentifier();				
	public String getSender();					
	public String getSent();					
	public Status getStatus();					
	public MsgType getMsgType();					
	public String getSource();					
	public Scope getScope();				
	public String getRestriction();
	public List<String> getAddresses();
	public String getCode();				
	public String getNote();
	public String getReferences();
	
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
	public String getParameter(int infoIndex, int parameterIndex);
	
	public String getResourceDesc(int infoIndex, int resourceIndex);
	public String getMimeType(int infoIndex, int resourceIndex);
	public String getSize(int infoIndex, int resourceIndex);	
	public String getUri(int infoIndex, int resourceIndex);	
	public String getDerefUri(int infoIndex, int resourceIndex);	
	public String getDigest(int infoIndex, int resourceIndex);	
	
	public String getAreaDesc(int infoIndex, int areaIndex);
	public String getPolygon(int infoIndex, int areaIndex, int polygonIndex);
	public String getCircle(int infoIndex, int areaIndex, int circleIndex);
	public String getGeoCode(int infoIndex, int areaIndex);
	public String getAltitude(int infoIndex, int areaIndex);
	public String getCeiling(int infoIndex, int areaIndex);
	
	public void setIdentifier(String text);
	public void setSender(String text);
	public void setSent(String text);
	public void setSent();
	public void setStatus(Status status);
	public void setStatus(String status);
	public void setMsgType(MsgType type);
	public void setMsgType(String type);
	public void setSource(String text);
	public void setScope(Scope scope);
	public void setScope(String text);
	public void setRestriction(String text);
	public void setAddresses(String text);
	public void setCode(String text);
	public void setReferences(String text);
	public void setNote(String text);

	public void setLanguage(int infoIndex, String text);
	public void setCategory(int infoIndex, Category category);
	public void setCategory(int infoIndex, String text);
	public void setResponseType(int infoIndex, String text);
	public void setEvent(int infoIndex, String text);
	public void setUrgency(int infoIndex, String text);
	public void setSeverity(int infoIndex, String text);
	public void setCertainty(int infoIndex, String text);
	public void setAudience(int infoIndex, String text);
	public void setEventCode(int infoIndex, String text);
	public void setEffective(int infoIndex, String text);
	public void setOnset(int infoIndex, String text);
	public void setExpires(int infoIndex, String text);
	public void setSenderName(int infoIndex, String text);
	public void setHeadline(int infoIndex, String text);
	public void setDescription(int infoIndex, String text);
	public void setInstruction(int infoIndex, String text);
	public void setWeb(int infoIndex, String text);
	public void setContact(int infoIndex, String text);
	public void setParameter(int infoIndex, int parameterIndex, String valueName, String value);
	
	public void setResourceDesc(int infoIndex, int resourceIndex, String text);
	public void setMimeType(int infoIndex, int resourceIndex, String text);
	public void setSize(int infoIndex, int resourceIndex, String text);	
	public void setUri(int infoIndex, int resourceIndex, String text);	
	public void setDerefUri(int infoIndex, int resourceIndex, String text);
	public void setDigest(int infoIndex, int resourceIndex, String text);		

	public void setAreaDesc(int infoIndex, int areaIndex, String text);
	public void setPolygon(int infoIndex, int areaIndex, int polygonIndex, Point[] points);
	public void setCircle(int infoIndex, int areaIndex, int circleIndex, double latitud, double longitude, double radius);
	public void setGeoCode(int infoIndex, int areaIndex, String text);
	public void setAltitude(int infoIndex, int areaIndex, double text);
	public void setCeiling(int infoIndex, int areaIndex, double text);
	
	public String build();
	public String buildDefaultMessage();
	public String getMessage();
	public Builder convertToReferences(String text);
	public String[] parseReferences(String references);
	public String generateKieasMessageIdentifier(String id);
	
	/**
	 * 현재 CAP 메시지에 대한 수신응답(Ack)메시지를 생성한다.
	 * @param identifier 현재 시스템의 식별자
	 * @sender 수신응답 메시지가 전달되어야 하는 목적지
	 * @return 생성한 수신응답 메시지
	 */
	public String createAckMessage(String message, String identifier, String sender, AckCode ackCode);
	public String getDate();
	public String convertDateToYmdhms(String date);
	public Map<Enum<?>, List<Item>> getCapEnumMap();
	
	public void parse(String message);
	public void setProfile(Profile profile);
	
	public int getInfoCount();
	public int getResourceCount(int infoIndex);
	public int getAreaCount(int infoIndex);
}
