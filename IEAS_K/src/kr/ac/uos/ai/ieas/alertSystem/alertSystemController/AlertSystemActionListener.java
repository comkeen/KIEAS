package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AlertSystemActionListener implements ActionListener, ItemListener, WindowListener
{	
	private AlertSystemView view;
	
	
	public AlertSystemActionListener(AlertSystemView view)
	{
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{

	}
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if(e.getStateChange() == ItemEvent.SELECTED)
		{
			view.selectTopic(e.getItem().toString());				
		}	
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e)
	{
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
}
