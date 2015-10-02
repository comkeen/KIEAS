package kr.ac.uos.ai.ieas.alerterController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AleterViewActionListener implements ActionListener
{	
	private _AlerterController controller;
	
	
	public AleterViewActionListener(_AlerterController alerterController)
	{
		this.controller = alerterController;
	}

	public void actionPerformed(ActionEvent event)
	{
		String actionCommand = event.getActionCommand();
		
		if (actionCommand.equals("GenerateCap"))
		{
			System.out.println("generatebutton");
			controller.generateCap();
		}
		else if(actionCommand.equals("Send"))
		{
			controller.sendMessage();			
		}
		else if(actionCommand.equals("TextAreaSend"))
		{
			controller.sendTextAreaMessage();			
		}
		else if(actionCommand.equals("ConnectServer"))
		{
			controller.connectToServer();
		}
		else if(actionCommand.equals("LoadCapDraft"))
		{
			controller.loadCapDraft();
		}
		else if(actionCommand.equals("SaveCap"))
		{
			controller.saveCap();
		}
		else if(actionCommand.equals("Apply"))
		{
			controller.applyAlertElement();
		}
	}
}
