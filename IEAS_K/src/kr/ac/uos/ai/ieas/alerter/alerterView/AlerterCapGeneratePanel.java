package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

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
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.IEAS_List;

public class AlerterCapGeneratePanel
{
	private static AlerterCapGeneratePanel alerterCapElementPanel;

	private AleterViewActionListener alerterActionListener;
	private GridBagConstraints gbc;
	private KieasMessageBuilder ieasMessage;

	private FileInputStream fileInputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private StringReader stringReader;

	private JPanel capPanel;
	private JPanel buttonPane;
	private JButton saveCapButton;
	private JButton loadCapDraftButton;


	private JTextArea textArea;
	private JScrollPane textAreaPane;
	private JScrollPane capScrollPanel;


	private JPanel alertPanel;
	private JTextField[] alertValues;
	private JComboBox<String>[] alertEnumValues;
	private JButton alertApplyButton;

	private JTabbedPane infoPanel;
	private ArrayList<JPanel> infoIndexPanels;
	private JComboBox<String>[] infoEnumValues;
	private JTextField[] infoValues;
	private JTextArea[] infoDescriptionValues;

	private JTextField savePathTextField;
	private JTextField loadPathTextField;

	private int infoCounter;



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
		this.gbc = new GridBagConstraints();
		this.ieasMessage = new KieasMessageBuilder();

		initFrame("alertViewPanel");		
	}

	private void initFrame(String name)
	{		
		this.capPanel = new JPanel();
		this.capScrollPanel = new JScrollPane(capPanel);
		capPanel.setLayout(new GridBagLayout());

		gbc.anchor = GridBagConstraints.BASELINE;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 0, 3, 1, 1, 1);
		initTextArea();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 1, 3, 1, 1, 1);
		initButtonPanel();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 2, 3, 1, 1, 1);
		initCapAlertPanel();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 3, 3, 1, 1, 1);
		initCapInfoPanel();
	}

	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}

	private void initTextArea()
	{
		this.textArea = new JTextArea(20, 20);
		this.textAreaPane = new JScrollPane(textArea);	
		textArea.setSize(1000, 1000);
		textArea.setEditable(false);
		textArea.setText("\n");

		capPanel.add(textAreaPane, gbc);
	}

	private void initCapAlertPanel()
	{
		this.alertPanel = new JPanel();
		alertPanel.setLayout(new GridBagLayout());

		this.alertValues = new JTextField[IEAS_List.ALERT_ELEMENT_LIST.length];
		this.alertEnumValues = new JComboBox[IEAS_List.ALERT_ELEMENT_LIST.length];
		//info가 여러개일 경우			
		int i = 0;
		for (String alertElementName : IEAS_List.ALERT_ELEMENT_LIST)
		{
			setGbc(0, i, 1, 1, 1, 1);
			createAndAddLable(alertElementName, alertPanel);
			if(alertElementName.equals("Status"))
			{
				setGbc(1, i, 1, 1, 1, 1);
				alertEnumValues[i] = createAndAddComboBox(alertPanel);	
				for (String value : ieasMessage.getCapEnumMap().get("Status")) {
					alertEnumValues[i].addItem(value);
				}
			}
			else if(alertElementName.equals("MsgType"))
			{
				setGbc(1, i, 1, 1, 1, 1);
				alertEnumValues[i] = createAndAddComboBox(alertPanel);	
				for (String value : ieasMessage.getCapEnumMap().get("MsgType")) {
					alertEnumValues[i].addItem(value);
				}
			}
			else if(alertElementName.equals("Scope"))
			{
				setGbc(1, i, 1, 1, 1, 1);
				alertEnumValues[i] = createAndAddComboBox(alertPanel);	
				for (String value : ieasMessage.getCapEnumMap().get("Scope")) {
					alertEnumValues[i].addItem(value);
				}
			}
			else if(alertElementName.equals("Code"))
			{
				setGbc(1, i, 9, 1, 9, 1);
				alertValues[i] = createAndAddTextField(alertPanel);
				alertValues[i].setText("대한민국정부1.0");
				alertValues[i].setEditable(false);
			}
			else
			{
				setGbc(1, i, 9, 1, 9, 1);
				alertValues[i] = createAndAddTextField(alertPanel);
			}
			i++;
		}
		capPanel.add(alertPanel, gbc);
	}

	private void initCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();
		this.infoValues = new JTextField[IEAS_List.INFO_ELEMENT_LIST.length];
		this.infoEnumValues = new JComboBox[IEAS_List.INFO_ELEMENT_LIST.length];
		this.infoDescriptionValues = new JTextArea[5];
		this.infoIndexPanels = new ArrayList<JPanel>();

		this.infoCounter = 0;
		addInfoIndexPanel();

		capPanel.add(infoPanel, gbc);
	}

	public void addInfoIndexPanel()
	{		
		removeTabPanel();
		this.infoIndexPanels.add(new JPanel());
		infoIndexPanels.get(infoCounter).setLayout(new GridBagLayout());

		int i = 0;
		for (String infoElementName : IEAS_List.INFO_ELEMENT_LIST)
		{
			if (infoElementName.equals("Description"))
			{
				setGbc(0, i, 1, 1, 1, 1);
				createAndAddLable(infoElementName, infoIndexPanels.get(infoCounter));
				setGbc(1, i, 9, 5, 9, 5);
				infoDescriptionValues[infoCounter] = new JTextArea();

				JScrollPane descriptionScrollPanel = new JScrollPane(infoDescriptionValues[infoCounter]);
				infoIndexPanels.get(infoCounter).add(descriptionScrollPanel, gbc);

				i++;
			}
			else
			{				
				setGbc(0, i, 1, 1, 1, 1);
				createAndAddLable(infoElementName, infoIndexPanels.get(infoCounter));
				if(infoElementName.equals("Category"))
				{
					setGbc(1, i, 1, 1, 1, 1);
					infoEnumValues[i] = createAndAddComboBox(infoIndexPanels.get(infoCounter));	
					for (String value : ieasMessage.getCapEnumMap().get("Category")) {
						infoEnumValues[i].addItem(value);
					}
				}
//				else if(infoElementName.equals("Language"))
//				{
//					setGbc(1, i, 1, 1, 1, 1);
//					infoEnumValues[i] = createAndAddComboBox(infoIndexPanels.get(infoCounter));
//					infoEnumValues[i].addItem("ko_kr");
//				}
				else if(infoElementName.equals("Urgency"))
				{
					setGbc(1, i, 1, 1, 1, 1);
					infoEnumValues[i] = createAndAddComboBox(infoIndexPanels.get(infoCounter));
					for (String value : ieasMessage.getCapEnumMap().get("Urgency")) {
						infoEnumValues[i].addItem(value);
					}
				}
				else if(infoElementName.equals("Severity"))
				{
					setGbc(1, i, 1, 1, 1, 1);
					infoEnumValues[i] = createAndAddComboBox(infoIndexPanels.get(infoCounter));
					for (String value : ieasMessage.getCapEnumMap().get("Severity")) {
						infoEnumValues[i].addItem(value);
					}
				}
				else if(infoElementName.equals("Certainty"))
				{
					setGbc(1, i, 1, 1, 1, 1);
					infoEnumValues[i] = createAndAddComboBox(infoIndexPanels.get(infoCounter));	
					for (String value : ieasMessage.getCapEnumMap().get("Certainty")) {
						infoEnumValues[i].addItem(value);
					}
				}				
				else
				{
					setGbc(1, i, 3, 1, 3, 1);
					infoValues[i] = createAndAddTextField(infoIndexPanels.get(infoCounter));
				}
				i++;
			}
		}
		infoPanel.addTab("Info" + infoCounter, infoIndexPanels.get(infoCounter));
		infoCounter++;
		addTabPanel();
	}

	private void addTabPanel()
	{
		JPanel panel =  new JPanel();
		createAndAddInfoApplyButton("Add Info", panel);
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


	private void createAndAddLable(String name, JPanel panel)
	{
		JLabel label = new JLabel(name);
		panel.add(label, gbc);
	}

	private JTextField createAndAddTextField(JPanel panel)
	{
		JTextField textField = new JTextField();
		textField.setPreferredSize(textField.getPreferredSize());
		panel.add(textField, gbc);		

		return textField;
	}

	private JComboBox<String> createAndAddComboBox(JPanel panel)
	{
		JComboBox<String> comboBox = new JComboBox<>();
		panel.add(comboBox, gbc);		

		return comboBox;
	}

	private JButton createAndAddInfoApplyButton(String name, JPanel panel)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		panel.add(button, gbc);

		return button;
	}


	private void initButtonPanel()
	{
		this.buttonPane = new JPanel();

		this.loadPathTextField = new JTextField("cap/HRA.xml");
		buttonPane.add(loadPathTextField, BorderLayout.WEST);

		this.loadCapDraftButton = createButton("LoadCapDraft");
		buttonPane.add(loadCapDraftButton, BorderLayout.WEST);		

		this.saveCapButton = createButton("SaveCap");
		buttonPane.add(saveCapButton, BorderLayout.EAST);

		this.savePathTextField = new JTextField("cap/out.xml");
		buttonPane.add(savePathTextField, BorderLayout.EAST);

		this.alertApplyButton = createButton("Apply");
		buttonPane.add(alertApplyButton, BorderLayout.EAST);

		capPanel.add(buttonPane, gbc);
	}

	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		return button;
	}

	public JScrollPane getPanel()
	{
		return this.capScrollPanel;
	}

	private void setAlertPanel(KieasMessageBuilder ieasMessage)
	{
		alertValues[0].setText(ieasMessage.getIdentifier());
		alertValues[1].setText(ieasMessage.getSender());
		alertValues[2].setText(ieasMessage.getSent());
		for(int i = 0; i < alertEnumValues[3].getItemCount(); i++)
		{
			if(alertEnumValues[3].getItemAt(i).equals(ieasMessage.getStatus()))
			{				
				alertEnumValues[3].setSelectedIndex(i);
			}				
		}
		for(int i = 0; i < alertEnumValues[4].getItemCount(); i++)
		{
			if(alertEnumValues[4].getItemAt(i).equals(ieasMessage.getMsgType()))
			{
				alertEnumValues[4].setSelectedIndex(i);
			}				
		}
		for(int i = 0; i < alertEnumValues[5].getItemCount(); i++)
		{
			if(alertEnumValues[5].getItemAt(i).equals(ieasMessage.getScope()))
			{
				alertEnumValues[5].setSelectedIndex(i);
			}				
		}
		alertValues[6].setText(ieasMessage.getCode());
	}

	private void setInfoPanel(KieasMessageBuilder ieasMessage)
	{
		infoValues[0].setText(ieasMessage.getLanguage());
		for(int i = 0; i < infoEnumValues[1].getItemCount(); i++)
		{
			if(infoEnumValues[1].getItemAt(i).equals(ieasMessage.getCategory()))
			{
				infoEnumValues[1].setSelectedIndex(i);
			}				
		}
		infoValues[2].setText(ieasMessage.getEvent());
		for(int i = 0; i < infoEnumValues[3].getItemCount(); i++)
		{
			if(infoEnumValues[3].getItemAt(i).equals(ieasMessage.getUrgency()))
			{
				infoEnumValues[3].setSelectedIndex(i);
			}				
		}
		for(int i = 0; i < infoEnumValues[4].getItemCount(); i++)
		{
			if(infoEnumValues[4].getItemAt(i).equals(ieasMessage.getSeverity()))
			{
				infoEnumValues[4].setSelectedIndex(i);
			}				
		}
		for(int i = 0; i < infoEnumValues[5].getItemCount(); i++)
		{
			if(infoEnumValues[5].getItemAt(i).equals(ieasMessage.getCertainty()))
			{
				infoEnumValues[5].setSelectedIndex(i);
			}				
		}
		infoValues[6].setText(ieasMessage.getEventCode());
		infoValues[7].setText(ieasMessage.getEffective());
		infoValues[8].setText(ieasMessage.getSenderName());
		infoValues[9].setText(ieasMessage.getWeb());
		infoValues[10].setText(ieasMessage.getContact());
		infoValues[11].setText(ieasMessage.getHeadline());
		infoDescriptionValues[0].setText(ieasMessage.getDescrpition());
	}

	private void capLoader()
	{
		try
		{
			String path = loadPathTextField.getText();

			String temp = "";	         
			String content = "";

			fileInputStream = new FileInputStream(new File(path));
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			while((temp = bufferedReader.readLine()) != null)
			{
				content += temp + "\n";
			}

			textArea.setText(content);
			textArea.setCaretPosition(0);
			ieasMessage.setMessage(content);

			setAlertPanel(ieasMessage);
			setInfoPanel(ieasMessage);
		}
		catch (IOException e)
		{	
			e.printStackTrace();
		}
	}

	private void capWriter()
	{
		this.stringReader = new StringReader(textArea.getText());
		bufferedReader = new BufferedReader(stringReader);

		//		String extension= ".xml";
		String path = savePathTextField.getText();
		String temp = "";

		try
		{
			this.bufferedWriter = new BufferedWriter(new FileWriter(path));

			while((temp = bufferedReader.readLine()) != null)
			{
				bufferedWriter.write(temp);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void loadCapDraft() 
	{
		capLoader();
	}

	public void saveCap() 
	{
		capWriter();
	}

	public void applyAlertElement() {
		ieasMessage.setIdentifier(alertValues[0].getText());
		ieasMessage.setSender(alertValues[1].getText());
		ieasMessage.setSent(alertValues[2].getText());
		ieasMessage.setStatus(alertValues[3].getText());
		ieasMessage.setMsgType(alertValues[4].getText());
		ieasMessage.setScope(alertValues[5].getText());
		ieasMessage.setCode(alertValues[6].getText());

		textArea.setText(ieasMessage.getMessage());
	}

	public void applyInfoElement() {
		ieasMessage.setIdentifier(infoValues[0].getText());
		ieasMessage.setSender(infoValues[1].getText());
		ieasMessage.setSent(infoValues[2].getText());
		ieasMessage.setStatus(infoValues[3].getText());
		ieasMessage.setMsgType(infoValues[4].getText());
		ieasMessage.setScope(infoValues[5].getText());
		ieasMessage.setCode(infoValues[6].getText());

		textArea.setText(ieasMessage.getMessage());
	}
}

