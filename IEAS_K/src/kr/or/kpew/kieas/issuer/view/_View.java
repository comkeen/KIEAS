package kr.or.kpew.kieas.issuer.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.or.kpew.kieas.issuer.controller._Controller;


public class _View implements Observer
{
	private _Controller controller;

	private AlertGeneratorPanel alertGeneratorPanel;
	private AlertLogPanel alerterLogPanel;

	private JFrame mainFrame;
	private JTabbedPane mainTabbedPane;

	/**
	 * Main Frame과 각 포함되는 View Component 초기화.
	 * @param alerterActionListener 이벤트 리스너
	 */
	public _View()
	{
		initLookAndFeel();
		this.alertGeneratorPanel = new AlertGeneratorPanel();
		this.alerterLogPanel = new AlertLogPanel(this);

		init();
	}

	private void init()
	{
		this.mainFrame = new JFrame();
		mainFrame.setSize(1024, 768);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(controller);

		this.mainTabbedPane = new JTabbedPane();
		Container container = mainFrame.getContentPane();
		container.add(mainTabbedPane);

		mainTabbedPane.addTab("CAP Generator Panel", alertGeneratorPanel.getPanel());
//		mainTabbedPane.addTab("Alert Log Panel", alerterLogPanel.getPanel());			

		mainFrame.setVisible(true);
	}

	private void initLookAndFeel()
	{
		try 
		{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} 
		catch (UnsupportedLookAndFeelException e) 
		{
			e.printStackTrace();
		}
	}

	public void addInfoIndexPanel()
	{
		alertGeneratorPanel.addInfoIndexPanel();
	}

	public void addResourceIndexPanel()
	{
		alertGeneratorPanel.addResourceIndexPanel();
	}
	
	public void addAreaIndexPanel()
	{
		alertGeneratorPanel.addAreaIndexPanel();
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
		case "AlertGenerator":
			alertGeneratorPanel.updateView(target, value);
			break;
		case "AlertLogManager":
			alerterLogPanel.addAlertTableRow(value);
			break;
		default:
			System.out.println("there is no such a view " + view);
			break;
		}
	}
	
	public void updateView(String view, String target, List<String> value)
	{
		switch (view)
		{
		case "AlerterDataBasePanel":
//			alerterDatabasePanel.getQueryResult(value);
			break;
		default:
			System.out.println("there is no such a view " + view);
			break;
		}
	}

	public String getLoadTextField()
	{
		return alertGeneratorPanel.getLoadTextField();
	}

	public String getSaveTextField()
	{
		return alertGeneratorPanel.getSaveTextField();
	}

	public void setId(String name) 
	{
		mainFrame.setTitle(name + "발령대");
	}

	public Component getFrame() 
	{
		return this.mainFrame;
	}

	public void setTextArea(String message)
	{
		alertGeneratorPanel.setTextArea(message);
	}

	public String getTextArea()
	{
		return alertGeneratorPanel.getTextArea();
	}
	
	public void addAlertTableRow(String message)
	{			
		alerterLogPanel.addAlertTableRow(message);
	}

	
	public void update(Observable obs, Object value)
	{
		String name = obs.getClass().getSimpleName();
		System.out.println(name + " invoke update value : " + value.toString());
		
		alertGeneratorPanel.setTextArea(value.toString());
		
//		switch (name)
//		{
//		case "":			
//			break;
//		default:
//			break;
//		}

	}
	
	public void addController(ActionListener controller)
	{
		System.out.println("View      : adding controller"); 
	}

	public void setController(_Controller controller)
	{
		this.controller = controller;
		alertGeneratorPanel.setController(controller);
		alerterLogPanel.setController(controller);
	}
}