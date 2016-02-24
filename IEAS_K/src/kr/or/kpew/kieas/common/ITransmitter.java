package kr.or.kpew.kieas.common;

public interface ITransmitter
{
	public void setMqServer(String ip);
	
	public void addReceiver(String myDestination);
	public void removeReceiver(String target);
	public void sendMessage(String message, String destination);
	
	public void openConnection();
	public void closeConnection();		
}
