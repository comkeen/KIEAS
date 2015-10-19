package kr.ac.uos.ai.ieas.alerter.alerterController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class AleterViewActionListener implements ActionListener, ListSelectionListener
{	
	private _AlerterController controller;
	
	
	public AleterViewActionListener(_AlerterController alerterController)
	{
		this.controller = alerterController;
	}

	
	/**
	 * event.getActionCommand()로 버튼 액션을 식별하여 처리한다.
	 * "LoadCapDraft" : "/cap/" 위치에 있는 지정된 이름의 Cap Draft를 로드한다.
	 * "SaveCap" : AlerterCapGeneratePanel.TextArea의 내용을 "/cap/" 위치에 지정된 이름으로 저장한다.
	 * "Apply" : AlerterCapGeneratePanel의 AlertPanel과 InfoPanel의 내용을 CapFormat으로 변환하여 TextArea에 적용한다.
	 * "Query" : 지정된 EventCode에 의하여 Database에 쿼리를 보낸다.
	 * "Add Info" : AlerterCapGeneratePanel.InfoPanel에서 InfoIndexPanel을 추가한다.
	 * 
	 */
	public void actionPerformed(ActionEvent event)
	{
		String actionCommand = event.getActionCommand();
		
		if (actionCommand.equals("GenerateCap"))
		{
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
		else if(actionCommand.equals("Query"))
		{
			controller.getQueryResult();
		}
		else if(actionCommand.equals("Add Info"))
		{
			controller.addInfoIndexPanel();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		controller.selectTableEvent();
	}
}
