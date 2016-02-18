package kr.ac.uos.ai.ieas.db.dbModel;

public class CAPArea implements CAPBean {

	private int area_eid;
	private String areaDesc;
	private String polygon;
	private String circle;
	private String geocode;
	private String altitude;
	private String ceiling;
	private int info_eid;

	public int getArea_eid() {
		return area_eid;
	}

	public void setArea_eid(int area_eid) {
		this.area_eid = area_eid;
	}

	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public String getPolygon() {
		return polygon;
	}

	public void setPolygon(String polygon) {
		this.polygon = polygon;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getGeocode() {
		return geocode;
	}

	public void setGeocode(String geocode) {
		this.geocode = geocode;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getCeiling() {
		return ceiling;
	}

	public void setCeiling(String ceiling) {
		this.ceiling = ceiling;
	}

	public int getInfo_eid() {
		return info_eid;
	}

	public void setInfo_eid(int info_eid) {
		this.info_eid = info_eid;
	}

}
