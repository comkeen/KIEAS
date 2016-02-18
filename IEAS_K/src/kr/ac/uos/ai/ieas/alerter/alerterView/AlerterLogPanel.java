package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterView.resource.AlertLogTableModel;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration.KieasList;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder.Item;


public class AlerterLogPanel 
{
	private _AlerterTopView alerterTopView;
	private AleterViewActionListener alerterActionListener;
	private KieasMessageBuilder kieasMessageBuilder;
	private GridBagConstraints gbc;

	private JPanel logPanel;
	private JPanel buttonPane;
	private JScrollPane textAreaPane;
	private JTextArea textArea;
	
	private JButton generateCapButton;
	private JButton messageSendButton;
	private JButton textAreaMessageSendButton;
	
	private JComboBox<Item> geoCodeCombobox;
	private JComboBox<String> alertSystemTypeComboBox;

	private JScrollPane tableScrollPane;
	private AlertLogTableModel alertLogTableModel;
	private JTable alertTable;
	
	
	public AlerterLogPanel(_AlerterTopView topView, AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
		this.alerterTopView = topView;
		this.kieasMessageBuilder = new KieasMessageBuilder();
		this.gbc = new GridBagConstraints();
		
		initFrame();
	}
	
	public JPanel getPanel()
	{
		return this.logPanel;
	}
		
	public void setActionListener(AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
	}

	private void initFrame()
	{		
		this.logPanel = new JPanel();
		logPanel.setLayout(new GridBagLayout());

		gbc.anchor = GridBagConstraints.NORTH;

		gbc.fill = GridBagConstraints.BOTH;
		this.setGbc(1, 0, 1, 1, 1, 1);
		this.initTextAreaPane();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(1, 1, 1, 1, 1, 2);
		this.initTable();

		this.setGbc(1, 2, 1, 1, 1, 1);
		this.initButtonPane();
		this.initComboBox();
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

	private void initTable()
	{
		this.alertLogTableModel = alerterTopView.getAlertLogTableModel();
		this.alertTable = new JTable(alertLogTableModel.getTableModel());
		this.tableScrollPane = new JScrollPane(alertTable);
		
		alertTable.setEnabled(true);
		alertTable.getSelectionModel().addListSelectionListener(alerterActionListener);

		logPanel.add(tableScrollPane, gbc);
	}

	private void initButtonPane()
	{
		this.buttonPane = new JPanel();

		this.generateCapButton = new JButton("GenerateCap");
		generateCapButton.addActionListener(alerterActionListener);
		buttonPane.add(generateCapButton, BorderLayout.WEST);

		this.messageSendButton = new JButton("Send");
		messageSendButton.addActionListener(alerterActionListener);
		buttonPane.add(messageSendButton, BorderLayout.EAST);
		
		this.textAreaMessageSendButton = new JButton("TextAreaSend");
		textAreaMessageSendButton.addActionListener(alerterActionListener);
		buttonPane.add(textAreaMessageSendButton, BorderLayout.EAST);
		
//		this.connectServerButton = new JButton("ConnectServer");
//		connectServerButton.addActionListener(alerterActionListener);
//		buttonPane.add(connectServerButton, BorderLayout.EAST);m,./>

		logPanel.add(buttonPane, gbc);
	}

	private void initComboBox()
	{
//		this.geoCodeCombobox = new JComboBox<Item>();
//		geoCodeCombobox.addActionListener(alerterActionListener);
//		for (Item item : kieasMessageBuilder.getCapEnumMap().get(KieasMessageBuilder.GEO_CODE))
//		{
//			geoCodeCombobox.addItem(item);
//		}
		
		this.alertSystemTypeComboBox = new JComboBox<String>();
		alertSystemTypeComboBox.addActionListener(alerterActionListener);
		for (String item : KieasList.ALERT_SYSTEM_TYPE_LIST)
		{
			alertSystemTypeComboBox.addItem(item);
		}
//		buttonPane.add(geoCodeCombobox, BorderLayout.WEST);
		buttonPane.add(alertSystemTypeComboBox, BorderLayout.WEST);
	}

	private void initTextAreaPane()
	{
		this.textArea = new JTextArea(20, 20);
		this.textAreaPane = new JScrollPane(textArea);	

		textArea.setText("\n");

		logPanel.add(textAreaPane, gbc);
	}

	public void setTextArea(String message)
	{
		textArea.setText(message);
	}
	
	public void setDataTextArea()
	{
		if(alertTable.getSelectedRow() > -1)
		{
			String identifier = alertLogTableModel.getTableModel().getValueAt(alertTable.getSelectedRow(), 2).toString();
			String message = alerterTopView.getAlertMessage(identifier);
			textArea.setText(message);
		}
	}

	public JComboBox<Item> getLocationCombobox()
	{
		return geoCodeCombobox;
	}

	public JComboBox<String> getEventComboBox()
	{
		return alertSystemTypeComboBox;
	}

	public JTextArea getTextArea()
	{
		return textArea;
	}

	public String getGeoCode()
	{
		return geoCodeCombobox.getSelectedItem().toString();
	}

	public String getAlertSystemType()
	{
		return alertSystemTypeComboBox.getSelectedItem().toString();
	}
}
