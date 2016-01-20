package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AlertSystemActionListener implements ActionListener, WindowListener
{	
	private AlertSystemController controller;
	
	
	public AlertSystemActionListener(AlertSystemController alertSystemController)
	{
		this.controller = alertSystemController;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand().toString())
		{
		case "Clear":
			controller.clear();
			break;
		case "Register":
			controller.registerToGateway();			
			break;
		case "setId":
			controller.setID();
			break;
		default:
			System.out.println("default");
			break;
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e)
	{
		System.out.println("win close");
		controller.exit();
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
