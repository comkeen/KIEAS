package kr.or.kpew.kieas.common;

import java.util.Observable;

/**
 * 통합경보시스템의 각 요소(발령대, 게이트웨이, 경보시스템 및 수신연계모듈)를 구현하는 데 필요한 공통 요소를 추상화(요약)한 클래스이다.
 * 통합경보시스템 참조모델은 공통적으로 MVC 디자인패턴과 관찰자(Observer) 디자인패턴을 바탕으로 설계되었다. 따라서 Observable을 상속받는다.
 * @author raychani
 *
 */
public abstract class IntegratedEmergencyAlertSystem extends Observable implements IOnMessageHandler{

	protected static final int DELAY = 1000;
	
	protected volatile long messageCount;

	public IntegratedEmergencyAlertSystem(Profile profile) {
		setProfile(profile);
	}
	
	abstract public void setProfile(Profile profile);
	abstract public Profile getProfile();
	
	/**
	 * CAP 메시지의 식별자를 생성하기 위한 메소드이다.
	 * @return CAP 메시지의 식별자
	 */
	public String createMessageId() {
		return getProfile().getAgency() + String.format("%010d", messageCount++);
	}
}
