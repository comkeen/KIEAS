package kr.or.kpew.kieas.issuer.controller;

import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.issuer.model.IssuerModel;
import kr.or.kpew.kieas.issuer.view.IssuerView;
import kr.or.kpew.kieas.network.ITransmitter;

public class IssuerManager
{
	IssuerModel model;
	IssuerView view;
	IssuerController controller;
	
	/**
	 * 경보발령대는 크게 Model과 View, Controller로 구성된다.
	 * 이 클래스에서는 경보발령대를 구성하는 클래스들을 생성하여 서로 관계를 이어주는 역할을 한다.
	 * Controller는 Model과 View에 대해 직접 연결되어 있으며 View에서 사용자의 입력과 Model 혹은 View의 변화를 제어한다.
	 * Model과 View 사이는 Observer Pattern이 적용되어 있어 간접적으로 관계되어있다.
	 * 
	 * @param transmitter 발령대에서 사용할 통신방식을 규정한다. 통신방식은 TCP/IP와 JMS 두가지 방법을 활용할 수 있게 고안되었으나 현재는 JMS만을 사용한다.
	 * @param profile 경보발령대에서 사용하는 발령대프로필은 프로필은 상속하여 구현되어있다. 프로필은 통신에서 라우팅 기능을 위해 사용된다. 프로필에 대한 정보는 kr.or.kpew.kieas.common.Profile을 참고하라.
	 */
	public IssuerManager(ITransmitter transmitter, IssuerProfile profile, Profile gateway)
	{
		init(transmitter, profile, gateway);
	}
	
	public void init(ITransmitter transmitter, IssuerProfile profile, Profile gateway)
	{
		this.model = new IssuerModel(transmitter, profile);
		this.view = new IssuerView();
		this.controller = new IssuerController();
		
		//Model에 갱신되는 값을 표현할 View를 지정한다. 
		model.addObserver(view);
		model.setGatewayName(gateway.getSender());
		
		//View에서 사용자 입력을 처리 하기 위한 Controller를 지정한다.
		view.addController(controller);
		
		//Controller에서 제어한 Model과 View를 지정한다.
		controller.setModel(model);
		controller.setView(view);
	}
}
