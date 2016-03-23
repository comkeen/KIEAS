package kr.or.kpew.kieas.gateway.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kr.or.kpew.kieas.gateway.model.GatewayModel;
import kr.or.kpew.kieas.gateway.view.GatewayView;


public class GatewayController implements ActionListener, ListSelectionListener, WindowListener
{	
	private GatewayModel model;
	private GatewayView view;
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals("OpenGateway")){
			model.openGateway();
		} else if(actionCommand.equals("CloseGateway")) {
			model.closeGateway();			
			view.systemExit();
		} else if(actionCommand.equals("ClearLog")) {
			view.clearLog();			
		} 
	}
	

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		view.selectTableEvent();
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e)
	{
		view.systemExit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

	
	public void setModel(GatewayModel model) {
		this.model = model;
		
	}

	public void setView(GatewayView view) {
		this.view = view;
		
	}


}
