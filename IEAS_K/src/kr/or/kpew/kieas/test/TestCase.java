package kr.or.kpew.kieas.test;

import static org.junit.Assert.assertEquals;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.issuer.view.resource.TableModel;

import org.junit.Test;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.Area;
import com.google.publicalerts.cap.CapValidator;
import com.google.publicalerts.cap.CapXmlBuilder;
import com.google.publicalerts.cap.Info;
import com.google.publicalerts.cap.Point;
import com.google.publicalerts.cap.Resource;

public class TestCase
{
	//간단한  CAP메시지 생성 테스트
	@Test
	public void SimpleCapBuildTest()
	{				
		//구글 CAP 라이브러리를 맵핑하여 만든 CAP메시지빌더 클래스를 이용한 기본 CAP메시지 생성
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		
		//Alert 요소 작성
		kieasMessageBuilder.setIdentifier("Identifier");
		kieasMessageBuilder.setSender("Sender");
		kieasMessageBuilder.setSent("2016-02-14T00:54:14+09:00");
		kieasMessageBuilder.setStatus(KieasMessageBuilder.ACTUAL);
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ALERT);
		kieasMessageBuilder.setScope(KieasMessageBuilder.PUBLIC);
		String result1 = kieasMessageBuilder.getMessage();
		
		//구글 CAP 라이브러리를 이용한 기본 CAP메시지 생성
		//Alert 요소 작성
		Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
				.setIdentifier("Identifier")
				.setSender("Sender")
				.setSent("2016-02-14T00:54:14+09:00")
				.setStatus(Alert.Status.ACTUAL)
				.setMsgType(Alert.MsgType.ALERT)
				.setScope(Alert.Scope.PUBLIC)
				.buildPartial();
		CapXmlBuilder xmlBuilder = new CapXmlBuilder();		
		String result2 = xmlBuilder.toXml(alert);
		
		System.out.println("-- Simple Cap Build Test --");
//		System.out.println("Using KieasMessageBuilder : \n" + result1);
//		System.out.println("Using GoogleCapLibrary : \n" + result2);
		
		assertEquals(true, result1.equals(result2));
	}
	
	@Test
	public void FullCapBuildTest()
	{
		//구글 CAP 라이브러리를 맵핑하여 만든 CAP메시지빌더 클래스를 이용한 CAP메시지 생성
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		
		//Alert 요소 작성
		kieasMessageBuilder.setIdentifier("Identifier");
		kieasMessageBuilder.setSender("Sender");
		kieasMessageBuilder.setSent("2016-02-14T00:54:14+09:00");
		kieasMessageBuilder.setStatus(KieasMessageBuilder.ACTUAL);
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ALERT);
		kieasMessageBuilder.setScope(KieasMessageBuilder.RESTRICTED);
		
		//Info 요소 작성
		//InfoElementSetter(int infoIndex, String value)
		kieasMessageBuilder.setLanguage(0, "ko-KR");
		kieasMessageBuilder.setCategory(0, KieasMessageBuilder.MET);
		kieasMessageBuilder.setEvent(0, "Event"); 
		kieasMessageBuilder.setUrgency(0, KieasMessageBuilder.UNKNOWN);
		kieasMessageBuilder.setSeverity(0, KieasMessageBuilder.UNKNOWN);
		kieasMessageBuilder.setCertainty(0, KieasMessageBuilder.UNKNOWN);

		//Resource 요소 작성
		//ResourceElementSetter(int infoIndex, int resourceIndex, String value)
		kieasMessageBuilder.setResourceDesc(0, 0, "Resource Description");
		kieasMessageBuilder.setMimeType(0, 0, "Mime Type");

		//Area 요소 작성
		//AreaElementSetter(int infoIndex, int areaIndex, String value)
		kieasMessageBuilder.setAreaDesc(0, 0, "Area Description");

		String result1 = kieasMessageBuilder.getMessage();
		
		//구글 CAP 라이브러리를 이용한 CAP메시지 생성
		//Alert 요소 작성
		Alert alert = Alert.newBuilder().setXmlns(CapValidator.CAP_LATEST_XMLNS)
				.setIdentifier("Identifier")
				.setSender("Sender")
				.setSent("2016-02-14T00:54:14+09:00")
				.setStatus(Alert.Status.ACTUAL)
				.setMsgType(Alert.MsgType.ALERT)
				.setScope(Alert.Scope.RESTRICTED)
				.buildPartial();
		//Info 요소 작성
		Info info = Info.newBuilder()
				.setLanguage("ko-KR")
				.addCategory(Info.Category.MET)
				.setEvent("Event") 
				.setUrgency(Info.Urgency.UNKNOWN_URGENCY)
				.setSeverity(Info.Severity.UNKNOWN_SEVERITY)
				.setCertainty(Info.Certainty.UNKNOWN_CERTAINTY)
				.buildPartial();
		//Resource 요소 작성
		Resource resource = Resource.newBuilder()
				.setResourceDesc("Resource Description")
				.setMimeType("Mime Type")
				.buildPartial();
		//Area 요소 작성
		Area area = Area.newBuilder()
				.setAreaDesc("Area Description")
				.buildPartial();
		
		//Info 요소에 Resource, Area 요소 추가
		info = Info.newBuilder(info)
				.addResource(resource)
				.addArea(area)
				.buildPartial();
		//Alert 요소에 Info 요소 추가
		alert = Alert.newBuilder(alert).addInfo(info).build();
		
		//Alert 객체를 xml로 변환
		CapXmlBuilder xmlBuilder = new CapXmlBuilder();		
		String result2 = xmlBuilder.toXml(alert);

		System.out.println("-- Full Cap Build Test --");
//		System.out.println("Using KieasMessageBuilder : \n" + result1);
//		System.out.println("Using GoogleCapLibrary : \n" + result2);
		
		assertEquals(true, result1.equals(result2));
	}
	
	@Test
	public void MultiElementBuildTest()
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		
		//Alert 요소 작성
		kieasMessageBuilder.setIdentifier("Identifier");
		kieasMessageBuilder.setSender("Sender");
		kieasMessageBuilder.setSent("2016-02-14T00:54:14+09:00");
		kieasMessageBuilder.setStatus(KieasMessageBuilder.ACTUAL);
		kieasMessageBuilder.setMsgType(KieasMessageBuilder.ALERT);
		kieasMessageBuilder.setScope(KieasMessageBuilder.PUBLIC);
		
		//Info 요소 작성
		//InfoElementSetter(int infoIndex, String value)
		kieasMessageBuilder.setLanguage(0, "infoIndex0 : ko-KR");
		kieasMessageBuilder.setLanguage(1, "infoIndex1 : ko-KR");
		kieasMessageBuilder.setCategory(0, KieasMessageBuilder.MET);
		kieasMessageBuilder.setEvent(2, "infoIndex2 : Event");
		kieasMessageBuilder.setEvent(3, "infoIndex3 : Event");
		kieasMessageBuilder.setUrgency(0, KieasMessageBuilder.UNKNOWN);
		kieasMessageBuilder.setSeverity(0, KieasMessageBuilder.UNKNOWN);
		kieasMessageBuilder.setCertainty(0, KieasMessageBuilder.UNKNOWN);

		//Area 요소 작성
		//AreaElementSetter(int infoIndex, int areaIndex, String value)
		kieasMessageBuilder.setAreaDesc(0, 0, "infoIndex0, areaIndex0, Area Description");
		kieasMessageBuilder.setAreaDesc(1, 2, "infoIndex1, areaIndex2, Area Description");
		kieasMessageBuilder.setAreaDesc(3, 1, "infoIndex3, areaIndex1, Area Description");
		//PolygonElementSetter(int infoIndex, int areaIndex, String value)
		Point[] points = new Point[3];
		points[0] = Point.newBuilder().setLatitude(1).setLongitude(1).build();
		points[1] = Point.newBuilder().setLatitude(2).setLongitude(2).build();
		points[2] = Point.newBuilder().setLatitude(3).setLongitude(3).build();
		kieasMessageBuilder.setPolygon(0, 0, 0, points);
		kieasMessageBuilder.setPolygon(0, 0, 1, points);
		
		System.out.println("-- Multi Element Build Test --");
		System.out.println(kieasMessageBuilder.getMessage());
	}

}
