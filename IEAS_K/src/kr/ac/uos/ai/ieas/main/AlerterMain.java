package kr.ac.uos.ai.ieas.main;

import kr.ac.uos.ai.ieas.alerterController.AlerterController;
import kr.ac.uos.ai.ieas.alerterModel.AlerterModel;
import kr.ac.uos.ai.ieas.alerterView.AlerterLogPane;
import kr.ac.uos.ai.ieas.alerterView.AlerterViewTabbedPanel;


public class AlerterMain{

	public AlerterMain() {
		
		AlerterController alerterController = new AlerterController();
		AlerterModel alerterModel = new AlerterModel(alerterController);
		AlerterViewTabbedPanel alerterViewTabbedPanel = new AlerterViewTabbedPanel(alerterController);
		
		alerterController.addModel(alerterModel);
		alerterController.addView(alerterViewTabbedPanel);
		alerterController.initAlerterController();
	}
}
