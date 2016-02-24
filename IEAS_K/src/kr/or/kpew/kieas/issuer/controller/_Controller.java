
package kr.or.kpew.kieas.issuer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kr.or.kpew.kieas.issuer.model._Model;
import kr.or.kpew.kieas.issuer.view._View;
import kr.or.kpew.kieas.issuer.view.resource.AlertLogTableModel;


public class _Controller implements ActionListener, ListSelectionListener, WindowListener
{ 	
	private _View topView;
	private _Model model;
	
	
	public _Controller()
	{		
		
	}
	
	//Joe I should be able to add any model/view with the correct API
	//but here I can only add Model/View
	public void addModel(_Model m){
		System.out.println("Controller : adding model");
		this.model = m;
	} //addModel()

	public void addView(_View v){
		System.out.println("Controller : adding view");
		this.topView = v;
	} //addView()
	
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
			sendMessage();
			return;
		case "Load Cap":
			loadCap();
			return;
		case "Save Cap":
			saveCap();
			return;
		case "Apply":
			applyAlertElement();
			return;
		case "Add Info":
			addInfoIndexPanel();
			return;
		case "Add Resource":
			addResourceIndexPanel();
			return;
		case "Add Area":
			addAreaIndexPanel();
			return;
		case "Register":
			registerToGateway();
			return;
		case "Set Id":
			setID();
			return;
		case "Clear":
			setClear();
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


	@Override
	public void windowClosing(WindowEvent e)
	{
		systemExit();
	}


	@Override
	public void windowDeactivated(WindowEvent e) {}


	@Override
	public void windowDeiconified(WindowEvent e) {}


	@Override
	public void windowIconified(WindowEvent e) {}


	@Override
	public void windowOpened(WindowEvent e) {}
	
	public void systemExit()
	{
		String question = "표준경보발령대 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";
		
		if (JOptionPane.showConfirmDialog(topView.getFrame(),
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
	
	/**
	 * CapGeneratePanel에 Cap 메시지를 불러올 때 호출됨.
	 */
	public void loadCap()
	{
		model.loadCap(topView.getLoadTextField());
	}
	
	/**
	 * CapGeneratePanel에서 작성된 Cap 메시지를 파일로 저장할 때 호출됨.
	 */
	public void saveCap()
	{
		model.saveCap(topView.getSaveTextField());
	}

	public void applyAlertElement()
	{
		model.applyAlertElement(topView.getAlertElement());
	}


	/**
	 * View 클래스인 CapGeneratePanel의 InfoPanel에 포함된 "Add Info"버튼에 의해 호출된다.
	 * InfoPanel의 Tab이 하나 추가 된다.
	 */
	public void addInfoIndexPanel() 
	{
		topView.addInfoIndexPanel();
	}

	/**
	 * View 클래스인 CapGeneratePanel의 ResourcePanel에 포함된 "Add Resource"버튼에 의해 호출된다.
	 * ResourcePanel의 Tab이 하나 추가 된다.
	 */
	public void addResourceIndexPanel() 
	{
		topView.addResourceIndexPanel();
	}
	
	/**
	 * View 클래스인 CapGeneratePanel의 AreaPanel에 포함된 "Add Area"버튼에 의해 호출된다.
	 * AreaPanel의 Tab이 하나 추가 된다.
	 */
	public void addAreaIndexPanel()
	{
		topView.addAreaIndexPanel();
	}
	
	/**
	 * Model의 데이터 값이 바뀌었을 경우 View 갱신을 위해 Model에 의해 호출된다.
	 * @param view 갱신되어야 하는 View 클래스 이름
	 * @param target 값이 표시되는 Component의 이름
	 * @param value 표시되는 값
	 */
	public void updateView(String view, String target, String value)
	{
		topView.updateView(view, target, value);
	}
	
	public void updateView(String view, String target, List<String> value)
	{
		topView.updateView(view, target, value);
	}

	public void setTextArea(String message)
	{
		topView.setTextArea(message);
	}

	public AlertLogTableModel getAlertTableModel()
	{
		return model.getAlertTableModel();
	}

	public String getAlertMessage(String identifier)
	{
		return model.getAlertMessage(identifier);
	}

	public void setId(String id)
	{
		topView.setId(id);
	}

	private void sendMessage() 
	{
		model.sendMessage();
	}

	private void setID()
	{
		model.setID();
	}

	private void registerToGateway()
	{
		model.registerToGateway();
	}


	private void setClear()
	{
		topView.setTextArea("");
	}
}
