package kr.ac.uos.ai.ieas.db.dbModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CAPInfo implements CAPBean {

	private int info_eid;
	private Language language;
	private Category category;
	private String event;
	private ResponseType responseType;
	private Urgency urgency;
	private Severity severity;
	private Certainty certainty;
	private String audience;
	private String eventCode;
	private Date effective;
	private Date onset;
	private Date expires;
	private String senderName;
	private String headline;
	private String description;
	private String instruction;
	private String web;
	private String contact;
	private String parameter;
	private int alert_eid;
	
	private List<CAPResource> resList;
	private List<CAPArea> areaList;

	public enum Language
	{
		koKR("ko-KR"), enUS("en-US");

		private String codename;

		private Language(String codename)
		{
			this.codename = codename;
		}

		public String toString()
		{
			return this.codename;
		}
	}

	public enum Category {
		Met, Safety, Security, Rescue, Fire, Health, Env, Transport, Infra, CBRNE, Other;
	}

	public enum ResponseType {
		Shelter, Evacuate, Prepare, Execute, Avoid, Monitor, Assess, AllClear, None;
	}

	public enum Urgency {
		Immediate, Expected, Future, Past, Unknown;
	}

	public enum Severity {
		Extreme, Severe, Moderate, Minor, Unknown;
	}

	public enum Certainty {
		Observed, Likely, Possible, Unlikely, Unknown;
	}

	public int getInfo_eid() {
		return info_eid;
	}

	public void setInfo_eid(int info_eid) {
		this.info_eid = info_eid;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public ResponseType getResponseType() {
		return responseType;
	}

	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}

	public Urgency getUrgency() {
		return urgency;
	}

	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public Certainty getCertainty() {
		return certainty;
	}

	public void setCertainty(Certainty certainty) {
		this.certainty = certainty;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public Date getEffective() {
		return effective;
	}

	public void setEffective(Date effective) {
		this.effective = effective;
	}

	public Date getOnset() {
		return onset;
	}

	public void setOnset(Date onset) {
		this.onset = onset;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public int getAlert_eid() {
		return alert_eid;
	}

	public void setAlert_eid(int alert_eid) {
		this.alert_eid = alert_eid;
	}

	public List<CAPResource> getResList() {
		return resList;
	}

	public void setResList(List<CAPResource> resList) {
		this.resList = resList;
	}

	public List<CAPArea> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<CAPArea> areaList) {
		this.areaList = areaList;
	}

}
