package kr.or.kpew.kieas.main;

import kr.or.kpew.kieas.issuer.controller._Controller;
import kr.or.kpew.kieas.issuer.model._Model;
import kr.or.kpew.kieas.issuer.view._View;

public class AlerterMain
{
	private static AlerterMain main;
	
	public static AlerterMain getInstance()
	{
		if(main == null)
		{
			main = new AlerterMain();
		}
		return main;
	}
	
	private AlerterMain()
	{
		//create Model and View
		_Controller myController = new _Controller();
		_Model myModel 	= new _Model(myController);
		_View myView 	= new _View(myController);

		//tell Model about View. 
		myModel.addObserver(myView);
		/*	
			init model after view is instantiated and can show the status of the model
			(I later decided that only the controller should talk to the model
			and moved initialisation to the controller (see below).)
		*/
		//uncomment to directly initialise Model
		//myModel.setValue(start_value);	

		//create Controller. tell it about Model and View, initialise model
		myController.addModel(myModel);
		myController.addView(myView);

		//tell View about Controller 
		myView.addController(myController);
		//and Model, 
		//this was only needed when the view inits the model
		//myView.addModel(myModel);
	}

	public static void main(String[] args) 
	{
		getInstance();		
	}	
}
