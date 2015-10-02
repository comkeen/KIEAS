package kr.ac.uos.ai.ieas.main;

import kr.ac.uos.ai.ieas.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.alerterModel.AlerterModel;
import kr.ac.uos.ai.ieas.alerterView._AlerterTopView;


public class AlerterMain{

	public AlerterMain() {
		
		_AlerterController alerterController = new _AlerterController();
		AlerterModel alerterModel = new AlerterModel(alerterController);
		_AlerterTopView alerterTopView = new _AlerterTopView(alerterController);
		
		alerterController.addModel(alerterModel);
		alerterController.addView(alerterTopView);
		alerterController.initAlerterController();
	}
}
