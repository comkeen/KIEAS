package kr.ac.uos.ai.ieas.alerter.alerterController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class AleterViewActionListener implements ActionListener, ListSelectionListener, WindowListener
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
		System.out.println("action triggered : " + actionCommand);
		switch (actionCommand)
		{
		case "GenerateCap":
			controller.generateCap();
			return;			
		case "Send":
			controller.sendMessage();
			return;
		case "TextAreaSend":
			controller.sendMessage();
			return;
		case "Load Cap":
			controller.loadCap();
			return;
		case "Save Cap":
			controller.saveCap();
			return;
		case "Insert DB":
			controller.insertDatabase();
			return;
		case "Apply":
			controller.applyAlertElement();
			return;
		case "Query":
			controller.getQueryResult();
			return;
		case "Add Info":
			controller.addInfoIndexPanel();
			return;
		case "Add Resource":
			controller.addResourceIndexPanel();
			return;
		case "Add Area":
			controller.addAreaIndexPanel();
			return;
		case "Load Draft":
			controller.loadDraft();
			return;
		default:
			System.out.println("There is no such a actionCommand : " + actionCommand);
			return;
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
