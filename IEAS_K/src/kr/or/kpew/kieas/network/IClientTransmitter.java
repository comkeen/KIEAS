package kr.or.kpew.kieas.network;

import kr.or.kpew.kieas.common.IOnMessageHandler;

public interface IClientTransmitter {
	
	/**
	 * 주어진 대상에 연결한다. 향후 이 대상에게만 데이터를 전송할 수 있다.
	 * @param id 이 시스템의 주소 cap의 <sender>를 이용한다.
	 * @param target 연결하기 위한 대상의 주소
	 */
	public void init(String id, String target);

	public void close();

	/**
	 * 메시지를 수신한 경우 처리하기 위한 메소드가 정의되어 있는 인터페이스를 지정한다.
	 * @param handler 메소드가 정의되어 있는 인터페이스
	 */
	public void setOnMessageHandler(IOnMessageHandler handler);

	
	/**
	 * init에서 정의한 대상으로 데이터를 전달한다.
	 * @param data 전달할 데이터
	 */
	public void send(byte[] data);

}
