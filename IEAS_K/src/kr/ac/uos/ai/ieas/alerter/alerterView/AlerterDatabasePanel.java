package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterModel.AlertTableModel;
import kr.ac.uos.ai.ieas.db.dbDriver.DatabaseDriver;
import kr.ac.uos.ai.ieas.resource.IeasMessageBuilder;


public class AlerterDatabasePanel {
	
	private static AlerterDatabasePanel alerterDataPane;
	private AleterViewActionListener alerterActionListener;
	private DatabaseDriver databaseDriver;
	private IeasMessageBuilder ieasMessageBuilder;
	private GridBagConstraints gbc;
	
	private JPanel databasePanel;
	private JPanel dataButtonPane;

	private JButton hraButton;
	private JTextArea dataTextArea;
	private JScrollPane dataTextAreaPane;

	private JScrollPane tableScrollPane;
	private JTable alertTable;
	private AlertTableModel alertTableModel;
	private JScrollPane databaseScrollPanel;
	private ArrayList<String> results;
	
	private HashMap<String, String> alertElementMap;
	private HashMap<String, String> alertMessageMap;
	private JButton hrwButton;


	public static AlerterDatabasePanel getInstance(AleterViewActionListener alerterActionListener)
	{
		if (alerterDataPane == null)
		{
			alerterDataPane = new AlerterDatabasePanel(alerterActionListener);
		}
		return alerterDataPane;
	}

	private AlerterDatabasePanel(AleterViewActionListener alerterActionListener)
	{		
		this.alerterActionListener = alerterActionListener;
		this.gbc = new GridBagConstraints();
		this.alertTableModel = new AlertTableModel();
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
		this.ieasMessageBuilder = new IeasMessageBuilder();
		initAlertElementMap();
		this.databaseDriver = new DatabaseDriver();

		
		initDatabasePanel();
	}
	
	private void initAlertElementMap() {

		String sender = "sender";
		String identifier = "identifier";
		String sent = "sent";
		String event = "event";
		String ack = "ack";
		
		alertElementMap.put(sender, sender);
		alertElementMap.put(identifier, identifier);
		alertElementMap.put(sent, sent);
		alertElementMap.put(event, event);
		alertElementMap.put(ack, ack);
	}
	
	private void initDatabasePanel()
	{
		this.databasePanel = new JPanel();
		this.databaseScrollPanel = new JScrollPane(databasePanel);
		
		databasePanel.setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.SOUTH;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 2, 3, 1, 1, 1);
		initDataButtonPane();
		
		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 1, 3, 1, 1, 3);
		initDataTable();

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 3, 1, 1, 7);
		initDataTextAreaPane();
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
	
	private void initDataButtonPane()
	{
		this.dataButtonPane = new JPanel();
		
		this.hraButton = new JButton("HRA");
		hraButton.addActionListener(alerterActionListener);
		
		this.hrwButton = new JButton("HRW");
		hrwButton.addActionListener(alerterActionListener);

		dataButtonPane.add(hraButton, BorderLayout.WEST);
		dataButtonPane.add(hrwButton, BorderLayout.WEST);

		databasePanel.add(dataButtonPane, gbc);
	}

	private void initDataTextAreaPane() {
		this.dataTextArea = new JTextArea(5, 20);
		this.dataTextAreaPane = new JScrollPane(dataTextArea);

		dataTextArea.setText("\n");

		databasePanel.add(dataTextAreaPane, gbc);
	}
	
	private void initDataTable() {

		this.alertTable = new JTable(alertTableModel.getTableModel());
		this.tableScrollPane = new JScrollPane(alertTable);

		alertTable.setEnabled(true);
		alertTable.getSelectionModel().addListSelectionListener(alerterActionListener);
		databasePanel.add(tableScrollPane, gbc);
	}

	public void setDataTextArea() 
	{
		if(alertTable.getSelectedRow() >= 0)
		{
			String identifier = alertTableModel.getTableModel().getValueAt(alertTable.getSelectedRow(), 2).toString();			
			String message = getAlertMessage(identifier);
			dataTextArea.setText(message);
		}
	}
	
	public boolean isAlertTableSelected() 
	{
		if(alertTable.getSelectedRow() >= 0) 
		{
			return true;
		}
		return false;
	}

	public Component getPanel()
	{
		return this.databaseScrollPanel;
	}

	public void selectTableEvent()
	{
		setDataTextArea();
	}
	
	public void addAlertTableRow(ArrayList<String> results) 
	{		
		for (String message : results)
		{
			alertTableModel.addTableRowData(getAlertElementMap(message));	
			ieasMessageBuilder.setMessage(message);
			putAlertMessageMap(ieasMessageBuilder.getIdentifier(), message);
		}		
	}
	
	public void putAlertMessageMap(String key, String message) {
		alertMessageMap.put(key, message);
	}
	
	public String getAlertMessage(String identifier) {
		return alertMessageMap.get(identifier);
	}
	
	public HashMap<String, String> getAlertElementMap(String message) {
		
		ieasMessageBuilder.setMessage(message);
		alertElementMap.replace("sender", ieasMessageBuilder.getSender());
		alertElementMap.replace("identifier", ieasMessageBuilder.getIdentifier());
		alertElementMap.replace("sent", ieasMessageBuilder.getSent());
		alertElementMap.replace("event", ieasMessageBuilder.getEvent());
		
		return alertElementMap;
	}

	public void getHRAResult() {
		this.results = databaseDriver.getHRAResult();
		addAlertTableRow(results);
	}
	
	public void getHRWResult() {
		this.results = databaseDriver.getHRWResult();
		addAlertTableRow(results);
	}
}
