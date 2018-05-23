package kr.or.kpew.kieas.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.Alert.MsgType;
import com.google.publicalerts.cap.Alert.Scope;
import com.google.publicalerts.cap.Alert.Status;
import com.google.publicalerts.cap.Area;
import com.google.publicalerts.cap.CapValidator;
import com.google.publicalerts.cap.CapXmlBuilder;
import com.google.publicalerts.cap.Info;
import com.google.publicalerts.cap.Info.Category;
import com.google.publicalerts.cap.Point;
import com.google.publicalerts.cap.Resource;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasConstant;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder.AlertElementNames;
import kr.or.kpew.kieas.issuer.view.resource.TableModel;


public class KIEAS_Test {
	
	//구글 CAP 라이브러리와 이를 사용하기 쉽게 맵핑해서 만든 CAP메시지빌더를 이용한 기본적인 CAP 메시지 생성
	@Test
	public void SimpleCapBuildTest() {
		// 구글 CAP 라이브러리를 맵핑하여 만든 CAP메시지빌더 클래스를 이용한 기본 CAP메시지 생성
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();

		// Alert 요소 작성
		kieasMessageBuilder.setIdentifier("Identifier");
		kieasMessageBuilder.setSender("Sender");
		kieasMessageBuilder.setSent("2016-02-14T00:54:14+09:00");
		kieasMessageBuilder.setStatus(Status.ACTUAL);
		kieasMessageBuilder.setMsgType(MsgType.ALERT);
		kieasMessageBuilder.setScope(Scope.PUBLIC);
		//CAP 메시지빌더의 xml 형태의 메시지 반환
		String result1 = kieasMessageBuilder.getMessage();

		// 구글 CAP 라이브러리를 이용한 기본 CAP메시지 생성
		// Alert 요소 작성
		Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS).setIdentifier("Identifier")
				.setSender("Sender").setSent("2016-02-14T00:54:14+09:00").setStatus(Alert.Status.ACTUAL)
				.setMsgType(Alert.MsgType.ALERT).setScope(Alert.Scope.PUBLIC).addCode("대한민국정부1.0").buildPartial();
		//Alert 객체를 xml로 변환
		CapXmlBuilder xmlBuilder = new CapXmlBuilder();
		String result2 = xmlBuilder.toXml(alert);

		// System.out.println("Using KieasMessageBuilder : \n" + result1);
		// System.out.println("Using GoogleCapLibrary : \n" + result2);

		assertEquals(true, result1.equals(result2));
	}

	//구글 CAP 라이브러리와 이를 사용하기 쉽게 맵핑해서 만든 CAP메시지빌더를 이용한 거의 모든 요소가 포함되는 CAP 메시지 생성
	@Test
	public void FullCapBuildTest() {
		// 구글 CAP 라이브러리를 맵핑하여 만든 CAP메시지빌더 클래스를 이용한 CAP메시지 생성
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();

		// Alert 요소 작성
		kieasMessageBuilder.setIdentifier("Identifier");
		kieasMessageBuilder.setSender("Sender");
		kieasMessageBuilder.setSent("2016-02-14T00:54:14+09:00");
		kieasMessageBuilder.setStatus(Status.ACTUAL);
		kieasMessageBuilder.setMsgType(MsgType.ALERT);
		kieasMessageBuilder.setScope(Scope.RESTRICTED);

		// Info 요소 작성
		// InfoElementSetter(int infoIndex, String value)
		kieasMessageBuilder.setLanguage(0, "ko-KR");
		kieasMessageBuilder.setCategory(0, Category.MET);
		kieasMessageBuilder.setEvent(0, "Event");
		kieasMessageBuilder.setUrgency(0, "Unknown");
		kieasMessageBuilder.setSeverity(0, "Unknown");
		kieasMessageBuilder.setCertainty(0, "Unknown");

		// Resource 요소 작성
		// ResourceElementSetter(int infoIndex, int resourceIndex, String value)
		kieasMessageBuilder.setResourceDesc(0, 0, "Resource Description");
		kieasMessageBuilder.setMimeType(0, 0, "Mime Type");

		// Area 요소 작성
		// AreaElementSetter(int infoIndex, int areaIndex, String value)
		kieasMessageBuilder.setAreaDesc(0, 0, "Area Description");

		String result1 = kieasMessageBuilder.getMessage();

		// 구글 CAP 라이브러리를 이용한 CAP메시지 생성
		// Alert 요소 작성
		Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS).setIdentifier("Identifier")
				.setSender("Sender").setSent("2016-02-14T00:54:14+09:00").setStatus(Alert.Status.ACTUAL)
				.setMsgType(Alert.MsgType.ALERT).setScope(Alert.Scope.RESTRICTED).addCode("대한민국정부1.0").buildPartial();
		// Info 요소 작성
		Info info = Info.newBuilder().setLanguage("ko-KR").addCategory(Info.Category.MET).setEvent("Event")
				.setUrgency(Info.Urgency.UNKNOWN_URGENCY).setSeverity(Info.Severity.UNKNOWN_SEVERITY)
				.setCertainty(Info.Certainty.UNKNOWN_CERTAINTY).buildPartial();
		// Resource 요소 작성
		Resource resource = Resource.newBuilder().setResourceDesc("Resource Description").setMimeType("Mime Type")
				.buildPartial();
		// Area 요소 작성
		Area area = Area.newBuilder().setAreaDesc("Area Description").buildPartial();

		// Info 요소에 Resource, Area 요소 추가
		info = Info.newBuilder(info).addResource(resource).addArea(area).buildPartial();
		// Alert 요소에 Info 요소 추가
		alert = Alert.newBuilder(alert).addInfo(info).build();

		// Alert 객체를 xml로 변환
		CapXmlBuilder xmlBuilder = new CapXmlBuilder();
		String result2 = xmlBuilder.toXml(alert);

		// System.out.println("Using KieasMessageBuilder : \n" + result1);
		// System.out.println("Using GoogleCapLibrary : \n" + result2);

		assertEquals(true, result1.equals(result2));
	}

	//1개 이상 있을 수 있는 요소들에 대한 생성 테스트
	@Test
	public void MultiElementBuildTest() {
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();

		// Alert 요소 작성
		kieasMessageBuilder.setIdentifier("Identifier");
		kieasMessageBuilder.setSender("Sender");
		kieasMessageBuilder.setSent("2016-02-14T00:54:14+09:00");
		kieasMessageBuilder.setStatus(Status.ACTUAL);
		kieasMessageBuilder.setMsgType(MsgType.ALERT);
		kieasMessageBuilder.setScope(Scope.PUBLIC);

		// Info 요소 작성
		// InfoElementSetter(int infoIndex, String value)
		kieasMessageBuilder.setLanguage(0, "infoIndex0 : ko-KR");
		kieasMessageBuilder.setLanguage(1, "infoIndex1 : ko-KR");
		kieasMessageBuilder.setCategory(0, Category.MET);
		kieasMessageBuilder.setEvent(2, "infoIndex2 : Event");
		kieasMessageBuilder.setEvent(3, "infoIndex3 : Event");
		kieasMessageBuilder.setUrgency(0, "Unknown");
		kieasMessageBuilder.setSeverity(0, "Unknown");
		kieasMessageBuilder.setCertainty(0, "Unknown");

		// Area 요소 작성
		// AreaElementSetter(int infoIndex, int areaIndex, String value)
		kieasMessageBuilder.setAreaDesc(0, 0, "infoIndex0, areaIndex0, Area Description");
		kieasMessageBuilder.setAreaDesc(1, 2, "infoIndex1, areaIndex2, Area Description");
		kieasMessageBuilder.setAreaDesc(3, 1, "infoIndex3, areaIndex1, Area Description");
		// PolygonElementSetter(int infoIndex, int areaIndex, String value)
		Point[] points = new Point[3];
		points[0] = Point.newBuilder().setLatitude(1).setLongitude(1).build();
		points[1] = Point.newBuilder().setLatitude(2).setLongitude(2).build();
		points[2] = Point.newBuilder().setLatitude(3).setLongitude(3).build();
		kieasMessageBuilder.setPolygon(0, 0, 0, points);
		kieasMessageBuilder.setPolygon(0, 0, 1, points);

		System.out.println(kieasMessageBuilder.getMessage());
	}

//	@Test
//	public void TableModelTest()
//	{
//		KieasMessageBuilder builder = new KieasMessageBuilder();
//		String[] columnNames =
//			{
//				AlertElementNames.Identifier.toString(),
//				AlertElementNames.Sender.toString(),
//				AlertElementNames.Sent.toString(),
//				KieasConstant.ACK
//			};
//		
//		TableModel tableModel = new TableModel(columnNames);
//		tableModel.updateTable(builder.buildDefaultMessage());
//		tableModel.updateTable(builder.buildDefaultMessage());
//	}
	
//	@Test
//	public void TcpIpTest() 
//	{
//		try {
//			System.out.println("-- Tcp Ip Test --");
//			GatewayTcp gw = new GatewayTcp();
//			gw.init("g@k.kr");
////			Thread gwT = new Thread(gw);
////			gwT.start();
//			long delay = 2000;
//			Thread.sleep(delay);
//			IssuerTcp issuer = new IssuerTcp();
//			issuer.init("i@k.r", KieasAddress.ALERTER_TO_GATEWAY_QUEUE_DESTINATION);
//			Thread.sleep(delay);
//			
//			AlertSystemTcp alertSystem1 = new AlertSystemTcp();
//			alertSystem1.init("1@k.r", KieasAddress.GATEWAY_TOPIC_DESTINATION);
//			Thread ast = new Thread(alertSystem1);
//			ast.start();
//			Thread.sleep(delay);			
//			AlertSystemTcp alertSystem2 = new AlertSystemTcp();
//			alertSystem2.init("2@k.r", KieasAddress.GATEWAY_TOPIC_DESTINATION);
//			Thread ast2 = new Thread(alertSystem2);
//			ast2.start();
//			Thread.sleep(delay);
//			AlertSystemTcp alertSystem3 = new AlertSystemTcp();
//			alertSystem3.init("3@k.r", KieasAddress.GATEWAY_TOPIC_DESTINATION);
//			Thread ast3 = new Thread(alertSystem3);
//			ast3.start();
//			Thread.sleep(delay);
//			
//			String message = new XmlReaderAndWriter().loadXml("cap/HRA.xml");
//			System.out.println("message : ");
////			System.out.println(message);
//			
//			KieasTcpProtocol protocol = new KieasTcpProtocol(Command.Issue, SenderType.Issuer, IntegratedEmergencyAlertSystem.stringToByte(message));
//			Thread.sleep(1000);
//			issuer.send(protocol.encode());
//			Thread.sleep(1000);
//			gw.broadcast(protocol.encode());
//		} 
//		catch (InterruptedException e) 
//		{
//			e.printStackTrace();
//		}
//	}
	

//	@Test
//	public void ensureDifferentValuesWhenMockIsCalled()
//	{
//		Clock clock = Clock.systemDefaultZone();
//		Instant first = Instant.now();                  // e.g. 12:00:00
//		Instant first2 = Instant.now();
//		Instant second = first.plusSeconds(1);          // 12:00:01
//		Instant thirdAndAfter = second.plusSeconds(1);  // 12:00:02
//		
//		for(int i = 0; i< 10000; i++)
//		{	
//			Instant now = Instant.now(); 	
//			System.out.println("now : " + now);		
//		}
//		
//		System.out.println("first : " + first);
//		System.out.println("first2 : " + first2);
//		System.out.println("thirdAndAfter : " + thirdAndAfter.plusSeconds(3));
//		System.out.println("compare : " + thirdAndAfter.compareTo(first));
//	}
}
