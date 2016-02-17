package kr.ac.uos.ai.ieas.resource;

public interface ITransmitter
{
	public void setMqServer(String ip);
	public void setQueueReceiver(String queueDestination);
	public void setTopicReceiver(String topicDestination);
	
	public void openConnection();
	public void closeConnection();
	public void stopConnection();
	
	public void sendQueueMessage(String message, String queueDestination);
	public void sendTopicMessage(String message, String topicDestination);	
}
