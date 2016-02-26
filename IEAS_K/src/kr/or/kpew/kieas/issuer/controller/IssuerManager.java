package kr.or.kpew.kieas.issuer.controller;

import kr.or.kpew.kieas.issuer.model.Model;
import kr.or.kpew.kieas.issuer.view.View;

public class IssuerManager
{
	Model model;
	View view;
	Controller controller;
	
	
	public IssuerManager()
	{
		this.model = new Model();
		this.view = new View();
		this.controller = new Controller();

		init();
	}
	
	private void init()
	{
		model.addObserver(view);
		
		view.addController(controller);
		controller.addModel(model);
		controller.addView(view);
	}
}
