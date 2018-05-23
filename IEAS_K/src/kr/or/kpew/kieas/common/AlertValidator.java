package kr.or.kpew.kieas.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 이 클래스는 내가 만든게 아니라 잘 모름.
 * 참조문서의 "TTAK.OT-06.0055/R1"의 6.3절 응답 코드 및 설명에서 서술하는 응답 코드에 관한 구현.
 * @author comkeen
 *
 */
public class AlertValidator
{
	public enum AckCode
	{
		ReceiveComplete("000", "메시지 수신 확인"),
		VerificationComplete("200", "메시지 유효성 확인"),
		ValidationComplete("400", "메시지 인증 확인"),
		ResourceValidationComplete("600", "리소스 인증 확인"),
		AlertServiceComplete("800", "경보 서비스 확인"),
		
		ReceiveError("010", "메시지 수신 오류"),
		VerificationError("210", "메시지 유효성 오류"),
		ValidationError("410", "메시지 인증 오류"),
		ResourceValidationError("610", "리소스 인증 오류"),
		AlertServiceError("810", "경보 서비스 오류");
		
		private String code;
		private String description;
		
		private AckCode(String code, String description)
		{
			this.code = code;
			this.description = description;
		}
		
		public String getCode()
		{
			return code;
		}
		
		public String getDescription()
		{
			return description;
		}
	}
	
	private Map<String, String> receivedAlertMap;
	
	
	public AlertValidator()
	{
		this.receivedAlertMap = new HashMap<>();
	}
	
	public boolean[] fullValidationMessage(String message)
	{
		boolean[] results = new boolean[6];
		
		results[0] = verifyDuplication(message);
		results[1] = verifyMessage(message);
		results[2] = validateMessage(message);
		results[3] = validateResource(message);
		results[4] = validateAlertService(message);
		results[5] = validateGeoCode(message);
		
		return results;
	}
	
	public AckCode getAckCode(boolean[] validationResults)
	{
		if(validationResults[1])
		{
			if(validationResults[2])
			{
				if(validationResults[3])
				{
					if(validationResults[4])
					{
						return AckCode.AlertServiceComplete;
					}
					return AckCode.ResourceValidationComplete;
				}
				return AckCode.ValidationComplete;
			}
			return AckCode.VerificationComplete;
		}
		return AckCode.VerificationError;
	}
	
	public boolean validateAlertService(String message)
	{
		return true;
	}

	public boolean validateResource(String message)
	{
		return true;
	}

	public boolean validateMessage(String message)
	{
		return true;
	}

	public boolean verifyMessage(String message)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		return true;
	}

	public boolean verifyDuplication(String message)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		String identifier = kieasMessageBuilder.getIdentifier();
		if(!receivedAlertMap.containsKey(identifier))
		{
			receivedAlertMap.put(identifier, message);
			System.out.println("AS: verifyDuplication Alert : New Alert");
			return true;
		}
		else
		{
			System.out.println("AS: verifyDuplication Alert : Duplicated Alert");
			return false;
		}
	}
	
	public boolean validateGeoCode(String message)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		int infoCount = kieasMessageBuilder.getInfoCount();
		if(infoCount > 0)
		{
			for(int i = 0; i < infoCount; i++)
			{
				int areaCount = kieasMessageBuilder.getAreaCount(i);
				if(areaCount > 0)
				{
					for(int j = 0; j < areaCount; j++)
					{
						String geoCode = kieasMessageBuilder.getGeoCode(i, j);
						if(geoCode.length() > 0)
						{
							return true;
						}
					}
				}
			}
		}
		//경보시스템 지역 확인 기능은 테스트용이기 때문에 구현하지 않았다. 전부 true를 반환한다.
		return true;
	}
}
