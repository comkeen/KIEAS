package kr.or.kpew.kieas.network;

import kr.or.kpew.kieas.common.IOnMessageHandler;

public interface IClientTransmitter
{	
	/**
	 * 통신 기능을 사용하기 위한 초기화를 진행한다. 이 인터페이스의 구현체는 메시지 송수신을 담당한다.
	 * @param id 이 시스템의 주소. cap의 <sender>를 이용한다.
	 * @param target 메시지 송신시 대상이 되는 주소
	 */
	public void init(String id, String destination);

	/**
	 * 커넥션을 닫는다.
	 */
	public void close();

	/**
	 * 메시지를 수신한 경우 처리하기 위한 메소드가 정의되어 있는 인터페이스를 지정한다.
	 * @param handler 메소드가 정의되어 있는 인터페이스
	 */
	public void setOnMessageHandler(IOnMessageHandler handler);

	
	/**
	 * init에서 정의한 대상으로 메시지를 전달한다.
	 * @param message 전달할 메시지.
	 */
	public void send(String message);

}
