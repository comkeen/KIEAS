package kr.or.kpew.kieas.common;

public class AlertSystemProfile extends Profile {

	private String name;
	private String geoCode;
	private AlertSystemType type;
	private String language;
	//수신기 표출기능을 정의한다. 문자, 그림, 음성, 영상 을 사용한다고 가정하자.
	private String capability;

	public String getCapability() {
		return capability;
	}


	public void setCapability(String capability) {
		this.capability = capability;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public AlertSystemProfile(String sender, String agency, AlertSystemType type) {
		super(sender, agency);
		this.type = type;
	}
		
	
	public String getName() {
		if(name == null)
			return type.toString();
		else
			return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public AlertSystemType getType() {
		return type;
	}
	
	public String getGeoCode() {
		return geoCode;
	}
	
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}
	

}
