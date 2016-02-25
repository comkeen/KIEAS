package kr.or.kpew.kieas.issuer.model;

import java.util.HashMap;
import java.util.Map;

public class AlertLogManager
{
	public static final String NACK = "Nack";
	public static final String ACK = "Ack";
	public static final String COMP = "Comp";
	
	private Map<String, MessageAckPair> alertLogMap;
	private Map<String, String> ackLogMap;
	
	
	public AlertLogManager()
	{
		this.alertLogMap = new HashMap<String, MessageAckPair>();
		this.ackLogMap = new HashMap<String, String>();		

		System.out.println("AlertLogManager instantiated");
	}

	public void saveAlertLog(String identifier, String message)
	{		
		alertLogMap.put(identifier, new MessageAckPair(message, NACK));
	}
	
	public void saveAckLog(String identifier, String message)
	{		
		ackLogMap.put(identifier, message);
		String state = alertLogMap.get(identifier).getState();
	
		switch (state)
		{
		case NACK:
			alertLogMap.get(identifier).setState(ACK);			
			break;
		case ACK:
			alertLogMap.get(identifier).setState(COMP);			
			break;

		default:
			alertLogMap.get(identifier).setState(NACK);		
			break;
		}
	}
	
	public String loadAlertLog(String identifier)
	{
		return alertLogMap.get(identifier).getMessage();
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
}
