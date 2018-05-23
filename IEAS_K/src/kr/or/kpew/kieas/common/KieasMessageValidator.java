package kr.or.kpew.kieas.common;

import org.xml.sax.SAXParseException;

import com.google.publicalerts.cap.CapException;
import com.google.publicalerts.cap.CapValidator;
import com.google.publicalerts.cap.CapXmlParser;
import com.google.publicalerts.cap.NotCapException;

/**
 * 구글 CAP 라이브러리의 CapValidator를 이용한 CAP 메시지 검증을 위한 클래스
 * @author comkeen
 *
 */
public class KieasMessageValidator
{
	private CapValidator capValidator;
	private CapXmlParser capXmlParser;

	public KieasMessageValidator()
	{
		init();
	}
	
	public void init()
	{
		this.capValidator = new CapValidator();
		this.capXmlParser = new CapXmlParser(false);
	}
	
	/**
	 * xml CAP 메시지의 유효성 검사.
	 * 
	 * @return boolean
	 */
	//TODO 지금 validate은 단순히 CAP 1.2 확인으로 보임. KIEAS를 따르는지에 대한 스키마 이상의 검사가 필요함
	public boolean validateMessage(String message) {
		try {
			capValidator.validateAlert(capXmlParser.parseFrom(message));
			System.out.println("Cap Message Validation Complete.");
			return true;
		} catch (CapException | NotCapException | SAXParseException e) {
			System.out.println("Cap Message Validation Fail.");
			return false;
		}
	}
}
