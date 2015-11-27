package kr.ac.uos.ai.ieas.alerter.alerterView;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterModel.AlertTableModel;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder.Item;


public class AlerterAlertGeneratePanel
{
	private static AlerterAlertGeneratePanel alerterAlertGeneratePanel;
	private AleterViewActionListener alerterActionListener;
	private KieasMessageBuilder kieasMessageBuilder;

	private JScrollPane alertGenerateScrollPanel;
	private JPanel alertGeneratePanel;

	private Vector<Object> mViewComponents;
	private HashMap<String, Component> panelComponenets;
	private JScrollPane textAreaPane;
	private JTextArea mTextArea;
	
	private JPanel buttonPane;

	private HashMap<String, Component> alertComponents;
	private JPanel alertPanel;
	private JButton alertApplyButton;

	private ArrayList<HashMap<String, Component>> infoComponents;
	private JTabbedPane infoPanel;
	private ArrayList<JPanel> infoIndexPanels;
	private int infoCounter;
	
	private JTabbedPane areaPanel;
	private ArrayList<JPanel> areaIndexPanels;
	private ArrayList<HashMap<String, Component>> areaComponents;
	private int areaCounter;
	
	private JButton sendButton;
	private JTextField mInsertDatabaseTextField;
	private AlertTableModel alertTableModel;
	private JTable alertTable;
	private JScrollPane tableScrollPane;
	

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

	
	public static AlerterAlertGeneratePanel getInstance(AleterViewActionListener alerterActionListener)
	{
		if (alerterAlertGeneratePanel == null)
		{
			alerterAlertGeneratePanel = new AlerterAlertGeneratePanel(alerterActionListener);
		}
		return alerterAlertGeneratePanel;
	}

	private AlerterAlertGeneratePanel(AleterViewActionListener alerterActionListener)
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

		alertGeneratePanel.add(initTextArea());
		alertGeneratePanel.add(initDataTable());
		alertGeneratePanel.add(initButtonPanel());	
		alertGeneratePanel.add(initCapAlertPanel());
		
		mViewComponents.addElement(panelComponenets);
		mViewComponents.addElement(alertComponents);
		mViewComponents.addElement(infoComponents);
		mViewComponents.addElement(areaComponents);

		this.alertGenerateScrollPanel = new JScrollPane(alertGeneratePanel);
	}

	private Component initTextArea()
	{
		this.mTextArea = new JTextArea(20, 20);
		mTextArea.setEditable(false);
		
		this.textAreaPane = new JScrollPane(mTextArea);
		panelComponenets.put("TextArea", mTextArea);
		textAreaPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return textAreaPane;
	}

	private Component initButtonPanel()
	{
		this.buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		
		Box loadBox = addBox(LOAD_DRAFT_BUTTON, BUTTON);
		loadBox.setBorder(BorderFactory.createRaisedBevelBorder());
		buttonPane.add(addBox(LOAD_DRAFT_BUTTON, BUTTON));
		
		buttonPane.add(Box.createRigidArea(new Dimension(200, 0)));
		
		Box sendBox = Box.createHorizontalBox();
		this.sendButton = createButton(SEND_BUTTON);
		sendButton.setPreferredSize(new Dimension(200, 10));
		sendBox.add(sendButton);
		
		buttonPane.add(sendBox);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return buttonPane;
	}

	private Component initDataTable() {

		this.alertTableModel = new AlertTableModel();
		this.alertTable = new JTable(alertTableModel.getTableModel());
		this.tableScrollPane = new JScrollPane(alertTable);

		alertTable.setEnabled(true);
		alertTable.getSelectionModel().addListSelectionListener(alerterActionListener);
		
		return tableScrollPane;
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
		alertPanel.add(addBox("지역추가", BUTTON));
		alertPanel.add(initCapInfoPanel());
//		alertPanel.add(initCapAreaPanel());	
		alertPanel.setBorder(BorderFactory.createTitledBorder("경보메시지"));
		
		return alertPanel;
	}

	private Component initCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		this.infoIndexPanels = new ArrayList<JPanel>();
		this.infoComponents = new ArrayList<HashMap<String, Component>>();

		this.infoCounter = 0;
		addInfoIndexPanel();
		infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return infoPanel;
	}
	
	private void resetCapInfoPanel()
	{
		this.infoPanel.removeAll();
		this.infoIndexPanels.clear();
		this.infoComponents.clear();
		this.infoCounter = 0;
		addInfoIndexPanel();
	}

	public void addInfoIndexPanel()
	{
		removeTabPanel(infoPanel);
		infoComponents.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		panel.add(addBox(HEADLINE, TEXT_FIELD, infoComponents, infoCounter));
		panel.add(addBox(DESCRIPTION, TEXT_AREA, infoComponents, infoCounter));
		
		infoIndexPanels.add(panel);
		infoPanel.addTab("한국어", infoIndexPanels.get(infoCounter));
		infoCounter++;
		addTabPanel("Add Info", infoPanel, infoCounter);
	}	
	
	private Component initCapAreaPanel()
	{
		this.areaPanel = new JTabbedPane();
		areaPanel.setBorder(BorderFactory.createEtchedBorder());
		this.areaIndexPanels = new ArrayList<JPanel>();
		this.areaComponents = new ArrayList<HashMap<String, Component>>();

		this.areaCounter = 0;
		addAreaIndexPanel();
		areaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return areaPanel;
	}
	
	public void addAreaIndexPanel()
	{
		removeTabPanel(areaPanel);
		areaComponents.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(addBox(AREA_DESC, TEXT_FIELD, areaComponents, areaCounter));
		panel.add(addBox(GEO_CODE, TEXT_FIELD, areaComponents, areaCounter));
		
		areaIndexPanels.add(panel);
		areaPanel.addTab("지역", areaIndexPanels.get(areaCounter));
		areaCounter++;
		addTabPanel("Add Area", areaPanel, areaCounter);
	}

	private JButton createButton(String name)
	{
		if(SEND_BUTTON.equals(name))
		{
			JButton button = new JButton(SEND_BUTTON_K);
			button.addActionListener(alerterActionListener);
			button.setAlignmentX(Component.LEFT_ALIGNMENT);
			return button;
		}
		else
		{
			JButton button = new JButton(name);
			button.addActionListener(alerterActionListener);
			button.setAlignmentX(Component.LEFT_ALIGNMENT);
			return button;
		}		
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
			for (Item value : kieasMessageBuilder.getCapEnumMap().get(EVENT_CODE))
			{
				buttonComboboxModel.addElement(value);
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

	private Box addBox(String labelName, String type, ArrayList<HashMap<String, Component>> components, int index)
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
			components.get(index).put(labelName + index, comboBox);
			box.add(comboBox);
			return box;
		case TEXT_FIELD:
			JTextField textField = new JTextField();
			components.get(index).put(labelName + index, textField);
			box.add(textField);
			return box;
		case TEXT_AREA:
			JTextArea textArea = new JTextArea();
			components.get(index).put(labelName + index, textArea);
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
	private int checkIndex(String target)
	{
		StringBuffer stringBuffer = new StringBuffer();
		for(int i = 0; i < target.length(); i++)
		{
			char currentChar = target.charAt(i);
			if(Character.isDigit(currentChar))
			{
				stringBuffer.append(currentChar);			
			}
		}
		if(stringBuffer.length() == 0)
		{
			return -1;
		}
		else
		{
			return Integer.parseInt(stringBuffer.toString());			
		}
	}
	
	public void updateView(String target, String value)
	{		
//		if(target.equals(INFO_INDEX))
//		{
//			int index = Integer.parseInt(value);
//			infoCounter = index;
//			System.out.println("view infocounter = "+ infoCounter);
//			resetCapInfoPanel();
//			for(int i = 0; i < index; i++)
//			{
//				System.out.println();
//				addInfoIndexPanel();
//			}
//			return;
//		}
		if(target.equals(TEXT_AREA))
		{
			this.mTextArea.setText(value);
			return;
		}
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

		for(int j = 0; j < infoCounter ; j++)
		{
			if(j > 0)
			{
//				addInfoIndexPanel();
			}
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
		//resourcePanel updateView
		/*
		for(int j = 0; j < resourceCounter; j++)
		{
			if(j > 0)
			{
				addResourceIndexPanel();
			}
			if (resourceComponents.get(j).get(target) instanceof JTextField)
			{
				((JTextField) resourceComponents.get(j).get(target)).setText(value);
				return;
			}
			if (resourceComponents.get(j).get(target) instanceof JComboBox<?>)
			{
				for(int i = 0; i < ((JComboBox<?>) resourceComponents.get(j).get(target)).getItemCount(); i++)
				{
					if((((Item) ((JComboBox<?>) resourceComponents.get(j).get(target)).getItemAt(i)).getKey()).equals(value))
					{
						((JComboBox<?>) resourceComponents.get(j).get(target)).setSelectedIndex(i);
						return;
					}
				}
			}
			if (resourceComponents.get(j).get(target) instanceof JTextArea)
			{
				((JTextArea) resourceComponents.get(j).get(target)).setText(value);
				return;
			}
		}
		*/
		//areaPanel updateView
		for(int j = 0; j < areaCounter; j++)
		{
			if(j > 0)
			{
				addAreaIndexPanel();
			}
			if (areaComponents.get(j).get(target) instanceof JTextField)
			{
				((JTextField) areaComponents.get(j).get(target)).setText(value);
				return;
			}
			if (areaComponents.get(j).get(target) instanceof JComboBox<?>)
			{
				for(int i = 0; i < ((JComboBox<?>) areaComponents.get(j).get(target)).getItemCount(); i++)
				{
					if((((Item) ((JComboBox<?>) areaComponents.get(j).get(target)).getItemAt(i)).getKey()).equals(value))
					{
						((JComboBox<?>) areaComponents.get(j).get(target)).setSelectedIndex(i);
						return;
					}
				}
			}
			if (areaComponents.get(j).get(target) instanceof JTextArea)
			{
				((JTextArea) areaComponents.get(j).get(target)).setText(value);
				return;
			}
		}
	}
}

