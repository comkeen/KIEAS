package kr.or.kpew.kieas.alertsystem;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.network.IClientTransmitter;

public class AlertSystemManager {
	AlertSystemModel model;
	AlertSystemView view;
	AlertSystemController controller;
	
	public AlertSystemManager(IClientTransmitter transmitter, AlertSystemProfile profile) {
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
