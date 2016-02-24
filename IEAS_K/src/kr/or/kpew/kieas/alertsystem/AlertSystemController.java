package kr.or.kpew.kieas.alertsystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AlertSystemController implements ActionListener, WindowListener
{	
	private AlertSystemModel controller;
	
	
	public AlertSystemController(AlertSystemModel alertSystemController)
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
