package kr.or.kpew.kieas.common;

public interface IOnMessageHandler
{
	/**
	 * 수신한 메시지를 처리하기 위한 메소드
	 * @param message
	 */
	public void onMessage(String message);
	
	/** 
	 * 수신한 등록 요청에 대해 서버는 등록을, 클라이언트는 등록 결과를 처리하기 위한 메소드
	 * @param sender
	 * @param address
	 */
	public void onRegister(String sender, String address);
}
