package kr.or.kpew.kieas.network;

import kr.or.kpew.kieas.common.IOnMessageHandler;

public interface ITransmitter
{	
	/**
	 * 각종 통신을 위한 초기화를 수행한다.
	 */
	public void init(String destination);
	
	/**
	 * 주어진 대상으로 데이터를 전달한다.
	 * @param target 전달할 목적지
	 * @param message 전달할 데이터
	 */
	public void sendTo(String target, String message);

	/**
	 * 클라이언트가 접속하는 것에 비동기적으로 대응한다.
	 */
	public void addReceiver(String destination);

	/**
	 * 메시지를 수신한 경우 처리하기 위한 메소드가 정의되어 있는 인터페이스를 지정한다.
	 * @param handler 메소드가 정의되어 있는 인터페이스
	 */
	public void setOnMessageHandler(IOnMessageHandler handler);
	
	public void open();
	public void close();
}
