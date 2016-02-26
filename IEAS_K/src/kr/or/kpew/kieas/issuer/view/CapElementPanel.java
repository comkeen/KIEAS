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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.Item;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasList;
import kr.or.kpew.kieas.issuer.controller.Controller;

public class CapElementPanel
{
	private static final int BASE_LINE = 100;
	
	
	private IKieasMessageBuilder kieasMessageBuilder;
	
	private JPanel mainPanel;
	private JTabbedPane infoPanel;
	
	private HashMap<String, Component> alertComponentMap;
	private ArrayList<HashMap<String, Component>> infoComponentMaps;

	private ArrayList<JPanel> infoIndexPanels;
	private ArrayList<JButton> buttons;


	private JTabbedPane resourcePanel;


	private ArrayList<JPanel> resourceIndexPanels;


	private ArrayList<HashMap<String, Component>> resourceComponents;


	private int resourceCounter;


	private JTabbedPane areaPanel;


	private ArrayList<JPanel> areaIndexPanels;


	private ArrayList<HashMap<String, Component>> areaComponents;


	private int areaCounter;

	
	
	public CapElementPanel()
	{
		init();
	}

	private void init()
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		this.buttons = new ArrayList<>();
		
		this.mainPanel = setCapAlertPanel(kieasMessageBuilder.buildDefaultMessage());
	}
	
	public HashMap<String, String> getAlertElement()
	{
		HashMap<String, String> alertElementMap = new HashMap<>();
		alertElementMap.put(KieasMessageBuilder.IDENTIFIER, ((JTextField) alertComponentMap.get(KieasMessageBuilder.IDENTIFIER)).getText());
		alertElementMap.put(KieasMessageBuilder.SENDER, ((JTextField) alertComponentMap.get(KieasMessageBuilder.SENDER)).getText());
		alertElementMap.put(KieasMessageBuilder.SENT, ((JTextField) alertComponentMap.get(KieasMessageBuilder.SENT)).getText());
		alertElementMap.put(KieasMessageBuilder.STATUS, ((JComboBox<?>) alertComponentMap.get(KieasMessageBuilder.STATUS)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.MSG_TYPE, ((JComboBox<?>) alertComponentMap.get(KieasMessageBuilder.MSG_TYPE)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.SCOPE, ((JComboBox<?>) alertComponentMap.get(KieasMessageBuilder.SCOPE)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.RESTRICTION, ((JComboBox<?>) alertComponentMap.get(KieasMessageBuilder.RESTRICTION)).getSelectedItem().toString());
		alertElementMap.put(KieasMessageBuilder.CODE, ((JTextField) alertComponentMap.get(KieasMessageBuilder.CODE)).getText());
		
		return alertElementMap;
	}
	
	private JPanel setCapAlertPanel(String cap)
	{
		JPanel capElementPanel = new JPanel();
		capElementPanel.setLayout(new BoxLayout(capElementPanel, BoxLayout.Y_AXIS));
		
		this.alertComponentMap = new HashMap<>();
		
		capElementPanel.add(addBox(KieasMessageBuilder.IDENTIFIER, View.TEXT_FIELD));
		capElementPanel.add(addBox(KieasMessageBuilder.SENDER, View.TEXT_FIELD));
		capElementPanel.add(addBox(KieasMessageBuilder.SENT, View.TEXT_FIELD));
		capElementPanel.add(addBox(KieasMessageBuilder.STATUS, View.COMBO_BOX));
		capElementPanel.add(addBox(KieasMessageBuilder.MSG_TYPE, View.COMBO_BOX));
		capElementPanel.add(addBox(KieasMessageBuilder.SCOPE, View.COMBO_BOX));
		capElementPanel.add(addBox(KieasMessageBuilder.CODE, View.TEXT_FIELD));
//		mainPanel.add(initCapInfoPanel());
//		mainPanel.add(initCapResourcePanel());	
//		mainPanel.add(initCapAreaPanel());	
		capElementPanel.setBorder(BorderFactory.createTitledBorder("Alert"));

		kieasMessageBuilder.setMessage(cap);		
		updateView(KieasMessageBuilder.IDENTIFIER, kieasMessageBuilder.getIdentifier());
		updateView(KieasMessageBuilder.SENDER, kieasMessageBuilder.getSender());
		updateView(KieasMessageBuilder.SENT, kieasMessageBuilder.getSent());
		updateView(KieasMessageBuilder.STATUS, kieasMessageBuilder.getStatus());
		updateView(KieasMessageBuilder.MSG_TYPE, kieasMessageBuilder.getMsgType());
		updateView(KieasMessageBuilder.SCOPE, kieasMessageBuilder.getScope());
		updateView(KieasMessageBuilder.CODE, kieasMessageBuilder.getCode());
		
		return capElementPanel;
	}
	
	private Component initCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		this.infoIndexPanels = new ArrayList<JPanel>();
		this.infoComponentMaps = new ArrayList<HashMap<String, Component>>();

		addInfoIndexPanel();
		infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return infoPanel;
	}
	
	public void addInfoIndexPanel()
	{
		removeTabPanel(infoPanel);
		infoComponentMaps.add(new HashMap<>());
		JPanel panel = new JPanel();
		int infoCounter = infoIndexPanels.size() + 1;
		System.out.println("infoCounter : " + infoCounter);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(addBox(KieasMessageBuilder.LANGUAGE, View.COMBO_BOX, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.CATEGORY, View.COMBO_BOX, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.EVENT, View.TEXT_FIELD, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.URGENCY, View.COMBO_BOX, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.SEVERITY, View.COMBO_BOX, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.CERTAINTY, View.COMBO_BOX, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.EVENT_CODE, View.COMBO_BOX, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.EFFECTIVE, View.TEXT_FIELD, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.SENDER_NAME, View.TEXT_FIELD, infoComponentMaps, infoCounter)); 
		panel.add(addBox(KieasMessageBuilder.HEADLINE, View.TEXT_FIELD, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.DESCRIPTION, View.TEXT_AREA, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.WEB, View.TEXT_FIELD, infoComponentMaps, infoCounter));
		panel.add(addBox(KieasMessageBuilder.CONTACT, View.TEXT_FIELD, infoComponentMaps, infoCounter));
		
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

		panel.add(addBox(KieasMessageBuilder.RESOURCE_DESC, View.TEXT_FIELD, resourceComponents, resourceCounter));
		panel.add(addBox(KieasMessageBuilder.MIME_TYPE, View.TEXT_FIELD, resourceComponents, resourceCounter));
		panel.add(addBox(KieasMessageBuilder.URI, View.TEXT_FIELD, resourceComponents, resourceCounter));
		
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

		panel.add(addBox(KieasMessageBuilder.AREA_DESC, View.TEXT_FIELD, areaComponents, areaCounter));
		panel.add(addBox(KieasMessageBuilder.GEO_CODE, View.TEXT_FIELD, areaComponents, areaCounter));
		
		areaIndexPanels.add(panel);
		areaPanel.addTab("Area" + areaCounter, areaIndexPanels.get(areaCounter));
		areaCounter++;
		addTabPanel("Add Area", areaPanel, areaCounter);
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
		case View.COMBO_BOX:
			if(KieasMessageBuilder.RESTRICTION.equals(labelName))
			{
				Vector<String> comboboxModel = new Vector<>();
				for (String value : KieasList.ALERT_SYSTEM_TYPE_LIST)
				{
					comboboxModel.addElement(value);
				}
				JComboBox<String> comboBox = new JComboBox<>(comboboxModel);
				alertComponentMap.put(labelName, comboBox);
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
				alertComponentMap.put(labelName, comboBox);
				box.add(comboBox);
				return box;	
			}					
		case View.TEXT_FIELD:
			JTextField textField = new JTextField();
			alertComponentMap.put(labelName, textField);
			box.add(textField);
			return box;
		case View.TEXT_AREA:
			JTextArea textArea = new JTextArea();
			alertComponentMap.put(labelName, textArea);
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
		case View.COMBO_BOX:
			Vector<Item> comboboxModel = new Vector<>();
			for (Item value : kieasMessageBuilder.getCapEnumMap().get(labelName))
			{
				comboboxModel.addElement(value);		
			}
			JComboBox<Item> comboBox = new JComboBox<>(comboboxModel);
			components.get(index).put(labelName + index, comboBox);
			box.add(comboBox);
			return box;
		case View.TEXT_FIELD:
			JTextField textField = new JTextField();
			components.get(index).put(labelName + index, textField);
			box.add(textField);
			return box;
		case View.TEXT_AREA:
			JTextArea textArea = new JTextArea();
			components.get(index).put(labelName + index, textArea);
			box.add(textArea);
			return box;
		default:
			return box;
		}
	}
		
	private JButton createAndAddAddButton(String name, JPanel panel)
	{
		JButton button = new JButton(name);
		buttons.add(button);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panel.add(button);

		return button;
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
	
	public Component getPanel()
	{
		return mainPanel;		
	}
	
	public void addController(Controller controller)
	{
		for (JButton button : buttons)
		{
			button.addActionListener(controller);
		}
	}
	
	public void removeController(Controller controller)
	{
		for (JButton button : buttons)
		{
			button.removeActionListener(controller);
		}
	}
	
	public void updateView(String target, String value)
	{		
		if (alertComponentMap.get(target) instanceof JTextField)
		{
			((JTextField) alertComponentMap.get(target)).setText(value);
			return;
		}
		if (alertComponentMap.get(target) instanceof JComboBox<?>)
		{
			for(int i = 0; i < ((JComboBox<?>) alertComponentMap.get(target)).getItemCount(); i++)
			{
				if((((Item) ((JComboBox<?>) alertComponentMap.get(target)).getItemAt(i)).getKey()).equals(value))
				{
					((JComboBox<?>) alertComponentMap.get(target)).setSelectedIndex(i);
					return;
				}
			}
		}
//
//		for(int j = 0; j < infoCounter ; j++)
//		{
//			if(j > 0)
//			{
////				addInfoIndexPanel();
//			}
//			if (infoComponents.get(j).get(target) instanceof JTextField)
//			{
//				((JTextField) infoComponents.get(j).get(target)).setText(value);
//				return;
//			}
//			if (infoComponents.get(j).get(target) instanceof JComboBox<?>)
//			{
//				for(int i = 0; i < ((JComboBox<?>) infoComponents.get(j).get(target)).getItemCount(); i++)
//				{
//					if((((Item) ((JComboBox<?>) infoComponents.get(j).get(target)).getItemAt(i)).getKey()).equals(value))
//					{
//						((JComboBox<?>) infoComponents.get(j).get(target)).setSelectedIndex(i);
//						return;
//					}
//				}
//			}
//			if (infoComponents.get(j).get(target) instanceof JTextArea)
//			{
//				((JTextArea) infoComponents.get(j).get(target)).setText(value);
//				return;
//			}
//		}		
//		//resourcePanel updateView
//		for(int j = 0; j < resourceCounter; j++)
//		{
//			if(j > 0)
//			{
//				addResourceIndexPanel();
//			}
//			if (resourceComponents.get(j).get(target) instanceof JTextField)
//			{
//				((JTextField) resourceComponents.get(j).get(target)).setText(value);
//				return;
//			}
//			if (resourceComponents.get(j).get(target) instanceof JComboBox<?>)
//			{
//				for(int i = 0; i < ((JComboBox<?>) resourceComponents.get(j).get(target)).getItemCount(); i++)
//				{
//					if((((Item) ((JComboBox<?>) resourceComponents.get(j).get(target)).getItemAt(i)).getKey()).equals(value))
//					{
//						((JComboBox<?>) resourceComponents.get(j).get(target)).setSelectedIndex(i);
//						return;
//					}
//				}
//			}
//			if (resourceComponents.get(j).get(target) instanceof JTextArea)
//			{
//				((JTextArea) resourceComponents.get(j).get(target)).setText(value);
//				return;
//			}
//		}
//		//areaPanel updateView
//		for(int j = 0; j < areaCounter; j++)
//		{
//			if(j > 0)
//			{
//				addAreaIndexPanel();
//			}
//			if (areaComponents.get(j).get(target) instanceof JTextField)
//			{
//				((JTextField) areaComponents.get(j).get(target)).setText(value);
//				return;
//			}
//			if (areaComponents.get(j).get(target) instanceof JComboBox<?>)
//			{
//				for(int i = 0; i < ((JComboBox<?>) areaComponents.get(j).get(target)).getItemCount(); i++)
//				{
//					if((((Item) ((JComboBox<?>) areaComponents.get(j).get(target)).getItemAt(i)).getKey()).equals(value))
//					{
//						((JComboBox<?>) areaComponents.get(j).get(target)).setSelectedIndex(i);
//						return;
//					}
//				}
//			}
//			if (areaComponents.get(j).get(target) instanceof JTextArea)
//			{
//				((JTextArea) areaComponents.get(j).get(target)).setText(value);
//				return;
//			}
//		}
	}
}
