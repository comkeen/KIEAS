package kr.ac.uos.ai.ieas.alerterView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;

import kr.ac.uos.ai.ieas.abstractClass.AbstractView;
import kr.ac.uos.ai.ieas.alerterController.AlerterController;
import kr.ac.uos.ai.ieas.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.gatewayController.GatewayActionListener;
import kr.ac.uos.ai.ieas.gatewayView.GatewayLogPane;
import kr.ac.uos.ai.ieas.resource.IeasConfiguration;


public class AlerterLogPane extends AbstractView{

	private static AlerterLogPane alerterViewPanel;
	private AlerterController alerterController;
	private AleterViewActionListener alerterActionListener;
	private IeasArcGisMap ieasArcGisMap;

	private JFrame frame;
	private JButton generateCapButton;
	private JButton messageSendButton;

	private JPanel logPanel;
	private JPanel buttonPane;
	private JScrollPane textAreaPane;
	private JTextArea textArea;
	private JComboBox<String> locationCombobox;
	private JComboBox<String> eventComboBox;
	private JScrollPane scrollPane;
	private GridBagConstraints gbc;

	private JTable alertTable;
	private DefaultTableModel alertModel;

	private String capMessage;
	private String[] rowData;
	private int alertCount;
	private String[] columnNames;
	private JButton textAreaMessageSendButton;
	private JButton saveCapButton;
	

	public static AlerterLogPane getInstance(AlerterController alerterController) {
		if (alerterViewPanel == null) {

			alerterViewPanel = new AlerterLogPane(alerterController);
		}
		return alerterViewPanel;
	}

	
	private AlerterLogPane(AlerterController alerterController) {

		this.alerterController = alerterController;
		this.alerterActionListener = new AleterViewActionListener(alerterController);
		this.gbc = new GridBagConstraints();
		
		initLookAndFeel();
		initFrame("alertViewPanel");
	}
	
	public JPanel getLogPane(){
		return this.logPanel;
	}
	

	public void modelPropertyChange(PropertyChangeEvent evt){
		
		System.out.println("trigger property change");
		switch (evt.getPropertyName()){
		
		case AlerterController.ALERT_TEXTAREA_TEXT_PROPERTY:
			textArea.setText(evt.getNewValue().toString());
			System.out.println("text area propertychange");
			break;
		default:
			return;
		}
	}
	
	public void setActionListener(AleterViewActionListener alerterActionListener) {
		this.alerterActionListener = alerterActionListener;
	}
	
	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initFrame(String name) {
		
		this.logPanel = new JPanel();
		logPanel.setLayout(new GridBagLayout());

		gbc.anchor = GridBagConstraints.NORTH;

		gbc.fill = GridBagConstraints.BOTH;
		this.setGbc(1, 0, 1, 1, 1, 1);
		this.initTextAreaPane();
		
		gbc.fill = GridBagConstraints.BOTH;
		this.setGbc(2, 0, 1, 3, 5, 1);
//		this.initMap();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(1, 1, 1, 1, 1, 2);
		this.initTable();

		this.setGbc(1, 2, 1, 1, 1, 1);
		this.initButtonPane();
		this.initComboBox();

		frame.setVisible(true);
	}

	private void initMap() {
		
		this.ieasArcGisMap = new IeasArcGisMap();
		logPanel.add(ieasArcGisMap.getMapPane(), gbc);
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
		
		this.saveCapButton = new JButton("SaveCap");
		saveCapButton.addActionListener(alerterActionListener);
		buttonPane.add(saveCapButton, BorderLayout.EAST);

		logPanel.add(buttonPane, gbc);
	}

	private void initComboBox() {
		this.locationCombobox = new JComboBox<String>();
		locationCombobox.addActionListener(alerterActionListener);
		this.eventComboBox = new JComboBox<String>();
		eventComboBox.addActionListener(alerterActionListener);

		for (String location : IeasConfiguration.IEAS_List.LOCATION_LIST) {
			locationCombobox.addItem(location);
		};
		for (String event : IeasConfiguration.IEAS_List.EVENT_LIST) {
			eventComboBox.addItem(event);
		}

		buttonPane.add(locationCombobox, BorderLayout.WEST);
		buttonPane.add(eventComboBox, BorderLayout.WEST);
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

	public void saveCap() {
		alerterController.saveCap(capMessage);
	}

	public JComboBox<String> getLocationCombobox() {
		return this.locationCombobox;
	}

	public JComboBox<String> getEventComboBox() {
		return this.eventComboBox;
	}

}
