package kr.or.kpew.kieas.issuer.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.or.kpew.kieas.common.Item;
import kr.or.kpew.kieas.issuer.controller.IssuerController;


public class IssuerView implements Observer
{	
	public static final String TEXT_AREA = "TextArea";
	public static final String TEXT_FIELD = "TextField";
	public static final String COMBO_BOX = "ComboBox";
	
	private AlertGeneratorPanel alertGeneratorPanel;
	private AlertLogPanel alerterLogPanel;

	private JFrame mainFrame;
	private JTabbedPane mainTabbedPane;

	/**
	 * Main Frame과 각 포함되는 View Component 초기화.
	 * @param alerterActionListener 이벤트 리스너
	 */
	public IssuerView()
	{
		initLookAndFeel();
		this.alertGeneratorPanel = new AlertGeneratorPanel();
		this.alerterLogPanel = new AlertLogPanel(this);

		init();
		System.out.println("View Instantiated");
	}

	private void init()
	{
		this.mainFrame = new JFrame();
		mainFrame.setTitle("기상청");
		mainFrame.setPreferredSize(new Dimension(1028, 768));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.mainTabbedPane = new JTabbedPane();
		Container container = mainFrame.getContentPane();
		container.add(mainTabbedPane);

		mainTabbedPane.addTab("CAP Generator Panel", alertGeneratorPanel.getPanel());
//		mainTabbedPane.addTab("Alert Log Panel", alerterLogPanel.getPanel());			

		mainFrame.pack();
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

	@Override
	public void update(Observable observable, Object value)
	{
		Item item = (Item) value;
		System.out.println(observable.getClass().getSimpleName() + " invoke update : " + item.getKey());

		switch (item.getKey())
		{
		case TEXT_AREA:
			alertGeneratorPanel.setTextArea(item.getValue());
			alertGeneratorPanel.updateView(item.getValue());
			break;

		default:
			break;
		};
	}
	
	public void addController(IssuerController controller)
	{
		mainFrame.addWindowListener(controller);
		alertGeneratorPanel.addController(controller);
		alerterLogPanel.addController(controller); 
	}
	
	public void removeController(IssuerController controller)
	{
		mainFrame.removeWindowListener(controller);
		alertGeneratorPanel.removeController(controller);
		alerterLogPanel.removeController(controller);
	}
}