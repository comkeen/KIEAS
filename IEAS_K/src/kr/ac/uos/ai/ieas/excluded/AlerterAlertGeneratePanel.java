package kr.or.kpew.kieas.excluded;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
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
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder.Item;


public class AlerterAlertGeneratePanel
{
	private AleterViewActionListener alerterActionListener;
	private KieasMessageBuilder kieasMessageBuilder;

	private JScrollPane alertGenerateScrollPanel;
	private JPanel alertGeneratePanel;

	private Vector<Object> mViewComponents;
	private HashMap<String, Component> panelComponenets;
	
	private JPanel buttonPane;

	private HashMap<String, Component> alertComponents;
	private JPanel alertPanel;

	private ArrayList<HashMap<String, Component>> infoComponents;
	private JTabbedPane infoPanel;
	private ArrayList<JPanel> infoIndexPanels;
	
	private JPanel areaPanel;
	private ArrayList<Component> areaElements;
	
	private JButton sendButton;

	public static final String TEXT_AREA = "TextArea";
	public static final String TEXT_FIELD = "TextField";
	public static final String COMBO_BOX = "ComboBox";
	public static final String BUTTON = "Button";

	public static final String LOAD_DRAFT_BUTTON = "Load Draft";
	public static final String SEND_BUTTON = "Send";
	public static final String SEND_BUTTON_K = "경보 송신";
	
	public static final String ORGANIZAION = "기관 이름";
	public static final String DOMAIN = "기관 도메인";
	public static final String USER = "발령자 이름";
	public static final String USER_DUTY = "발령자 직책";

	public static final String ALERT_TYPE = "경보종류";
	public static final String EVENT_CODE = "EventCode";	
	
	public static final String STATUS = "Status";
	public static final String SCOPE = "Scope";
	public static final String RESTRICTION = "Restriction";
	
	public static final String STATUS_K = "경보상황코드";
	public static final String SCOPE_K = "수신자범위";
	public static final String RESTRICTION_K = "수신장치";

	public static final String EVENT = "Event";
	public static final String URGENCY = "Urgency";
	public static final String SEVERITY = "Severity";
	public static final String CERTAINTY = "Certainty";
	public static final String HEADLINE = "Headline";
	public static final String DESCRIPTION = "Description";
	
	public static final String URGENCY_K = "대응의 긴급성";
	public static final String SEVERITY_K = "피해규모";
	public static final String CERTAINTY_K = "사건발생확률";
	public static final String HEADLINE_K = "경보 제목";
	public static final String DESCRIPTION_K = "경보 내용";

	public static final String AREA_DESC = "AreaDesc";
	public static final String GEO_CODE = "GeoCode";
	
	private static final int BASE_LINE = 100;


	public AlerterAlertGeneratePanel(AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
		this.kieasMessageBuilder = new KieasMessageBuilder();
	
		initPanel();			
	}

	private void initPanel()
	{		
		this.mViewComponents = new Vector<>();
		this.panelComponenets = new HashMap<>();
		this.alertGeneratePanel = new JPanel();
		
		alertGeneratePanel.setLayout(new BoxLayout(alertGeneratePanel, BoxLayout.Y_AXIS));

		alertGeneratePanel.add(initButtonPanel());
		alertGeneratePanel.add(initCapAlertPanel());
		
		mViewComponents.addElement(panelComponenets);
		mViewComponents.addElement(alertComponents);
		mViewComponents.addElement(infoComponents);
		mViewComponents.addElement(areaElements);

		this.alertGenerateScrollPanel = new JScrollPane(alertGeneratePanel);
	}

	private Component initButtonPanel()
	{
		this.buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		
		Box loadBox = addBox(LOAD_DRAFT_BUTTON, BUTTON);
		loadBox.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonPane.add(addBox(LOAD_DRAFT_BUTTON, BUTTON));
		
//		buttonPane.add(Box.createRigidArea(new Dimension(200, 0)));
		
		Box sendBox = Box.createHorizontalBox();
		this.sendButton = createButton(SEND_BUTTON_K);
		sendButton.setActionCommand(SEND_BUTTON);
		sendButton.setPreferredSize(new Dimension(200, 10));
		sendBox.add(sendButton);
		
		buttonPane.add(sendBox);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return buttonPane;
	}
	
	private JPanel initCapAlertPanel()
	{
		this.alertPanel = new JPanel();
		alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
		
		this.alertComponents = new HashMap<>();

		alertPanel.add(addBox(ORGANIZAION, TEXT_FIELD));
		alertPanel.add(addBox(DOMAIN, TEXT_FIELD));
		alertPanel.add(addBox(USER, TEXT_FIELD));
		alertPanel.add(addBox(USER_DUTY, TEXT_FIELD));
		alertPanel.add(addBox(URGENCY, COMBO_BOX));
		alertPanel.add(addBox(SEVERITY, COMBO_BOX));
		alertPanel.add(addBox(CERTAINTY, COMBO_BOX));
		alertPanel.add(addBox(EVENT_CODE, COMBO_BOX));
		alertPanel.add(addBox(GEO_CODE, BUTTON));
		alertPanel.add(initCapInfoPanel());
		alertPanel.add(initCapAreaPanel());
		alertPanel.setBorder(BorderFactory.createTitledBorder("경보메시지"));
		
		return alertPanel;
	}

	private Component initCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		this.infoIndexPanels = new ArrayList<JPanel>();
		this.infoComponents = new ArrayList<HashMap<String, Component>>();
		
		addInfoIndexPanel();
		infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return infoPanel;
	}

	public void addInfoIndexPanel()
	{
		removeTabPanel(infoPanel);
		HashMap<String, Component> map = new HashMap<>();
		infoComponents.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				
		panel.add(addBox(HEADLINE, TEXT_FIELD, map));
		panel.add(addBox(DESCRIPTION, TEXT_AREA, map));
		
		infoComponents.add(map);
		infoIndexPanels.add(panel);
		infoPanel.addTab("한국어", infoIndexPanels.get(0));
		addTabPanel("Add Launguage", infoPanel, 1);
	}	
	
	private Component initCapAreaPanel()
	{
		this.areaElements = new ArrayList<Component>();
		
		this.areaPanel = new JPanel();
		areaPanel.setBorder(BorderFactory.createEtchedBorder());
		areaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return areaPanel;
	}

	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		return button;
	}

	private Box addBox(String labelName, String type)
	{
		Box box = Box.createHorizontalBox();

		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		int offset = (int) (BASE_LINE - label.getPreferredSize().getWidth());		
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		
		box.add(label);
		switch (type)
		{
		case COMBO_BOX:
			Vector<Item> comboboxModel = new Vector<>();
			for (Item value : kieasMessageBuilder.getCapEnumMap().get(labelName))
			{
				comboboxModel.addElement(value);
			}

			JComboBox<Item> comboBox = new JComboBox<>(comboboxModel);
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
		case BUTTON:	
			JButton button = createButton(labelName);
			box.add(button);		
			Vector<Item> buttonComboboxModel = new Vector<>();
			if(LOAD_DRAFT_BUTTON.equals(labelName))
			{
				for (Item value : kieasMessageBuilder.getCapEnumMap().get(EVENT_CODE))
				{
					buttonComboboxModel.addElement(value);
				}
			}
			else if(GEO_CODE.equals(labelName))
			{
				for (Item value : kieasMessageBuilder.getCapEnumMap().get(GEO_CODE))
				{
					button.setText("지역추가");
					buttonComboboxModel.addElement(value);
				}
			}
			else
			{
				for (Item value : kieasMessageBuilder.getCapEnumMap().get(labelName))
				{
					buttonComboboxModel.addElement(value);
				}
			}
			
			JComboBox<Item> buttonComboBox = new JComboBox<>(buttonComboboxModel);
			AutoCompletionComboBox autoCompeletionComboBox = new AutoCompletionComboBox(buttonComboBox);
			panelComponenets.put(labelName, autoCompeletionComboBox.getComoboBox());
			box.add(buttonComboBox);
			return box;
		default:
			return box;
		}
	}

	private Box addBox(String labelName, String type, HashMap<String, Component> map)
	{
		Box box = Box.createHorizontalBox();

		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		int offset = (int) (BASE_LINE - label.getPreferredSize().getWidth());
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		box.add(label);

		switch (type)
		{
		case COMBO_BOX:
			Vector<Item> comboboxModel = new Vector<>();
			for (Item value : kieasMessageBuilder.getCapEnumMap().get(labelName))
			{
				comboboxModel.addElement(value);
			}
			JComboBox<Item> comboBox = new JComboBox<>(comboboxModel);
			map.put(labelName, comboBox);
			box.add(comboBox);
			return box;
		case TEXT_FIELD:
			JTextField textField = new JTextField();
			map.put(labelName, textField);
			box.add(textField);
			return box;
		case TEXT_AREA:
			JTextArea textArea = new JTextArea();
			map.put(labelName, textArea);
			box.add(textArea);
			return box;
		default:
			return box;
		}
	}
	
	private JPanel addTabPanel(String name, JTabbedPane target, int index) {
		JPanel panel =  new JPanel();
		createAndAddAddButton(name, panel);
		target.addTab("+", panel);
		target.setSelectedIndex(index - 1);
		
		return panel;
	}

	private void removeTabPanel(JTabbedPane target)
	{
		for(int i = 0 ; i < target.getComponentCount(); i++)
		{
			if(target.getTitleAt(i).equals("+"))
			{
				target.remove(target.getSelectedComponent());
			}
		}
	}
	
	private JButton createAndAddAddButton(String name, JPanel panel)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panel.add(button);

		return button;
	}


	public JScrollPane getPanel()
	{
		return this.alertGenerateScrollPanel;
	}

//	public void applyAlertElement()
//	{
//		kieasMessageBuilder.setIdentifier(alertComponents.get(KIEAS_Constant.[0].getText());
//		kieasMessageBuilder.setSender(alertValues[1].getText());
//		kieasMessageBuilder.setSent(alertValues[2].getText());
//		kieasMessageBuilder.setStatus(alertValues[3].getText());
//		kieasMessageBuilder.setMsgType(alertValues[4].getText());
//		kieasMessageBuilder.setScope(alertValues[5].getText());
//		kieasMessageBuilder.setCode(alertValues[6].getText());
//		
//				mTextArea.setText(ieasMessage.getMessage());
//	}

//	private void setAlertValue(String target, String value)
//	{
//		if (alertComponents.get(target) instanceof JTextField)
//		{
//			((JTextField) alertComponents.get(target)).setText(value);
//			return;
//		}
//		if (alertComponents.get(target) instanceof JComboBox<?>)
//		{
//			((JComboBox<?>) alertComponents.get(target)).setSelectedItem(value);
//			return;
//		}
//	}
	
	public void updateView(String target, String value)
	{		
		if (alertComponents.get(target) instanceof JTextField)
		{
			((JTextField) alertComponents.get(target)).setText(value);
			return;
		}
		if (alertComponents.get(target) instanceof JComboBox<?>)
		{
			for(int i = 0; i < ((JComboBox<?>) alertComponents.get(target)).getItemCount(); i++)
			{
				if((((Item) ((JComboBox<?>) alertComponents.get(target)).getItemAt(i)).getKey()).equals(value))
				{
					((JComboBox<?>) alertComponents.get(target)).setSelectedIndex(i);
					return;
				}
			}
		}

		for(int j = 0; j < infoComponents.size() ; j++)
		{
			if (infoComponents.get(j).get(target) instanceof JTextField)
			{
				((JTextField) infoComponents.get(j).get(target)).setText(value);
				return;
			}
			if (infoComponents.get(j).get(target) instanceof JComboBox<?>)
			{
				for(int i = 0; i < ((JComboBox<?>) infoComponents.get(j).get(target)).getItemCount(); i++)
				{
					if((((Item) ((JComboBox<?>) infoComponents.get(j).get(target)).getItemAt(i)).getKey()).equals(value))
					{
						((JComboBox<?>) infoComponents.get(j).get(target)).setSelectedIndex(i);
						return;
					}
				}
			}
			if (infoComponents.get(j).get(target) instanceof JTextArea)
			{
				((JTextArea) infoComponents.get(j).get(target)).setText(value);
				return;
			}
		}		
		//areaPanel updateView
		for(int j = 0; j < areaElements.size(); j++)
		{
			for(int i = 0; i < ((JComboBox<?>) areaElements.get(j)).getItemCount(); i++)
			{
				if((((Item) ((JComboBox<?>) areaElements.get(j)).getItemAt(i)).getKey()).equals(value))
				{
					((JComboBox<?>) areaElements.get(j)).setSelectedIndex(i);
					return;
				}
			}
		}
	}
}

