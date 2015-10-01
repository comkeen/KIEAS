package kr.ac.uos.ai.ieas.alerterView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;

import kr.ac.uos.ai.ieas.abstractClass.AbstractView;
import kr.ac.uos.ai.ieas.alerterController.AlerterController;
import kr.ac.uos.ai.ieas.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.gatewayView.GatewayDataPane;
import kr.ac.uos.ai.ieas.gatewayView.GatewayInfoPane;
import kr.ac.uos.ai.ieas.gatewayView.GatewayLogPane;
import kr.ac.uos.ai.ieas.resource.IeasConfiguration;


public class AlerterViewTabbedPanel extends AbstractView{

	private AlerterController alerterController;
	private JFrame frame;
	private JTabbedPane mainTabbedPane;
	private JTextArea textArea;

	private AlerterLogPane alerterLogPanel;
	

	public AlerterViewTabbedPanel(AlerterController alerterController) {

		this.alerterController = alerterController;
		
		initLookAndFeel();
		initFrame("alertViewPanel");
	}

	public void modelPropertyChange(PropertyChangeEvent evt){
		
		System.out.println("trigger property change");
		switch (evt.getPropertyName()){
		
		case AlerterController.ALERT_TEXTAREA_TEXT_PROPERTY:
			textArea.setText(evt.getNewValue().toString());
			System.out.println("propertychange");
		default:
			
		}
	}
	
	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initFrame(String name) {
		this.frame = new JFrame(name);
		frame.setSize(1600, 900);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.mainTabbedPane = new JTabbedPane();
		Container container = frame.getContentPane();
		container.add(mainTabbedPane);
				
		this.alerterLogPanel = AlerterLogPane.getInstance(alerterController);
		mainTabbedPane.addTab("경보로그", alerterLogPanel.getLogPane());		
		

		frame.setVisible(true);
	}
}