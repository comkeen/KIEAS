package kr.ac.uos.ai.ieas.main;

import kr.ac.uos.ai.ieas.alerterController.AlerterController;
import kr.ac.uos.ai.ieas.alerterModel.AlerterModel;
import kr.ac.uos.ai.ieas.alerterView.AlerterTopView;


public class AlerterMain{

	public AlerterMain() {
		
		AlerterController alerterController = new AlerterController();
		AlerterModel alerterModel = new AlerterModel(alerterController);
		AlerterTopView alerterTopView = new AlerterTopView(alerterController);
		
		alerterController.addModel(alerterModel);
		alerterController.addView(alerterTopView);
		alerterController.initAlerterController();
	}
}
