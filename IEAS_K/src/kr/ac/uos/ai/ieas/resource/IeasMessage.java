package kr.ac.uos.ai.ieas.resource;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

import org.xml.sax.SAXParseException;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.Area;
import com.google.publicalerts.cap.CapException;
import com.google.publicalerts.cap.CapUtil;
import com.google.publicalerts.cap.CapValidator;
import com.google.publicalerts.cap.CapXmlBuilder;
import com.google.publicalerts.cap.CapXmlParser;
import com.google.publicalerts.cap.Group;
import com.google.publicalerts.cap.Group.Builder;
import com.google.publicalerts.cap.Info;
import com.google.publicalerts.cap.NotCapException;
import com.google.publicalerts.cap.Resource;
import com.google.publicalerts.cap.ValuePair;

public class IeasMessage {

	private CapXmlBuilder 	capXmlBuilder;
	private CapXmlParser 	capXmlParser;
	private CapValidator 	capValidator;

	private Alert 			alert;
	private Info 			info;
	private Resource 		resource;
	private Area 			area;

	private String xmlMessage;
	

	public IeasMessage()  {

		capXmlBuilder = new CapXmlBuilder();
		capXmlParser = new CapXmlParser(true);
		capValidator = new CapValidator();

		buildDefaultMessage();
	}
	
	public void buildDefaultMessage() {
		
		this.alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
				.setIdentifier("Identifier")
				.setSender("Sender")
				.setSent("Sent")
				.setStatus(Alert.Status.SYSTEM)
				.setMsgType(Alert.MsgType.ALERT)
				.setScope(Alert.Scope.PUBLIC)
				.build();
		
		this.info = Info.newBuilder()
				.setLanguage("ko-KR")
				.addCategory(Info.Category.SAFETY)
				.setEvent("event")
				.setUrgency(Info.Urgency.UNKNOWN_URGENCY)
				.setSeverity(Info.Severity.UNKNOWN_SEVERITY)
				.setCertainty(Info.Certainty.UNKNOWN_CERTAINTY)
				.buildPartial();
		
		this.resource = Resource.newBuilder().buildPartial();
		
		this.area = Area.newBuilder().buildPartial();
				
		alert = Alert.newBuilder(alert).addInfo(info).build();
	}
	
	/*
	private void buildCapMessage()  {

		alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
				//
				.setIdentifier("Identifier") //REQUIRED
				.setSender("Sender") //REQUIRED
				.setSent("Sent") //REQUIRED
				.setStatus(Alert.Status.SYSTEM) //REQUIRED
				.setMsgType(Alert.MsgType.ALERT) //REQUIRED
				.setSource("Source")
				.setScope(Alert.Scope.PUBLIC) //REQUIRED
//				.setRestriction("Restriction")
				.setAddresses(Group.newBuilder().addValue("Address").build())
				.addCode("Code") //REQUIRED
				.setNote("Note")
//				.setReferences(Group.newBuilder().addValue("refernce").build())
//				.setIncidents(Group.newBuilder().addValue("incident").build())
				//
				.buildPartial();
		
		info = Info.newBuilder()
				//
				.setLanguage("ko-KR")
				.addCategory(Info.Category.SAFETY)
				.setEvent("Event")
				.addResponseType(Info.ResponseType.AVOID)
				.setUrgency(Info.Urgency.UNKNOWN_URGENCY)
				.setSeverity(Info.Severity.UNKNOWN_SEVERITY)
				.setCertainty(Info.Certainty.UNKNOWN_CERTAINTY)
				.setAudience("Public")
				.addEventCode(Info.newBuilder().addEventCodeBuilder().setValueName("자연재난").setValue("0100").build())
				.setSenderName("SenderName")
				.setHeadline("Headline")
				.setDescription("Description")
				.setInstruction("Instruction")
				.setWeb("http://ai.uos.ac.kr")
				.setContact("AILab-byun-01076771085")
				.addParameter(Info.newBuilder().addParameterBuilder().setValueName("CMAMtext").setValue("abcdefghijindain").build())
				//
				.buildPartial();
		
//		resource = Resource.newBuilder()
//				.setResourceDesc("ResourceDesc")
//				.setMimeType("MimeType")
//				.setSize(8)
//				.setUri("Uri")
//				.setDerefUri("DerefUri")
//				.setDigest("")
//				.buildPartial();

		area = Area.newBuilder()
				.setAreaDesc("UOS")
				.addGeocode(ValuePair.newBuilder().setValueName("GeocodeName").setValue("Geocode").build())
				.buildPartial();

		info = Info.newBuilder(info)
//				.addResource(resource)
				.addArea(area)
				.buildPartial();
		
		alert = Alert.newBuilder(alert)
				.addInfo(info)
				.buildPartial();
		
		this.message = capXmlBuilder.toXml(alert);	
	}
	*/
	
	public boolean validateMessage() {

		try {
			capValidator.validateAlert(alert);
			return true;
		} catch (CapException e) {
			return false;
		}
	}
	
	public void build() {
		
		alert = Alert.newBuilder(alert).clearInfo().addInfo(info).build();
	}
	
	public Alert setMessage(String message) {
		try {
			alert = capXmlParser.parseFrom(message);
			return alert;

		} catch (NotCapException | SAXParseException | CapException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getMessage() {
		try {
			this.xmlMessage = capXmlBuilder.toXml(alert);
			return xmlMessage;

		} catch (NotCapException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setSender(String sender) {
		alert = Alert.newBuilder(alert).setSender(sender).build();
	}

	public void setReceiver(List<String> receiverList) {
		Builder addressGroupbuilder = Group.newBuilder();

		for (String receiver : receiverList) {
			addressGroupbuilder.addValue(receiver);
		}
		addressGroupbuilder.build();
		alert = Alert.newBuilder(alert).setAddresses(addressGroupbuilder).build();
	}

	public void setMsgTypeToAck(){
		alert = Alert.newBuilder(alert).setMsgType(Alert.MsgType.ACK).build();
	}

	public String getSender(){

		try {
			return alert.getSender();

		} catch (NotCapException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setSent() {
		GregorianCalendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("Asia/Seoul"));
		cal.setGregorianChange(new Date());
		cal.setTime(new Date());
		alert = Alert.newBuilder(alert).setSent(CapUtil.formatCapDate(cal)).build();
	}

	public void setIdentifier(String source) {
		UUID id = UUID.randomUUID();
		String identifier = source+"-"+id;
		alert = Alert.newBuilder(alert).setIdentifier(identifier).build();
	}

	public String getIdentifier() {
		try {
			return alert.getIdentifier();
		} catch (NotCapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void setCode(String code) {
		alert = Alert.newBuilder(alert).addCode(code).build();
	}

	public String getCode(IeasMessage message) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMsgTypeToAlert() {
		alert = Alert.newBuilder(alert).setMsgType(Alert.MsgType.ALERT).build();		
	}

	public String getAddresses() {
		try {
			return alert.getAddresses().getValue(0);

		} catch (NotCapException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setAddresses(String string) {
		alert = Alert.newBuilder(alert).setAddresses(Group.newBuilder().addValue(string).build()).build();		
	}

	public void setScopeToRestricted() {
		alert = Alert.newBuilder(alert).setScope(Alert.Scope.RESTRICTED).build();		
	}

	public String getEvent() {
		try {
			return alert.getInfo(0).getEvent();
		} catch (NotCapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void setSource(String id) {
		alert = Alert.newBuilder(alert).setSource(id).build();
	}
	
	public String getSource() {
		try {
			return alert.getSource();

		} catch (NotCapException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setEvent(String event) {
		info = Info.newBuilder(info).setEvent(event).build();
	}

	public String getSent() {
		try {
			return alert.getSent();

		} catch (NotCapException e) {
			e.printStackTrace();
		}
		return null;
	}
}
