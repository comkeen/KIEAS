
package kr.or.kpew.kieas.issuer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kr.or.kpew.kieas.issuer.model.IssuerModel;
import kr.or.kpew.kieas.issuer.view.IssuerView;

/**
 * 경보발령대의 Controller는 각종 Listener 인터페이스를 구현하여 이벤트에 따른 처리를 담당한다. 
 * @author byun-ai
 *
 */
public class IssuerController implements ActionListener, ListSelectionListener, WindowListener
{ 		
	private IssuerView view;
	private IssuerModel model;
	
	public void setModel(IssuerModel model)
	{
		this.model = model;
	}

	public void setView(IssuerView view)
	{
		this.view = view;
	}
	
	
	/**
	 * 버튼 이벤트를 처리한다. 액션커맨드에 의해 이벤트를 식별하여 해당하는 작업을 수행한다.
	 * 
	 */
	public void actionPerformed(ActionEvent event)
	{
		String actionCommand = event.getActionCommand();
		
		String path;
		switch (actionCommand)
		{		
		case "Send":
			model.setAlertMessage(view.getCapElement());
			model.sendMessage();
			return;
		case "Load":
			path = view.showFileOpenDialog();
			if(path != null)
				model.loadCap(path);
			return;
		case "Save":
			path = view.showFileOpenDialog();
			if(path != null)
				model.writeCap(path, view.getTextArea());
			return;
		case "Set Id":
//			model.generateAndSetID();
			return;
		case "Apply":
			model.setAlertMessage(view.getCapElement());
			return;
		case "Register":
//			model.registerToGateway();
			return;
			
		case "Clear":
			view.setTextArea("");
			return;
		case "Info Add":
			view.addInfoIndexPanel();
			return;
		default:
			System.out.println("AO: There is no such a actionCommand : " + actionCommand);
			return;
		}
	}

	/**
	 * View의 JTable에서 발생한 이벤트를 처리한다.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		String identifier = view.getSelectedRowIdentifier();
		model.setSelectedAlertLog(identifier);
	}


	@Override
	public void windowActivated(WindowEvent e) {}


	@Override
	public void windowClosed(WindowEvent e) {}

	
	
	/**
	 * 윈도우 창 종료에 대한 프로그램 종료 작업을 수행한다.
	 */
	@Override
	public void windowClosing(WindowEvent e)
	{
		String question = "표준경보발령대 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";
				
		if (JOptionPane.showConfirmDialog(view.getFrame(),
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
	    {
//			model.closeConnection();
	        System.exit(0);
	    }
		else
		{
			System.out.println("AO: cancel exit program");
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
