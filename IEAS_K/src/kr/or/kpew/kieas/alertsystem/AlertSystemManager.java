package kr.or.kpew.kieas.alertsystem;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.network.ITransmitter;

public class AlertSystemManager
{
	private AlertSystemModel model;
	private AlertSystemView view;
	private AlertSystemController controller;
	
	
	public AlertSystemManager(ITransmitter transmitter, AlertSystemProfile profile)
	{
		model = new AlertSystemModel(transmitter, profile);
		view = new AlertSystemView();
		controller = new AlertSystemController();
		
		model.addObserver(view);
		view.setController(controller);
		controller.setModel(model);
		controller.setView(view);
		
		view.init();
		model.init();		
		
		view.show();
	}
}
