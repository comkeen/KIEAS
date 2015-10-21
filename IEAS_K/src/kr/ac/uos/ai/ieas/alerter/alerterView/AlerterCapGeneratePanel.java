package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KIEAS_Constant;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class AlerterCapGeneratePanel
{
	private static AlerterCapGeneratePanel alerterCapElementPanel;
	
	private AleterViewActionListener alerterActionListener;
	private KieasMessageBuilder ieasMessage;

	private JScrollPane capGenerateScrollPanel;
	private JPanel capGeneratePanel;

	private JScrollPane textAreaPane;
	private JTextArea textArea;
	
	private JPanel buttonPane;
	private JButton saveCapButton;
	private JButton loadCapDraftButton;
	private JTextField savePathTextField;
	private JTextField loadPathTextField;

	private JPanel alertPanel;
	private HashMap<String, Component> alertComponents;
	private JButton alertApplyButton;

	private JTabbedPane infoPanel;
	private ArrayList<JPanel> infoIndexPanels;
	private ArrayList<HashMap<String, Component>> infoComponents;
	private int infoCounter;


	public static final String TEXT_FIELD = "TextField";
	public static final String COMBO_BOX = "ComboBox";
	public static final String TEXT_AREA = "TextArea";
	

	public static AlerterCapGeneratePanel getInstance(AleterViewActionListener alerterActionListener)
	{
		if (alerterCapElementPanel == null)
		{
			alerterCapElementPanel = new AlerterCapGeneratePanel(alerterActionListener);
		}
		return alerterCapElementPanel;
	}

	private AlerterCapGeneratePanel(AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
		this.ieasMessage = new KieasMessageBuilder();

		initFrame("alertViewPanel");		
	}

	private void initFrame(String name)
	{		
		this.capGeneratePanel = new JPanel();
		this.capGenerateScrollPanel = new JScrollPane(capGeneratePanel);
		capGeneratePanel.setLayout(new BoxLayout(capGeneratePanel, BoxLayout.Y_AXIS));
		
		capGeneratePanel.add(initTextArea());

		capGeneratePanel.add(initButtonPanel());
		
		capGeneratePanel.add(initCapAlertPanel());
//
		capGeneratePanel.add(initCapInfoPanel());
	}

	private Component initTextArea()
	{
		this.textArea = new JTextArea(20, 20);
		this.textAreaPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		textArea.setText("\n");

		return textAreaPane;
	}

	private Component initButtonPanel()
	{
		this.buttonPane = new JPanel();

		this.loadPathTextField = new JTextField();
		buttonPane.add(loadPathTextField, BorderLayout.WEST);

		this.loadCapDraftButton = createButton("LoadCapDraft");
		buttonPane.add(loadCapDraftButton, BorderLayout.WEST);		

		this.saveCapButton = createButton("SaveCap");
		buttonPane.add(saveCapButton, BorderLayout.EAST);

		this.savePathTextField = new JTextField();
		buttonPane.add(savePathTextField, BorderLayout.EAST);

		this.alertApplyButton = createButton("Apply");
		buttonPane.add(alertApplyButton, BorderLayout.EAST);
		
		return buttonPane;
	}

	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		return button;
	}

	private JPanel initCapAlertPanel()
	{
		this.alertPanel = new JPanel();
		alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
		this.alertComponents = new HashMap<>();
		
		alertPanel.add(addBox("Identifier", TEXT_FIELD));
		alertPanel.add(addBox("Sender", TEXT_FIELD));
		alertPanel.add(addBox("Sent", TEXT_FIELD));
		alertPanel.add(addBox("Status", COMBO_BOX));
		alertPanel.add(addBox("MsgType", COMBO_BOX));
		alertPanel.add(addBox("Scope", COMBO_BOX));
		alertPanel.add(addBox("Code", TEXT_FIELD));
		
		
		for (String value : ieasMessage.getCapEnumMap().get(KIEAS_Constant.MSG_TYPE)) {
			((JComboBox<String>) alertComponents.get(KIEAS_Constant.MSG_TYPE)).addItem(value);			
		}
		for (String value : ieasMessage.getCapEnumMap().get(KIEAS_Constant.SCOPE)) {
			((JComboBox<String>) alertComponents.get(KIEAS_Constant.SCOPE)).addItem(value);			
		}		
		
		return alertPanel;
	}
	
	private Box addBox(String labelName, String type)
	{
		Box box = Box.createHorizontalBox();
		
		JLabel label = new JLabel(labelName);
		int offset = (int) (100 - label.getPreferredSize().getWidth());
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		box.add(label);	
		
		switch (type)
		{
		case COMBO_BOX:
			JComboBox<String> comboBox = new JComboBox<>();
			for (String value : ieasMessage.getCapEnumMap().get(labelName))
			{
				comboBox.addItem(value);		
			}
			alertComponents.put(labelName, comboBox);
			box.add(comboBox);
			return box;			
		case TEXT_FIELD:
			JTextField textField = new JTextField();
			alertComponents.put(labelName, textField);
			box.add(textField);
			return box;
		case TEXT_AREA:
			JTextArea textArea = new JTextArea();
			alertComponents.put(labelName, textArea);
			box.add(textArea);
			return box;
		default:
			return box;
		}
	}
	
	private Box addBox(String labelName, String type, int index)
	{
		Box box = Box.createHorizontalBox();
		
		JLabel label = new JLabel(labelName);
		int offset = (int) (100 - label.getPreferredSize().getWidth());
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		box.add(label);		
		
		switch (type)
		{
		case COMBO_BOX:
			JComboBox<String> comboBox = new JComboBox<>();
			for (String value : ieasMessage.getCapEnumMap().get(labelName))
			{
				comboBox.addItem(value);		
			}
			infoComponents.get(index).put(labelName, comboBox);
			box.add(comboBox);
			return box;
		case TEXT_FIELD:
			JTextField textField = new JTextField();
			infoComponents.get(index).put(labelName, textField);
			box.add(textField);
			return box;
		case TEXT_AREA:
			JTextArea textArea = new JTextArea();
			infoComponents.get(index).put(labelName, textArea);
			box.add(textArea);
			return box;
		default:
			return box;
		}
	}
	
	private Component initCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();
		this.infoIndexPanels = new ArrayList<JPanel>();
		this.infoComponents = new ArrayList<HashMap<String, Component>>();

		this.infoCounter = 0;
		addInfoIndexPanel();

		return infoPanel;
	}

	public void addInfoIndexPanel()
	{		
		removeTabPanel();
		JPanel panel = new JPanel();
		infoComponents.add(new HashMap<>());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(addBox("Language", COMBO_BOX, infoCounter));
		panel.add(addBox("Category", COMBO_BOX, infoCounter));
		panel.add(addBox("Event", TEXT_FIELD, infoCounter));
		panel.add(addBox("Urgency", COMBO_BOX, infoCounter));
		panel.add(addBox("Severity", COMBO_BOX, infoCounter));
		panel.add(addBox("Certainty", COMBO_BOX, infoCounter));
		panel.add(addBox("EventCode", COMBO_BOX, infoCounter));
		panel.add(addBox("Effective", TEXT_FIELD, infoCounter));
		panel.add(addBox("SenderName", TEXT_FIELD, infoCounter));
		panel.add(addBox("HeadLine", TEXT_FIELD, infoCounter));
		panel.add(addBox("Description", TEXT_AREA, infoCounter));
		panel.add(addBox("Web", TEXT_FIELD, infoCounter));
		panel.add(addBox("Contact", TEXT_FIELD, infoCounter));

		infoIndexPanels.add(panel);
		infoPanel.addTab("Info" + infoCounter, infoIndexPanels.get(infoCounter));
		infoCounter++;
		addTabPanel();
	}

	private void addTabPanel()
	{
		JPanel panel =  new JPanel();
		createAndAddInfoAddButton("Add Info", panel);
		infoPanel.addTab("+", panel);
		infoPanel.setSelectedIndex(infoCounter-1);

	}

	private void removeTabPanel()
	{
		for(int i = 0 ; i < infoPanel.getComponentCount(); i++)
		{
			if(infoPanel.getTitleAt(i).equals("+"))
			{
				infoPanel.remove(infoPanel.getSelectedComponent());
			}
		}
	}

	private JButton createAndAddInfoAddButton(String name, JPanel panel)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(button);

		return button;
	}


	public JScrollPane getPanel()
	{
		return this.capGenerateScrollPanel;
	}
	
	public void applyAlertElement() {
//		ieasMessage.setIdentifier(alertComponents.get(KIEAS_Constant.[0].getText());
//		ieasMessage.setSender(alertValues[1].getText());
//		ieasMessage.setSent(alertValues[2].getText());
//		ieasMessage.setStatus(alertValues[3].getText());
//		ieasMessage.setMsgType(alertValues[4].getText());
//		ieasMessage.setScope(alertValues[5].getText());
//		ieasMessage.setCode(alertValues[6].getText());
//
//		textArea.setText(ieasMessage.getMessage());
	}

	public void setTextArea(String string)
	{
		textArea.setText(string);
		textArea.setCaretPosition(0);
	}
	
	public void setIdentifierValue(String string)
	{
		((JTextField) alertComponents.get(KIEAS_Constant.IDENTIFIER)).setText(string);;
	}
	
	public void setSenderValue(String string)
	{
		((JTextField) alertComponents.get(KIEAS_Constant.SENDER)).setText(string);;
	}
	
	public void setSentValue(String string)
	{
		((JTextField) alertComponents.get(KIEAS_Constant.SENT)).setText(string);;
	}
	
	public void setStatusValue(String string)
	{		
		((JComboBox) alertComponents.get(KIEAS_Constant.STATUS)).setSelectedItem(string);
	}
}

