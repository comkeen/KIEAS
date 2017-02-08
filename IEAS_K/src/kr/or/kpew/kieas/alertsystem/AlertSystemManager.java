package kr.or.kpew.kieas.alertsystem;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.network.ITransmitter;

public class AlertSystemManager
{
	private AlertSystemModel model;
	private AlertSystemView view;
	private AlertSystemController controller;
	
	
	public AlertSystemManager(ITransmitter transmitter, AlertSystemProfile profile, Profile gateway)
	{
		model = new AlertSystemModel(transmitter, profile);
		view = new AlertSystemView();
		controller = new AlertSystemController();
		
		model.addObserver(view);
		model.setGatewayName(gateway.getSender());
		view.setController(controller);
		controller.setModel(model);
		controller.setView(view);
		
		view.init();
		model.init();		
		
		view.show();
	}
}
