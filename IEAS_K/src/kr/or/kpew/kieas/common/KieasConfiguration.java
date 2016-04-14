package kr.or.kpew.kieas.common;

/**
 * 전체 시스템에서 사용하는 각종 상수값들을 정의한 클래스이다.
 * @author byun-ai
 *
 */
public class KieasConfiguration
{	
	public final static String PACKAGE_NAME 				= "kieas.";
		
	public static class KieasName 
	{
		public final static String GATEWAY_NAME 			= PACKAGE_NAME + "gateway";
		public final static String STANDARD_ALERTER 		= PACKAGE_NAME + "standardAlerter";
		public final static String OLD_ALERTER 				= PACKAGE_NAME + "oldAlerter";
		public final static String STANDARD_ALERT_SYSTEM 	= PACKAGE_NAME + "standardAlertSystem";
		public final static String OLD_ALERT_SYSTEM 		= PACKAGE_NAME + "oldAlertSystem";
	}
	
	public static class KieasAddress 
	{
		public static final String ACTIVEMQ_SERVER_IP_LOCAL = "tcp://localhost:61616";
//		public static final String ACTIVEMQ_SERVER_IP_LOCAL = "tcp://127.0.0.1:61616";
		
		public final static String GATEWAY_ID = "maingateway@korea.kr";
//		public final static String ALERTER_TO_GATEWAY_QUEUE_DESTINATION 		= PACKAGE_NAME+"alerterToGatewayQueue";
//		public final static String ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION 	= PACKAGE_NAME+"alertSystemToGatewayQueue";
		
//		public final static String GATEWAY_TOPIC_DESTINATION 					= PACKAGE_NAME+"gatewayTopic";
	}
	
	public static class KieasConstant
	{
		public static final String CODE = "대한민국정부1.0";
		public static final String EVENT_CODE_VALUE_NAME = "TTAS.KO-07.0046/R5 재난 종류 코드";
		public static final String DEFAULT_TIME_ZONE = "Asia/Seoul";
		public static final String GEO_CODE_VALUE_NAME = "행정구역코드";

		public static final String ACK = "ACK";
		public static final String NACK = "NACK";
		public static final String COMP = "COMP";
	}
	
	public static class KieasList
	{			
		public static final String[] ALERT_SYSTEM_TYPE_LIST =
		{
			"자동우량경보시스템",
			"자동음성통보시스템",
			"DMB재난경보방송시스템",
			"RDS시스템",
			"민방위경보시스템",
			"CBS시스템",
			"재해문자전광판시스템",
			"옥내경보방송시스템",
			"옥외가로등경보시스템",
			"전광판",
			"BIS시스템"
		};
		
		public enum DisasterEventType
		{
			AL1("민방공공습경보"), 
			AL2("민방공경계경보"), 
			CLR("민방공경보해제"), 
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
			
			private DisasterEventType(String koreanEventCode)
			{
				this.koreanEventCode = koreanEventCode;
			}
			
			public String getKoreanEventCode() {
				return this.koreanEventCode;
			}
		}
		
		public static final String[] GEO_CODE_LIST =
		{
			"1100000000",
			"2600000000",
			"2700000000",
			"5000000000"
		};
		
		public static final String[] LANGUAGE_LIST =
		{
			"ko-KR",
			"us-EN"
		};
	}	
}

