
package kr.or.kpew.kieas.issuer.controller;

import java.util.List;

import javax.swing.JOptionPane;

import kr.or.kpew.kieas.issuer.model._AlerterModel;
import kr.or.kpew.kieas.issuer.view._AlerterTopView;
import kr.or.kpew.kieas.issuer.view.resource.AlertLogTableModel;


public class _AlerterController
{ 
	private static _AlerterController controller;
	
	private _AlerterTopView topView;
	private _AlerterModel modelManager;
	private AleterViewActionListener viewActionListener;
	

	public static _AlerterController getInstance()
	{
		if (controller == null)
		{
			controller = new _AlerterController(controller);
		}
		return controller;
	}
	
	
	/**
	 * Model과 View 초기화.
	 * @param alerterController 
	 */		
	private _AlerterController(_AlerterController alerterController)
	{		
		this.viewActionListener = new AleterViewActionListener(this);
		this.modelManager = new _AlerterModel(this);
		this.topView = new _AlerterTopView(this, viewActionListener);
		
		init();
	}
	
	private void init()
	{
//		this.setID();
//		alerterTopView.setId(alerterId);
	}
	
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
			modelManager.closeConnection();
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
		modelManager.loadCap(topView.getLoadTextField());
	}
	
	/**
	 * CapGeneratePanel에서 작성된 Cap 메시지를 파일로 저장할 때 호출됨.
	 */
	public void saveCap()
	{
		modelManager.saveCap(topView.getSaveTextField());
	}

	public void applyAlertElement()
	{
		modelManager.applyAlertElement(topView.getAlertElement());
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
		return modelManager.getAlertTableModel();
	}

	public String getAlertMessage(String identifier)
	{
		return modelManager.getAlertMessage(identifier);
	}

	public void setId(String id)
	{
		topView.setId(id);
	}

	public void sendMessage() 
	{
		modelManager.sendMessage();
	}

	public void setID()
	{
		modelManager.setID();
	}

	public void registerToGateway()
	{
		modelManager.registerToGateway();
	}


	public void setClear()
	{
		topView.setTextArea("");
	}
}
