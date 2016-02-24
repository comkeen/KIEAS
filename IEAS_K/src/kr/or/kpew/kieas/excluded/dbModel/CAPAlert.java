package kr.ac.uos.ai.ieas.db.dbModel;

import java.util.Date;
import java.util.List;

public class CAPAlert implements CAPBean {

	private int alert_eid;
	private String identifier;
	private String sender;
	private Date sent;
	private Status status;
	private MsgType msgType;
	private String source;
	private Scope scope;
	private String restriction;
	private String addresses;
	private String code;
	private String note;
	private String references;
	private String incidents;

	private List<CAPInfo> infoList;

	public enum Status {
		Actual, Exercise, System, Test, Draft;
	}

	public enum MsgType {
		Alert, Update, Cancel, Ack, Error;
	}
	
	public enum Scope {
		Public, Restricted, Private;
	}

	public int getAlert_eid() {
		return alert_eid;
	}

	public void setAlert_eid(int alert_eid) {
		this.alert_eid = alert_eid;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	public String getIncidents() {
		return incidents;
	}

	public void setIncidents(String incidents) {
		this.incidents = incidents;
	}

	public List<CAPInfo> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<CAPInfo> infoList) {
		this.infoList = infoList;
	}

}
