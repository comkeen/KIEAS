package kr.or.kpew.kieas.common;

public class AlertSystemProfile extends Profile {

	private String name;
	private String geoCode;
	private AlertSystemType type;

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
