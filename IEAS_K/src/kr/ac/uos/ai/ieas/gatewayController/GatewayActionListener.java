package kr.ac.uos.ai.ieas.gatewayController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class GatewayActionListener implements ActionListener, ListSelectionListener{
	
	private GatewayController gatewayController;

	
	public GatewayActionListener(GatewayController gatewayController) {
		this.gatewayController = gatewayController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals("OpenGateway")){
			gatewayController.openGateway();
		} else if(actionCommand.equals("CloseGateway")) {
			gatewayController.closeGateway();			
		} else if(actionCommand.equals("ClearLog")) {
			gatewayController.clearLog();			
		} 
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		gatewayController.selectTableEvent();
	}

}
