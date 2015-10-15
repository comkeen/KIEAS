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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.db.dbHandler._DatabaseHandler;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.IEAS_List;

public class AlerterCapGeneratePanel
{
	private static AlerterCapGeneratePanel alerterCapElementPanel;

	private AleterViewActionListener alerterActionListener;
	private GridBagConstraints gbc;
	private KieasMessageBuilder ieasMessage;
	private _DatabaseHandler databaseDriver;
	
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
	private JButton alertApplyButton;

	private JTabbedPane infoPanel;
	private JPanel infoIndexPanel;	
	private JTextField[][] infoValues;
	private JTextArea[] infoDescriptionValues;
	private JButton[] infoButtons;
	
	private JTextField savePathTextField;
	private JTextField loadPathTextField;

	private JTextArea infoDescriptionTextArea;




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
	
	private void setGbc(int gridx, int gridy)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
	}

	private void initTextArea()
	{
		this.textArea = new JTextArea(20, 20);
		this.textAreaPane = new JScrollPane(textArea);	

		textArea.setText("\n");

		capPanel.add(textAreaPane, gbc);
	}

	private void initCapAlertPanel()
	{
		this.alertPanel = new JPanel();
		alertPanel.setLayout(new GridBagLayout());
//		this.alertScrollPanel = new JScrollPane(alertPanel);

		this.alertValues = new JTextField[IEAS_List.ALERT_ELEMENT_LIST.length];
		
		//info가 여러개일 경우			
		int i = 0;
		for (String alertElementName : IEAS_List.ALERT_ELEMENT_LIST)
		{
			setGbc(0, i, 1, 1, 1, 1);
			createAndAddLable(alertElementName, alertPanel);
			setGbc(1, i, 9, 1, 9, 1);
			alertValues[i] = createAndAddTextField(alertPanel);
			
			i++;
		}
		
//		setGbc(0, i, 2, 1, 2, 1);		
//		this.alertApplyButton = createButton("Apply");
//		alertApplyButton.setActionCommand("AlertApply");
//		alertPanel.add(alertApplyButton, gbc);
		
//		capPanel.add(alertScrollPanel, gbc);
		capPanel.add(alertPanel, gbc);
	}

	private void initCapInfoPanel()
	{
		this.infoPanel = new JTabbedPane();
		
		this.infoValues = new JTextField[2][IEAS_List.INFO_ELEMENT_LIST.length];
		this.infoDescriptionValues = new JTextArea[2];
		this.infoButtons = new JButton[2];
		
		// TODO Auto-generated method stub
		//info가 여러개일 경우 처리
		for (int infoCounter = 1; infoCounter < 3; infoCounter++)
		{
			this.infoIndexPanel = new JPanel();
			infoIndexPanel.setLayout(new GridBagLayout());
//			this.infoScrollPanel = new JScrollPane(infoIndexPanel);			
			
			int i = 0;
			for (String infoElementName : IEAS_List.INFO_ELEMENT_LIST)
			{
				if (infoElementName.equals("Description"))
				{
					setGbc(0, i, 1, 1, 1, 1);
					createAndAddLable(infoElementName, infoIndexPanel);
					setGbc(1, i, 9, 5, 9, 5);
					infoDescriptionValues[infoCounter-1] = new JTextArea();
					JScrollPane descriptionScrollPanel = new JScrollPane(infoDescriptionValues[infoCounter-1]);
					infoIndexPanel.add(descriptionScrollPanel, gbc);
					
					i++;
				}
				else 
				{
					
				setGbc(0, i, 1, 1, 1, 1);
				createAndAddLable(infoElementName, infoIndexPanel);
				setGbc(1, i, 9, 1, 9, 1);
				infoValues[infoCounter-1][i] = createAndAddTextField(infoIndexPanel);
								
				i++;
				}
			}
			
//			setGbc(0, i, 2, 1, 2, 1);
//			infoButtons[infoCounter - 1] = createAndAddInfoApplyButton("Apply", infoCounter);
//			infoIndexPanel.add(alertApplyButton, gbc);			
			
//			infoPanel.addTab("Info" + infoCounter, infoScrollPanel);		
			infoPanel.addTab("Info" + infoCounter, infoIndexPanel);	
		}
		capPanel.add(infoPanel, gbc);
	}


	private void createAndAddLable(String name, JPanel panel)
	{
		JLabel label = new JLabel(name);
		panel.add(label, gbc);
	}
	
	private JTextField createAndAddTextField(JPanel panel)
	{
		JTextField textField = new JTextField();
		panel.add(textField, gbc);		
		
		return textField;
	}
	
	private JButton createAndAddInfoApplyButton(String name, int index)
	{
		JButton button = new JButton(name);
		button.setActionCommand(name + index);
		infoIndexPanel.add(button, gbc);
				
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
			ieasMessage.setMessage(content);
			
			setAlertPanel(ieasMessage);
			setInfoPanel(ieasMessage);
		}
		catch (IOException e)
		{	
			e.printStackTrace();
		}
	}
		
	private void setAlertPanel(KieasMessageBuilder ieasMessage)
	{
		alertValues[0].setText(ieasMessage.getIdentifier());
		alertValues[1].setText(ieasMessage.getSender());
		alertValues[2].setText(ieasMessage.getSent());
		alertValues[3].setText(ieasMessage.getStatus());
		alertValues[4].setText(ieasMessage.getMsgType());
		alertValues[5].setText(ieasMessage.getScope());
		alertValues[6].setText(ieasMessage.getCode());
	}
	
	private void setInfoPanel(KieasMessageBuilder ieasMessage)
	{
		infoValues[0][0].setText(ieasMessage.getLanguage());
		infoValues[0][1].setText(ieasMessage.getCategory());
		infoValues[0][2].setText(ieasMessage.getEvent());
		infoValues[0][3].setText(ieasMessage.getUrgency());
		infoValues[0][4].setText(ieasMessage.getSeverity());
		infoValues[0][5].setText(ieasMessage.getCertainty());
		infoValues[0][6].setText(ieasMessage.getEventCode());
		infoValues[0][7].setText(ieasMessage.getEffective());
		infoValues[0][8].setText(ieasMessage.getSenderName());
		infoValues[0][9].setText(ieasMessage.getWeb());
		infoValues[0][10].setText(ieasMessage.getContact());
		infoValues[0][11].setText(ieasMessage.getHeadline());
		infoDescriptionValues[0].setText(ieasMessage.getDescrpition());
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
	
	//todo
	public void applyInfoElement() {
		ieasMessage.setIdentifier(infoValues[0][0].getText());
		ieasMessage.setSender(infoValues[0][1].getText());
		ieasMessage.setSent(infoValues[0][2].getText());
		ieasMessage.setStatus(infoValues[0][3].getText());
		ieasMessage.setMsgType(infoValues[0][4].getText());
		ieasMessage.setScope(infoValues[0][5].getText());
		ieasMessage.setCode(infoValues[0][6].getText());
		
		textArea.setText(ieasMessage.getMessage());
	}
}

