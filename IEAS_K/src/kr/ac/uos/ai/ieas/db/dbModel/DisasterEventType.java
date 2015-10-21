package kr.ac.uos.ai.ieas.db.dbModel;

public enum DisasterEventType {

	HRA("호우주의보"), 
	HRW("호우경보"), 
	HSW("대설주의보"),
	HAS("대설경보"),
	SSA("폭풍해일주의보"),
	SSW("폭풍해일경보"),
	YSW("황사경보"),
	CWA("한파주의보"),
	CWW("한파경보"),
	WWW("풍랑경보"),
	HAW("건조경보"),
	MFW("산불경보"), 
	RTW("교통통제"),
	EAN("국가비상상황발생"), 
	EAT("국가비상상황종료"),
	NIC("중앙재난안전대책본부"),
	NPT("전국적주기테스트"), 
	RMT("전국적월별의무테스트"),
	RWT("전국적주간별의무테스트"),
	STT("특수수신기테스트"),
	ADR("행정메시지"),
	AVW("산사태경보"), 
	AVA("산사태주의보"),
	BZW("폭풍설경보"),
	CAE("어린이유괴긴급상황"),
	CDW("시민위험상황경보"),
	CEM("시민응급상황메시지"),
	CFW("해안침수경보"),
	CFA("해안침수주의보"),
	DSW("모래폭풍경보"),
	EQW("지진경보"),
	EVI("즉시대피"),
	FRW("화재경보"),
	FFW("긴급홍수경보"),
	FFA("긴급홍수주의보"),
	FFS("긴급홍수상황"),
	FLW("홍수경보"),
	FLA("홍수주의보"),
	FLS("홍수상황"),
	HMW("위험물질경보"),
	HWA("강풍주의보"),
	HUW("강풍경보"),
	HUA("태풍주의보"),
	HLS("태풍정보"),
	LEW("법집행경고"),
	LAE("지역긴급상황"),
	NMN("통신메시지알림"),
	TOE("119전화불통응급상황"),
	NUW("핵발전소관련경보"),
	DMO("연습/시연경보"),
	RHW("방사능위험경보"),
	SVR("뇌우경보"),
	SVA("뇌우주의보"),
	SVS("악기상경보"), 
	SPW("안전한장소로피난경보"),
	SMW("특수해양경보"),
	SPS("특이기상정보"),
	TOR("토네이도경보"),
	TOA("토네이도주의보"),
	TRW("열대폭풍(태풍)경보"),
	TRA("열대폭풍(태풍)주의보"), 
	TSW("지진해일경보"),
	TSA("지진해일주의보"),
	VOW("화산경보"),
	WSW("눈폭풍경보"),
	WSA("눈폭풍주의보");
	
	private String koreanEventCode;
	
	private DisasterEventType(String koreanEventCode) {
		this.koreanEventCode = koreanEventCode;
	}
	
	public String getKoreanEventCode() {
		return this.koreanEventCode;
	}	
}
