package kr.ac.uos.ai.ieas.alerterView;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.ac.uos.ai.ieas.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.resource.IeasMessageBuilder;

public class AlerterCapElementPanel
{
	private static AlerterCapElementPanel alerterCapElementPanel;

	private AleterViewActionListener alerterActionListener;
	private GridBagConstraints gbc;
	
	private FileInputStream fileInputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private StringReader stringReader;
	
	private JPanel capPanel;
	private JPanel buttonPane;
	private JButton saveCapButton;
	private JButton loadCapDraftButton;

	private IeasMessageBuilder ieasMessage;

	private JTextArea textArea;
	private JScrollPane textAreaPane;
	private JScrollPane alertScrollPanel;
	private JPanel alertPanel;
	private JPanel infoPanel;
	private JTextField identifierValue;


	private JTextField senderValue;
	private JTextField SentValue;
	private JTextField MsgTypeValue;
	private JTextField ScopeValue;


	private JTextField savePathTextField;
	private JTextField loadPathTextField;


	public static AlerterCapElementPanel getInstance(AleterViewActionListener alerterActionListener)
	{
		if (alerterCapElementPanel == null)
		{
			alerterCapElementPanel = new AlerterCapElementPanel(alerterActionListener);
		}
		return alerterCapElementPanel;
	}


	private AlerterCapElementPanel(AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
		this.gbc = new GridBagConstraints();
		this.ieasMessage = new IeasMessageBuilder();

		initFrame("alertViewPanel");
	}

	private void initFrame(String name)
	{		
		this.capPanel = new JPanel();
		capPanel.setLayout(new GridBagLayout());

		gbc.anchor = GridBagConstraints.NORTH;

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 2, 2, 3, 2);
		initTextArea();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(2, 0, 1, 1, 1, 1);
		initCapAlert();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(2, 1, 1, 1, 1, 1);
		initCapInfo();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 2, 1, 1, 1, 1);
		initButtonPanel();
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

		textArea.setText("\n");

		capPanel.add(textAreaPane, gbc);
	}

	private void initCapAlert()
	{
		this.alertPanel = new JPanel();
		this.alertScrollPanel = new JScrollPane(alertPanel);
		alertPanel.setLayout(new GridLayout(2,4));
		
		JLabel identifier = new JLabel("Identifier");
		this.identifierValue = new JTextField();
		
		JLabel sender = new JLabel("Sender");
		this.senderValue = new JTextField();
		
		JLabel sent = new JLabel("Sent");
		this.SentValue = new JTextField();
		
		JLabel msgType = new JLabel("MsgType");
		this.MsgTypeValue = new JTextField();
		
		JLabel scope = new JLabel("Scope");
		this.ScopeValue = new JTextField();
		
		alertPanel.add(identifier,0);
		alertPanel.add(identifierValue,1);
		
		alertPanel.add(sender,2);
		alertPanel.add(senderValue,3);
		
		capPanel.add(alertScrollPanel, gbc);
	}

	private void initCapInfo()
	{
		this.infoPanel = new JPanel();
		
		capPanel.add(infoPanel, gbc);
	}

	private void initButtonPanel()
	{
		this.buttonPane = new JPanel();

		this.loadPathTextField = new JTextField("cap/cap.xml");
		buttonPane.add(loadPathTextField, BorderLayout.WEST);
		
		this.loadCapDraftButton = createButton("LoadCapDraft");
		buttonPane.add(loadCapDraftButton, BorderLayout.WEST);		
		
		this.saveCapButton = createButton("SaveCap");
		buttonPane.add(saveCapButton, BorderLayout.EAST);
		
		this.savePathTextField = new JTextField("cap/out.xml");
		buttonPane.add(savePathTextField, BorderLayout.EAST);

		capPanel.add(buttonPane, gbc);
	}

	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		button.addActionListener(alerterActionListener);
		return button;
	}

	public JPanel getCapElementPanel()
	{
		return this.capPanel;
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
}

