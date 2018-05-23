package kr.or.kpew.kieas.alertsystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AlertSystemController implements ActionListener, WindowListener, ItemListener
{	
	private AlertSystemView view;
	private AlertSystemModel model;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand().toString()) {
		case "Clear":
			view.clear();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
//		model.selectTopic(AlertSystemModel.ALERT_SYSTEM_TYPE, e.getItem().toString());
	}	

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e)
	{
//		model.close();
		view.systemExit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

	public void setModel(AlertSystemModel model)
	{
		this.model = model;
	}

	public void setView(AlertSystemView view) {
		this.view = view;
	}
}
