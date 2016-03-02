package kr.or.kpew.kieas.issuer.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.Pair;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasList;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.issuer.controller.IssuerController;

public class CapElementPanel
{
	private static final int BASE_LINE = 100;
	
	private static final String ALERT = "Alert";
	private static final String INFO = "Info";
	
	private static final String ADD = "+";	
	private static final String INFO_ADDER_BUTTON = "Info Add";
	private static final String RESOURCE_ADDER_BUTTON = "ResourceAdder";
	private static final String AREA_ADDER_BUTTON = "AreaAdder";	
	
	private IKieasMessageBuilder kieasMessageBuilder;
	
	private JComponent mainPanel;
	
	private Map<String, Component> alertComponentMap;
	private List<Map<String, Component>> infoComponentMaps;
	private List<Map<String, Component>> resourceComponentMaps;
	private List<Map<String, Component>> areaComponentMaps;
	private List<JButton> buttons;

	private List<JPanel> resourceIndexPanels;
	private List<JPanel> areaIndexPanels;

	private JTabbedPane resourcePanel;
	private int resourceCounter;

	private JTabbedPane areaPanel;
	private int areaCounter;


	private JTabbedPane infoPanel;
	
	private String capMessage;

	private IssuerController controller;
	
	
	public CapElementPanel()
	{
		init();
	}

	private void init()
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		this.buttons = new ArrayList<>();
		
		this.capMessage = kieasMessageBuilder.buildDefaultMessage();
		this.mainPanel = createCapAlertPanel(capMessage);
	}
	
	public JComponent createCapAlertPanel(String capMessage)
	{
		JPanel capElementPanel = new JPanel();
		capElementPanel.setLayout(new BoxLayout(capElementPanel, BoxLayout.Y_AXIS));
		capElementPanel.setBorder(BorderFactory.createTitledBorder(ALERT));
		
		this.alertComponentMap = new HashMap<>();

		this.capMessage = capMessage;		
		kieasMessageBuilder.setMessage(capMessage);
		
		capElementPanel.add(addBox(KieasMessageBuilder.IDENTIFIER, IssuerView.TEXT_FIELD));
		capElementPanel.add(addBox(KieasMessageBuilder.SENDER, IssuerView.TEXT_FIELD));
		capElementPanel.add(addBox(KieasMessageBuilder.SENT, IssuerView.TEXT_FIELD));
		capElementPanel.add(addBox(KieasMessageBuilder.STATUS, IssuerView.COMBO_BOX));
		capElementPanel.add(addBox(KieasMessageBuilder.MSG_TYPE, IssuerView.COMBO_BOX));
		if(kieasMessageBuilder.getSource().length() != 0)
		{
			capElementPanel.add(addBox(KieasMessageBuilder.SOURCE, IssuerView.TEXT_FIELD));			
		}
		capElementPanel.add(addBox(KieasMessageBuilder.SCOPE, IssuerView.COMBO_BOX));
		if(kieasMessageBuilder.getRestriction().length() != 0)
		{
			capElementPanel.add(addBox(KieasMessageBuilder.RESTRICTION, IssuerView.TEXT_FIELD));				
		}
		if(kieasMessageBuilder.getAddresses().length() != 0)
		{
			capElementPanel.add(addBox(KieasMessageBuilder.ADDRESSES, IssuerView.TEXT_FIELD));				
		}
		capElementPanel.add(addBox(KieasMessageBuilder.CODE, IssuerView.TEXT_FIELD));
		if(kieasMessageBuilder.getNote().length() != 0)
		{
			capElementPanel.add(addBox(KieasMessageBuilder.NOTE, IssuerView.TEXT_FIELD));				
		}
		if(kieasMessageBuilder.getReferences().length() != 0)
		{
			capElementPanel.add(addBox(KieasMessageBuilder.NOTE, IssuerView.TEXT_FIELD));				
		}
		
		capElementPanel.add(createCapInfoPanel());
		
		addController(controller);
		setCapAlertPanel(capMessage);
		
		this.mainPanel = capElementPanel;
		
		return capElementPanel;
	}
	
	public void setCapAlertPanel(String capMessage)
	{			
		setCapElement(KieasMessageBuilder.IDENTIFIER, kieasMessageBuilder.getIdentifier());
		setCapElement(KieasMessageBuilder.SENDER, kieasMessageBuilder.getSender());
		setCapElement(KieasMessageBuilder.SENT, kieasMessageBuilder.getSent());
		setCapElement(KieasMessageBuilder.STATUS, kieasMessageBuilder.getStatus());
		setCapElement(KieasMessageBuilder.MSG_TYPE, kieasMessageBuilder.getMsgType());
		setCapElement(KieasMessageBuilder.SCOPE, kieasMessageBuilder.getScope());
		setCapElement(KieasMessageBuilder.RESTRICTION, kieasMessageBuilder.getScope());
		setCapElement(KieasMessageBuilder.CODE, kieasMessageBuilder.getCode());	

		System.out.println("setElement : " + kieasMessageBuilder.getInfoCount());
		for(int i = 0; i < kieasMessageBuilder.getInfoCount(); i++)
		{
			System.out.println();
			setInfoIndexPanel(i);
		}
	}
	
	private JComponent createCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();		
		infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		infoPanel.setBorder(BorderFactory.createEtchedBorder());

		this.infoComponentMaps = new ArrayList<>();
		
//		infoPanel.addTab(ADD, addTabAdder(INFO_ADDER_BUTTON, infoPanel));
		
		for(int i = 0; i < kieasMessageBuilder.getInfoCount(); i++)
		{
			createInfoIndexPanel(i);
		}		
		
		return infoPanel;
	}
	
	public JPanel createInfoIndexPanel(int infoIndex)
	{
		removeTabAdder(infoPanel);
		
		infoComponentMaps.add(new HashMap<>());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(addBox(KieasMessageBuilder.LANGUAGE, IssuerView.COMBO_BOX, infoIndex));
		panel.add(addBox(KieasMessageBuilder.CATEGORY, IssuerView.COMBO_BOX, infoIndex));
		panel.add(addBox(KieasMessageBuilder.EVENT, IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addBox(KieasMessageBuilder.RESPONSE_TYPE, IssuerView.COMBO_BOX, infoIndex));
		panel.add(addBox(KieasMessageBuilder.URGENCY, IssuerView.COMBO_BOX, infoIndex));
		panel.add(addBox(KieasMessageBuilder.SEVERITY, IssuerView.COMBO_BOX, infoIndex));
		panel.add(addBox(KieasMessageBuilder.CERTAINTY, IssuerView.COMBO_BOX, infoIndex));
		panel.add(addBox(KieasMessageBuilder.AUDIENCE, IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addBox(KieasMessageBuilder.EVENT_CODE, IssuerView.COMBO_BOX, infoIndex));
		panel.add(addBox(KieasMessageBuilder.EFFECTIVE, IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addBox(KieasMessageBuilder.SENDER_NAME, IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addBox(KieasMessageBuilder.HEADLINE, IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addBox(KieasMessageBuilder.DESCRIPTION, IssuerView.TEXT_AREA, infoIndex));
		panel.add(addBox(KieasMessageBuilder.INSTRUCTION, IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addBox(KieasMessageBuilder.WEB, IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addBox(KieasMessageBuilder.CONTACT, IssuerView.TEXT_FIELD, infoIndex));
		
//		if(kieasMessageBuilder.getLanguage(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.LANGUAGE, IssuerView.COMBO_BOX, infoIndex));
//		}
//		panel.add(addBox(KieasMessageBuilder.CATEGORY, IssuerView.COMBO_BOX, infoIndex));
//		panel.add(addBox(KieasMessageBuilder.EVENT, IssuerView.TEXT_FIELD, infoIndex));
//		if(kieasMessageBuilder.getResponseType(infoIndex).length() != 0)
//		{
//			//TODO
//			panel.add(addBox(KieasMessageBuilder.RESPONSE_TYPE, IssuerView.COMBO_BOX, infoIndex));
//		}
//		panel.add(addBox(KieasMessageBuilder.URGENCY, IssuerView.COMBO_BOX, infoIndex));
//		panel.add(addBox(KieasMessageBuilder.SEVERITY, IssuerView.COMBO_BOX, infoIndex));
//		panel.add(addBox(KieasMessageBuilder.CERTAINTY, IssuerView.COMBO_BOX, infoIndex));
//		if(kieasMessageBuilder.getAudience(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.AUDIENCE, IssuerView.TEXT_FIELD, infoIndex));
//		}
//		if(kieasMessageBuilder.getEventCode(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.EVENT_CODE, IssuerView.COMBO_BOX, infoIndex));
//		}
//		if(kieasMessageBuilder.getEffective(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.EFFECTIVE, IssuerView.TEXT_FIELD, infoIndex));
//		}
//		//TODO onset
//		//TODO expires
//		if(kieasMessageBuilder.getSenderName(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.SENDER_NAME, IssuerView.TEXT_FIELD, infoIndex));		
//		}
//		if(kieasMessageBuilder.getHeadline(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.HEADLINE, IssuerView.TEXT_FIELD, infoIndex));
//		}
//		if(kieasMessageBuilder.getDescription(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.DESCRIPTION, IssuerView.TEXT_AREA, infoIndex));
//		}
//		if(kieasMessageBuilder.getInstruction(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.INSTRUCTION, IssuerView.TEXT_FIELD, infoIndex));
//		}
//		if(kieasMessageBuilder.getWeb(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.WEB, IssuerView.TEXT_FIELD, infoIndex));
//		}
//		if(kieasMessageBuilder.getContact(infoIndex).length() != 0)
//		{
//			panel.add(addBox(KieasMessageBuilder.CONTACT, IssuerView.TEXT_FIELD, infoIndex));
//		}
		
//		panel.add(createCapResourcePanel());
//		panel.add(initCapAreaPanel());
		
		infoPanel.addTab(INFO + infoIndex, panel);
		addTabAdder(INFO_ADDER_BUTTON, infoPanel);
				
		return panel;
	}

	public void addInfoIndexPanel()
	{
		createInfoIndexPanel(infoPanel.getTabCount() - 1);
		addController(controller);
	}
	
	private void setInfoIndexPanel(int infoIndex)
	{
		setCapElement(KieasMessageBuilder.LANGUAGE, kieasMessageBuilder.getLanguage(infoIndex));
		setCapElement(KieasMessageBuilder.CATEGORY, kieasMessageBuilder.getCategory(infoIndex));
		setCapElement(KieasMessageBuilder.EVENT, kieasMessageBuilder.getEvent(infoIndex));
		setCapElement(KieasMessageBuilder.URGENCY, kieasMessageBuilder.getUrgency(infoIndex));
		setCapElement(KieasMessageBuilder.SEVERITY, kieasMessageBuilder.getSeverity(infoIndex));
		setCapElement(KieasMessageBuilder.CERTAINTY, kieasMessageBuilder.getCertainty(infoIndex));
		setCapElement(KieasMessageBuilder.EVENT_CODE, kieasMessageBuilder.getEvent(infoIndex));
		setCapElement(KieasMessageBuilder.EFFECTIVE, kieasMessageBuilder.getEffective(infoIndex));
		setCapElement(KieasMessageBuilder.SENDER_NAME, kieasMessageBuilder.getSenderName(infoIndex));
		setCapElement(KieasMessageBuilder.HEADLINE, kieasMessageBuilder.getHeadline(infoIndex));
		setCapElement(KieasMessageBuilder.DESCRIPTION, kieasMessageBuilder.getDescription(infoIndex));
		setCapElement(KieasMessageBuilder.WEB, kieasMessageBuilder.getWeb(infoIndex));
		setCapElement(KieasMessageBuilder.CONTACT, kieasMessageBuilder.getContact(infoIndex));		
	}
	
	private JComponent createCapResourcePanel()
	{
		JTabbedPane resourcePanel = new JTabbedPane();
		resourcePanel.setBorder(BorderFactory.createEtchedBorder());
		this.resourceComponentMaps = new ArrayList<Map<String, Component>>();

		addResourceIndexPanel();
		resourcePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return resourcePanel;
	}
	
	public void addResourceIndexPanel()
	{
		removeTabAdder(resourcePanel);
		resourceComponentMaps.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//		panel.add(addBox(KieasMessageBuilder.RESOURCE_DESC, View.TEXT_FIELD, resourceComponentMaps, resourceCounter));
//		panel.add(addBox(KieasMessageBuilder.MIME_TYPE, View.TEXT_FIELD, resourceComponentMaps, resourceCounter));
//		panel.add(addBox(KieasMessageBuilder.URI, View.TEXT_FIELD, resourceComponentMaps, resourceCounter));
		
		resourceIndexPanels.add(panel);
		resourcePanel.addTab("Resource" + resourceCounter, resourceIndexPanels.get(resourceCounter));
		resourceCounter++;
		addTabAdder("Add Resource", resourcePanel);
	}
	
	private Component initCapAreaPanel()
	{
		this.areaPanel = new JTabbedPane();
		areaPanel.setBorder(BorderFactory.createEtchedBorder());
		this.areaIndexPanels = new ArrayList<JPanel>();
		this.areaComponentMaps = new ArrayList<Map<String, Component>>();

		this.areaCounter = 0;
		addAreaIndexPanel();
		areaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return areaPanel;
	}
	
	public void addAreaIndexPanel()
	{
		removeTabAdder(areaPanel);
		areaComponentMaps.add(new HashMap<>());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//		panel.add(addBox(KieasMessageBuilder.AREA_DESC, View.TEXT_FIELD, areaComponentMaps, areaCounter));
//		panel.add(addBox(KieasMessageBuilder.GEO_CODE, View.TEXT_FIELD, areaComponentMaps, areaCounter));
		
		areaIndexPanels.add(panel);
		areaPanel.addTab("Area" + areaCounter, areaIndexPanels.get(areaCounter));
		areaCounter++;
		addTabAdder("Add Area", areaPanel);
	}
	
	
	private void addTabAdder(String name, JTabbedPane target)
	{		
		JPanel panel =  new JPanel();
		JButton button = createButton(name, panel);	
		panel.add(button);
		target.addTab(ADD, panel);
		
		int index = target.getTabCount();
		System.out.println("tabcount : " + index);
		if(index == 0)
		{
			target.setSelectedIndex(index);	
		}
		else
		{
			target.setSelectedIndex(index - 2);			
		}
	}
	
	private JComponent removeTabAdder(JTabbedPane target)
	{
		for(int i = 0 ; i < target.getTabCount(); i++)
		{
			if(target.getTitleAt(i).equals(ADD))
			{
				target.removeTabAt(i);
			}
		}
		return target;
	}
	
	private JButton createButton(String name, JComponent component)
	{
		JButton button = new JButton(name);
		
		switch (name)
		{
		case INFO_ADDER_BUTTON:
			button.setText(INFO_ADDER_BUTTON);
			break;
		case RESOURCE_ADDER_BUTTON:
			button.setText(ADD);
			break;
		case AREA_ADDER_BUTTON:
			button.setText(ADD);
			break;
		default:
			break;
		}
		
		buttons.add(button);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);

		return button;
	}
	
	public JComponent getPanel()
	{
		return mainPanel;		
	}

	private Box addBox(String labelName, String type)
	{
		Box box = Box.createHorizontalBox();
		box.setPreferredSize(new Dimension(30, 30));
		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		int offset = (int) (BASE_LINE - label.getPreferredSize().getWidth());		
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		
		box.add(label);
		
		switch (type)
		{
		case IssuerView.COMBO_BOX:
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
				Vector<Pair> comboboxModel = new Vector<>();
				for (Pair value : kieasMessageBuilder.getCapEnumMap().get(labelName))
				{
					comboboxModel.addElement(value);
				}
				JComboBox<Pair> comboBox = new JComboBox<>(comboboxModel);
				alertComponentMap.put(labelName, comboBox);
				box.add(comboBox);
				return box;	
			}					
		case IssuerView.TEXT_FIELD:
			JTextField textField = new JTextField();
			alertComponentMap.put(labelName, textField);
			box.add(textField);
			return box;
		case IssuerView.TEXT_AREA:
			JTextArea textArea = new JTextArea();
			alertComponentMap.put(labelName, textArea);
			box.add(textArea);
			return box;
		default:
			System.out.println("Fail to add Box");
			return box;
		}
	}	

	private Box addBox(String labelName, String type, int index)
	{
		Box box = Box.createHorizontalBox();

		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		int offset = (int) (BASE_LINE - label.getPreferredSize().getWidth());
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		box.add(label);

		Map<String, Component> infoComponentMap = infoComponentMaps.get(index);
		switch (type)
		{
		case IssuerView.COMBO_BOX:
			Vector<Pair> comboboxModel = new Vector<>();
			for (Pair value : kieasMessageBuilder.getCapEnumMap().get(labelName))
			{
				comboboxModel.addElement(value);		
			}
			JComboBox<Pair> comboBox = new JComboBox<>(comboboxModel);
			infoComponentMap.put(labelName, comboBox);
			box.add(comboBox);
			return box;
		case IssuerView.TEXT_FIELD:
			JTextField textField = new JTextField();
			infoComponentMap.put(labelName, textField);
			box.add(textField);
			return box;
		case IssuerView.TEXT_AREA:
			JTextArea textArea = new JTextArea();
			infoComponentMaps.get(index).put(labelName, textArea);
			box.add(textArea);
			return box;
		default:
			System.out.println("Fail to add Box");
			return box;
		}
	}
	
	public void setCapElement(String target, String value)
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
				if((((Pair) ((JComboBox<?>) alertComponentMap.get(target)).getItemAt(i)).getKey()).equals(value))
				{
					((JComboBox<?>) alertComponentMap.get(target)).setSelectedIndex(i);
					return;
				}
			}
		}
		
		System.out.println("info size : " + infoComponentMaps.size());
		for(int j = 0; j < infoComponentMaps.size() ; j++)
		{
			System.out.println("target : " + target + " : " + j);
			Map<String, Component> infoComponentMap = infoComponentMaps.get(j);
			
			if (infoComponentMap.get(target) instanceof JTextField)
			{
				((JTextField) infoComponentMap.get(target)).setText(value);
				return;
			}
			if (infoComponentMap.get(target) instanceof JComboBox<?>)
			{
				for(int i = 0; i < ((JComboBox<?>) infoComponentMap.get(target)).getItemCount(); i++)
				{
					if((((Pair) ((JComboBox<?>) infoComponentMap.get(target)).getItemAt(i)).getKey()).equals(value))
					{
						((JComboBox<?>) infoComponentMap.get(target)).setSelectedIndex(i);
						return;
					}
				}
			}
			if (infoComponentMap.get(target) instanceof JTextArea)
			{
				((JTextArea) infoComponentMap.get(target)).setText(value);
				return;
			}
		}		
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
	
	public void addController(IssuerController controller)
	{
		this.controller = controller;
		for (JButton button : buttons)
		{
			button.addActionListener(controller);
		}
	}
	
	public void removeController(IssuerController controller)
	{
		for (JButton button : buttons)
		{
			button.removeActionListener(controller);
		}
	}
}
