package kr.or.kpew.kieas.issuer.controller;

import kr.or.kpew.kieas.issuer.model._Model;
import kr.or.kpew.kieas.issuer.view._View;

public class IssuerManager
{
	_Controller controller;
	_View view;
	_Model model;
	
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
		
		view.setController(controller);
		controller.setModel(model);
		controller.setView(view);
	}
}
