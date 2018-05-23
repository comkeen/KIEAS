package kr.or.kpew.kieas.gateway.controller;

import kr.or.kpew.kieas.gateway.view.GatewayView;
import kr.or.kpew.kieas.network.ITransmitter;
import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.gateway.model.GatewayModel;


public class GatewayManager
{
	GatewayModel model;
	
	public GatewayManager(ITransmitter transmitter, Profile profile)
	{
		model = new GatewayModel(transmitter, profile);		
		GatewayView view = new GatewayView();
		GatewayController controller = new GatewayController();
		
		model.addObserver(view);		
		view.setController(controller);		
		controller.setModel(model);
		controller.setView(view);
		
		view.init();
		model.init();		
	}

	public void registAlertSystem(AlertSystemProfile asProfile)
	{
		model.registerAlertSystem(asProfile);
	}

	public void registIssuer(IssuerProfile iProfile)
	{
		model.registerIssuer(iProfile);
	}
}
