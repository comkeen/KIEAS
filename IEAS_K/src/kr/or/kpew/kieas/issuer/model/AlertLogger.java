package kr.or.kpew.kieas.issuer.model;

import java.util.HashMap;
import java.util.Map;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasConstant;
import kr.or.kpew.kieas.gateway.view.AlertMessageTable.Responses;

/**
 * 발령하는 경보메시지와 수신하는 수신응답메시지를 저장한다. 경보로그를 표현하는 테이블의 정보를 갱신한다.
 * @author byun-ai
 *
 */
public class AlertLogger
{
	private Map<String, MessageAckPair> alertLogMap;
	private Map<String, String> ackLogMap;

	private IKieasMessageBuilder kieasMessageBuilder;
	private IssuerModel issuerModel;

	private static int count = 0;

	public AlertLogger()
	{
		init();
	}

	public void init()
	{
		this.alertLogMap = new HashMap<String, MessageAckPair>();
		this.ackLogMap = new HashMap<String, String>();		
		this.kieasMessageBuilder = new KieasMessageBuilder();
	}

	/**
	 * 발령한 경보메시지를 저장한다.
	 * @param message 저장되는 cap 형식의 메시지.
	 */
	public void saveAlertLog(String message)
	{		
		kieasMessageBuilder.parse(message);
		String identifier = kieasMessageBuilder.getIdentifier();
		MessageAckPair pair = new MessageAckPair(message, Responses.NACK.toString());
		alertLogMap.put(identifier, pair);
		
		issuerModel.updateTable(pair);
	}

	/**
	 * 수신한 수신응답메시지를 저장한다.
	 * 수신한 수신응답메시지의 종류에 따라 경보로그 테이블의 수신응답 상태를 갱신한다.
	 * @param message 저장되는 cap 형식의 메시지
	 */
	public void saveAckLog(String message)
	{		
		String c = Integer.toString(count++);
		String path = "cap/out" + c + ".xml";
		issuerModel.writeCap(path, message);		
		
		kieasMessageBuilder.parse(message);
		if(!ackLogMap.containsKey(kieasMessageBuilder.getIdentifier()))
		{
			ackLogMap.put(kieasMessageBuilder.getIdentifier(), message);			
		}
		
		String references = kieasMessageBuilder.getReferences();
		String[] parsedReferences = kieasMessageBuilder.parseReferences(references);
//		String sender = parsedReferences[0];		
		String identifier = parsedReferences[1];
//		String sent = parsedReferences[2];
	
		if(alertLogMap.containsKey(identifier))
		{
			MessageAckPair pair = alertLogMap.get(identifier);
			String state = pair.getState();
			
			switch (state)
			{
			case KieasConstant.NACK:
				pair.setState(Responses.ACK.toString());
				break;
			case KieasConstant.ACK:
				pair.setState(Responses.COMP.toString());				
				break;
			default:
				pair.setState(Responses.NACK.toString());	
				break;
			}
			alertLogMap.replace(identifier, pair);
			issuerModel.updateTable(pair);
		}
		else
		{
			System.out.println("AlertLogger: There is no alertLogMap key - " + identifier);
		}
	}

	/**
	 * 저장된 경보메시지를 불러온다.
	 * @param identifier 저장된 메시지를 식별하는데 사용된다.
	 * @return
	 */
	public String loadAlertLog(String identifier)
	{
		return alertLogMap.get(identifier).getMessage();
	}

	/**
	 * 저장된 경보메시지의 수신응답 상태를 불러온다.
	 * @param identifier
	 * @return
	 */
	public String loadAlertLogState(String identifier)
	{
		return alertLogMap.get(identifier).getState();
	}
	
	/**
	 * 저장된 수신응답메시지를 불러온다.
	 * @param identifier
	 * @return
	 */
	public String loadAckLog(String identifier)
	{
		return ackLogMap.get(identifier);
	}

	/**
	 * 경보메시지와 수신응답 상태를 표현하기 위한 pair 자료구조.
	 * @author byun-ai
	 *
	 */
	public class MessageAckPair
	{		
		String message;
		String state;

		public MessageAckPair(String message, String state)
		{
			this.message = message;
			this.state = state;
		}

		public String getMessage()
		{
			return this.message;
		}

		public String getState()
		{
			return this.state;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public void setState(String state)
		{
			this.state = state;
		}
	}

	public void addModel(IssuerModel issuerModel)
	{
		this.issuerModel = issuerModel;
	}
}
