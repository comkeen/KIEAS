package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;


public class _AlerterTopView
{
	private static _AlerterTopView alerterTopView;
	private JFrame mainFrame;
	private JTabbedPane mainTabbedPane;

	private AlerterLogPanel alerterLogPanel;
	private AlerterCapGeneratePanel alerterCapGeneratePanel;
	private AlerterDatabasePanel alerterDatabasePanel;

	public static _AlerterTopView getInstance(AleterViewActionListener alerterActionListener)
	{
		if (alerterTopView == null)
		{
			alerterTopView = new _AlerterTopView(alerterActionListener);
		}
		return alerterTopView;
	}

	/**
	 * Main Frame과 각 포함되는 View Component 초기화.
	 * @param alerterActionListener 이벤트 리스너
	 */
	private _AlerterTopView(AleterViewActionListener alerterActionListener)
	{
		initLookAndFeel();
		this.alerterCapGeneratePanel = AlerterCapGeneratePanel.getInstance(alerterActionListener);
		this.alerterDatabasePanel = AlerterDatabasePanel.getInstance(alerterActionListener);

		initFrame("alertViewPanel");
	}

	private void initFrame(String name) {
		this.mainFrame = new JFrame(name);
		mainFrame.setSize(1200, 900);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.mainTabbedPane = new JTabbedPane();
		Container container = mainFrame.getContentPane();
		container.add(mainTabbedPane);

		mainTabbedPane.addTab("CAP", alerterCapGeneratePanel.getPanel());
		mainTabbedPane.addTab("Database", alerterDatabasePanel.getPanel());

		//	this.alerterLogPanel = AlerterLogPanel.getInstance(alerterActionListener);
		//	mainTabbedPane.addTab("경보로그", alerterLogPanel.getLogPanel());

		mainFrame.setVisible(true);
	}

	private void initLookAndFeel() {
		try 
		{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} 
		catch (UnsupportedLookAndFeelException e) 
		{
			e.printStackTrace();
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
		//alerterCapGeneratePanel.applyAlertElement();
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

	public void addResourceIndexPanel()
	{
		alerterCapGeneratePanel.addResourceIndexPanel();
	}
	
	public void addAreaIndexPanel() {
		alerterCapGeneratePanel.addAreaIndexPanel();
	}	
	
	/**
	 * Model의 데이터 값이 바뀌었을 경우 View 갱신을 위해 Model에 의해 호출된다.
	 * 
	 * @param view 갱신되어야 하는 View 클래스 이름
	 * @param target 값이 표시되는 Component의 이름
	 * @param value 표시되는 값
	 */
	public void updateView(String view, String target, String value)
	{
		switch (view)
		{
		case "AlerterCapGeneratePanel":
			alerterCapGeneratePanel.updateView(target, value);
			break;

		default:
			System.out.println("there is no such a view " + view);
			break;
		}
	}

	public String getLoadTextField()
	{
		return alerterCapGeneratePanel.getLoadTextField();
	}

	public String getSaveTextField()
	{
		return alerterCapGeneratePanel.getSaveTextField();
	}
}