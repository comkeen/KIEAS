
package kr.or.kpew.kieas.issuer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kr.or.kpew.kieas.issuer.model.Model;
import kr.or.kpew.kieas.issuer.view._View;


public class _Controller implements ActionListener, ListSelectionListener, WindowListener
{ 	
	private _View view;
	private Model model;
	
	private List<_View> views;
	private List<Model> models;
	
	
	public _Controller()
	{		
		this.models = new ArrayList<Model>();
		this.views = new ArrayList<_View>();
	}

	public void addModel(Model model)
	{
		models.add(model);
//		this.model = model;
	}

	public void addView(_View view)
	{
		views.add(view);
//		this.view = view;
	}
	
	public void removeModel(Model model)
	{
		models.remove(model);
	}
	
	public void removeView(_View view)
	{
		models.remove(view);
	}
	
	/**
	 * event.getActionCommand()로 버튼 액션을 식별하여 처리한다.
	 * "Load Cap" : "~/cap/" 위치에 있는 지정된 이름의 Cap Draft를 로드한다.
	 * "Save Cap" : AlerterCapGeneratePanel.TextArea의 내용을 "/cap/" 위치에 지정된 이름으로 저장한다.
	 * "Apply" : AlerterCapGeneratePanel의 AlertPanel과 InfoPanel의 내용을 CapFormat으로 변환하여 TextArea에 적용한다.
	 * "Add Info" : AlerterCapGeneratePanel.InfoPanel에서 InfoIndexPanel을 추가한다.
	 * 
	 */
	public void actionPerformed(ActionEvent event)
	{
		String actionCommand = event.getActionCommand();
		System.out.println("action triggered : " + actionCommand);
		switch (actionCommand)
		{		
		case "Send":
			model.sendMessage();
			return;
		case "Load Cap":
			model.loadCap(view.getLoadTextField());
			return;
		case "Save Cap":
			model.writeCap(view.getSaveTextField(), view.getTextArea());
			return;
		case "Set Id":
			model.generateAndSetID();
			return;
		case "Apply":
			//TODO
			model.applyMessage(view.getTextArea());
			return;
		case "Register":
			model.registerToGateway();
			return;
			
		case "Clear":
			view.setTextArea("");
			return;			
		case "Add Info":
			view.addInfoIndexPanel();
			return;
		case "Add Resource":
			view.addResourceIndexPanel();
			return;
		case "Add Area":
			view.addAreaIndexPanel();
			return;
			
		default:
			System.out.println("There is no such a actionCommand : " + actionCommand);
			return;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
//		controller.selectTableEvent();
	}


	@Override
	public void windowActivated(WindowEvent e) {}


	@Override
	public void windowClosed(WindowEvent e) {}

	
	
	/**
	 * 프로그램 종료시 네트워크 접속 종료
	 */
	@Override
	public void windowClosing(WindowEvent e)
	{
		System.out.println("Program closing");
		String question = "표준경보발령대 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";
		
		if (JOptionPane.showConfirmDialog(view.getFrame(),
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
	    {
			model.closeConnection();
	        System.exit(0);
	    }
		else
		{
			System.out.println("cancel exit program");
		}
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
