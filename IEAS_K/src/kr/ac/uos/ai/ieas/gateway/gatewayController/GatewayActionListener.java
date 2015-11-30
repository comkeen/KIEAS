package kr.ac.uos.ai.ieas.gateway.gatewayController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class GatewayActionListener implements ActionListener, ListSelectionListener, WindowListener
{	
	private GatewayController controller;

	
	public GatewayActionListener(GatewayController gatewayController) {
		this.controller = gatewayController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals("OpenGateway")){
			controller.openGateway();
		} else if(actionCommand.equals("CloseGateway")) {
			controller.closeGateway();			
		} else if(actionCommand.equals("ClearLog")) {
			controller.clearLog();			
		} 
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		controller.selectTableEvent();
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e)
	{
		controller.systemExit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

}
