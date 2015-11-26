package kr.ac.uos.ai.ieas.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXParseException;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.Alert.MsgType;
import com.google.publicalerts.cap.Alert.Scope;
import com.google.publicalerts.cap.Alert.Status;
import com.google.publicalerts.cap.Area;
import com.google.publicalerts.cap.CapException;
import com.google.publicalerts.cap.CapUtil;
import com.google.publicalerts.cap.CapValidator;
import com.google.publicalerts.cap.CapXmlBuilder;
import com.google.publicalerts.cap.CapXmlParser;
import com.google.publicalerts.cap.Group;
import com.google.publicalerts.cap.Info;
import com.google.publicalerts.cap.Info.Category;
import com.google.publicalerts.cap.Info.Certainty;
import com.google.publicalerts.cap.Info.ResponseType;
import com.google.publicalerts.cap.Info.Severity;
import com.google.publicalerts.cap.Info.Urgency;
import com.google.publicalerts.cap.NotCapException;
import com.google.publicalerts.cap.Resource;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.db.dbModel.CAPArea;
import kr.ac.uos.ai.ieas.db.dbModel.CAPInfo;
import kr.ac.uos.ai.ieas.db.dbModel.CAPResource;
import kr.ac.uos.ai.ieas.db.dbModel.DisasterEventType;

/**
 * CAP 형식의 메시지를 생성하고 다루는 클래스.
 * Google CAP Library를 활용하여 CAP 메시지를 다룬다.
 * @author byun-ai
 *
 */

public class KieasMessageBuilder
{
	private CapXmlBuilder 	capXmlBuilder;
	private CapXmlParser 	capXmlParser;
	private CapValidator 	capValidator;

	private Alert 			alert;
	private Info 			info;
//	private Resource 		resource;
//	private Area 			area;
	
	private HashMap<String, ArrayList<Item>> capEnumMap;
	private String xmlMessage;
	

	public KieasMessageBuilder()  {

		this.capXmlBuilder = new CapXmlBuilder();
		this.capXmlParser = new CapXmlParser(true);
		this.capValidator = new CapValidator();
		
//		buildDefaultMessage();
	}
	
	/**
	 * View에 사용될 Enum을 Item으로 사용하여 Value를 디스플레이하고 Key에 의해 아이템 판별이 이루어진다.
	 * 
	 * @author byun-ai
	 *
	 */
	public class Item
	{
		private String key;
		private String value;
		
		
		public Item(String key, String value)
		{
			this.key = key;
			this.value = value;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public String toString()
		{
			return value;
		}
	}
	
	/**
	 * CAP 요소에서 사용하는 Enum들을 가져옴.
	 * CAP 요소 이름 elementName을 Key로 사용하며 이것으로 Enum 리스트를 식별함.
	 * 
	 * @return HashMap<String elementName, ArrayList<Item> enum>
	 */
	public HashMap<String, ArrayList<Item>> getCapEnumMap()
	{
		this.capEnumMap = new HashMap<>();
		buildAlertCapEnumMap();
		buildInfoCapEnumMap();
		return capEnumMap;
	}
	
	private void buildAlertCapEnumMap()
	{
		ArrayList<Item> capEnum1 = new ArrayList<>();
		for (Status value : Alert.Status.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Alert.Status.ACTUAL.toString()))
			{
				modifiedValue = value.toString() + " (실제)";
			}
			else if(value.toString().equals(Alert.Status.EXERCISE.toString()))
			{
				modifiedValue = value.toString() + " (훈련)";
			}
			else if(value.toString().equals(Alert.Status.SYSTEM.toString()))
			{
				modifiedValue = value.toString() + " (시스템)";
			}
			else if(value.toString().equals(Alert.Status.TEST.toString()))
			{
				modifiedValue = value.toString() + " (테스트)";
			}
			else if(value.toString().equals(Alert.Status.DRAFT.toString()))
			{
				modifiedValue = value.toString() + " (초안)";
			}
			capEnum1.add(new Item(value.toString(), modifiedValue));	
		}
		capEnumMap.put("Status", capEnum1);
		
		ArrayList<Item> capEnum2 = new ArrayList<>();
		for (MsgType value : Alert.MsgType.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Alert.MsgType.ALERT.toString()))
			{
				modifiedValue = value.toString() + " (경보)";
			}
			else if(value.toString().equals(Alert.MsgType.UPDATE.toString()))
			{
				modifiedValue = value.toString() + " (갱신)";
			}
			else if(value.toString().equals(Alert.MsgType.CANCEL.toString()))
			{
				modifiedValue = value.toString() + " (취소)";
			}
			else if(value.toString().equals(Alert.MsgType.ACK.toString()))
			{
				modifiedValue = value.toString() + " (응답)";
			}
			else if(value.toString().equals(Alert.MsgType.ERROR.toString()))
			{
				modifiedValue = value.toString() + " (오류)";
			}
			capEnum2.add(new Item(value.toString(), modifiedValue));	
		}
		capEnumMap.put("MsgType", capEnum2);
		
		ArrayList<Item> capEnum3 = new ArrayList<>();
		for (Scope value : Alert.Scope.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Alert.Scope.PUBLIC.toString()))
			{
				modifiedValue = value.toString() + " (공용)";
			}
			else if(value.toString().equals(Alert.Scope.RESTRICTED.toString()))
			{
				modifiedValue = value.toString() + " (제한)";
			}
			else if(value.toString().equals(Alert.Scope.PRIVATE.toString()))
			{
				modifiedValue = value.toString() + " (개별)";
			}
			capEnum3.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("Scope", capEnum3);	
	}
	
	private void buildInfoCapEnumMap()
	{
		//Category
		ArrayList<Item> capEnum1 = new ArrayList<>();
		for (Category value : Info.Category.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Info.Category.GEO.toString()))
			{
				modifiedValue = value.toString() + " (지리)";
			}
			else if(value.toString().equals(Info.Category.MET.toString()))
			{
				modifiedValue = value.toString() + " (기상)";
			}
			else if(value.toString().equals(Info.Category.SAFETY.toString()))
			{
				modifiedValue = value.toString() + " (안전)";
			}
			else if(value.toString().equals(Info.Category.SECURITY.toString()))
			{
				modifiedValue = value.toString() + " (보안)";
			}
			else if(value.toString().equals(Info.Category.RESCUE.toString()))
			{
				modifiedValue = value.toString() + " (구조)";
			}
			else if(value.toString().equals(Info.Category.FIRE.toString()))
			{
				modifiedValue = value.toString() + " (화재)";
			}
			else if(value.toString().equals(Info.Category.HEALTH.toString()))
			{
				modifiedValue = value.toString() + " (건강)";
			}
			else if(value.toString().equals(Info.Category.ENV.toString()))
			{
				modifiedValue = value.toString() + " (환경)";
			}
			else if(value.toString().equals(Info.Category.TRANSPORT.toString()))
			{
				modifiedValue = value.toString() + " (교통)";
			}
			else if(value.toString().equals(Info.Category.INFRA.toString()))
			{
				modifiedValue = value.toString() + " (기반시설)";
			}
			else if(value.toString().equals(Info.Category.CBRNE.toString()))
			{
				modifiedValue = value.toString() + " (화생방)";
			}
			else if(value.toString().equals(Info.Category.OTHER.toString()))
			{
				modifiedValue = value.toString() + " (기타)";
			}
			capEnum1.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("Category", capEnum1);
		
		ArrayList<Item> capEnum2 = new ArrayList<>();
		for (Certainty value : Info.Certainty.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Info.Certainty.OBSERVED.toString()))
			{
				modifiedValue = value.toString() + " (지리)";
			}
			else if(value.toString().equals(Info.Certainty.VERY_LIKELY.toString()))
			{
				continue;
			}
			else if(value.toString().equals(Info.Certainty.LIKELY.toString()))
			{
				modifiedValue = value.toString() + " (일어날 가능성 50% 이상)";
			}
			else if(value.toString().equals(Info.Certainty.POSSIBLE.toString()))
			{
				modifiedValue = value.toString() + " (일어날 가능성 50% 미만)";
			}
			else if(value.toString().equals(Info.Certainty.UNLIKELY.toString()))
			{
				modifiedValue = value.toString() + " (일어날 가능성 거의 없음)";
			}
			else if(value.toString().equals(Info.Certainty.UNKNOWN_CERTAINTY.toString()))
			{
				modifiedValue = value.toString() + " (일어날 가능성 알수없음)";
			}
			capEnum2.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("Certainty", capEnum2);
		
		ArrayList<Item> capEnum3 = new ArrayList<>();
		for (ResponseType value : Info.ResponseType.values())
		{			
			String modifiedValue = "";
			modifiedValue = value.toString();
			capEnum3.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("ResponseType", capEnum3);
		
		ArrayList<Item> capEnum4 = new ArrayList<>();
		for (Severity value : Info.Severity.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Info.Severity.EXTREME.toString()))
			{
				modifiedValue = value.toString() + " (치명적인 피해 발생 예상)";
			}
			else if(value.toString().equals(Info.Severity.SEVERE.toString()))
			{
				modifiedValue = value.toString() + " (심각한 피해 발생 예상)";
			}
			else if(value.toString().equals(Info.Severity.MODERATE.toString()))
			{
				modifiedValue = value.toString() + " (피해 발생 가능성 있음)";
			}
			else if(value.toString().equals(Info.Severity.MINOR.toString()))
			{
				modifiedValue = value.toString() + " (피해 발생 가능성 낮음)";
			}
			else if(value.toString().equals(Info.Severity.UNKNOWN_SEVERITY.toString()))
			{
				modifiedValue = value.toString() + " (피해 정도를 알수없음)";
			}
			capEnum4.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("Severity", capEnum4);
		
		ArrayList<Item> capEnum5 = new ArrayList<>();
		for (Urgency value : Info.Urgency.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Info.Urgency.IMMEDIATE.toString()))
			{
				modifiedValue = value.toString() + " (사건 발생 촉박)";
			}
			else if(value.toString().equals(Info.Urgency.EXPECTED.toString()))
			{
				modifiedValue = value.toString() + " (곧 사건  발생 예상)";
			}
			else if(value.toString().equals(Info.Urgency.FUTURE.toString()))
			{
				modifiedValue = value.toString() + " (가까운 미래에 사건  발생 예상)";
			}
			else if(value.toString().equals(Info.Urgency.PAST.toString()))
			{
				modifiedValue = value.toString() + " (과거에 사건 발생 했었음)";
			}
			else if(value.toString().equals(Info.Urgency.UNKNOWN_URGENCY.toString()))
			{
				modifiedValue = value.toString() + " (사건 발생 시점 알수없음)";
			}
			capEnum5.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("Urgency", capEnum5);
		
		ArrayList<Item> capEnum6 = new ArrayList<>();
		for (DisasterEventType value : DisasterEventType.values())
		{
			String modifiedValue = "";
			modifiedValue = value.toString() + " (" + value.getKoreanEventCode() + ")";
			capEnum6.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("EventCode", capEnum6);
		
		ArrayList<Item> capEnum7 = new ArrayList<>();
		for (String value : KieasConfiguration.IEAS_List.LANGUAGE_LIST)
		{
			String modifiedValue = "";
			if(value.toString().equals(KieasConfiguration.IEAS_List.LANGUAGE_LIST[0]))
			{
				modifiedValue = value.toString() + " (한국어)";
			}
			else if(value.toString().equals(KieasConfiguration.IEAS_List.LANGUAGE_LIST[1]))
			{
				modifiedValue = value.toString() + " (영어)";
			}
			capEnum7.add(new Item(value.toString(), modifiedValue));
		}
		capEnumMap.put("Language", capEnum7);
	}
	
	public void buildDefaultMessage() {
		
		this.alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
				.setIdentifier("Identifier")
				.setSender("Sender")
				.setSent("Sent")
				.setStatus(Alert.Status.SYSTEM)
				.setMsgType(Alert.MsgType.ALERT)
				.setScope(Alert.Scope.PUBLIC)
				.buildPartial();
		
		this.info = Info.newBuilder()
				.setLanguage("ko-KR")
				.addCategory(Info.Category.SAFETY)
				.setEvent("event")
				.setUrgency(Info.Urgency.UNKNOWN_URGENCY)
				.setSeverity(Info.Severity.UNKNOWN_SEVERITY)
				.setCertainty(Info.Certainty.UNKNOWN_CERTAINTY)
				.buildPartial();
		
//		this.resource = Resource.newBuilder().buildPartial();		
//		this.area = Area.newBuilder().buildPartial();
				
		alert = Alert.newBuilder(alert).addInfo(info).build();
	}
	
	public boolean validateMessage()
	{
		try
		{
			capValidator.validateAlert(alert);
			return true;
		}
		catch (CapException e)
		{
			return false;
		}
	}
	
	public void build()
	{		
		alert = Alert.newBuilder(alert).clearInfo().addInfo(info).build();
	}	

	private String dateToString(Date date)
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setTime(date);
		return CapUtil.formatCapDate(cal);
	}
	
	public String transformToYmdhms(String date)
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setTime(CapUtil.toJavaDate(date));
		
		StringBuffer sb = new StringBuffer();
		sb.append(cal.get(Calendar.YEAR)).append("년")
		.append(cal.get(Calendar.MONTH)+1).append("월")
		.append(cal.get(Calendar.DATE)).append("일").append(" ")
		.append(cal.get(Calendar.HOUR_OF_DAY)).append("시")
		.append(cal.get(Calendar.MINUTE)).append("분")
		.append(cal.get(Calendar.SECOND)).append("초");
		
		return sb.toString();
	}
		
	public int getInfoCount()
	{
		return alert.getInfoCount();
	}

	public int getResourceCount(int index)
	{
		return alert.getInfo(index).getResourceCount();
	}
	
	public int getAreaCount(int index)
	{
		return alert.getInfo(index).getAreaCount();
	}
	
	public String getMessage()
	{
		try
		{
			this.xmlMessage = capXmlBuilder.toXml(alert);
			return xmlMessage;
		}
		catch (NotCapException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Alert setMessage(String message) 
	{
		try
		{
			alert = capXmlParser.parseFrom(message);
			return alert;
		} 
		catch (NotCapException | SAXParseException | CapException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	//CAP 요소 Getter
	public String getIdentifier()
	{
		try
		{
			return alert.getIdentifier();
		}
		catch (NotCapException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String getSender()
	{
		try 
		{
			return alert.getSender();

		} 
		catch (NotCapException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String getSent()
	{
		try
		{
			return alert.getSent();

		} 
		catch (NotCapException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String getSentCalendar()
	{
		try
		{
			return alert.getSent();

		} 
		catch (NotCapException e)
		{
			e.printStackTrace();
		}
		return null;
	}	

	public String getStatus()
	{
		return alert.getStatus().toString();
	}

	public String getMsgType() 
	{
		return alert.getMsgType().toString();
	}

	public String getSource() 
	{
		return alert.getSource().toString();
	}
	
	public String getScope()
	{
		return alert.getScope().toString();
	}
	
	public String getRestriction()
	{
		return alert.getRestriction().toString();
	}

	public String getAddresses()
	{
		try
		{
			return alert.getAddresses().getValue(0);
		}
		catch (NotCapException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String getCode()
	{
		return alert.getCode(0).toString();
	}		

	public String getLanguage(int index)
	{
		return alert.getInfo(index).getLanguage().toString();
	}

	public String getCategory(int index)
	{
		return alert.getInfo(index).getCategory(0).toString();
	}

	public String getEvent(int index)
	{
		try
		{
			return alert.getInfo(index).getEvent();
		}
		catch (NotCapException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getUrgency(int index)
	{
		return alert.getInfo(index).getUrgency().toString();
	}

	public String getSeverity(int index)
	{
		return alert.getInfo(index).getSeverity().toString();
	}

	public String getCertainty(int index)
	{
		return alert.getInfo(index).getCertainty().toString();
	}

	public String getEventCode(int index) 
	{
		return alert.getInfo(index).getEventCodeList().get(0).getValue().toString();
	}

	public String getEffective(int index)
	{
		return alert.getInfo(index).getEffective().toString();
	}

	public String getSenderName(int index)
	{
		return alert.getInfo(index).getSenderName().toString();
	}

	public String getHeadline(int index)
	{
		return alert.getInfo(index).getHeadline().toString();
	}

	public String getDescrpition(int index)
	{
		return alert.getInfo(index).getDescription().toString();
	}

	public String getWeb(int index)
	{
		return alert.getInfo(index).getWeb().toString();
	}

	public String getContact(int index)
	{
		return alert.getInfo(index).getContact().toString();
	}

	public String getResourceDesc(int infoIndex, int index)
	{
		return alert.getInfo(infoIndex).getResource(index).getResourceDesc();
	}
	
	public String getMimeType(int infoIndex, int index)
	{
		return alert.getInfo(infoIndex).getResource(index).getMimeType();
	}

	public String getUri(int infoIndex, int index)
	{
		return alert.getInfo(infoIndex).getResource(index).getUri();
	}
	
	public String getAreaDesc(int infoIndex, int index)
	{
		return alert.getInfo(infoIndex).getArea(index).getAreaDesc();
	}
	
	public String getGeoCode(int infoIndex, int index)
	{
		return alert.getInfo(infoIndex).getArea(index).getGeocode(0).getValue();
	}
	


	
	//CAP 요소 Setter
	public void setIdentifier(String text)
	{
		alert = Alert.newBuilder(alert).setIdentifier(text).build();
	}	
	
	public void setSender(String sender)
	{
		alert = Alert.newBuilder(alert).setSender(sender).build();
	}
	
	public void setSent(String text)
	{
		alert = Alert.newBuilder(alert).setSent(text).build();
	}
	
	public void setSent(GregorianCalendar cal)
	{
		alert = Alert.newBuilder(alert).setSent(CapUtil.formatCapDate(cal)).build();
	}	

	public Status setStatus(String text)
	{
		text = text.toUpperCase();
		for (Status status : Alert.Status.values())
		{
			if(text.equals(status.toString()))
			{
				alert = Alert.newBuilder(alert).setStatus(status).build();
				return status;
			}
		}
		return null;
	}

	public MsgType setMsgType(String text)
	{
		for (MsgType msgType : Alert.MsgType.values())
		{
			if(text.toUpperCase().equals(msgType.toString()))
			{
				alert = Alert.newBuilder(alert).setMsgType(msgType).build();		
				return msgType;
			}
		}
		return null;
	}

	public void setSource(String source) 
	{
		alert = Alert.newBuilder(alert).setSource(source).build();
	}
	
	public Scope setScope(String text)
	{
		for (Scope scope : Alert.Scope.values())
		{
			if(text.toUpperCase().equals(scope.toString()))
			{
				alert = Alert.newBuilder(alert).setScope(scope).build();		
				return scope;
			}
		}
		return null;
	}
	
	public void setAddresses(String string) 
	{
		alert = Alert.newBuilder(alert).setAddresses(Group.newBuilder().addValue(string).build()).build();		
	}

	public void setCode(String code) 
	{
		alert = Alert.newBuilder(alert).setCode(0, code).build();
	}

	public void setLanguage(String text)
	{
		info = Info.newBuilder(info).setLanguage(text).build();
	}

	public Category setCategory(String text)
	{
		for (Category category : Info.Category.values())
		{
			if(text.toUpperCase().equals(category.toString()))
			{
				info = Info.newBuilder(info).setCategory(0, category).build();				
				return category;
			}
		}
		return null;
	}
		
	public void setEvent(String event) 
	{
		info = Info.newBuilder(info).setEvent(event).build();
	}

	public Urgency setUrgency(String text)
	{
		for (Urgency urgency : Info.Urgency.values())
		{
			if(text.toUpperCase().equals(urgency.toString()))
			{
				info = Info.newBuilder(info).setUrgency(urgency).build();				
				return urgency;
			}
		}
		return null;
	}
	
	public Severity setSeverity(String text) 
	{
		for (Severity severity : Info.Severity.values())
		{
			if(text.toUpperCase().equals(severity.toString()))
			{
				info = Info.newBuilder(info).setSeverity(severity).build();				
				return severity;
			}
		}
		return null;
	}
	
	public Certainty setCertainty(String text)
	{
		for (Certainty certainty : Info.Certainty.values())
		{
			if(text.toUpperCase().equals(certainty.toString()))
			{
				info = Info.newBuilder(info).setCertainty(certainty).build();				
				return certainty;
			}
		}
		return null;
	}

	public void setEventCode(String text)
	{
		info = Info.newBuilder(info).setEventCode(0, Info.newBuilder().addEventCodeBuilder().setValueName("?먯뿰?щ궃").setValue(text).build()).build();
	}

	public void setEffective(GregorianCalendar cal)
	{
		info = Info.newBuilder(info).setEffective(CapUtil.formatCapDate(cal)).build();
	}

	public void setSenderName(String senderName)
	{
		info = Info.newBuilder(info).setSenderName(senderName).build();
	}

	public void setHeadline(String headline) {
		info = Info.newBuilder(info).setHeadline(headline).build();
	}

	public void setDescription(String description) {
		info = Info.newBuilder(info).setDescription(description).build();
	}

	public void setWeb(String web)
	{
		info = Info.newBuilder(info).setWeb(web).build();
	}

	public void setContact(String contact)
	{
		info = Info.newBuilder(info).setContact(contact).build();
	}
	
	
	
	private String getValueInJasonObject(String jsonInput) {
		
		try {
			JSONObject jsonObj = new JSONObject(jsonInput);
			return jsonObj.getString("value");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Database에서 사용하는 CAP 객체를 Google CAP Library에서 사용하는 CAP 객체로 변환.
	 * 
	 * @param alertList Database에서 사용하는 CAP 객체들의 리스트.
	 * @return Google CAP Library에서 사용하는 CAP 객체들의 리스트.
	 */	
	public ArrayList<String> databaseObjectToCapLibraryObject(ArrayList<CAPAlert> alertList) {
		
		ArrayList<String> capList = new ArrayList<String>();
		
		for (CAPAlert capAlert : alertList)
		{
			Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
					.setIdentifier(capAlert.getIdentifier())
					.setSender(capAlert.getSender())
					.setSent(this.dateToString(capAlert.getSent()))
					.setStatus(this.setStatus(capAlert.getStatus().toString()))
					.setMsgType(this.setMsgType(capAlert.getMsgType().toString()))
					.setScope(this.setScope(capAlert.getScope().toString()))
//					.addCode(capAlert.getCode())
					.buildPartial();

			for (CAPInfo capInfo : capAlert.getInfoList())
			{
				Info info = Info.newBuilder()
						.setLanguage(capInfo.getLanguage().toString())
						.addCategory(this.setCategory(capInfo.getCategory().toString()))
						.setEvent(capInfo.getEvent().toString())
						.setUrgency(this.setUrgency(capInfo.getUrgency().toString()))
						.setSeverity(this.setSeverity(capInfo.getSeverity().toString()))
						.setCertainty(this.setCertainty(capInfo.getCertainty().toString()))
						.addEventCode(Info.newBuilder().addEventCodeBuilder().setValueName("TTAS.KO-07.0046/R5 재난 종류 코드").setValue(getValueInJasonObject(capInfo.getEventCode())).build())
//						.setEffective(this.dateToString(capInfo.getEffective()))
						.setSenderName(capInfo.getSenderName())
						.setHeadline(capInfo.getHeadline())
						.setDescription(capInfo.getDescription())
						.setWeb(capInfo.getWeb())
						.setContact(capInfo.getContact())
						.buildPartial();
				

				for (CAPResource capResource : capInfo.getResList())
				{
					Resource resource = Resource.newBuilder()
							.setResourceDesc(capResource.getResourceDesc())
							.setMimeType(capResource.getMimeType())
							.setSize((long)capResource.getSize())
							.setUri(capResource.getUri())
//							.setDerefUri(capResource.getDeferURI())
//							.setDigest(capResource.getDigest().toString())
							.buildPartial();
					
					info = Info.newBuilder(info)
							.addResource(resource)
							.buildPartial();
				}
				for (CAPArea capArea : capInfo.getAreaList())
				{
					Area area = Area.newBuilder()
							.setAreaDesc(capArea.getAreaDesc())
//							.addGeocode(ValuePair.newBuilder().setValueName("G1").setValue(capArea.getGeocode()).build())
							.buildPartial();
					
					info = Info.newBuilder(info)
							.addArea(area)
							.buildPartial();
				}
				alert = Alert.newBuilder(alert)
						.addInfo(info)
						.build();
			}			
			capList.add(capXmlBuilder.toXml(alert));
		}
		return capList;
	}
}
