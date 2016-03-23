package kr.or.kpew.kieas.issuer.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.or.kpew.kieas.issuer.controller.IssuerController;
import kr.or.kpew.kieas.issuer.model.AlertLogger.MessageAckPair;


public class AlertGeneratorPanel
{	
	private static final String LOAD_CAP_BUTTON = "Load";
	private static final String SAVE_CAP_BUTTON = "Save";
	private static final String APPLY_BUTTON = "Apply";
	private static final String SEND_BUTTON = "Send";

	private JScrollPane alertGenerateScrollPanel;
	private Box mainPanel;

	private Vector<Object> mViewComponents;
	private HashMap<String, JComponent> panelComponenets;
	private List<JButton> buttons;
	
	private Box buttonPane;
	private JTextField mSaveTextField;
	private JTextField mLoadTextField;

	private JTabbedPane capTabPanel;
	private CapElementPanel capElementPanel;
	private Box capElementBox;
	private Box alertBox;

	private AlertLogPanel alertLogPanel;

	public AlertGeneratorPanel()
	{
		init();
	}

	private void init()
	{
		this.mViewComponents = new Vector<>();
		this.panelComponenets = new HashMap<>();
		this.buttons = new ArrayList<>();
		this.mainPanel = Box.createVerticalBox();
		
		mainPanel.add(initButtonPanel());
		mainPanel.add(initCapPanelContainer());
		
		mViewComponents.addElement(panelComponenets);

		this.alertGenerateScrollPanel = new JScrollPane(mainPanel);
	}

	private JComponent initButtonPanel()
	{
		this.buttonPane = Box.createHorizontalBox();
		
		JButton loadCapDraftButton = createButton(LOAD_CAP_BUTTON);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		buttonPane.add(loadCapDraftButton);
				
		JButton saveCapButton = createButton(SAVE_CAP_BUTTON);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		buttonPane.add(saveCapButton);
		

		JButton alertApplyButton = createButton(APPLY_BUTTON);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		buttonPane.add(alertApplyButton);
		
		JButton sendButton = createButton(SEND_BUTTON);
		sendButton.setActionCommand(SEND_BUTTON);
		buttonPane.add(sendButton);
		
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return buttonPane;
	}
	
	private JComponent initCapPanelContainer()
	{
		this.capTabPanel = new JTabbedPane();
		this.capElementBox = Box.createVerticalBox();
		this.alertBox = Box.createVerticalBox();
		
		capElementBox.add(initCapElementPanel());
		alertBox.add(initAlertBox());
		
		capTabPanel.addTab("CAP 요소", capElementBox);
		capTabPanel.addTab("경보 로그", alertBox);
		
		return capTabPanel;
	}

	private JComponent initCapElementPanel()
	{
		this.capElementPanel = new CapElementPanel();
		return capElementPanel.getPanel();
	}

	private JComponent initAlertBox()
	{
		this.alertLogPanel = new AlertLogPanel();
		return alertLogPanel.getPanel();
	}
	
	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		buttons.add(button);
		return button;
	}

	public JScrollPane getPanel()
	{
		return this.alertGenerateScrollPanel;
	}
	
	public void updateCapElementView(String value)
	{
		capElementPanel.setCapAlertPanel(value);
		alertLogPanel.setTextArea(value);
		
		JScrollBar vertical = alertGenerateScrollPanel.getVerticalScrollBar();
		vertical.getY();
		vertical.setValue(vertical.getMinimum());
	}
	
	public String getLoadTextField()
	{
		return mLoadTextField.getText();
	}

	public String getSaveTextField()
	{
		return mSaveTextField.getText();
	}

	public String getTextArea()
	{
		return ((JTextArea) panelComponenets.get(IssuerView.TEXT_AREA)).getText();
	}
	
	public String getCapElement()
	{		
		return capElementPanel.getCapElement();
	}

	public void setTextArea(String message)
	{
		alertLogPanel.setTextArea(message);
	}

	public void addController(IssuerController controller)
	{
		for (JButton button : buttons)
		{
			button.addActionListener(controller);
		}
		capElementPanel.addController(controller);
		alertLogPanel.addController(controller);
	}
	
	public void removeController(IssuerController controller)
	{
		for (JButton button : buttons)
		{
			button.removeActionListener(controller);
		}
		capElementPanel.removeController(controller);
		alertLogPanel.removeController(controller);
	}

	public void addInfoIndexPanel()
	{
		capElementPanel.addInfoIndexPanel();
	}
	
	public void updateIdentifier(String message)
	{
		capElementPanel.setIdentifier(message);		
	}

	public void updateTable(MessageAckPair value)
	{
		alertLogPanel.updateTable(value);
	}
	
	public String getSelectedRowIdentifier()
	{
		return alertLogPanel.getSelectedRowIdentifier();
	}

}

