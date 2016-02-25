package kr.or.kpew.kieas.issuer.view;

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

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasList;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder.Item;
import kr.or.kpew.kieas.issuer.controller._Controller;


public class AlertGeneratorPanel
{	
	public static final String TEXT_AREA = "TextArea";
	private static final String TEXT_FIELD = "TextField";
	private static final String COMBO_BOX = "ComboBox";

	public static final String LOAD_TEXT_FIELD = "LoadTextField";
	public static final String SAVE_TEXT_FIELD = "SaveTextField";
	public static final String LOAD_CAP_BUTTON = "Load Cap";
	public static final String SAVE_CAP_BUTTON = "Save Cap";
	private static final String REGISTER_BUTTON = "Register";
	private static final String SET_ID = "Set Id";
	private static final String INSERT_DATABASE_BUTTON = "Insert DB";

	public static final String INFO_INDEX = "InfoIndex";
	
	private static final int BASE_LINE = 100;
	
	
	private _Controller controller;
	private IKieasMessageBuilder kieasMessageBuilder;

	private JScrollPane alertGenerateScrollPanel;
	private JPanel mainPanel;

	private Vector<Object> mViewComponents;
	private HashMap<String, Component> panelComponenets;
	private JScrollPane textAreaPane;
	private JTextArea mTextArea;
	
	private JPanel buttonPane;
	private JButton saveCapButton;
	private JButton loadCapDraftButton;
	private JTextField mSaveTextField;
	private JTextField mLoadTextField;

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
	
	private JTabbedPane resourcePanel;
	private ArrayList<JPanel> resourceIndexPanels;
	private ArrayList<HashMap<String, Component>> resourceComponents;
	private int resourceCounter;
	private JButton insertDatabaseButton;
	private JButton sendButton;
	private JButton registerButton;
	private JButton setIdButton;



	public AlertGeneratorPanel()
	{
//		this.alerterActionListener = controller;
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		initPanel();
	}

	private void initPanel()
	{		
		this.mViewComponents = new Vector<>();		
		this.panelComponenets = new HashMap<>();		
		this.mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(initTextArea());
		mainPanel.add(initButtonPanel());
		mainPanel.add(initCapAlertPanel());
		
		mViewComponents.addElement(panelComponenets);
		mViewComponents.addElement(alertComponents);
		mViewComponents.addElement(infoComponents);
		mViewComponents.addElement(areaComponents);

		this.alertGenerateScrollPanel = new JScrollPane(mainPanel);
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

		Box loadBox = Box.createHorizontalBox();		
		this.loadCapDraftButton = createButton(LOAD_CAP_BUTTON);
		loadBox.add(loadCapDraftButton);		
		this.mLoadTextField = new JTextField();
		panelComponenets.put(LOAD_TEXT_FIELD, mLoadTextField);
		mLoadTextField.setText("cap/HRA.xml");
		loadBox.add(mLoadTextField);
		loadBox.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPane.add(loadBox);

		Box saveBox = Box.createHorizontalBox();
		this.saveCapButton = createButton(SAVE_CAP_BUTTON);
		saveBox.add(saveCapButton);
		this.mSaveTextField = new JTextField();
		panelComponenets.put(SAVE_TEXT_FIELD, mSaveTextField);
		mSaveTextField.setText("cap/out.xml");
		saveBox.add(mSaveTextField);
		saveBox.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPane.add(saveBox);

		Box insertDatabaseBox = Box.createHorizontalBox();
		this.insertDatabaseButton = createButton(INSERT_DATABASE_BUTTON);
		insertDatabaseBox.add(insertDatabaseButton);
//		this.mInsertDatabaseTextField = new JTextField();
//		panelComponenets.put(INSERT_DATABASE_TEXT_FIELD, mInsertDatabaseTextField);
//		mInsertDatabaseTextField.setText("ALERT ID");
//		insertDatabaseBox.add(mInsertDatabaseTextField);
		insertDatabaseBox.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPane.add(insertDatabaseBox);
		
		this.alertApplyButton = createButton("Apply");
		buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		buttonPane.add(alertApplyButton);
		this.sendButton = createButton("Send");
		sendButton.setActionCommand("Send");
		buttonPane.add(sendButton);
		this.registerButton = createButton(REGISTER_BUTTON);
		registerButton.setActionCommand(REGISTER_BUTTON);
		buttonPane.add(registerButton);
		this.setIdButton = createButton(SET_ID);
		setIdButton.setActionCommand(SET_ID);
		buttonPane.add(setIdButton);
		
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return buttonPane;
	}

	private JPanel initCapAlertPanel()
	{
		this.alertPanel = new JPanel();
		alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
		
		this.alertComponents = new HashMap<>();

		alertPanel.add(addBox(KieasMessageBuilder.IDENTIFIER, TEXT_FIELD));
		alertPanel.add(addBox(KieasMessageBuilder.SENDER, TEXT_FIELD));
		alertPanel.add(addBox(KieasMessageBuilder.SENT, TEXT_FIELD));
		alertPanel.add(addBox(KieasMessageBuilder.STATUS, COMBO_BOX));
		alertPanel.add(addBox(KieasMessageBuilder.MSG_TYPE, COMBO_BOX));
		alertPanel.add(addBox(KieasMessageBuilder.SCOPE, COMBO_BOX));
		alertPanel.add(addBox(KieasMessageBuilder.RESTRICTION, COMBO_BOX));
		alertPanel.add(addBox(KieasMessageBuilder.CODE, TEXT_FIELD));
		alertPanel.add(initCapInfoPanel());
		alertPanel.add(initCapResourcePanel());	
		alertPanel.add(initCapAreaPanel());	
		alertPanel.setBorder(BorderFactory.createTitledBorder("Alert"));
		
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
	
//	private void resetCapInfoPanel()
//	{
//		this.infoPanel.removeAll();
//		this.infoIndexPanels.clear();
//		this.infoComponents.clear();
//		this.infoCounter = 0;
//		addInfoIndexPanel();
//	}

	public void addInfoIndexPanel()
	{
		removeTabPanel(infoPanel);
		infoComponents.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(addBox(KieasMessageBuilder.LANGUAGE, COMBO_BOX, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.CATEGORY, COMBO_BOX, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.EVENT, TEXT_FIELD, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.URGENCY, COMBO_BOX, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.SEVERITY, COMBO_BOX, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.CERTAINTY, COMBO_BOX, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.EVENT_CODE, COMBO_BOX, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.EFFECTIVE, TEXT_FIELD, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.SENDER_NAME, TEXT_FIELD, infoComponents, infoCounter)); 
		panel.add(addBox(KieasMessageBuilder.HEADLINE, TEXT_FIELD, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.DESCRIPTION, TEXT_AREA, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.WEB, TEXT_FIELD, infoComponents, infoCounter));
		panel.add(addBox(KieasMessageBuilder.CONTACT, TEXT_FIELD, infoComponents, infoCounter));
		
		infoIndexPanels.add(panel);
		infoPanel.addTab("Info" + infoCounter, infoIndexPanels.get(infoCounter));
		infoCounter++;
		addTabPanel("Add Info", infoPanel, infoCounter);
	}
	
	private Component initCapResourcePanel()
	{
		this.resourcePanel = new JTabbedPane();
		resourcePanel.setBorder(BorderFactory.createEtchedBorder());
		this.resourceIndexPanels = new ArrayList<JPanel>();
		this.resourceComponents = new ArrayList<HashMap<String, Component>>();

		this.resourceCounter = 0;
		addResourceIndexPanel();
		resourcePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return resourcePanel;
	}
	
	public void addResourceIndexPanel()
	{
		removeTabPanel(resourcePanel);
		resourceComponents.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(addBox(KieasMessageBuilder.RESOURCE_DESC, TEXT_FIELD, resourceComponents, resourceCounter));
		panel.add(addBox(KieasMessageBuilder.MIME_TYPE, TEXT_FIELD, resourceComponents, resourceCounter));
		panel.add(addBox(KieasMessageBuilder.URI, TEXT_FIELD, resourceComponents, resourceCounter));
		
		resourceIndexPanels.add(panel);
		resourcePanel.addTab("Resource" + resourceCounter, resourceIndexPanels.get(resourceCounter));
		resourceCounter++;
		addTabPanel("Add Resource", resourcePanel, resourceCounter);
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

		panel.add(addBox(KieasMessageBuilder.AREA_DESC, TEXT_FIELD, areaComponents, areaCounter));
		panel.add(addBox(KieasMessageBuilder.GEO_CODE, TEXT_FIELD, areaComponents, areaCounter));
		
		areaIndexPanels.add(panel);
		areaPanel.addTab("Area" + areaCounter, areaIndexPanels.get(areaCounter));
		areaCounter++;
		addTabPanel("Add Area", areaPanel, areaCounter);
	}

	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		button.addActionListener(controller);
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
			if(KieasMessageBuilder.RESTRICTION.equals(labelName))
			{
				Vector<String> comboboxModel = new Vector<>();
				for (String value : KieasList.ALERT_SYSTEM_TYPE_LIST)
				{
					comboboxModel.addElement(value);
				}
				JComboBox<String> comboBox = new JComboBox<>(comboboxModel);
				alertComponents.put(labelName, comboBox);
				box.add(comboBox);
				return box;	
			}
			else
			{
				Vector<Item> comboboxModel = new Vector<>();
				for (Item value : kieasMessageBuilder.getCapEnumMap().get(labelName))
				{
					comboboxModel.addElement(value);
				}
				JComboBox<Item> comboBox = new JComboBox<>(comboboxModel);
				alertComponents.put(labelName, comboBox);
				box.add(comboBox);
				return box;	
			}					
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
		button.addActionListener(controller);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panel.add(button);

		return button;
	}


	public JScrollPane getPanel()
	{
		return this.alertGenerateScrollPanel;
	}

	public HashMap<String, String> getAlertElement()
	{
		HashMap<String, String> alertElementMap = new HashMap<>();
		alertElementMap.put(KieasMessageBuilder.IDENTIFIER, ((JTextField) alertComponents.get(KieasMessageBuilder.IDENTIFIER)).getText());
		alertElementMap.put(KieasMessageBuilder.SENDER, ((JTextField) alertComponents.get(KieasMessageBuilder.SENDER)).getText());
		alertElementMap.put(KieasMessageBuilder.SENT, ((JTextField) alertComponents.get(KieasMessageBuilder.SENT)).getText());
		alertElementMap.put(KieasMessageBuilder.STATUS, ((JComboBox<?>) alertComponents.get(KieasMessageBuilder.STATUS)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.MSG_TYPE, ((JComboBox<?>) alertComponents.get(KieasMessageBuilder.MSG_TYPE)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.SCOPE, ((JComboBox<?>) alertComponents.get(KieasMessageBuilder.SCOPE)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.RESTRICTION, ((JComboBox<?>) alertComponents.get(KieasMessageBuilder.RESTRICTION)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.CODE, ((JTextField) alertComponents.get(KieasMessageBuilder.CODE)).getText());
		
		return alertElementMap;
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
		if(target.equals(LOAD_TEXT_FIELD))
		{
			this.mLoadTextField.setText(value);
			return;
		}
		if(target.equals(SAVE_TEXT_FIELD))
		{
			this.mSaveTextField.setText(value);
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
		return ((JTextArea) panelComponenets.get(TEXT_AREA)).getText();
	}

	public void setController(_Controller controller)
	{
		this.controller = controller;
	}

	public void setTextArea(String message) {
		// TODO Auto-generated method stub
		
	}
}

