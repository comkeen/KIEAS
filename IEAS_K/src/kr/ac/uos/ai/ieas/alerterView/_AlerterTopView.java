package kr.ac.uos.ai.ieas.alerterView;

import java.awt.Container;
import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.ac.uos.ai.ieas.abstractClass.AbstractView;
import kr.ac.uos.ai.ieas.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.alerterController.AleterViewActionListener;


public class _AlerterTopView extends AbstractView{

	private _AlerterController alerterController;
	private JFrame frame;
	private JTabbedPane mainTabbedPane;
	private JTextArea textArea;

	private AlerterLogPanel alerterLogPanel;
	private AlerterCapElementPanel alerterCapElementPanel;
	private AleterViewActionListener alerterActionListener;
	

	public _AlerterTopView(_AlerterController alerterController) {

		this.alerterController = alerterController;
		this.alerterActionListener = new AleterViewActionListener(alerterController);
		
		initLookAndFeel();
		initFrame("alertViewPanel");
	}

	public void modelPropertyChange(PropertyChangeEvent evt){
		
		System.out.println("trigger property change");
		switch (evt.getPropertyName()){
		
		case _AlerterController.ALERT_TEXTAREA_TEXT_PROPERTY:
			alerterLogPanel.getTextArea().setText(evt.getNewValue().toString());
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
				
		this.alerterLogPanel = AlerterLogPanel.getInstance(alerterActionListener);
		mainTabbedPane.addTab("경보로그", alerterLogPanel.getLogPanel());
		
		this.alerterCapElementPanel = AlerterCapElementPanel.getInstance(alerterActionListener);
		mainTabbedPane.addTab("CAP", alerterCapElementPanel.getCapElementPanel());
		
		frame.setVisible(true);
	}

	public void receiveGatewayAck(String identifier) {
		alerterLogPanel.receiveGatewayAck(identifier);
	}

	public void receiveAlertSystemAck(String identifier) {
		alerterLogPanel.receiveAlertSystemAck(identifier);		
	}

	public void addAlertTableRow(String id, String event, String addresses) {
		alerterLogPanel.addAlertTableRow(id, event, addresses);		
	}

	public void loadCapDraft() {
		alerterCapElementPanel.loadCapDraft();
	}

	public void saveCap() {
		alerterCapElementPanel.saveCap();
	}

	public void applyAlertElement() {
		alerterCapElementPanel.applyAlertElement();
	}
}