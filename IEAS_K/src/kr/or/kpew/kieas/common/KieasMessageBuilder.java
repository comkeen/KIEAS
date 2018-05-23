package kr.or.kpew.kieas.common;

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
import com.google.publicalerts.cap.Circle;
import com.google.publicalerts.cap.Group;
import com.google.publicalerts.cap.Group.Builder;
import com.google.publicalerts.cap.Info;
import com.google.publicalerts.cap.Info.Category;
import com.google.publicalerts.cap.Info.Certainty;
import com.google.publicalerts.cap.Info.ResponseType;
import com.google.publicalerts.cap.Info.Severity;
import com.google.publicalerts.cap.Info.Urgency;
import com.google.publicalerts.cap.NotCapException;
import com.google.publicalerts.cap.Point;
import com.google.publicalerts.cap.Polygon;
import com.google.publicalerts.cap.Resource;
import com.google.publicalerts.cap.ValuePair;

import kr.or.kpew.kieas.common.AlertValidator.AckCode;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasConstant;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasList;

/**
 * CAP 형식의 메시지를 생성하고 다루는 클래스. Google CAP Library를 활용하여 CAP 메시지를 다룬다.
 * 
 * @author byun-ai
 */
public class KieasMessageBuilder implements IKieasMessageBuilder
{
	public enum AlertElementNames
	{
		Identifier,
		Sender,
		Sent,
		Status,
		MsgType,
		Source,
		Scope,
		Public,
		Restricted,
		Private,
		Restriction,
		Addresses,
		Code,
		Note,
		References,
		Info
	}
	
	public enum InfoElementNames
	{
		Language,
		Category,
		Event,
		ResponseType,
		Urgency,
		Severity,
		Certainty,
		Audience,
		EventCode,
		Effective,
		Onset,
		Expires,
		SenderName,
		Headline,
		Description,
		Instruction,
		Web,
		Contact,
		Parameter
	}
	
	public enum ResourceElementNames
	{
		ResourceDesc,
		MimeType,
		Size,
		Uri,
		DerefUri,
		Digest
	}
	
	public enum AreaElementNames
	{
		Area,
		AreaDesc,
		Polygon,
		Circle,
		GeoCode,
		Altitude,
		Ceiling
	}
	public enum AlertSystemType {
		CivelDefense("민방위 경보시스템"),
		DmbAlertSystem("DMB 재난경보방송"),
		CbsAlertSystem("CBS 재난문자방송"),
		LocalBroadcasting("마을방송시스템");
		
		private String description;
		private AlertSystemType(String description) {
			this.description = description;
		}
		public String getDescription() {
			return description;
		}
	}

		
//	public enum Category {
//		Geo,
//		Met,
//		Safety,
//		Security,
//		Rescue,
//		Fire,
//		Health,
//		Env,
//		Transport,
//		Infra,
//		CBRNE,
//		Other,
//	}

	private Map<Enum<?>, List<Item>> capElementToEnumMap;
	
	private static final int DEFAULT_INFO_SIZE = 0;
	private static final String EMPTY = "";

	private CapXmlBuilder capXmlBuilder;
	private CapXmlParser capXmlParser;

	private Alert mAlert;
	
	public KieasMessageBuilder()
	{
		init();
	}

	public void init()
	{
		this.capXmlBuilder = new CapXmlBuilder();
		this.capXmlParser = new CapXmlParser(true);

		this.mAlert = buildDefaultAlert();
	}

	/**
	 * CAP 메시지에서 최소한의 요소만 작성되어있는 기본적인 메시지 생성
	 * 
	 * @return xml 형태
	 */
	public String buildDefaultMessage()
	{
		this.mAlert = buildDefaultAlert();

		for (int infoIndex = 0; infoIndex < DEFAULT_INFO_SIZE; infoIndex++)
		{
			mAlert = Alert.newBuilder(mAlert).addInfo(buildDefaultInfo()).build();
		}

		return capXmlBuilder.toXml(mAlert);
	}

	private Alert buildDefaultAlert()
	{
		Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS).setIdentifier("Identifier")
				.setSender("Sender").setSent(CapUtil.formatCapDate(getDateCalendar())).setStatus(Alert.Status.ACTUAL)
				.setMsgType(Alert.MsgType.ALERT).setScope(Alert.Scope.RESTRICTED).addCode(KieasConstant.CODE)
				.buildPartial();

		return alert;
	}

	private Info buildDefaultInfo() {
		Info info = Info.newBuilder().addCategory(Info.Category.MET).setLanguage("Language").setEvent("Event")
				.setUrgency(Info.Urgency.UNKNOWN_URGENCY).setSeverity(Info.Severity.UNKNOWN_SEVERITY)
				.setCertainty(Info.Certainty.UNKNOWN_CERTAINTY).buildPartial();

		return info;
	}

	private Resource buildDefaultResource() {
		Resource resource = Resource.newBuilder().setResourceDesc("Resource Description").setMimeType("Mime Type")
				.buildPartial();

		return resource;
	}

	private Area buildDefaultArea() {
		Area area = Area.newBuilder().setAreaDesc("Area Description").buildPartial();

		return area;
	}

	private Polygon buildDefaultPolygon() {
		Polygon polygon = Polygon.newBuilder().addPoint(Point.newBuilder().setLatitude(0).setLongitude(0).build())
				.buildPartial();

		return polygon;
	}

	private Circle buildDefaultCircle() {
		Circle circle = Circle.newBuilder().setPoint(Point.newBuilder().setLatitude(0).setLongitude(0).build())
				.setRadius(0).buildPartial();

		return circle;
	}

	private void incrementInfoByInfoIndex(int infoIndex) {
		int infoCount = mAlert.getInfoCount();

		for (int i = infoIndex - infoCount; i >= 0; i--) {
			mAlert = Alert.newBuilder(mAlert).addInfo(buildDefaultInfo()).build();
		}
	}

	private void incrementParameterByParameterIndex(int infoIndex, int paramterIndex) {
		Info info = mAlert.getInfo(infoIndex);
		int parameterCount = info.getParameterCount();

		for (int i = paramterIndex - parameterCount; i >= 0; i--) {
			info = Info.newBuilder(info).addParameter(ValuePair.newBuilder().setValueName("").setValue(""))
					.buildPartial();
		}
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	private void incrementResourceByResourceIndex(int infoIndex, int resourceIndex) {
		Info info = mAlert.getInfo(infoIndex);
		int resourceCount = info.getResourceCount();

		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		for (int i = resourceIndex - resourceCount; i >= 0; i--) {
			info = Info.newBuilder(info).addResource(buildDefaultResource()).buildPartial();
		}
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	private void incrementAreaByAreaIndex(int infoIndex, int areaIndex) {
		Info info = mAlert.getInfo(infoIndex);
		int areaCount = info.getAreaCount();

		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		for (int i = areaIndex - areaCount; i >= 0; i--) {
			info = Info.newBuilder(info).addArea(buildDefaultArea()).buildPartial();
		}
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	private void incrementPolygonByPolygonIndex(int infoIndex, int areaIndex, int polygonIndex) {
		int areaCount = mAlert.getInfo(infoIndex).getAreaCount();
		int polygonCount = mAlert.getInfo(infoIndex).getArea(areaIndex).getPolygonCount();

		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= areaCount) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}

		Area area = mAlert.getInfo(infoIndex).getArea(areaIndex);
		for (int i = polygonIndex - polygonCount; i >= 0; i--) {
			area = Area.newBuilder(area).addPolygon(buildDefaultPolygon()).buildPartial();
		}
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	private void incrementCircleByCircleIndex(int infoIndex, int areaIndex, int circleIndex) {
		int areaCount = mAlert.getInfo(infoIndex).getAreaCount();
		int circleCount = mAlert.getInfo(infoIndex).getArea(areaIndex).getCircleCount();

		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= areaCount) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}

		Area area = mAlert.getInfo(infoIndex).getArea(areaIndex);
		for (int i = circleIndex - circleCount; i >= 0; i--) {
			area = Area.newBuilder(area).addCircle(buildDefaultCircle()).buildPartial();
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	private Status convertToStatus(String text) {
		text = text.toUpperCase();
		for (Status status : Alert.Status.values()) {
			if (text.equals(status.toString())) {
				if (mAlert != null) {
					mAlert = Alert.newBuilder(mAlert).setStatus(status).build();
					return status;
				} else {
					return status;
				}
			}
		}
		return null;
	}

	private MsgType convertToMsgType(String text) {
		for (MsgType msgType : Alert.MsgType.values()) {
			if (text.toUpperCase().equals(msgType.toString())) {
				if (mAlert != null) {
					mAlert = Alert.newBuilder(mAlert).setMsgType(msgType).build();
					return msgType;
				} else {
					return msgType;
				}
			}
		}
		return null;
	}

	private Scope convertToScope(String text) {
		for (Scope scope : Alert.Scope.values()) {
			if (text.toUpperCase().equals(scope.toString())) {
				if (mAlert != null) {
					mAlert = Alert.newBuilder(mAlert).setScope(scope).build();
					return scope;
				} else {
					return scope;
				}
			}
		}
		return null;
	}

	private Group convertToAddresses(String address) {
		return Group.newBuilder().addValue(address).build();
	}

	private Category convertToCategory(String text) {
		for (Category category : Category.values()) {
			if (text.toUpperCase().equals(category.toString())) {
				return category;
			}
		}
		return null;
	}

	private ResponseType convertToResponseType(String text) {
		for (ResponseType responseType : Info.ResponseType.values()) {
			if (text.toUpperCase().equals(responseType.toString())) {
				return responseType;
			}
		}
		return null;
	}

	private Urgency convertToUrgency(String text) {
		for (Urgency urgency : Info.Urgency.values()) {
			if (text.toUpperCase().equals(urgency.toString())) {
				return urgency;
			} else if (text.equals("Unknown")) {
				return Info.Urgency.UNKNOWN_URGENCY;
			}
		}
		return null;
	}

	private Severity convertToSeverity(String text) {
		for (Severity severity : Info.Severity.values()) {
			if (text.toUpperCase().equals(severity.toString())) {
				return severity;
			} else if (text.equals("Unknown")) {
				return Info.Severity.UNKNOWN_SEVERITY;
			}
		}
		return null;
	}

	private Certainty convertToCertainty(String text) {
		for (Certainty certainty : Info.Certainty.values()) {
			if (text.toUpperCase().equals(certainty.toString())) {
				return certainty;
			} else if (text.equals("Unknown")) {
				return Info.Certainty.UNKNOWN_CERTAINTY;
			}
		}
		return null;
	}

	private ValuePair convertToEventCode(String text) {
		return ValuePair.newBuilder().setValueName(KieasConstant.EVENT_CODE_VALUE_NAME).setValue(text).build();
	}

	private ValuePair convertToGeoCode(String text) {
		return ValuePair.newBuilder().setValueName(KieasConstant.GEO_CODE_VALUE_NAME).setValue(text).build();
	}

	/**
	 * 작성된 CAP 메시지 빌드
	 */
	@Override
	public String build()
	{
		// Alert build
		Alert alert = Alert.newBuilder(mAlert).setXmlns(CapValidator.CAP_LATEST_XMLNS).clearInfo().buildPartial();

		// Info build
		for (int infoIndex = 0; infoIndex < mAlert.getInfoCount(); infoIndex++) {
			Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).clearResource().clearArea().buildPartial();

			// Resource build
			for (int resourceIndex = 0; resourceIndex < info.getResourceCount(); resourceIndex++) {
				Resource resource = info.getResource(resourceIndex);

				info = Info.newBuilder(info).addResource(resource).buildPartial();
			}
			// Area build
			for (int areaIndex = 0; areaIndex < info.getAreaCount(); areaIndex++) {
				Area area = info.getArea(areaIndex);

				info = Info.newBuilder(info).addArea(area).buildPartial();
			}

			alert = Alert.newBuilder(alert).addInfo(info).build();
		}

		this.mAlert = alert;
		
		return capXmlBuilder.toXml(alert);
	}

	/**
	 * CAP 메시지의 xml 리턴
	 * 
	 * @return xml 형태
	 */
	@Override
	public String getMessage()
	{
		try
		{
			return capXmlBuilder.toXml(mAlert);
		} 
		catch (NotCapException e)
		{
			System.out.println("There is no CAP message");
			return EMPTY;
		}
		
	}

	/**
	 * xml 형태의 CAP 메시지를 KieasMessageBuilder로 적용하여 수정 가능한 객체로 적용.
	 */
	@Override
	public void parse(String message)
	{		
		try
		{
			mAlert = capXmlParser.parseFrom(message);
		}
		catch (NotCapException | SAXParseException | CapException e)
		{
			System.out.println("MessageBuilder: Invalid Parsing from Cap message");
			e.printStackTrace();
		}
	}
	
	public static IKieasMessageBuilder createBuilder(String message) {
		KieasMessageBuilder builder = new KieasMessageBuilder();
		builder.parse(message);
		return builder;		
	}
	


	/**
	 * 통합경보시스템에서 사용하는 CAP 메시지 Identifier 생성.
	 * 
	 * @param 통합경보시스템
	 *            콤포넌트의 ID
	 */
	@Override
	public String generateKieasMessageIdentifier(String id) {
		String idNum = Double.toString(Math.random());

		String identifier = id + "@" + idNum.substring(2, 12);
		return identifier;
	}

	/**
	 * 현재 시간을 CAP 메시지에서 사용하는 시간표현방식대로 표현하여 리턴.
	 * 
	 * @return 현재 시간
	 */
	@Override
	public String getDate() {
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone(KieasConstant.DEFAULT_TIME_ZONE));
		cal.setTime(new Date());
		return CapUtil.formatCapDate(cal);
	}

	/**
	 * 시간을 년월일시분초 형식으로 변환.
	 * 
	 * @return 년월일시분초
	 */
	@Override
	public String convertDateToYmdhms(String date) {
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone(KieasConstant.DEFAULT_TIME_ZONE));
		cal.setTime(CapUtil.toJavaDate(date));

		StringBuffer sb = new StringBuffer();
		sb.append(cal.get(Calendar.YEAR)).append("년").append(cal.get(Calendar.MONTH) + 1).append("월")
				.append(cal.get(Calendar.DATE)).append("일").append(" ").append(cal.get(Calendar.HOUR_OF_DAY))
				.append("시").append(cal.get(Calendar.MINUTE)).append("분").append(cal.get(Calendar.SECOND)).append("초");

		return sb.toString();
	}

	private GregorianCalendar getDateCalendar() {
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone(KieasConstant.DEFAULT_TIME_ZONE));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		return cal;
	}

	// private String dateToString(Date date)
	// {
	// GregorianCalendar cal = new
	// GregorianCalendar(SimpleTimeZone.getTimeZone(KIEAS_Constant.DEFAULT_TIME_ZONE));
	// cal.setTime(date);
	// return CapUtil.formatCapDate(cal);
	// }

	/**
	 * CAP 메시지의 Info 요소 갯수 리턴.
	 * 
	 * @return Info 갯수
	 */
	@Override
	public int getInfoCount() {
		return mAlert.getInfoCount();
	}

	/**
	 * CAP 메시지의 Resource 요소 갯수 리턴.
	 * 
	 * @return Resource 갯수
	 */
	@Override
	public int getResourceCount(int infoIndex) {
		return mAlert.getInfo(infoIndex).getResourceCount();
	}

	/**
	 * CAP 메시지의 Area 요소 갯수 리턴.
	 * 
	 * @return Area 갯수
	 */
	@Override
	public int getAreaCount(int infoIndex) {
		return mAlert.getInfo(infoIndex).getAreaCount();
	}


	/**
	 * @return Alert 요소의 Identifier 값 리턴.
	 */
	@Override
	public String getIdentifier() {
		return mAlert.getIdentifier();
	}

	/**
	 * @return Alert 요소의 Sender 값 리턴.
	 */
	@Override
	public String getSender() {
		return mAlert.getSender();
	}

	/**
	 * @return Alert 요소의 Sent 값 리턴.
	 */
	@Override
	public String getSent() {
		return mAlert.getSent();
	}

	/**
	 * @return Alert 요소의 Status 값 리턴.
	 */
	@Override
	public Status getStatus() {
		return mAlert.getStatus();
	}
//	@Override
//	public String getStatus() {
//		return mAlert.getStatus().toString();
//	}

	/**
	 * @return Alert 요소의 MsgType 값 리턴.
	 */
	@Override
	public MsgType getMsgType() {
		return mAlert.getMsgType();
	}

	/**
	 * @return Alert 요소의 Source 값 리턴.
	 */
	@Override
	public String getSource() {
		return mAlert.getSource().toString();
	}

	/**
	 * @return Alert 요소의 Scope 값 리턴.
	 */
	@Override
	public Scope getScope() {
		return mAlert.getScope();
	}

	/**
	 * @return Alert 요소의 Restriction 값 리턴.
	 */
	@Override
	public String getRestriction() {
		return mAlert.getRestriction();
	}

	/**
	 * @return Alert 요소의 Addresses 값 리턴.
	 */
	@Override
	public List<String> getAddresses() {
		if (mAlert.hasAddresses() && mAlert.getAddresses().getValueCount() > 0) {
			return mAlert.getAddresses().getValueList();
		}
		return null;
	}

	/**
	 * @return Alert 요소의 Code 값 리턴.
	 */
	@Override
	public String getCode() {
		return mAlert.getCode(0);
	}

	/**
	 * @return Alert 요소의 Note 값 리턴.
	 */
	@Override
	public String getNote() {
		return mAlert.getNote();
	}

	/**
	 * @return Alert 요소의 References 값 리턴.
	 */
	@Override
	public String getReferences() {
		if (mAlert.hasReferences() && mAlert.getReferences().getValueCount() != 0) {
			return mAlert.getReferences().getValue(0);
		}
		return "";
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Language 값 리턴.
	 */
	@Override
	public String getLanguage(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasLanguage()) {
			return mAlert.getInfo(index).getLanguage().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Category 값 리턴.
	 */
	@Override
	public String getCategory(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).getCategoryCount() != 0) {
			return mAlert.getInfo(index).getCategory(0).toString();
		}
		return EMPTY;
	}

	// public String getCategory(int infoIndex, int categoryIndex){}
	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 ResponseType 값 리턴.
	 */
	@Override
	public String getResponseType(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).getResponseTypeCount() != 0) {
			return mAlert.getInfo(index).getResponseType(0).toString();
		}
		return EMPTY;
	}

	// public String getResponseType(int infoIndex, int responseTypeIndex){}
	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Event 값 리턴.
	 */
	@Override
	public String getEvent(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasEvent()) {
			return mAlert.getInfo(index).getEvent();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Urgency 값 리턴.
	 */
	@Override
	public String getUrgency(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasUrgency()) {
			return mAlert.getInfo(index).getUrgency().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Severity 값 리턴.
	 */
	@Override
	public String getSeverity(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasSeverity()) {
			return mAlert.getInfo(index).getSeverity().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Certainty 값 리턴.
	 */
	@Override
	public String getCertainty(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasCertainty()) {
			return mAlert.getInfo(index).getCertainty().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Audience 값 리턴.
	 */
	@Override
	public String getAudience(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasAudience()) {
			return mAlert.getInfo(index).getAudience();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 EventCode 값 리턴.
	 */
	@Override
	public String getEventCode(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).getEventCodeCount() != 0) {
			return mAlert.getInfo(index).getEventCodeList().get(0).getValue().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Effective 값 리턴.
	 */
	@Override
	public String getEffective(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasEffective()) {
			return mAlert.getInfo(index).getEffective().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Expires 값 리턴.
	 */
	@Override
	public String getExpires(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasExpires()) {
			return mAlert.getInfo(index).getExpires();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 SenderName 값 리턴.
	 */
	@Override
	public String getSenderName(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasSenderName()) {
			return mAlert.getInfo(index).getSenderName().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Headline 값 리턴.
	 */
	@Override
	public String getHeadline(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasHeadline()) {
			return mAlert.getInfo(index).getHeadline().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Description 값 리턴.
	 */
	@Override
	public String getDescription(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasDescription()) {
			return mAlert.getInfo(index).getDescription().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Instruction 값 리턴.
	 */
	@Override
	public String getInstruction(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasInstruction()) {
			return mAlert.getInfo(index).getInstruction();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Web 값 리턴.
	 */
	@Override
	public String getWeb(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasWeb()) {
			return mAlert.getInfo(index).getWeb().toString();
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index
	 * @return Info 요소의 Contact 값 리턴.
	 */
	@Override
	public String getContact(int index) {
		if (mAlert.getInfoCount() > index && mAlert.getInfo(index).hasContact()) {
			return mAlert.getInfo(index).getContact().toString();
		}
		return EMPTY;
	}
	
	@Override
	public String getParameter(int infoIndex, int parameterIndex)
	{
		if (mAlert.getInfoCount() > infoIndex && mAlert.getInfo(infoIndex).getParameter(parameterIndex) != null)
		{
			String valueName = mAlert.getInfo(infoIndex).getParameter(parameterIndex).getValueName();
			String value = mAlert.getInfo(infoIndex).getParameter(parameterIndex).getValue();
			return valueName + "," + value;
		}
		return EMPTY;
	}

	/**
	 * @param 목표가되는 Info 요소의 index, 목표가되는 Resource 요소의 index
	 * @return Resource 요소의 ResourceDesc 값 리턴.
	 */
	@Override
	public String getResourceDesc(int infoIndex, int resourceIndex) {
		return mAlert.getInfo(infoIndex).getResource(resourceIndex).getResourceDesc();
	}

	/**
	 * @param 목표가되는 Info 요소의 index, 목표가되는 Resource 요소의 index
	 * @return Resource 요소의 MimeType 값 리턴.
	 */
	@Override
	public String getMimeType(int infoIndex, int resourceIndex) {
		return mAlert.getInfo(infoIndex).getResource(resourceIndex).getMimeType();
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Resource 요소의 index
	 * @return Resource 요소의 Size 값 리턴.
	 */
	@Override
	public String getSize(int infoIndex, int resourceIndex) {
		return Long.toString(mAlert.getInfo(infoIndex).getResource(resourceIndex).getSize());
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Resource 요소의 index
	 * @return Resource 요소의 Uri 값 리턴.
	 */
	@Override
	public String getUri(int infoIndex, int resourceIndex) {
		return mAlert.getInfo(infoIndex).getResource(resourceIndex).getUri();
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Resource 요소의 index
	 * @return Resource 요소의 DerefUri 값 리턴.
	 */
	@Override
	public String getDerefUri(int infoIndex, int resourceIndex) {
		return mAlert.getInfo(infoIndex).getResource(resourceIndex).getDerefUri();
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Resource 요소의 index
	 * @return Resource 요소의 Digest 값 리턴.
	 */
	@Override
	public String getDigest(int infoIndex, int resourceIndex) {
		return mAlert.getInfo(infoIndex).getResource(resourceIndex).getDigest();
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Area 요소의 index
	 * @return Area 요소의 AreaDesc 값 리턴.
	 */
	@Override
	public String getAreaDesc(int infoIndex, int areaIndex) {
		return mAlert.getInfo(infoIndex).getArea(areaIndex).getAreaDesc();
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Area 요소의 index, 목표가 되는 Polygon 요소의 index
	 * @return Area 요소의 Polygon 값 리턴.
	 */
	@Override
	public String getPolygon(int infoIndex, int areaIndex, int polygonIndex) {
		return mAlert.getInfo(infoIndex).getArea(areaIndex).getPolygon(polygonIndex).toString();
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Area 요소의 index, 목표가 되는 Circle 요소의 index
	 * @return Area 요소의 Circle 값 리턴.
	 */
	@Override
	public String getCircle(int infoIndex, int areaIndex, int circleIndex) {
		return mAlert.getInfo(infoIndex).getArea(areaIndex).getCircle(circleIndex).toString();
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Area 요소의 index
	 * @return Area 요소의 GeoCode 값 리턴.
	 */
	@Override
	public String getGeoCode(int infoIndex, int areaIndex) {
		if(mAlert.getInfo(infoIndex).getArea(areaIndex).getGeocodeCount() <= 0)
			return "";
		if (mAlert.getInfo(infoIndex).getArea(areaIndex).getGeocode(0) != null) {
			return mAlert.getInfo(infoIndex).getArea(areaIndex).getGeocode(0).getValue();
		} else {
			return "";
		}
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Area 요소의 index
	 * @return Area 요소의 Altitude 값 리턴.
	 */
	@Override
	public String getAltitude(int infoIndex, int areaIndex) {
		if(mAlert.getInfo(infoIndex).getArea(areaIndex).hasAltitude())
		{
			double altitud = mAlert.getInfo(infoIndex).getArea(areaIndex).getAltitude();
			return Double.toString(altitud);
		}
		return "";
	}

	/**
	 * @param 목표가되는
	 *            Info 요소의 index, 목표가되는 Area 요소의 index
	 * @return Area 요소의 Ceiling 값 리턴.
	 */
	@Override
	public String getCeiling(int infoIndex, int areaIndex) {
		if(mAlert.getInfo(infoIndex).getArea(areaIndex).hasCeiling())
		{
			double ceiling = mAlert.getInfo(infoIndex).getArea(areaIndex).getCeiling();
			return Double.toString(ceiling);
		}
		return "";
	}

	/**
	 * @param Identifier
	 *            값
	 */
	@Override
	public void setIdentifier(String text) {
		mAlert = Alert.newBuilder(mAlert).setIdentifier(text).buildPartial();
	}

	/**
	 * @param Sender
	 *            값
	 */
	@Override
	public void setSender(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setSender(text).buildPartial();
	}

	/**
	 * @param Sent
	 *            값
	 */
	@Override
	public void setSent(String text)
	{
		mAlert = Alert.newBuilder(mAlert).setSent(text).buildPartial();
	}

	/**
	 * Alert 요소의 Sent 값에 현재 시간을 작성.
	 */
	@Override
	public void setSent() {
		mAlert = Alert.newBuilder(mAlert).setSent(CapUtil.formatCapDate(getDateCalendar())).build();
	}

	/**
	 * @param Status
	 *            값
	 */
	@Override
	public void setStatus(String status) {
		mAlert = Alert.newBuilder(mAlert).setStatus(this.convertToStatus(status)).buildPartial();
	}
	
	/**
	 * @param Status
	 *            값
	 */
	@Override
	public void setStatus(Status status) {
		mAlert = Alert.newBuilder(mAlert).setStatus(status).buildPartial();
	}

	/**
	 * @param MsgType
	 *            값
	 */
	@Override
	public void setMsgType(String text) {
		mAlert = Alert.newBuilder(mAlert).setMsgType(this.convertToMsgType(text)).buildPartial();
	}
	
	/**
	 * @param MsgType
	 *            값
	 */
	@Override
	public void setMsgType(MsgType type) {
		mAlert = Alert.newBuilder(mAlert).setMsgType(type).buildPartial();
	}

	/**
	 * @param MsgType
	 *            값
	 */
	@Override
	public void setSource(String text) {
		mAlert = Alert.newBuilder(mAlert).setSource(text).buildPartial();
	}
	
	/**
	 * @param Source
	 *            값
	 */
	@Override
	public void setScope(Scope scope) {
		mAlert = Alert.newBuilder(mAlert).setScope(scope).buildPartial();
	}

	/**
	 * @param Scope
	 *            값
	 */
	@Override
	public void setScope(String text) {
		mAlert = Alert.newBuilder(mAlert).setScope(this.convertToScope(text)).buildPartial();
	}

	/**
	 * @param Restriciton
	 *            값
	 */
	@Override
	public void setRestriction(String text) {
		if(mAlert.getScope() != Scope.PUBLIC && text.length() != 0)
		{
			mAlert = Alert.newBuilder(mAlert).setRestriction(text).buildPartial();
			return;
		}
		mAlert = Alert.newBuilder(mAlert).clearRestriction().buildPartial();
	}

	/**
	 * @param Addresses
	 *            값
	 */
	@Override
	public void setAddresses(String text) {
		mAlert = Alert.newBuilder(mAlert).setAddresses(this.convertToAddresses(text)).buildPartial();
	}

	/**
	 * @param Code
	 *            값
	 */
	@Override
	public void setCode(String text) {
		mAlert = Alert.newBuilder(mAlert).setCode(0, text).buildPartial();
	}

	/**
	 * @param Note
	 *            값
	 */
	@Override
	public void setNote(String text) {
		mAlert = Alert.newBuilder(mAlert).setNote(text).buildPartial();
	}
	
	@Override
	public void setReferences(String text)
	{
		Builder builder = mAlert.getReferences().toBuilder();
		builder.clear().addValue(text);
		mAlert = Alert.newBuilder(mAlert).setReferences(builder).buildPartial();
	}
	
	@Override
	public Builder convertToReferences(String text)
	{
		Builder builder = mAlert.getReferences().toBuilder();
		builder.clear().addValue(text);
		return builder;
	}

	/**
	 * @param 목표 Info 요소 index, Language 값
	 */
	@Override
	public void setLanguage(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setLanguage(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Category 값
	 */
	@Override
	public void setCategory(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setCategory(0, this.convertToCategory(text))
				.buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}
	@Override
	public void setCategory(int infoIndex, Category category) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setCategory(0, category)
				.buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, ResponseType 값
	 */
	@Override
	public void setResponseType(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResponseType(0, this.convertToResponseType(text))
				.buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Event 값
	 */
	@Override
	public void setEvent(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setEvent(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Urgency 값
	 */
	@Override
	public void setUrgency(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setUrgency(this.convertToUrgency(text)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Severity 값
	 */
	@Override
	public void setSeverity(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setSeverity(this.convertToSeverity(text)).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Certatinty 값
	 */
	@Override
	public void setCertainty(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setCertainty(this.convertToCertainty(text))
				.buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Audience 값
	 */
	@Override
	public void setAudience(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setEvent(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, EventCode 값
	 */
	@Override
	public void setEventCode(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).clearEventCode().addEventCode(convertToEventCode(text))
				.buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Effective 값
	 */
	@Override
	public void setEffective(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setEffective(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Onset 값
	 */
	@Override
	public void setOnset(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setOnset(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, Expires 값
	 */
	@Override
	public void setExpires(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setExpires(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표
	 *            Info 요소 index, SenderName 값
	 */
	@Override
	public void setSenderName(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setSenderName(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표
	 *            Info 요소 index, Headline 값
	 */
	@Override
	public void setHeadline(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setHeadline(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표
	 *            Info 요소 index, Description 값
	 */
	@Override
	public void setDescription(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setDescription(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표
	 *            Info 요소 index, Instruction 값
	 */
	@Override
	public void setInstruction(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setInstruction(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표
	 *            Info 요소 index, Web 값
	 */
	@Override
	public void setWeb(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setWeb(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표
	 *            Info 요소 index, Contact 값
	 */
	@Override
	public void setContact(int infoIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setContact(text).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Parameter 요소 index, Parameter의 ValueName 값, Parameter의 Value 값
	 */
	@Override
	public void setParameter(int infoIndex, int parameterIndex, String valueName, String value) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		if (parameterIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getParameterCount()) {
			incrementParameterByParameterIndex(infoIndex, parameterIndex);
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex))
				.setParameter(parameterIndex, ValuePair.newBuilder().setValueName(valueName).setValue(value))
				.buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Resource 요소 index, ResourceDesc 값
	 */
	@Override
	public void setResourceDesc(int infoIndex, int resourceIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		if (resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount()) {
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex))
				.setResourceDesc(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Resource 요소 index, MimeType 값
	 */
	@Override
	public void setMimeType(int infoIndex, int resourceIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		if (resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount()) {
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setMimeType(text)
				.buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Resource 요소 index, Size 값
	 */
	@Override
	public void setSize(int infoIndex, int resourceIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		if (resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount()) {
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex))
				.setSize(Long.parseLong(text)).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Resource 요소 index, Uri 값
	 */
	@Override
	public void setUri(int infoIndex, int resourceIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount()) {
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}

		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setUri(text)
				.buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Resource 요소 index, DerefUri 값
	 */
	@Override
	public void setDerefUri(int infoIndex, int resourceIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		if (resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount()) {
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setDerefUri(text)
				.buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Resource 요소 index, Digest 값
	 */
	@Override
	public void setDigest(int infoIndex, int resourceIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}

		if (resourceIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getResourceCount()) {
			incrementResourceByResourceIndex(infoIndex, resourceIndex);
		}
		Resource resource = Resource.newBuilder(mAlert.getInfo(infoIndex).getResource(resourceIndex)).setDigest(text)
				.buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setResource(resourceIndex, resource).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Area 요소 index, AreaDesc 값
	 */
	@Override
	public void setAreaDesc(int infoIndex, int areaIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= mAlert.getInfo(infoIndex).getAreaCount()) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}

		Info info = mAlert.getInfo(infoIndex);
		Area area = info.getArea(areaIndex);

		area = Area.newBuilder(area).setAreaDesc(text).buildPartial();
		info = Info.newBuilder(info).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info요소 index, 목표 Area요소 index, Polygon 값
	 */
	@Override
	public void setPolygon(int infoIndex, int areaIndex, int polygonIndex, Point[] points) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= mAlert.getInfo(infoIndex).getAreaCount()) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}
		if (polygonIndex >= mAlert.getInfo(infoIndex).getArea(areaIndex).getPolygonCount()) {
			incrementPolygonByPolygonIndex(infoIndex, areaIndex, polygonIndex);
		}

		Polygon polygon = Polygon.newBuilder().build();
		for (int i = 0; i < points.length; i++) {
			polygon = Polygon.newBuilder(polygon).addPoint(Point.newBuilder().setLatitude(points[i].getLatitude())
					.setLongitude(points[i].getLongitude()).build()).buildPartial();
		}

		Info info = mAlert.getInfo(infoIndex);
		Area area = info.getArea(infoIndex);

		area = Area.newBuilder(area).setPolygon(polygonIndex, polygon).buildPartial();
		info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Area 요소 index, Circle 값
	 */
	@Override
	public void setCircle(int infoIndex, int areaIndex, int circleIndex, double latitude, double longitude, double radius) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= mAlert.getInfo(infoIndex).getAreaCount()) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}
		if (circleIndex >= mAlert.getInfo(infoIndex).getArea(areaIndex).getCircleCount()) {
			incrementCircleByCircleIndex(infoIndex, areaIndex, circleIndex);
		}

		Info info = mAlert.getInfo(infoIndex);
		Area area = info.getArea(areaIndex);
		area = Area.newBuilder(area)
				.setCircle(circleIndex, Circle.newBuilder()
						.setPoint(Point.newBuilder().setLatitude(latitude).setLongitude(longitude)).setRadius(radius))
				.buildPartial();
		info = Info.newBuilder(info).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info 요소 index, 목표 Area 요소 index, GeoCode 값
	 */
	@Override
	public void setGeoCode(int infoIndex, int areaIndex, String text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getAreaCount()) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}

		Area area = null;
		if (mAlert.getInfo(infoIndex).getArea(areaIndex).getGeocodeCount() == 0) {
			area = Area.newBuilder(mAlert.getInfo(infoIndex).getArea(areaIndex)).addGeocode(convertToGeoCode(text))
					.buildPartial();
		} else {
			area = Area.newBuilder(mAlert.getInfo(infoIndex).getArea(areaIndex)).setGeocode(0, convertToGeoCode(text))
					.buildPartial();
		}

		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info요소 index, 목표 Area요소 index, Altitude 값
	 */
	@Override
	public void setAltitude(int infoIndex, int areaIndex, double text) {
		if (infoIndex >= mAlert.getInfoCount()) {
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getAreaCount()) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}

		Area area = Area.newBuilder(mAlert.getInfo(infoIndex).getArea(areaIndex)).setAltitude(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	/**
	 * @param 목표 Info요소 index, 목표 Area요소 index, Ceiling 값
	 */
	@Override
	public void setCeiling(int infoIndex, int areaIndex, double text)
	{
		if (infoIndex >= mAlert.getInfoCount())
		{
			incrementInfoByInfoIndex(infoIndex);
		}
		if (areaIndex >= Info.newBuilder(mAlert.getInfo(infoIndex)).getAreaCount()) {
			incrementAreaByAreaIndex(infoIndex, areaIndex);
		}

		Area area = Area.newBuilder(mAlert.getInfo(infoIndex).getArea(areaIndex)).setCeiling(text).buildPartial();
		Info info = Info.newBuilder(mAlert.getInfo(infoIndex)).setArea(areaIndex, area).buildPartial();
		mAlert = Alert.newBuilder(mAlert).setInfo(infoIndex, info).buildPartial();
	}

	@Override
	public String createAckMessage(String message, String identifier, String sender, AckCode ackCode)
	{
		Alert parsedAlert = null;
		try
		{
			parsedAlert = capXmlParser.parseFrom(message);
		}
		catch (NotCapException | SAXParseException | CapException e)
		{
			e.printStackTrace();
		}
		
		Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
				.setIdentifier(identifier)
				.setSender(sender)
				.setSent(CapUtil.formatCapDate(getDateCalendar()))
				.setStatus(Status.SYSTEM)
				.setMsgType(MsgType.ACK)
				.setScope(Scope.PRIVATE)
				.setAddresses(this.convertToAddresses(sender))
				.setReferences(this.convertToReferences(parsedAlert.getSender()+","+parsedAlert.getIdentifier()+","+parsedAlert.getSent()))
				.addCode(KieasConstant.CODE)
				.setNote(ackCode.getCode()+","+ackCode.getDescription())
				.build();
		
		return capXmlBuilder.toXml(alert);
	}

	@Override
	public String[] parseReferences(String references)
	{
		String[] tokens = references.split(",");
		return tokens;
	}
	
	@Override
	public void setProfile(Profile profile)
	{
		this.setSender(profile.getSender());
	}

	public Map<Enum<?>, List<Item>> getCapEnumMap()
	{		
		if(capElementToEnumMap == null)
		{
			this.capElementToEnumMap = new HashMap<>();
			capElementToEnumMap = buildAlertCapEnumMap(capElementToEnumMap);
			capElementToEnumMap = buildInfoCapEnumMap(capElementToEnumMap);
		}		
		return capElementToEnumMap;
	}
	
	private Map<Enum<?>, List<Item>> buildAlertCapEnumMap(Map<Enum<?>, List<Item>> capElementToEnumMap)
	{
		List<Item> capEnum1 = new ArrayList<>();
		for (Status value : Alert.Status.values()) {
			String modifiedValue = "";
			if (value.toString().equals(Alert.Status.ACTUAL.toString())) {
				modifiedValue = value.toString() + " (실제)";
			} else if (value.toString().equals(Alert.Status.EXERCISE.toString())) {
				modifiedValue = value.toString() + " (훈련)";
			} else if (value.toString().equals(Alert.Status.SYSTEM.toString())) {
				modifiedValue = value.toString() + " (시스템)";
			} else if (value.toString().equals(Alert.Status.TEST.toString())) {
				modifiedValue = value.toString() + " (테스트)";
			} else if (value.toString().equals(Alert.Status.DRAFT.toString())) {
				modifiedValue = value.toString() + " (초안)";
			}
			capEnum1.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(AlertElementNames.Status, capEnum1);

		List<Item> capEnum2 = new ArrayList<>();
		for (MsgType value : Alert.MsgType.values()) {
			String modifiedValue = "";
			if (value.toString().equals(Alert.MsgType.ALERT.toString())) {
				modifiedValue = value.toString() + " (경보)";
			} else if (value.toString().equals(Alert.MsgType.UPDATE.toString())) {
				modifiedValue = value.toString() + " (갱신)";
			} else if (value.toString().equals(Alert.MsgType.CANCEL.toString())) {
				modifiedValue = value.toString() + " (취소)";
			} else if (value.toString().equals(Alert.MsgType.ACK.toString())) {
				modifiedValue = value.toString() + " (응답)";
			} else if (value.toString().equals(Alert.MsgType.ERROR.toString())) {
				modifiedValue = value.toString() + " (오류)";
			}
			capEnum2.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(AlertElementNames.MsgType, capEnum2);

		List<Item> capEnum3 = new ArrayList<>();
		for (Scope value : Alert.Scope.values()) {
			String modifiedValue = "";
			if (value.toString().equals(Alert.Scope.PUBLIC.toString())) {
				modifiedValue = value.toString() + " (공용)";
			} else if (value.toString().equals(Alert.Scope.RESTRICTED.toString())) {
				modifiedValue = value.toString() + " (제한)";
			} else if (value.toString().equals(Alert.Scope.PRIVATE.toString())) {
				modifiedValue = value.toString() + " (개별)";
			}
			capEnum3.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(AlertElementNames.Scope, capEnum3);
		
		return capElementToEnumMap;
	}

	private Map<Enum<?>, List<Item>> buildInfoCapEnumMap(Map<Enum<?>, List<Item>> capElementToEnumMap) {
		// Category
		List<Item> capEnum1 = new ArrayList<>();
		for (Category value : Info.Category.values()) {
			String modifiedValue = "";
			if (value.toString().equals(Info.Category.GEO.toString())) {
				modifiedValue = value.toString() + " (지리)";
			} else if (value.toString().equals(Info.Category.MET.toString())) {
				modifiedValue = value.toString() + " (기상)";
			} else if (value.toString().equals(Info.Category.SAFETY.toString())) {
				modifiedValue = value.toString() + " (안전)";
			} else if (value.toString().equals(Info.Category.SECURITY.toString())) {
				modifiedValue = value.toString() + " (안보)";
			} else if (value.toString().equals(Info.Category.RESCUE.toString())) {
				modifiedValue = value.toString() + " (구조)";
			} else if (value.toString().equals(Info.Category.FIRE.toString())) {
				modifiedValue = value.toString() + " (화재)";
			} else if (value.toString().equals(Info.Category.HEALTH.toString())) {
				modifiedValue = value.toString() + " (건강)";
			} else if (value.toString().equals(Info.Category.ENV.toString())) {
				modifiedValue = value.toString() + " (환경)";
			} else if (value.toString().equals(Info.Category.TRANSPORT.toString())) {
				modifiedValue = value.toString() + " (교통)";
			} else if (value.toString().equals(Info.Category.INFRA.toString())) {
				modifiedValue = value.toString() + " (기반시설)";
			} else if (value.toString().equals(Info.Category.CBRNE.toString())) {
				modifiedValue = value.toString() + " (화생방)";
			} else if (value.toString().equals(Info.Category.OTHER.toString())) {
				modifiedValue = value.toString() + " (기타)";
			}
			capEnum1.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(InfoElementNames.Category, capEnum1);

		List<Item> capEnum2 = new ArrayList<>();
		for (Certainty value : Info.Certainty.values()) {
			String modifiedValue = "";
			if (value.toString().equals(Info.Certainty.OBSERVED.toString())) {
				modifiedValue = value.toString() + " (이미 발생하였거나 진행 중)";
			} else if (value.toString().equals(Info.Certainty.VERY_LIKELY.toString())) {
				continue;
			} else if (value.toString().equals(Info.Certainty.LIKELY.toString())) {
				modifiedValue = value.toString() + " (50%를 초과하는 가능성)";
			} else if (value.toString().equals(Info.Certainty.POSSIBLE.toString())) {
				modifiedValue = value.toString() + " (50% 이하의 가능성)";
			} else if (value.toString().equals(Info.Certainty.UNLIKELY.toString())) {
				modifiedValue = value.toString() + " (희박한 가능성)";
			} else if (value.toString().equals(Info.Certainty.UNKNOWN_CERTAINTY.toString())) {
				modifiedValue = value.toString() + " (미상)";
			}
			capEnum2.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(InfoElementNames.Certainty, capEnum2);

		List<Item> capEnum3 = new ArrayList<>();
		for (ResponseType value : Info.ResponseType.values()) {
			String modifiedValue = "";
			modifiedValue = value.toString();
			capEnum3.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(InfoElementNames.ResponseType, capEnum3);

		List<Item> capEnum4 = new ArrayList<>();
		for (Severity value : Info.Severity.values()) {
			String modifiedValue = "";
			if (value.toString().equals(Info.Severity.EXTREME.toString())) {
				modifiedValue = value.toString() + " (이례적인 피해)";
			} else if (value.toString().equals(Info.Severity.SEVERE.toString())) {
				modifiedValue = value.toString() + " (심각한 피해)";
			} else if (value.toString().equals(Info.Severity.MODERATE.toString())) {
				modifiedValue = value.toString() + " (피해 가능성 존재)";
			} else if (value.toString().equals(Info.Severity.MINOR.toString())) {
				modifiedValue = value.toString() + " (피해 가능성 낮음)";
			} else if (value.toString().equals(Info.Severity.UNKNOWN_SEVERITY.toString())) {
				modifiedValue = value.toString() + " (미상)";
			}
			capEnum4.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(InfoElementNames.Severity, capEnum4);

		List<Item> capEnum5 = new ArrayList<>();
		for (Urgency value : Info.Urgency.values()) {
			String modifiedValue = "";
			if (value.toString().equals(Info.Urgency.IMMEDIATE.toString())) {
				modifiedValue = value.toString() + " (즉각적인 대응이 필요함)";
			} else if (value.toString().equals(Info.Urgency.EXPECTED.toString())) {
				modifiedValue = value.toString() + " (한 시간 이내의 빠른 대응이 필요함)";
			} else if (value.toString().equals(Info.Urgency.FUTURE.toString())) {
				modifiedValue = value.toString() + " (근시일 내의 대응이 필요함)";
			} else if (value.toString().equals(Info.Urgency.PAST.toString())) {
				modifiedValue = value.toString() + " (대응이 필요 없음)";
			} else if (value.toString().equals(Info.Urgency.UNKNOWN_URGENCY.toString())) {
				modifiedValue = value.toString() + " (미상)";
			}
			capEnum5.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(InfoElementNames.Urgency, capEnum5);

		List<Item> capEnum6 = new ArrayList<>();
		for (KieasList.DisasterEventType value : KieasList.DisasterEventType.values()) {
			String modifiedValue = "";
			modifiedValue = value.toString() + " (" + value.getKoreanEventCode() + ")";
			capEnum6.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(InfoElementNames.EventCode, capEnum6);

		List<Item> capEnum7 = new ArrayList<>();
		for (String value : KieasConfiguration.KieasList.LANGUAGE_LIST) {
			String modifiedValue = "";
			if (value.toString().equals(KieasConfiguration.KieasList.LANGUAGE_LIST[0])) {
				modifiedValue = value.toString() + " (한국어)";
			} else if (value.toString().equals(KieasConfiguration.KieasList.LANGUAGE_LIST[1])) {
				modifiedValue = value.toString() + " (영어)";
			}
			capEnum7.add(new Item(value.toString(), modifiedValue));
		}
		capElementToEnumMap.put(InfoElementNames.Language, capEnum7);

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
		capEnum8.add(new Item(item1.getKey(), item1.getKey() + " (" + item1.getValue() + ")"));
		capEnum8.add(new Item(item2.getKey(), item2.getKey() + " (" + item2.getValue() + ")"));
		capEnum8.add(new Item(item3.getKey(), item3.getKey() + " (" + item3.getValue() + ")"));
		capEnum8.add(new Item(item4.getKey(), item4.getKey() + " (" + item4.getValue() + ")"));
		capEnum8.add(new Item(item5.getKey(), item5.getKey() + " (" + item5.getValue() + ")"));
		capEnum8.add(new Item(item6.getKey(), item6.getKey() + " (" + item6.getValue() + ")"));
		capEnum8.add(new Item(item7.getKey(), item7.getKey() + " (" + item7.getValue() + ")"));
		capEnum8.add(new Item(item8.getKey(), item8.getKey() + " (" + item8.getValue() + ")"));
		capEnum8.add(new Item(item9.getKey(), item9.getKey() + " (" + item9.getValue() + ")"));
		capEnum8.add(new Item(item10.getKey(), item10.getKey() + " (" + item10.getValue() + ")"));
		capEnum8.add(new Item(item11.getKey(), item11.getKey() + " (" + item11.getValue() + ")"));
		capEnum8.add(new Item(item12.getKey(), item12.getKey() + " (" + item12.getValue() + ")"));
		capEnum8.add(new Item(item13.getKey(), item13.getKey() + " (" + item13.getValue() + ")"));
		capEnum8.add(new Item(item14.getKey(), item14.getKey() + " (" + item14.getValue() + ")"));
		capEnum8.add(new Item(item15.getKey(), item15.getKey() + " (" + item15.getValue() + ")"));
		capEnum8.add(new Item(item16.getKey(), item16.getKey() + " (" + item16.getValue() + ")"));
		capElementToEnumMap.put(AreaElementNames.GeoCode, capEnum8);
		
		return capElementToEnumMap;
	}
}
