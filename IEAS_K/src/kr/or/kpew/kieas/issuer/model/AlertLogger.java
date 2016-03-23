package kr.or.kpew.kieas.issuer.model;

import java.util.HashMap;
import java.util.Map;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.gateway.view.AlertMessageTable.Responses;

public class AlertLogger
{
	private Map<String, MessageAckPair> alertLogMap;
	private Map<String, String> ackLogMap;

	private IKieasMessageBuilder kieasMessageBuilder;
	private IssuerModel issuerModel;


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

	public void saveAlertLog(String message)
	{		
		kieasMessageBuilder.parse(message);
		String identifier = kieasMessageBuilder.getIdentifier();
		MessageAckPair pair = new MessageAckPair(message, Responses.NACK.toString());
		alertLogMap.put(identifier, pair);
		
		issuerModel.updateTable(pair);
	}

	public void saveAckLog(String message)
	{
		kieasMessageBuilder.parse(message);
		if(!ackLogMap.containsKey(kieasMessageBuilder.getIdentifier()))
		{
			ackLogMap.put(kieasMessageBuilder.getIdentifier(), message);			
		}
		
		String references = kieasMessageBuilder.getReferences();
		String[] tokens = references.split(",");
		String sender = tokens[0];		
		String identifier = tokens[1];
		String sent = tokens[2];
	
		if(alertLogMap.containsKey(identifier))
		{
			MessageAckPair pair = alertLogMap.get(identifier);
			String state = pair.getState();			
			
			switch (state)
			{
			case "NACK":
				pair.setState(Responses.ACK.toString());
				break;
			case "ACK":
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
			System.out.println("AlertLogger: there is no alertLogMap key - " + identifier);
		}
	}

	public String loadAlertLog(String identifier)
	{
		return alertLogMap.get(identifier).getMessage();
	}

	public String loadAlertLogState(String identifier)
	{
		return alertLogMap.get(identifier).getState();
	}
	
	public String loadAckLog(String identifier)
	{
		return ackLogMap.get(identifier);
	}

	public void receiveAck(String identifier, String state)
	{
		alertLogMap.get(identifier).setState(state);
	}

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
