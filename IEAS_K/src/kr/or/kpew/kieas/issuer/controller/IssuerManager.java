package kr.or.kpew.kieas.issuer.controller;

import kr.or.kpew.kieas.issuer.model.IssuerModel;
import kr.or.kpew.kieas.issuer.view.View;

public class IssuerManager
{
	IssuerModel model;
	View view;
	IssuerController controller;
	
	
	public IssuerManager()
	{
		this.model = new IssuerModel();
		this.view = new View();
		this.controller = new IssuerController();

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
