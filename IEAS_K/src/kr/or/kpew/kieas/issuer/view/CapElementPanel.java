package kr.or.kpew.kieas.issuer.view;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.Item;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder.AlertElementNames;
import kr.or.kpew.kieas.common.KieasMessageBuilder.AreaElementNames;
import kr.or.kpew.kieas.common.KieasMessageBuilder.InfoElementNames;
import kr.or.kpew.kieas.common.KieasMessageBuilder.ResourceElementNames;

import kr.or.kpew.kieas.common.Profile.AlertSystemType;
import kr.or.kpew.kieas.issuer.controller.IssuerController;

public class CapElementPanel
{
	private static final int BASE_LINE = 100;
	
	private static final String ALERT = "Alert";
	private static final String INFO = "Info";
	private static final String RESOURCE = "Resource";
	private static final String AREA = "Area";
	
	private static final String ADD = "+";	
	private static final String INFO_ADDER_BUTTON = "Info Add";
	private static final String RESOURCE_ADDER_BUTTON = "ResourceAdder";
	private static final String AREA_ADDER_BUTTON = "AreaAdder";	
	
	private IKieasMessageBuilder kieasMessageBuilder;
	
	private JComponent mainPanel;
	
	private Map<String, JComponent> alertComponentMap;
	private List<Map<String, JComponent>> infoComponentMaps;
	private Map<String, JComponent> resourceComponentMap;
	private Map<String, JComponent> areaComponentMap;
	private List<JButton> buttons;

	private JTabbedPane infoPanel;
	private JComponent resourcePanel;
	private JComponent areaBox;

	private IssuerController controller;
	
	
	public CapElementPanel()
	{
		init();
	}

	private void init()
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		this.buttons = new ArrayList<>();
		
		this.mainPanel = createCapAlertPanel();
		
		alertComponentMap.get(AlertElementNames.Identifier.toString()).setEnabled(false);
		alertComponentMap.get(AlertElementNames.Sent.toString()).setEnabled(false);
	}
	
	public JComponent createCapAlertPanel()
	{
		
		Box capElementPanel = Box.createVerticalBox();
		capElementPanel.setBorder(BorderFactory.createTitledBorder(ALERT));
		
		this.alertComponentMap = new HashMap<>();
		
		capElementPanel.add(addBox(AlertElementNames.Identifier.toString(), IssuerView.TEXT_FIELD, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Sender.toString(), IssuerView.TEXT_FIELD, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Sent.toString(), IssuerView.TEXT_FIELD, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Status.toString(), IssuerView.COMBO_BOX, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.MsgType.toString(), IssuerView.COMBO_BOX, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Source.toString(), IssuerView.TEXT_FIELD, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Scope.toString(), IssuerView.COMBO_BOX, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Restriction.toString(), IssuerView.COMBO_BOX, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Addresses.toString(), IssuerView.TEXT_FIELD, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Code.toString(), IssuerView.TEXT_FIELD, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.Note.toString(), IssuerView.TEXT_FIELD, alertComponentMap));
		capElementPanel.add(addBox(AlertElementNames.References.toString(), IssuerView.TEXT_FIELD, alertComponentMap));	
				
		capElementPanel.add(createCapInfoPanel());
		capElementPanel.add(createCapResourcePanel());
		capElementPanel.add(createCapAreaPanel());
		addController(controller);
		
		this.mainPanel = capElementPanel;
		
		return capElementPanel;
	}
	
	public void setCapAlertPanel(String capMessage)
	{
		kieasMessageBuilder.parse(capMessage);
		setCapElement(AlertElementNames.Identifier, kieasMessageBuilder.getIdentifier());
		setCapElement(AlertElementNames.Sender, kieasMessageBuilder.getSender());
		setCapElement(AlertElementNames.Sent, kieasMessageBuilder.getSent());
		setCapElement(AlertElementNames.Status, kieasMessageBuilder.getStatus().toString());
		setCapElement(AlertElementNames.MsgType, kieasMessageBuilder.getMsgType().toString());
		setCapElement(AlertElementNames.Source, kieasMessageBuilder.getSource().toString());
		setCapElement(AlertElementNames.Scope, kieasMessageBuilder.getScope().toString());
		setCapElement(AlertElementNames.Restriction, kieasMessageBuilder.getRestriction().toString());
		if(kieasMessageBuilder.getAddresses() != null && kieasMessageBuilder.getAddresses().size() > 0)
		{
			setCapElement(AlertElementNames.Addresses, kieasMessageBuilder.getAddresses().get(0).toString());			
		}
		setCapElement(AlertElementNames.Code, kieasMessageBuilder.getCode());	
		setCapElement(AlertElementNames.Note, kieasMessageBuilder.getNote());	
		setCapElement(AlertElementNames.References, kieasMessageBuilder.getReferences());	

		for(int i = 0; i < kieasMessageBuilder.getInfoCount(); i++)
		{
			setInfoIndexPanel(i);
		}
	}
	
	private JComponent createCapInfoPanel()
	{
		this.infoComponentMaps = new ArrayList<>();
		
		this.infoPanel = new JTabbedPane();
		infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		
		if(kieasMessageBuilder.getInfoCount() > 0)
		{
			for(int i = 1; i < kieasMessageBuilder.getInfoCount(); i++)
			{
				createInfoIndexPanel(i);
			}		
		}
		else
		{
			createInfoIndexPanel(0);
		}		
		
		return infoPanel;
	}
	
	public JComponent createInfoIndexPanel(int infoIndex)
	{
		removeTabAdder(INFO_ADDER_BUTTON, infoPanel);
		
		infoComponentMaps.add(new HashMap<>());
		
		
		Box panel = Box.createVerticalBox();
		
		panel.add(addInfoBox(InfoElementNames.Language.toString(), IssuerView.COMBO_BOX, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Category.toString(), IssuerView.COMBO_BOX, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Event.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.ResponseType.toString(), IssuerView.COMBO_BOX, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Urgency.toString(), IssuerView.COMBO_BOX, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Severity.toString(), IssuerView.COMBO_BOX, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Certainty.toString(), IssuerView.COMBO_BOX, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Audience.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.EventCode.toString(), IssuerView.COMBO_BOX, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Effective.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Onset.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Expires.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.SenderName.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Headline.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Description.toString(), IssuerView.TEXT_AREA, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Instruction.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Web.toString(), IssuerView.TEXT_FIELD, infoIndex));
		panel.add(addInfoBox(InfoElementNames.Contact.toString(), IssuerView.TEXT_FIELD, infoIndex));
				
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
		setCapElement(InfoElementNames.Language, kieasMessageBuilder.getLanguage(infoIndex));
		setCapElement(InfoElementNames.Category, kieasMessageBuilder.getCategory(infoIndex));
		setCapElement(InfoElementNames.Event, kieasMessageBuilder.getEvent(infoIndex));
		setCapElement(InfoElementNames.Urgency, kieasMessageBuilder.getUrgency(infoIndex));
		setCapElement(InfoElementNames.Severity, kieasMessageBuilder.getSeverity(infoIndex));
		setCapElement(InfoElementNames.Certainty, kieasMessageBuilder.getCertainty(infoIndex));
		setCapElement(InfoElementNames.EventCode, kieasMessageBuilder.getEvent(infoIndex));
		setCapElement(InfoElementNames.Effective, kieasMessageBuilder.getEffective(infoIndex));
		setCapElement(InfoElementNames.SenderName, kieasMessageBuilder.getSenderName(infoIndex));
		setCapElement(InfoElementNames.Headline, kieasMessageBuilder.getHeadline(infoIndex));
		setCapElement(InfoElementNames.Description, kieasMessageBuilder.getDescription(infoIndex));
		setCapElement(InfoElementNames.Web, kieasMessageBuilder.getWeb(infoIndex));
		setCapElement(InfoElementNames.Contact, kieasMessageBuilder.getContact(infoIndex));	
	}
	
	private JComponent createCapResourcePanel()
	{
		Box resourceBox = Box.createVerticalBox();
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), RESOURCE);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		resourceBox.setBorder(title);
		
		this.resourceComponentMap = new HashMap<String, JComponent>();
		
		resourceBox.add(addBox(ResourceElementNames.ResourceDesc.toString(), IssuerView.TEXT_FIELD, resourceComponentMap));
		resourceBox.add(addBox(ResourceElementNames.MimeType.toString(), IssuerView.TEXT_FIELD, resourceComponentMap));
		resourceBox.add(addBox(ResourceElementNames.Size.toString(), IssuerView.TEXT_FIELD, resourceComponentMap));
		
		
		return resourceBox;		
	}
	
	private JComponent createCapAreaPanel()
	{
		this.areaBox = Box.createVerticalBox();
		
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), AREA);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		areaBox.setBorder(title);
		
		this.areaComponentMap = new HashMap<String, JComponent>();

		areaBox.add(addBox(AreaElementNames.AreaDesc.toString(), IssuerView.TEXT_FIELD, areaComponentMap));
		areaBox.add(addBox(AreaElementNames.GeoCode.toString(), IssuerView.TEXT_FIELD, areaComponentMap));
		
		return areaBox;
	}
	
	
	private void addTabAdder(String name, JTabbedPane target)
	{		
		JPanel panel =  new JPanel();
		JButton button = createButton(name, panel);	
		panel.add(button);
		target.addTab(name, panel);
		
		int index = target.getTabCount();
		if(index == 0)
		{
			target.setSelectedIndex(index);	
		}
		else
		{
			target.setSelectedIndex(index - 2);			
		}
	}
	
	private JComponent removeTabAdder(String name, JTabbedPane target)
	{
		for(int i = 0 ; i < target.getTabCount(); i++)
		{
			if(target.getTitleAt(i).equals(name))
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

		return button;
	}

	private Box addBox(String labelName, String type, Map<String, JComponent> componentMap)
	{
		Box box = Box.createHorizontalBox();
		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		int offset = (int) (BASE_LINE - label.getPreferredSize().getWidth());		
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		
		box.add(label);
		switch (type)
		{
		case IssuerView.COMBO_BOX:
			if (AlertElementNames.Restriction.toString().equals(labelName))
			{
				Vector<String> comboboxModel = new Vector<>();
				for (AlertSystemType systemtype : AlertSystemType.values())
				{
					comboboxModel.addElement(systemtype.name());

				}
				JComboBox<String> comboBox = new JComboBox<>(comboboxModel);
				alertComponentMap.put(labelName, comboBox);
				box.add(comboBox);
			}
			else
			{
				Vector<Item> comboboxModel = new Vector<>();
				for (Item value : kieasMessageBuilder.getCapEnumMap().get(AlertElementNames.valueOf(labelName)))
				{
					comboboxModel.addElement(value);
				}
				JComboBox<Item> comboBox = new JComboBox<>(comboboxModel);
				alertComponentMap.put(labelName, comboBox);
				box.add(comboBox);					
			}
			return box;			
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
			System.out.println("AO: Fail to add Box");
			return box;
		}
	}	

	private Box addInfoBox(String labelName, String type, int index)
	{
		Box box = Box.createHorizontalBox();

		JLabel label = new JLabel(labelName);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		int offset = (int) (BASE_LINE - label.getPreferredSize().getWidth());
		box.add(Box.createRigidArea(new Dimension(offset, 0)));
		box.add(label);

		Map<String, JComponent> infoComponentMap = infoComponentMaps.get(index);
		switch (type)
		{
		case IssuerView.COMBO_BOX:
			Vector<Item> comboboxModel = new Vector<>();
			for (Item value : kieasMessageBuilder.getCapEnumMap().get(InfoElementNames.valueOf(labelName)))
			{
				comboboxModel.addElement(value);		
			}
			JComboBox<Item> comboBox = new JComboBox<>(comboboxModel);
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
			System.out.println("AO: Fail to add Box");
			return box;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setCapElement( Enum e, String value)
	{
		String target = e.toString();
		Object object = alertComponentMap.get(target);
		if (object instanceof JTextField)
		{
			((JTextField) object).setText(value);
			return;
		}
		if (object instanceof JComboBox<?>)
		{
			for(int i = 0; i < ((JComboBox<?>) object).getItemCount(); i++)
			{
				Object comboBoxItemObject = ((JComboBox<?>) object).getItemAt(i);
				if(comboBoxItemObject instanceof Item && ((Item) comboBoxItemObject).getKey().equals(value))
				{
					((JComboBox) object).setSelectedIndex(i);
					return;
				}
				else if(comboBoxItemObject instanceof String && comboBoxItemObject.equals(value))
				{
					((JComboBox) object).setSelectedIndex(i);
					return;
				}
			}
		}
		
		for(int j = 0; j < infoComponentMaps.size() ; j++)
		{
			Map<String, JComponent> infoComponentMap = infoComponentMaps.get(j);
			
			if (infoComponentMap.get(target) instanceof JTextField)
			{
				((JTextField) infoComponentMap.get(target)).setText(value);
				return;
			}
			if (infoComponentMap.get(target) instanceof JComboBox<?>)
			{
				for(int i = 0; i < ((JComboBox<?>) infoComponentMap.get(target)).getItemCount(); i++)
				{
					if((((Item) ((JComboBox<?>) infoComponentMap.get(target)).getItemAt(i)).getKey()).equals(value))
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
	}
	
	public String getCapElement()
	{
		for (AlertElementNames alertElement : AlertElementNames.values())
		{
			String key = alertElement.toString();
			if(alertComponentMap.containsKey(key))
			{
				String value = null;
				Object object = alertComponentMap.get(key);
				
				if(object instanceof JComboBox<?>)
				{
					Object comboBoxItemObject = ((JComboBox<?>) object).getSelectedItem();
					if(comboBoxItemObject instanceof Item)
					{
						value = ((Item) comboBoxItemObject).getKey();						
					}
					else if(comboBoxItemObject instanceof String)
					{
						value = comboBoxItemObject.toString();
					}
				}
				else if(object instanceof JTextField)
				{
					value = ((JTextField) object).getText();
				}
				else if(object instanceof JTextArea)
				{
					value = ((JTextArea) object).getText();
				}
				
				String methodName = "set" + key;
				try
				{
					if(value.trim().length() != 0)
					{
						Method method = kieasMessageBuilder.getClass().getMethod(methodName.trim(), new String().getClass());
						method.invoke(kieasMessageBuilder, value);
					}			
				}
				catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					e.printStackTrace();
				}
				catch (NoSuchMethodException ex)
				{
					System.out.println("AO: there is no such a method name : " + methodName);
					continue;
				}			
			}			
		}
		return kieasMessageBuilder.build();
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
	
	public JComponent getPanel()
	{
		return mainPanel;		
	}

	public void setIdentifier(String message)
	{
		kieasMessageBuilder.parse(message);		
		String identifier = kieasMessageBuilder.getIdentifier();
		((JTextField) alertComponentMap.get(AlertElementNames.Identifier.toString())).setText(identifier);
	}
}
