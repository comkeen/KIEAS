package kr.ac.uos.ai.ieas.resource;


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
//		public static final String ACTIVEMQ_SERVER_IP = "tcp://127.0.0.1:61616";
//		public static final String ACTIVEMQ_SERVER_IP = "tcp://192.168.0.7:61616"; //home window
//		public static final String ACTIVEMQ_SERVER_IP = "tcp://172.16.165.135:61616"; //host window

		public static final String DATABASE_SERVER_IP_LOCAL = "jdbc:mysql://localhost:3306";
//		public static final String DATABASE_SERVER_IP = "jdbc:mysql://172.16.165.135:3306";
		
		public final static String ALERTER_TO_GATEWAY_QUEUE_DESTINATION 		= PACKAGE_NAME+"alerterToGatewayQueue";
		public final static String ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION 	= PACKAGE_NAME+"alertSystemToGatewayQueue";
		public final static String GATEWAY_TO_ALERTER_QUEUE_DESTINATION 		= PACKAGE_NAME+"gatewayToAlerterQueue";		
		public final static String GATEWAY_TO_ALERTSYSTEM_QUEUE_DESTINATION 	= PACKAGE_NAME+"gatewayToAlertSystemQueue";
		
		public final static String GATEWAY_TOPIC_DESTINATION 					= PACKAGE_NAME+"gatewayTopic";
	}
	
	public static class KIEAS_Constant
	{
		public static final String CODE = "대한민국정부1.0";
		public static final String EVENT_CODE_VALUE_NAME = "TTAS.KO-07.0046/R5 재난 종류 코드";
		public static final String DEFAULT_TIME_ZONE = "Asia/Seoul";
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
		
		public static final String[] ALERT_ELEMENT_LIST =
		{
			"Identifier",
			"Sender",
			"Sent",
			"Status",
			"MsgType",
			"Scope",
			"Restriction",
			"Code",
		};
		
		public static final String[] INFO_ELEMENT_LIST =
		{
			"Language",
			"Category",
			"Event",
			"Urgency",
			"Severity",
			"Certainty",
			"EventCode",
			"Effective",
			"SenderName",
			"Web",
			"Contact",
			"Headline",
			"Description"
		};
		
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

