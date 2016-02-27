package kr.or.kpew.kieas.issuer.model;

public class IssuerProfile
{
	private final String DEFAULT_ID = "Issuer";
	private final String DEFAULT_ADDRESS = "localhost";
	
	private String id;
	private String address;
	
	
	public IssuerProfile()
	{
		init();
	}
	
	private void init()
	{
		this.id = DEFAULT_ID;
		this.address = DEFAULT_ADDRESS;
	}
		
	public String getId()
	{
		return this.id;
	}
	
	public String getAddress()
	{
		return this.address;
	}
	
	public void setId(String issuerId)
	{
		this.id = issuerId;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
}
