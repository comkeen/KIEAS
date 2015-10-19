package kr.ac.uos.ai.ieas.main;

import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.alerter.alerterModel._AlerterModelManager;
import kr.ac.uos.ai.ieas.alerter.alerterView._AlerterTopView;


public class AlerterMain{

	public AlerterMain() {
		
		_AlerterController alerterController = new _AlerterController();
		_AlerterModelManager alerterModel = new _AlerterModelManager(alerterController);
		_AlerterTopView alerterTopView = new _AlerterTopView(alerterController);
		
		alerterController.addModel(alerterModel);
		alerterController.addView(alerterTopView);
		alerterController.initAlerterController();
	}
}
