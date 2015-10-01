package kr.ac.uos.ai.ieas.alertSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AlertSystemActionListener implements ActionListener, ItemListener{

	private AlertSystemView view;
	
	public AlertSystemActionListener(AlertSystemView view) {
		this.view = view;
	}
	@Override
	public void actionPerformed(ActionEvent event) {

	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			view.selectTopic(e.getItem().toString());				
		}	
	}	
}
