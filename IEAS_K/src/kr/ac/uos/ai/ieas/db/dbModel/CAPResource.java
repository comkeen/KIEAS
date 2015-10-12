package kr.ac.uos.ai.ieas.db.dbModel;

public class CAPResource implements CAPBean {

	private int resource_eid;
	private String resourceDesc;
	private String mimeType;
	private float size;
	private String uri;
	private String deferURI;
	private byte[] digest;
	private int info_eid;

	public int getResource_eid() {
		return resource_eid;
	}

	public void setResource_eid(int resource_eid) {
		this.resource_eid = resource_eid;
	}

	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDeferURI() {
		return deferURI;
	}

	public void setDeferURI(String deferURI) {
		this.deferURI = deferURI;
	}

	public byte[] getDigest() {
		return digest;
	}

	public void setDigest(byte[] digest) {
		this.digest = digest;
	}

	public int getInfo_eid() {
		return info_eid;
	}

	public void setInfo_eid(int info_eid) {
		this.info_eid = info_eid;
	}

}