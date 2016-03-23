package kr.or.kpew.kieas.common;

public class Profile {
	
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
	
	/**
	 * CAP의 <identifier>에서 이름 부분을 관리하기 위한 변수이다. 
	 * 각 시스템은 스스로 메시지 ID를 관리하여야 한다. 메시지 ID는 senderName+10자리 숫자로 이루어져 있다.
	 * ex: 국민안전처0123456789
	 */
	protected String agency;

	/**
	 * CAP의 <identifier>에서 10자리 숫자 부분을 관리하기 위한 변수이다. 
	 * 각 시스템은 스스로 메시지 ID를 관리하여야 한다. 메시지 ID는 senderName+10자리 숫자로 이루어져 있다.
	 * ex: 국민안전처0123456789
	 * 현재는 프로그램을 구동하면 0000000000부터 순차적으로 증가하도록 구현하였다.
	 * 이후 필요에 따라 파일 또는 DB로 이 값을 보존하는 것을 권장한다.
	 */
	protected String sender;
	
	protected String address;
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(!(obj instanceof Profile))
			return false;
		
		Profile p = (Profile)obj;
					
		return this.getSender().equals(p.getSender()); 
	};

	
	/**
	 * 
	 * @param sender 시스템의 식별자로 이메일 형식이다.(ex: raychani@uos.ac.kr)
	 * @param address 시스템의 네트워크 주소이다.(ex: alert.kpew.or.kr, 192.168.0.119, alertsystem0123(JMS 등에서 사용 가능))
	 */
	public Profile(String sender, String agency) {
		setSender(sender);
		setAgency(agency);
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}


}
