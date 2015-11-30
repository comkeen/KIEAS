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
import javax.swing.table.DefaultTableModel;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder.Item;


public class AlerterLogPanel 
{
	private static AlerterLogPanel alerterViewPanel;
	private AleterViewActionListener alerterActionListener;
	private KieasMessageBuilder kieasMessageBuilder;

	private JButton generateCapButton;
	private JButton messageSendButton;

	private JPanel logPanel;
	private JPanel buttonPane;
	private JScrollPane textAreaPane;
	private JTextArea textArea;
	private JComboBox<Item> geoCodeCombobox;
	private JComboBox<String> alertSystemTypeComboBox;
	private JScrollPane scrollPane;
	private GridBagConstraints gbc;

	private JTable alertTable;
	private DefaultTableModel alertModel;

	private String capMessage;
	private int alertCount;
	private String[] rowData;
	private String[] columnNames;
	private JButton textAreaMessageSendButton;
	private JButton connectServerButton;
	

	public static AlerterLogPanel getInstance(AleterViewActionListener alerterActionListener) 
	{
		if (alerterViewPanel == null)
		{
			alerterViewPanel = new AlerterLogPanel(alerterActionListener);
		}
		return alerterViewPanel;
	}

	
	private AlerterLogPanel(AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
		this.kieasMessageBuilder = new KieasMessageBuilder();
		this.gbc = new GridBagConstraints();
		
		initFrame("alertViewPanel");
	}
	
	public JPanel getLogPanel()
	{
		return this.logPanel;
	}
		
	public void setActionListener(AleterViewActionListener alerterActionListener)
	{
		this.alerterActionListener = alerterActionListener;
	}

	private void initFrame(String name)
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

	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}

	private void initTable() {

		alertCount = 1;

		initColumnNames();

		alertModel = new DefaultTableModel(columnNames, 0);

		this.alertTable = new JTable(alertModel);
		this.scrollPane = new JScrollPane(alertTable);

		this.initRowData();

		logPanel.add(scrollPane, gbc);
	}

	private void initColumnNames() {

		this.columnNames = new String[5];
		columnNames[0] = "No.";
		columnNames[1] = "MessageID";
		columnNames[2] = "Event";
		columnNames[3] = "TartgetArea";
		columnNames[4] = "Ack";
	}

	private void initRowData() {

		rowData = new String[5];
		rowData[0] = "no-string";
		rowData[1] = "MessgaeID-string";
		rowData[2] = "Event-string";
		rowData[3] = "Location-string";
		rowData[4] = "Ack-string{NACK,GWAY,COMP}";
	}

	private void initButtonPane() {
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
		
		this.connectServerButton = new JButton("ConnectServer");
		connectServerButton.addActionListener(alerterActionListener);
		buttonPane.add(connectServerButton, BorderLayout.EAST);

		logPanel.add(buttonPane, gbc);
	}

	private void initComboBox() {
		this.geoCodeCombobox = new JComboBox<Item>();
		geoCodeCombobox.addActionListener(alerterActionListener);
		for (Item item : kieasMessageBuilder.getCapEnumMap().get(KieasMessageBuilder.GEO_CODE))
		{
			geoCodeCombobox.addItem(item);
		}
		
		this.alertSystemTypeComboBox = new JComboBox<String>();
		alertSystemTypeComboBox.addActionListener(alerterActionListener);
		for (String item : KieasConfiguration.KieasList.ALERT_SYSTEM_TYPE_LIST)
		{
			alertSystemTypeComboBox.addItem(item);
		}
		buttonPane.add(geoCodeCombobox, BorderLayout.WEST);
		buttonPane.add(alertSystemTypeComboBox, BorderLayout.WEST);
	}

	private void initTextAreaPane() {
		this.textArea = new JTextArea(20, 20);
		this.textAreaPane = new JScrollPane(textArea);	

		textArea.setText("\n");

		logPanel.add(textAreaPane, gbc);
	}

	public void setTextArea(String message) {
		textArea.setText(message);
	}


	public void addAlertTableRow(String id, String event, String addresses) {
		rowData[0] = Integer.toString(alertCount++);
		rowData[1] = id;
		rowData[2] = event;
		rowData[3] = addresses;
		rowData[4] = "NACK";

		alertModel.addRow(rowData);
	}

	public void receiveGatewayAck(String identifier) {
		for(int i=0;i<alertModel.getRowCount();i++) {
			if(alertModel.getValueAt(i, 4).toString().equals("NACK")) {
				if(alertModel.getValueAt(i, 1).toString().equals(identifier)) {
					alertModel.setValueAt("GWAY", i, 4);
					return;
				}
			}
		}
	}

	public void receiveAlertSystemAck(String identifier) {
		for(int i=0;i<alertModel.getRowCount();i++) {
			if(alertModel.getValueAt(i, 4).toString().equals("GWAY")) {
				if(alertModel.getValueAt(i, 1).toString().equals(identifier)) {
					alertModel.setValueAt("COMP", i, 4);
					return;
				}
			}
		}
	}

	public JComboBox<Item> getLocationCombobox() {
		return geoCodeCombobox;
	}

	public JComboBox<String> getEventComboBox() {
		return alertSystemTypeComboBox;
	}


	public JTextArea getTextArea() {
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
