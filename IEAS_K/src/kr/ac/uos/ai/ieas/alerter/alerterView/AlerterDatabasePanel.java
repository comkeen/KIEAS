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
import javax.swing.JTextField;

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterModel.AlertTableModel;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterDatabasePanel {
	
	private static AlerterDatabasePanel alerterDataPane;	
	private AleterViewActionListener alerterActionListener;
	private KieasMessageBuilder kieasMessageBuilder;
	private GridBagConstraints gbc;
	
	private JPanel databasePanel;
	private JPanel dataButtonPane;

	private JButton queryButton;
	private JTextArea dataTextArea;
	private JScrollPane dataTextAreaPane;

	private JScrollPane tableScrollPane;
	private JTable alertTable;
	private AlertTableModel alertTableModel;
	private JScrollPane databaseScrollPanel;
	
	private HashMap<String, String> alertElementMap;
	private HashMap<String, String> alertMessageMap;
	
	private JTextField queryTextField;


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
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		this.alertElementMap = new HashMap<String, String>();
		this.alertMessageMap = new HashMap<String, String>();
		
		initAlertElementMap();
		
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
		initButtonPane();
		
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
	
	private void initButtonPane()
	{
		this.dataButtonPane = new JPanel();
		
		this.queryButton = new JButton("Query");
		queryButton.addActionListener(alerterActionListener);

		this.queryTextField = new JTextField();
		queryTextField.setSize(500, 100);
		queryTextField.setText("HRA");
		
		dataButtonPane.add(queryButton, BorderLayout.WEST);
		dataButtonPane.add(queryTextField, BorderLayout.WEST);

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
			kieasMessageBuilder.setMessage(message);
			putAlertMessageMap(kieasMessageBuilder.getIdentifier(), message);
		}
	}
	
	public void putAlertMessageMap(String key, String message)
	{
		alertMessageMap.put(key, message);
	}
	
	public String getAlertMessage(String identifier)
	{
		return alertMessageMap.get(identifier);
	}
	
	public HashMap<String, String> getAlertElementMap(String message) 
	{		
		kieasMessageBuilder.setMessage(message);
		alertElementMap.replace("sender", kieasMessageBuilder.getSender());
		alertElementMap.replace("identifier", kieasMessageBuilder.getIdentifier());
		alertElementMap.replace("sent", kieasMessageBuilder.transformToYmdhms(kieasMessageBuilder.getSent()));
		alertElementMap.replace("event", "Event");
//		alertElementMap.replace("event", kieasMessageBuilder.getEvent(0));
		
		return alertElementMap;
	}

	public void getQueryResult(ArrayList<String> results)
	{	
		System.out.println("panel getquery");
		alertTableModel.getTableModel().setRowCount(0);
		addAlertTableRow(results);
	}

	public String getQuery()
	{
		return queryTextField.getText();
	}
}
