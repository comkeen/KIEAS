package kr.or.kpew.kieas.alertsystem;

public class AlertSystemManager {
	AlertSystemModel model;
	AlertSystemView view;
	AlertSystemController controller;
	
	public AlertSystemManager() {
		model = new AlertSystemModel();
		view = new AlertSystemView();
		controller = new AlertSystemController();
		
		model.addObserver(view);
		view.setController(controller);
		controller.setModel(model);
		controller.setView(view);
		
		view.init();
		model.init();
	}
	
	public void run() {
		
	}
	
	public static void main(String[] args) {
		AlertSystemManager manager = new AlertSystemManager();
		
		manager.run();
	}
}
