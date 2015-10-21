package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.ac.uos.ai.ieas.abstractClass.AbstractView;
import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;


public class _AlerterTopView extends AbstractView
{
	private JFrame mainFrame;
	private JTabbedPane mainTabbedPane;

	private AlerterLogPanel alerterLogPanel;
	private AlerterCapGeneratePanel alerterCapGeneratePanel;
	private AleterViewActionListener alerterActionListener;
	private AlerterDatabasePanel alerterDatabasePanel;

	/**
	 * _AlerterTopView 생성자.
	 * AlerterViewActionListener 초기화
	 * MainFrame 초기화
	 * 
	 * @param alerterController
	 */
	public _AlerterTopView(_AlerterController alerterController)
	{
		this.alerterActionListener = new AleterViewActionListener(alerterController);
		initFrame("alertViewPanel");
	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initFrame(String name) {
		this.mainFrame = new JFrame(name);
		mainFrame.setSize(1200, 900);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initLookAndFeel();

		this.mainTabbedPane = new JTabbedPane();
		Container container = mainFrame.getContentPane();
		container.add(mainTabbedPane);

		this.alerterCapGeneratePanel = AlerterCapGeneratePanel.getInstance(alerterActionListener);
		mainTabbedPane.addTab("CAP", alerterCapGeneratePanel.getPanel());

		this.alerterDatabasePanel = AlerterDatabasePanel.getInstance(alerterActionListener);
		mainTabbedPane.addTab("Database", alerterDatabasePanel.getPanel());

//		this.alerterLogPanel = AlerterLogPanel.getInstance(alerterActionListener);
//		mainTabbedPane.addTab("경보로그", alerterLogPanel.getLogPanel());

		mainFrame.setVisible(true);
	}

	public void modelPropertyChange(PropertyChangeEvent evt)
	{
		System.out.println(evt.getPropertyName());
		switch (evt.getPropertyName())
		{
		case _AlerterController.ALERT_TEXTAREA_TEXT_PROPERTY:
			alerterLogPanel.getTextArea().setText(evt.getNewValue().toString());
		case _AlerterController.ALERTER_CAPGENERATEPANEL_TEXTAREA_PROPERTY:
			alerterCapGeneratePanel.setTextArea(evt.getNewValue().toString());
		case _AlerterController.ALERTER_IDENTIFIER_PROPERTY:
			alerterCapGeneratePanel.setIdentifierValue(evt.getNewValue().toString());
		case _AlerterController.ALERTER_SENDER_PROPERTY:
			alerterCapGeneratePanel.setSenderValue(evt.getNewValue().toString());
		case _AlerterController.ALERTER_STATUS_PROPERTY:
			alerterCapGeneratePanel.setStatusValue(evt.getNewValue().toString());
		default:

		}
	}

	public void receiveGatewayAck(String identifier) {
		alerterLogPanel.receiveGatewayAck(identifier);
	}

	public void receiveAlertSystemAck(String identifier)
	{
		alerterLogPanel.receiveAlertSystemAck(identifier);		
	}

	public void addAlertTableRow(String id, String event, String addresses)
	{
		alerterLogPanel.addAlertTableRow(id, event, addresses);		
	}

	public void applyAlertElement()
	{
//		alerterCapGeneratePanel.applyAlertElement();
	}

	public void selectTableEvent()
	{
		alerterDatabasePanel.selectTableEvent();
	}

	public void getQueryResult(ArrayList<String> results)
	{
		alerterDatabasePanel.getQueryResult(results);
	}

	public String getQuery()
	{
		return alerterDatabasePanel.getQuery();
	}

	public void addInfoIndexPanel()
	{
		alerterCapGeneratePanel.addInfoIndexPanel();
	}
}