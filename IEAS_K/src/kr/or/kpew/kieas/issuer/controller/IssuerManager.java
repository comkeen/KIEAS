package kr.or.kpew.kieas.issuer.controller;

import kr.or.kpew.kieas.issuer.model._Model;
import kr.or.kpew.kieas.issuer.view._View;

public class IssuerManager
{
	_Model model;
	_View view;
	_Controller controller;
	
	
	public IssuerManager()
	{
		this.model = new _Model();
		this.view = new _View();
		this.controller = new _Controller();

		init();
	}
	
	private void init()
	{
		model.addObserver(view);
//		model.addAlertGeneratorObserver(view.getAlertGeneratorPanel());
		
		view.setController(controller);
		controller.addModel(model);
		controller.addView(view);
	}
}
