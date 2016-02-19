package kr.ac.uos.ai.ieas.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

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
import com.google.publicalerts.cap.ValuePair;

import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KIEAS_Constant;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasList;

/**
 * CAP 형식의 메시지를 생성하고 다루는 클래스.
 * Google CAP Library를 활용하여 CAP 메시지를 다룬다.
 * @author byun-ai
 *
 */
public class KieasMessageBuilder implements IKieasMessageBuilder
{
	public static final String IDENTIFIER = "Identifier";
	public static final String SENDER = "Sender";
	public static final String SENT = "Sent";
	public static final String STATUS = "Status";
		public static final String ACTUAL = "Actual";
		public static final String EXERCISE = "Exercise";
		public static final String SYSTEM = "System";
		public static final String TEST = "Test";
		public static final String DRAFT = "Draft";
	public static final String MSG_TYPE = "MsgType";
		public static final String ALERT = "Alert";
		public static final String UPDATE = "Update";
		public static final String CANCEL = "Cancel";
		public static final String ACK = "Ack";
		public static final String ERROR = "Error";
	public static final String SCOPE = "Scope";
		public static final String PUBLIC = "Public";
		public static final String RESTRICTED = "Restricted";
		public static final String PRIVATE = "private";
	public static final String RESTRICTION = "Restriction";
	public static final String ADDRESSES = "Addresses";	
	public static final String CODE = "Code";
	public static final String NOTE = "Note";

	public static final String INFO = "Info";
	public static final String LANGUAGE = "Language";
		public static final String KOREAN = "ko-KR";
		public static final String ENGLISH = "en-US";
	public static final String CATEGORY = "Category";
		public static final String GEO = "Geo";
		public static final String MET = "Met";
		public static final String SAFETY = "Safety";
		public static final String SECURITY = "Security";
		public static final String RESCUE = "Rescue";
		public static final String FIRE = "Fire";
		public static final String HEALTH = "Health";
		public static final String ENV = "Env";
		public static final String TRANSPORT = "Transport";
		public static final String INFRA = "Infra";
		public static final String CBRNE = "CBRNE";
		public static final String OTHER = "Other";
	public static final String EVENT = "Event";
	public static final String RESPONSE_TYPE = "ResponseType";
		public static final String SHELTER = "Shelter";
		public static final String EVACUATE = "Evacuate";
		public static final String PREPARE = "Prepare";
		public static final String EXECUTE = "Execute";
		public static final String AVOID = "Aboid";
		public static final String MONITOR = "Monitor";
		public static final String ASSESS = "Assess";
		public static final String ALLCLEAR = "AllClear";
		public static final String NONE = "None";
	public static final String URGENCY = "Urgency";
	public static final String SEVERITY = "Severity";
	public static final String CERTAINTY = "Certainty";
	public static final String AUDIENCE = "Audience";
	public static final String EVENT_CODE = "EventCode";
	public static final String EFFECTIVE = "Effective";
	public static final String ON_SET = "Onset";	
	public static final String EXPIRES = "Expires";
	public static final String SENDER_NAME = "SenderName";
	public static final String HEADLINE = "Headline";
	public static final String DESCRIPTION = "Description";
	public static final String INSTRUCTION = "Instruction";
	public static final String WEB = "Web";
	public static final String CONTACT = "Contact";
	public static final String PARAMETER = "Parameter";

	public static final String RESOURCE = "Resource";
	public static final String RESOURCE_DESC = "ResourceDesc";
	public static final String MIME_TYPE = "MimeType";
	public static final String SIZE = "Size";
	public static final String URI = "Uri";
	public static final String DEREF_URI = "DerefUri";
	public static final String DIGEST = "Digest";

	public static final String AREA = "Area";
	public static final String AREA_DESC = "AreaDesc";
	public static final String POLYGON = "Polygon";
	public static final String CIRCLE = "Circle";
	public static final String GEO_CODE = "GeoCode";
	public static final String ALTITUDE = "Altitude";
	public static final String CEILING = "Ceiling";
	
	
	private static final int DEFAULT_INFO_SIZE = 0;
	
	
	private CapXmlBuilder 	capXmlBuilder;
	private CapXmlParser 	capXmlParser;
	private CapValidator 	capValidator;
	
	private Map<String, List<Item>> CapElementToEnumMap;

	private Alert  mAlert;
	private String xmlMessage;
	
	

	public KieasMessageBuilder()
	{
		this.capXmlBuilder = new CapXmlBuilder();
		this.capXmlParser = new CapXmlParser(true);
		this.capValidator = new CapValidator();
		
		init();
	}
	
	private void init()
	{
		this.mAlert = buildDefaultAlert();
	}

	/**
	 * View에 사용될 Enum을 Item으로 사용하여 Value를 디스플레이하고 Key에 의해 아이템 판별이 이루어진다.
	 * 
	 * @author byun-ai
	 *
	 */
	public static class Item
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

		public String getValue()
		{
			return value;
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
	public Map<String, List<Item>> getCapEnumMap()
	{
		this.CapElementToEnumMap = new HashMap<>();
		buildAlertCapEnumMap();
		buildInfoCapEnumMap();
		return CapElementToEnumMap;
	}

	
	public String buildDefaultMessage()
	{		
		this.mAlert = buildDefaultAlert();
		
		for (int infoIndex = 0; infoIndex < DEFAULT_INFO_SIZE; infoIndex++)
		{
			mAlert = Alert.newBuilder(mAlert).addInfo(buildDefaultInfo()).build();
		}	
		System.out.println("build default cap");
		
		return capXmlBuilder.toXml(mAlert);
	}
	
	private Alert buildDefaultAlert()
	{
		Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
				.setIdentifier("Identifier")
				.setSender("Sender")
				.setSent(CapUtil.formatCapDate(getDateCalendar()))
				.setStatus(Alert.Status.ACTUAL)
				.setMsgType(Alert.MsgType.ALERT)
				.setScope(Alert.Scope.RESTRICTED)
				.buildPartial();
		
		return alert;
	}

	private Info buildDefaultInfo()
	{
		Info info = Info.newBuilder()
				.addCategory(Info.Category.MET)
				.setLanguage("ko-KR")
				.setEvent("Event") 
				.setUrgency(Info.Urgency.UNKNOWN_URGENCY)
				.setSeverity(Info.Severity.UNKNOWN_SEVERITY)
				.setCertainty(Info.Certainty.UNKNOWN_CERTAINTY)
				.buildPartial();
		
		return info;
	}
	
	private Resource buildDefaultResource()
	{
		Resource resource = Resource.newBuilder()
				.setResourceDesc("")
				.setMimeType("")
				.buildPartial();
		
		return resource;
	}
	
	private Area buildDefaultArea()
	{
		Area area = Area.newBuilder()
				.setAreaDesc("")
				.buildPartial();
		
		return area;
	}

	private void incrementInfoByInfoIndex(int infoIndex)
	{
		for (int i = infoIndex - mAlert.getInfoCount(); i < mAlert.getInfoCount(); i++)
		{
			mAlert = Alert.newBuilder(mAlert).addInfo(buildDefaultInfo()).build();				
		}
	}
	
	private void incrementParameterByParameterIndex(int infoIndex, int paramterIndex)
	{
		Info info = mAlert.getInfo(infoIndex);
		
		for (int i = paramterIndex - info.getParameterCount(); i < info.getParameterCount(); i++)
		{			
			info = Info.newBuilder(info).addParameter(ValuePair.newBuilder().setValueName("").setValue("")).buildPartial();
		}
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}
	
	private void incrementResourceByResourceIndex(int infoIndex, int resourceIndex)
	{
		Info info = mAlert.getInfo(infoIndex);
		
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		for (int i = resourceIndex - info.getResourceCount(); i < info.getResourceCount(); i++)
		{			
			info = Info.newBuilder(info).addResource(buildDefaultResource()).buildPartial();
		}
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}
	
	private void incrementAreaByAreaIndex(int infoIndex, int areaIndex)
	{
		Info info = mAlert.getInfo(infoIndex);
		
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		for (int i = areaIndex - info.getAreaCount(); i < info.getAreaCount(); i++)
		{			
			info = Info.newBuilder(info).addArea(buildDefaultArea()).buildPartial();
		}
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}
		
	@Override
	public boolean validateMessage(String message)
	{
		try
		{			
			capValidator.validateAlert(capXmlParser.parseFrom(message));
			return true;
		}
		catch (CapException | NotCapException | SAXParseException e)
		{
			return false;
		}
	}

	private GregorianCalendar getDateCalendar()
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone(KIEAS_Constant.DEFAULT_TIME_ZONE));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		return cal;
	}

//	private String dateToString(Date date)
//	{
//		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone(KIEAS_Constant.DEFAULT_TIME_ZONE));
//		cal.setTime(date);
//		return CapUtil.formatCapDate(cal);
//	}

	@Override
	public int getInfoCount()
	{
		return mAlert.getInfoCount();
	}

	@Override
	public int getResourceCount(int infoIndex)
	{
		return mAlert.getInfo(infoIndex).getResourceCount();
	}

	@Override
	public int getAreaCount(int infoIndex)
	{
		return mAlert.getInfo(infoIndex).getAreaCount();
	}

	@Override
	public String getResourceDesc(int index)
	{
		return mAlert.getInfo(0).getResource(index).getResourceDesc();
	}
	@Override
	public String getMimeType(int index)
	{
		return mAlert.getInfo(0).getResource(index).getMimeType();
	}
	@Override
	public String getUri(int index)
	{
		return mAlert.getInfo(0).getResource(index).getUri();
	}
	@Override
	public String getAreaDesc(int index)
	{
		return mAlert.getInfo(0).getArea(index).getAreaDesc();
	}
	@Override
	public String getGeoCode(int index)
	{		
		if(mAlert.getInfo(0).getArea(0).getGeocode(0) != null)
		{
			return mAlert.getInfo(0).getArea(0).getGeocode(0).getValue();
		}
		else
		{
			return "";
		}
	}

	//CAP 요소 Setter
//	public void setAlert(HashMap<String, String> alertElementList)
//	{
//		mAlert = Alert.newBuilder()
//				.setIdentifier(alertElementList.get(IDENTIFIER))
//				.setSender(alertElementList.get(SENDER))
//				.setSent(CapUtil.formatCapDate(getDateCalendar()))
//				.setStatus(this.setStatus(alertElementList.get(STATUS)))
//				.setMsgType(this.setMsgType(alertElementList.get(MSG_TYPE)))
//				.setScope(this.setScope(alertElementList.get(SCOPE)))
//				.setRestriction(alertElementList.get(RESTRICTION))
//				.addCode(KieasConfiguration.KIEAS_Constant.CODE)
//				.buildPartial();
//	}

//	public void setInfo(List<Map<String, String>> infoElementList)
//	{
//		mAlert = Alert.newBuilder(mAlert).clearInfo().buildPartial();
//
//		for(int i = 0; i < infoElementList.size(); i++)
//		{
//			Info info = Info.newBuilder()
//					.setLanguage(infoElementList.get(i).get(LANGUAGE))
//					.setCategory(0, this.setCategory(infoElementList.get(i).get(CATEGORY)))
//					.setEvent(infoElementList.get(i).get(EVENT))
//					.setUrgency(this.setUrgency(infoElementList.get(i).get(URGENCY)))
//					.setSeverity(this.setSeverity(infoElementList.get(i).get(SEVERITY)))
//					.setCertainty(this.setCertainty(infoElementList.get(i).get(CERTAINTY)))
//					.addEventCode(Info.newBuilder().addEventCodeBuilder().setValueName(infoElementList.get(i).get(EVENT)).setValue(infoElementList.get(i).get(EVENT)) )
//					.setSenderName(infoElementList.get(i).get(SENDER_NAME))
//					.setHeadline(infoElementList.get(i).get(HEADLINE))
//					.setDescription(infoElementList.get(i).get(DESCRIPTION))
//					.setContact(infoElementList.get(i).get(CONTACT))
//					.buildPartial();
//			mAlert = Alert.newBuilder(mAlert).addInfo(info).build();
//		}
//	}

//	public void setArea(Map<String, String> areaElementList)
//	{
//		for(int i = 0; i < areaElementList.size(); i++)
//		{
//			Area area = Area.newBuilder()
//					.setAreaDesc("")
//					.addGeocode(Area.newBuilder().addGeocodeBuilder().setValueName(areaElementList.get(GEO_CODE)).setValue(areaElementList.get(GEO_CODE)))
//					.buildPartial();
//			mInfo = Info.newBuilder(mInfo).addArea(area).build();
//			mAlert = Alert.newBuilder(mAlert).addInfo(mInfo).build();			
//		}
//	}	

	private Status convertToStatus(String text)
	{
		text = text.toUpperCase();
		for (Status status : Alert.Status.values())
		{
			if(text.equals(status.toString()))
			{
				if(mAlert != null)
				{
					mAlert = Alert.newBuilder(mAlert).setStatus(status).build();
					return status;					
				}
				else
				{
					return status;
				}
			}
		}
		return null;
	}
	
	private MsgType convertToMsgType(String text)
	{
		for (MsgType msgType : Alert.MsgType.values())
		{
			if(text.toUpperCase().equals(msgType.toString()))
			{
				if(mAlert != null)
				{
					mAlert = Alert.newBuilder(mAlert).setMsgType(msgType).build();
					return msgType;			
				}
				else
				{	
					return msgType;
				}
			}
		}
		return null;
	}

	private Scope convertToScope(String text)
	{
		for (Scope scope : Alert.Scope.values())
		{
			if(text.toUpperCase().equals(scope.toString()))
			{
				if(mAlert != null)
				{
					mAlert = Alert.newBuilder(mAlert).setScope(scope).build();		
					return scope;				
				}
				else
				{	
					return scope;
				}
			}
		}
		return null;
	}

	private Group convertToAddresses(String address) 
	{
		return Group.newBuilder().addValue(address).build();		
	}

	private Category convertToCategory(String text)
	{
		for (Category category : Info.Category.values())
		{
			if(text.toUpperCase().equals(category.toString()))
			{
				return category;				
			}
		}
		return null;
	}

	private ResponseType convertToResponseType(String text)
	{
		for (ResponseType responseType : Info.ResponseType.values())
		{
			if(text.toUpperCase().equals(responseType.toString()))
			{
				return responseType;				
			}
		}
		return null;
	}
	
	private Urgency convertToUrgency(String text)
	{
		for (Urgency urgency : Info.Urgency.values())
		{
			if(text.toUpperCase().equals(urgency.toString()))
			{
				return urgency;						
			}
		}
		return null;
	}

	private Severity convertToSeverity(String text) 
	{
		for (Severity severity : Info.Severity.values())
		{
			if(text.toUpperCase().equals(severity.toString()))
			{
				return severity;				
			}
		}
		return null;
	}

	private Certainty convertToCertainty(String text)
	{
		for (Certainty certainty : Info.Certainty.values())
		{
			if(text.toUpperCase().equals(certainty.toString()))
			{
				return certainty;				
			}
		}
		return null;
	}

	private ValuePair convertToEventCode(String text)
	{
		return ValuePair.newBuilder().setValueName(KIEAS_Constant.EVENT_CODE_VALUE_NAME).setValue(text).build();
	}
	
	private ValuePair convertToGeoCode(String text)
	{
		return ValuePair.newBuilder().setValueName(KIEAS_Constant.GEO_CODE_VALUE_NAME).setValue(text).build();
	}

//	private void setEffective(String text)
//	{
//		mInfo = Info.newBuilder(mInfo).setEffective(text).build();
//	}
//	
//	private void setEffective(GregorianCalendar cal)
//	{
//		mInfo = Info.newBuilder(mInfo).setEffective(CapUtil.formatCapDate(cal)).build();
//	}

	
//	private String getValueInJasonObject(String jsonInput)
//	{
//		try
//		{
//			JSONObject jsonObj = new JSONObject(jsonInput);
//			return jsonObj.getString("value");
//		}
//		catch (JSONException e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//	}

//	/**
//	 * Database에서 사용하는 CAP 객체를 Google CAP Library에서 사용하는 CAP 객체로 변환.
//	 * 
//	 * @param alertList Database에서 사용하는 CAP 객체들의 리스트.
//	 * @return Google CAP Library에서 사용하는 CAP 객체들의 리스트.
//	 */	
//	public List<String> convertDbToCap(List<CAPAlert> alertList)
//	{		
//		List<String> capList = new ArrayList<String>();
//
//		for (CAPAlert capAlert : alertList)
//		{
//			Alert mAlert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
//					.setIdentifier(capAlert.getIdentifier())
//					.setSender(capAlert.getSender())
//					.setSent(this.dateToString(capAlert.getSent()))
//					.setStatus(this.convertToStatus(capAlert.getStatus().toString()))
//					.setMsgType(this.convertToMsgType(capAlert.getMsgType().toString()))
//					.setScope(this.convertToScope(capAlert.getScope().toString()))
//					//					.addCode(capAlert.getCode())
//					.buildPartial();
//
//			for (CAPInfo capInfo : capAlert.getInfoList())
//			{
//				Info mInfo = Info.newBuilder()
//						.setLanguage(capInfo.getLanguage().toString())
//						.addCategory(this.convertToCategory(capInfo.getCategory().toString()))
//						.setEvent(capInfo.getEvent().toString())
//						.setUrgency(this.convertToUrgency(capInfo.getUrgency().toString()))
//						.setSeverity(this.convertToSeverity(capInfo.getSeverity().toString()))
//						.setCertainty(this.convertToCertainty(capInfo.getCertainty().toString()))
//						.addEventCode(this.convertToEventCode(capInfo.getEventCode()))
//						//						.setEffective(this.dateToString(capInfo.getEffective()))
//						.setSenderName(capInfo.getSenderName())
//						.setHeadline(capInfo.getHeadline())
//						.setDescription(capInfo.getDescription())
//						//						.setWeb(capInfo.getWeb())
//						//						.setContact(capInfo.getContact())
//						.buildPartial();
//
//
//				for (CAPResource capResource : capInfo.getResList())
//				{
//					Resource resource = Resource.newBuilder()
//							.setResourceDesc(capResource.getResourceDesc())
//							.setMimeType(capResource.getMimeType())
//							.setSize((long)capResource.getSize())
//							.setUri(capResource.getUri())
//							//							.setDerefUri(capResource.getDeferURI())
//							//							.setDigest(capResource.getDigest().toString())
//							.buildPartial();
//
//					mInfo = Info.newBuilder(mInfo)
//							.addResource(resource)
//							.buildPartial();
//				}
//				for (CAPArea capArea : capInfo.getAreaList())
//				{
//					Area area = Area.newBuilder()
//							.setAreaDesc(capArea.getAreaDesc())
//							//							.addGeocode(ValuePair.newBuilder().setValueName("G1").setValue(capArea.getGeocode()).build())
//							.buildPartial();
//
//					mInfo = Info.newBuilder(mInfo)
//							.addArea(area)
//							.buildPartial();
//				}
//				mAlert = Alert.newBuilder(mAlert)
//						.addInfo(mInfo)
//						.build();
//			}			
//			capList.add(capXmlBuilder.toXml(mAlert));
//		}
//		return capList;
//	}
//
//	public CAPAlert convertCapToDb(String capMessage)
//	{	
//		CAPAlert capAlert = new CAPAlert();
//		CAPInfo capInfo = new CAPInfo();
//		try
//		{
//			mAlert = capXmlParser.parseFrom(capMessage);
//
//			capAlert.setIdentifier(mAlert.getIdentifier());
//			capAlert.setSender(mAlert.getSender());
//			capAlert.setSent(getDateCalendar().getTime());
//			for (CAPAlert.Status status : CAPAlert.Status.values())
//			{
//				if(status.toString().toUpperCase().equals(mAlert.getStatus().toString()))
//				{
//					capAlert.setStatus(status);
//				}
//			}
//			for (CAPAlert.MsgType msgType : CAPAlert.MsgType.values())
//			{
//				if(msgType.toString().toUpperCase().equals(mAlert.getMsgType().toString()))
//				{
//					capAlert.setMsgType(msgType);
//				}
//			}
//			for (CAPAlert.Scope scope : CAPAlert.Scope.values())
//			{
//				if(scope.toString().toUpperCase().equals(mAlert.getScope().toString()))
//				{
//					capAlert.setScope(scope);
//				}
//			}
//			capAlert.setCode(mAlert.getCode(0));
//
//			for (CAPInfo.Language language : CAPInfo.Language.values())
//			{
//				if(language.toString().toUpperCase().equals(mAlert.getInfo(0).getLanguage()))
//				{
//					capInfo.setLanguage(language);
//				}
//			}
//			for (CAPInfo.Category category : CAPInfo.Category.values())
//			{
//				if(category.toString().toUpperCase().equals(mAlert.getInfo(0).getCategory(0).toString()))
//				{
//					capInfo.setCategory(category);
//				}
//			}
//			capInfo.setEvent(mAlert.getInfo(0).getEvent());
//			for (CAPInfo.Urgency urgency : CAPInfo.Urgency.values())
//			{
//				if(urgency.toString().toUpperCase().equals(mAlert.getInfo(0).getUrgency().toString()))
//				{
//					capInfo.setUrgency(urgency);
//				}
//			}
//			for (CAPInfo.Severity severity : CAPInfo.Severity.values())
//			{
//				if(severity.toString().toUpperCase().equals(mAlert.getInfo(0).getSeverity().toString()))
//				{
//					capInfo.setSeverity(severity);
//				}
//			}
//			for (CAPInfo.Certainty certainty : CAPInfo.Certainty.values())
//			{
//				if(certainty.toString().toUpperCase().equals(mAlert.getInfo(0).getCertainty().toString()))
//				{
//					capInfo.setCertainty(certainty);
//				}
//			}
//			capInfo.setEventCode(mAlert.getInfo(0).getEventCode(0).getValue());
//			capInfo.setEffective(DataFormatUtils.convertStringToDate(mAlert.getInfo(0).getEffective()));
//			capInfo.setSenderName(mAlert.getInfo(0).getSenderName());
//			capInfo.setHeadline(mAlert.getInfo(0).getHeadline());
//			capInfo.setDescription(mAlert.getInfo(0).getDescription());
//			capInfo.setWeb(mAlert.getInfo(0).getWeb());
//			capInfo.setContact(mAlert.getInfo(0).getContact());
//
//			List<CAPInfo> infoList = new ArrayList<CAPInfo>();
//			infoList.add(capInfo);
//
//			capAlert.setInfoList(infoList);
//		}
//		catch (NotCapException | SAXParseException | CapException e) 
//		{
//			e.printStackTrace();
//		}
//
//		return capAlert;
//	}
	
	@Override
	public String build()
	{		
		//Alert build
		//필수 Alert 요소들
		Alert alert = Alert.newBuilder(mAlert).setXmlns(CapValidator.CAP_LATEST_XMLNS)
//			.setIdentifier(mAlert.getIdentifier())
//			.setSender(mAlert.getSender())
//			.setSent(mAlert.getSent())
//			.setStatus(mAlert.getStatus())
//			.setMsgType(mAlert.getMsgType())
//			.setScope(mAlert.getScope())
//			.clearCode()
//			.addCode(KieasConfiguration.KIEAS_Constant.CODE)
			.clearInfo()
			.buildPartial();
		
		//옵션 Alert 요소들
//		if(mAlert.getAddresses().getValue(0) != null)
//		{
//			alert = Alert.newBuilder(alert)
//				.setAddresses(Group.newBuilder())
//				.buildPartial();
//		}	
		if(mAlert.getRestriction() != null)
		{
			alert = Alert.newBuilder(alert)
				.setRestriction(mAlert.getRestriction())
				.buildPartial();
		}
		if(mAlert.getNote() != null)
		{
			alert = Alert.newBuilder(alert)
				.setNote(mAlert.getNote())
				.buildPartial();
		}
		
		//Info build
		for(int infoIndex = 0; infoIndex < mAlert.getInfoCount(); infoIndex++)
		{
			Info info = mAlert.getInfo(infoIndex);
			//필수 Info 요소들
			info = Info.newBuilder(info)
				.setLanguage(info.getLanguage())
				.clearCategory()
				.addCategory(info.getCategory(0))
				.setEvent(info.getEvent())
				.setUrgency(info.getUrgency())
				.setSeverity(info.getSeverity())
				.setCertainty(info.getCertainty())
				.buildPartial();
			
			//옵션 Info 요소들
			if(info.getAudience() != null)
			{
				info = Info.newBuilder(info)
					.setAudience(info.getAudience())
					.buildPartial();
			}			
			if(info.getResponseType(0) != null)
			{
				info = Info.newBuilder(info)
					.clearResponseType()
					.addResponseType(info.getResponseType(0))
					.buildPartial();
			}			
			if(info.getEventCode(0) != null)
			{
				info = Info.newBuilder(info)
					.clearEventCode()
					.addEventCode(info.getEventCode(0))
					.buildPartial();
			}			
//			if(infos.get(infoIndex).get(EFFECTIVE).toString().length() != 0)
//			{
//				info = Info.newBuilder(info)
//					.setEffective(infoElementNameToValueMap.get(EFFECTIVE).toString())
//					.buildPartial();
//			}			
//			if(infos.get(infoIndex).get(EXPIRES).toString().length() != 0)
//			{
//				info = Info.newBuilder(info)
//					.setExpires(infoElementNameToValueMap.get(EXPIRES).toString())
//					.buildPartial();
//			}			
			if(info.getSenderName() != null)
			{
				info = Info.newBuilder(info)
					.setSenderName(info.getSenderName())
					.buildPartial();
			}			
			if(info.getHeadline() != null)
			{
				info = Info.newBuilder(info)
					.setHeadline(info.getHeadline())
					.buildPartial();
			}			
			if(info.getDescription() != null)
			{
				info = Info.newBuilder(info)
					.setDescription(info.getDescription())
					.buildPartial();
			}			
			if(info.getInstruction() != null)
			{
				info = Info.newBuilder(info)
					.setInstruction(info.getInstruction())
					.buildPartial();
			}			
			if(info.getWeb() != null)
			{
				info = Info.newBuilder(info)
					.setWeb(info.getWeb())
					.buildPartial();
			}			
			if(info.getContact() != null)
			{
				info = Info.newBuilder(info)
					.setContact(info.getContact())
					.buildPartial();
			}
			
//			//Resource build
//			for(int resourceIndex = 0; resourceIndex < resources.size(); resourceIndex++)
//			{
//				resourceElementNameToValueMap = (Map<String, String>) ((List<?>) infoElementNameToValueMap.get(RESOURCE)).get(resourceIndex);
//				
//				//필수 Resource 요소들
//				Resource resource = Resource.newBuilder()
//					.setResourceDesc(resourceElementNameToValueMap.get(RESOURCE_DESC).toString())
//					.setMimeType(resourceElementNameToValueMap.get(MIME_TYPE).toString())
//					.buildPartial();
//				
//				//옵션 Resource 요소들
//				
//				info = Info.newBuilder(info)
//						.addResource(resource)
//						.buildPartial();
//			}
//			//Area build
//			for(int areaIndex = 0; areaIndex < resources.size(); areaIndex++)
//			{
//				areaElementNameToValueMap = ((List<Map<String, String>>) infoElementNameToValueMap.get(AREA)).get(areaIndex);
//				
//				//필수 Area 요소들
//				Area area = Area.newBuilder()
//					.setAreaDesc(areaElementNameToValueMap.get(AREA_DESC).toString())
//					.buildPartial();
//				
//				//옵션 Area 요소들
//				
//				info = Info.newBuilder(info)
//						.addArea(area)
//						.buildPartial();
//			}
						
			alert = Alert.newBuilder(alert)
					.addInfo(info)
					.build();
		}
		
		
		
		this.mAlert = alert;
		this.xmlMessage = capXmlBuilder.toXml(alert);
		System.out.println("Build Comp. mAlert ID : " + mAlert.getIdentifier());
		
		return xmlMessage;
	}	

	@Override
	public String getMessage()
	{
		try
		{
			this.xmlMessage = capXmlBuilder.toXml(mAlert);
			return xmlMessage;
		}
		catch (NotCapException e)
		{
			e.printStackTrace();
		}
		System.out.println("There is no CAP message");
		return "";
	}

	@Override
	public void setMessage(String message)
	{
		try
		{			
			mAlert = capXmlParser.parseFrom(message);
		}
		catch (NotCapException | SAXParseException | CapException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String getIdentifier()
	{		
		return mAlert.getIdentifier();
	}

	@Override
	public String getSender()
	{
		return mAlert.getSender();
	}

	@Override
	public String getSent()
	{
		return mAlert.getSent();
	}

	@Override
	public String getStatus() 
	{
		return mAlert.getStatus().toString();
	}

	@Override
	public String getMsgType()
	{
		return mAlert.getMsgType().toString();
	}

	@Override
	public String getScope()
	{
		return mAlert.getScope().toString();
	}

	@Override
	public String getRestriction()
	{
		return mAlert.getRestriction();
	}

	@Override
	public String getAddresses()
	{
		return mAlert.getAddresses().getValue(0).toString();
	}

	@Override
	public String getCode()
	{
		return mAlert.getCode(0);
	}

	@Override
	public String getNote()
	{
		return mAlert.getNote();
	}
	

	
	@Override
	public String getLanguage(int index)
	{
		return mAlert.getInfo(index).getLanguage().toString();
	}
	
	@Override
	public String getCategory(int index)
	{
		return mAlert.getInfo(index).getCategory(0).toString();
	}
	//public String getCategory(int infoIndex, int categoryIndex){}

	@Override
	public String getResponseType(int index)
	{
		return mAlert.getInfo(index).getResponseType(0).toString();
	}
	//public String getResponseType(int infoIndex, int responseTypeIndex){}

	@Override
	public String getEvent(int index)
	{
		try
		{
			if(mAlert.getInfoCount() != 0 && mAlert.getInfo(index).hasEvent())
			{
				return mAlert.getInfo(index).getEvent();				
			}
			else
			{
				return "";
			}
		}
		catch (NotCapException e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public String getUrgency(int index)
	{
		return mAlert.getInfo(index).getUrgency().toString();
	}
	
	@Override
	public String getSeverity(int index)
	{
		return mAlert.getInfo(index).getSeverity().toString();
	}
	
	@Override
	public String getCertainty(int index)
	{
		return mAlert.getInfo(index).getCertainty().toString();
	}
	
	@Override
	public String getAudience(int index)
	{
		return mAlert.getInfo(index).getAudience();
	}

	@Override
	public String getEventCode(int index) 
	{
		return mAlert.getInfo(index).getEventCodeList().get(0).getValue().toString();
	}
	
	@Override
	public String getEffective(int index)
	{
		return mAlert.getInfo(index).getEffective().toString();
	}
	
	@Override
	public String getExpires(int index)
	{
		return mAlert.getInfo(index).getExpires();
	}

	@Override
	public String getSenderName(int index)
	{
		return mAlert.getInfo(index).getSenderName().toString();
	}
	
	@Override
	public String getHeadline(int index)
	{
		return mAlert.getInfo(index).getHeadline().toString();
	}
	
	@Override
	public String getDescription(int index)
	{
		return mAlert.getInfo(index).getDescription().toString();
	}
	
	@Override
	public String getInstruction(int index)
	{
		return mAlert.getInfo(index).getInstruction();
	}
	
	@Override
	public String getWeb(int index)
	{
		return mAlert.getInfo(index).getWeb().toString();
	}
	@Override
	public String getContact(int index)
	{
		return mAlert.getInfo(index).getContact().toString();
	}

	@Override
	public void setIdentifier(String text)
	{
		if(mAlert == null)
		{
			System.out.println("mAlert null");
			buildDefaultAlert();
		}
		else
		{
			mAlert = Alert.newBuilder(mAlert).setIdentifier(text).buildPartial();			
		}	
	}

	@Override
	public void setSender(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setSender(text).buildPartial();	
	}

	@Override
	public void setSent(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setSent(text).buildPartial();		
	}
	
	@Override
	public void setSent(GregorianCalendar cal)
	{
		mAlert = Alert.newBuilder(mAlert).setSent(CapUtil.formatCapDate(cal)).build();
	}

	@Override
	public void setStatus(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setStatus(this.convertToStatus(text)).buildPartial();	
	}

	@Override
	public void setMsgType(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setMsgType(this.convertToMsgType(text)).buildPartial();	
	}

	@Override
	public void setScope(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setScope(this.convertToScope(text)).buildPartial();	
	}

	@Override
	public void setRestriction(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setRestriction(text).buildPartial();	
	}

	@Override
	public void setAddresses(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setAddresses(this.convertToAddresses(text)).buildPartial();	
	}

	@Override
	public void setCode(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setCode(0, text).buildPartial();	
	}

	@Override
	public void setNote(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setNote(text).buildPartial();	
	}

	@Override
	public void setLanguage(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setLanguage(text).buildPartial();		
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}


	@Override
	public void setCategory(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setCategory(0, this.convertToCategory(text)).buildPartial();		
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setResponseType(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResponseType(0, this.convertToResponseType(text)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setEvent(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setEvent(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setUrgency(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setUrgency(this.convertToUrgency(text)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setSeverity(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setSeverity(this.convertToSeverity(text)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setCertainty(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setCertainty(this.convertToCertainty(text)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setAudience(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setEvent(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setEventCode(int infoIndex, String text)
	{		
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).clearEventCode().addEventCode(convertToEventCode(text)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setEffective(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setEffective(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}
	
	@Override
	public void setOnset(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setOnset(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setExpires(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setExpires(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setSenderName(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setSenderName(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setHeadline(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setHeadline(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setDescription(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setDescription(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setInstruction(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setInstruction(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setWeb(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setWeb(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setContact(int infoIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setContact(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}	

	@Override
	public void setParameter(int infoIndex, int parameterIndex, String valueName, String value)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		if(parameterIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getParameterCount())
		{
			incrementParameterByParameterIndex(infoIndex, parameterIndex);
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setParameter(parameterIndex, ValuePair.newBuilder().setValueName(valueName).setValue(value)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();	
	}

	@Override
	public void setResourceDesc(int infoIndex, int resourceIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		if(resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount())
		{
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setResourceDesc(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setMimeType(int infoIndex, int resourceIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		if(resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount())
		{
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setMimeType(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setSize(int infoIndex, int resourceIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		if(resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount())
		{
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setSize(Long.parseLong(text)).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setUri(int infoIndex, int resourceIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}		
		if(resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount())
		{
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setUri(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setDerefUri(int infoIndex, int resourceIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		if(resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount())
		{
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setDerefUri(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setDigest(int infoIndex, int resourceIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		if(resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount())
		{
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setDigest(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setAreaDesc(int infoIndex, int areaIndex, String text)
	{
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		
		if(areaIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getAreaCount())
		{
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}
		Area area = Area.newBuilder(mAlert.getInfo(infoIndex).getArea(areaIndex)).setAreaDesc(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setPolygon(int infoIndex, int areaIndex, String text)
	{
		//TODO
	}

	@Override
	public void setCircle(int infoIndex, int areaIndex, String text)
	{
		//TODO
	}

	@Override
	public void setGeoCode(int infoIndex, int areaIndex, String text)
	{		
		if(infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}		
		if(areaIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getAreaCount())
		{
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}
		
		Area area = null;		
		
		if(mAlert.getInfo(infoIndex).getArea(areaIndex).getGeocodeCount() == 0)
		{
			area = Area.newBuilder(mAlert.getInfo(infoIndex).getArea(areaIndex)).addGeocode(convertToGeoCode(text)).buildPartial();
		}
		else
		{
			area = Area.newBuilder(mAlert.getInfo(infoIndex).getArea(areaIndex)).setGeocode(0, convertToGeoCode(text)).buildPartial();
		}
		
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public void setAltitude(int infoIndex, int areaIndex, String text)
	{
		//TODO
	}

	@Override
	public void setCeiling(int infoIndex, int areaIndex, String text)
	{
		//TODO
	}	
	
	@Override
	public String getDate()
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone(KIEAS_Constant.DEFAULT_TIME_ZONE));
		cal.setTime(new Date());
		return CapUtil.formatCapDate(cal);
	}
	
	@Override
	public String convertDateToYmdhms(String date)
	{
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone(KIEAS_Constant.DEFAULT_TIME_ZONE));
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

	public String generateKieasMessageIdentifier(String id)
	{
		String idNum = Double.toString(Math.random());		
		
		String identifier = id + "@" + idNum.substring(2, 12);
		return identifier;
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
		CapElementToEnumMap.put(STATUS, capEnum1);

		List<Item> capEnum2 = new ArrayList<>();
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
		CapElementToEnumMap.put(MSG_TYPE, capEnum2);

		List<Item> capEnum3 = new ArrayList<>();
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
		CapElementToEnumMap.put(SCOPE, capEnum3);	
	}

	private void buildInfoCapEnumMap()
	{
		//Category
		List<Item> capEnum1 = new ArrayList<>();
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
				modifiedValue = value.toString() + " (안보)";
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
		CapElementToEnumMap.put(CATEGORY, capEnum1);

		List<Item> capEnum2 = new ArrayList<>();
		for (Certainty value : Info.Certainty.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Info.Certainty.OBSERVED.toString()))
			{
				modifiedValue = value.toString() + " (이미 발생하였거나 진행 중)";
			}
			else if(value.toString().equals(Info.Certainty.VERY_LIKELY.toString()))
			{
				continue;
			}
			else if(value.toString().equals(Info.Certainty.LIKELY.toString()))
			{
				modifiedValue = value.toString() + " (50%를 초과하는 가능성)";
			}
			else if(value.toString().equals(Info.Certainty.POSSIBLE.toString()))
			{
				modifiedValue = value.toString() + " (50% 이하의 가능성)";
			}
			else if(value.toString().equals(Info.Certainty.UNLIKELY.toString()))
			{
				modifiedValue = value.toString() + " (희박한 가능성)";
			}
			else if(value.toString().equals(Info.Certainty.UNKNOWN_CERTAINTY.toString()))
			{
				modifiedValue = value.toString() + " (미상)";
			}
			capEnum2.add(new Item(value.toString(), modifiedValue));
		}
		CapElementToEnumMap.put(CERTAINTY, capEnum2);

		List<Item> capEnum3 = new ArrayList<>();
		for (ResponseType value : Info.ResponseType.values())
		{			
			String modifiedValue = "";
			modifiedValue = value.toString();
			capEnum3.add(new Item(value.toString(), modifiedValue));
		}
		CapElementToEnumMap.put(RESPONSE_TYPE, capEnum3);

		List<Item> capEnum4 = new ArrayList<>();
		for (Severity value : Info.Severity.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Info.Severity.EXTREME.toString()))
			{
				modifiedValue = value.toString() + " (이례적인 피해)";
			}
			else if(value.toString().equals(Info.Severity.SEVERE.toString()))
			{
				modifiedValue = value.toString() + " (심각한 피해)";
			}
			else if(value.toString().equals(Info.Severity.MODERATE.toString()))
			{
				modifiedValue = value.toString() + " (피해 가능성 존재)";
			}
			else if(value.toString().equals(Info.Severity.MINOR.toString()))
			{
				modifiedValue = value.toString() + " (피해 가능성 낮음)";
			}
			else if(value.toString().equals(Info.Severity.UNKNOWN_SEVERITY.toString()))
			{
				modifiedValue = value.toString() + " (미상)";
			}
			capEnum4.add(new Item(value.toString(), modifiedValue));
		}
		CapElementToEnumMap.put(SEVERITY, capEnum4);

		List<Item> capEnum5 = new ArrayList<>();
		for (Urgency value : Info.Urgency.values())
		{
			String modifiedValue = "";
			if(value.toString().equals(Info.Urgency.IMMEDIATE.toString()))
			{
				modifiedValue = value.toString() + " (즉각적인 대응이 필요함)";
			}
			else if(value.toString().equals(Info.Urgency.EXPECTED.toString()))
			{
				modifiedValue = value.toString() + " (한 시간 이내의 빠른 대응이 필요함)";
			}
			else if(value.toString().equals(Info.Urgency.FUTURE.toString()))
			{
				modifiedValue = value.toString() + " (근시일 내의 대응이 필요함)";
			}
			else if(value.toString().equals(Info.Urgency.PAST.toString()))
			{
				modifiedValue = value.toString() + " (대응이 필요 없음)";
			}
			else if(value.toString().equals(Info.Urgency.UNKNOWN_URGENCY.toString()))
			{
				modifiedValue = value.toString() + " (미상)";
			}
			capEnum5.add(new Item(value.toString(), modifiedValue));
		}
		CapElementToEnumMap.put(URGENCY, capEnum5);

		List<Item> capEnum6 = new ArrayList<>();
		for (KieasList.DisasterEventType value : KieasList.DisasterEventType.values())
		{
			String modifiedValue = "";
			modifiedValue = value.toString() + " (" + value.getKoreanEventCode() + ")";
			capEnum6.add(new Item(value.toString(), modifiedValue));
		}
		CapElementToEnumMap.put(EVENT_CODE, capEnum6);

		List<Item> capEnum7 = new ArrayList<>();
		for (String value : KieasConfiguration.KieasList.LANGUAGE_LIST)
		{
			String modifiedValue = "";
			if(value.toString().equals(KieasConfiguration.KieasList.LANGUAGE_LIST[0]))
			{
				modifiedValue = value.toString() + " (한국어)";
			}
			else if(value.toString().equals(KieasConfiguration.KieasList.LANGUAGE_LIST[1]))
			{
				modifiedValue = value.toString() + " (영어)";
			}
			capEnum7.add(new Item(value.toString(), modifiedValue));
		}
		CapElementToEnumMap.put(LANGUAGE, capEnum7);

		List<Item> capEnum8 = new ArrayList<>();
		Item item1 = new Item("1100000000", "서울특별시");
		Item item2 = new Item("2600000000", "부산광역시");
		Item item3 = new Item("2700000000", "대구광역시");
		Item item4 = new Item("2800000000", "인천광역시");
		Item item5 = new Item("2900000000", "광주광역시");
		Item item6 = new Item("3000000000", "대전광역시");
		Item item7 = new Item("3100000000", "울산광역시");
		Item item8 = new Item("4100000000", "경기도");
		Item item9 = new Item("4200000000", "강원도");
		Item item10 = new Item("4300000000", "충청북도");
		Item item11 = new Item("4400000000", "충청남도");
		Item item12 = new Item("4500000000", "전라북도");
		Item item13 = new Item("4600000000", "전라남도");
		Item item14 = new Item("4700000000", "경상북도");
		Item item15 = new Item("4800000000", "경상남도");
		Item item16 = new Item("5000000000", "제주특별자치도");		
		capEnum8.add(new Item(item1.getKey(), item1.getKey() + " (" + item1.getValue() + ")" ));
		capEnum8.add(new Item(item2.getKey(), item2.getKey() + " (" + item2.getValue() + ")" ));
		capEnum8.add(new Item(item3.getKey(), item3.getKey() + " (" + item3.getValue() + ")" ));
		capEnum8.add(new Item(item4.getKey(), item4.getKey() + " (" + item4.getValue() + ")" ));
		capEnum8.add(new Item(item5.getKey(), item5.getKey() + " (" + item5.getValue() + ")" ));
		capEnum8.add(new Item(item6.getKey(), item6.getKey() + " (" + item6.getValue() + ")" ));
		capEnum8.add(new Item(item7.getKey(), item7.getKey() + " (" + item7.getValue() + ")" ));
		capEnum8.add(new Item(item8.getKey(), item8.getKey() + " (" + item8.getValue() + ")" ));
		capEnum8.add(new Item(item9.getKey(), item9.getKey() + " (" + item9.getValue() + ")" ));
		capEnum8.add(new Item(item10.getKey(), item10.getKey() + " (" + item10.getValue() + ")" ));
		capEnum8.add(new Item(item11.getKey(), item11.getKey() + " (" + item11.getValue() + ")" ));
		capEnum8.add(new Item(item12.getKey(), item12.getKey() + " (" + item12.getValue() + ")" ));
		capEnum8.add(new Item(item13.getKey(), item13.getKey() + " (" + item13.getValue() + ")" ));
		capEnum8.add(new Item(item14.getKey(), item14.getKey() + " (" + item14.getValue() + ")" ));
		capEnum8.add(new Item(item15.getKey(), item15.getKey() + " (" + item15.getValue() + ")" ));
		capEnum8.add(new Item(item16.getKey(), item16.getKey() + " (" + item16.getValue() + ")" ));
		CapElementToEnumMap.put(GEO_CODE, capEnum8);
	}
}
