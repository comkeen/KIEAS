package kr.ac.uos.ai.ieas.resource;

public interface ITransmitter
{
	public void openConnection();
	public void closeConnection();
	
	public void sendMessage(String message, String destination);
	
	public void setReceiver(String id);
	public void setMqServer(String ip);
}
