package kr.ac.uos.ai.ieas.gateway.gatewayView;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import kr.ac.uos.ai.ieas.gateway.gatewayController.GatewayActionListener;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertTableModel;

public class GatewayDataPane {
	
	private static GatewayDataPane gatewayDataPane;

	private GatewayView gatewayView;
	private GatewayActionListener gatewayActionListener;
	private GatewayAlertTableModel alertTableModel;

	private GridBagConstraints gbc;
	
	private JPanel dataPane;
	private JPanel dataButtonPane;

	private JButton testTableButton;
	private JTextArea dataTextArea;
	private JScrollPane dataTextAreaPane;

	private JScrollPane tableScrollPane;
	private JTable alertTable;



	public static GatewayDataPane getInstance(GatewayView gatewayView, GatewayActionListener gatewayActionListener) {
		if (gatewayDataPane == null) {

			gatewayDataPane = new GatewayDataPane(gatewayView, gatewayActionListener);
		}
		return gatewayDataPane;
	}

	private GatewayDataPane(GatewayView gatewayView, GatewayActionListener gatewayActionListener) {
		
		this.gatewayActionListener = gatewayActionListener;
		this.gatewayView = gatewayView;
		this.gbc = new GridBagConstraints();

		initDataPane();
	}
	
	public JPanel getDataPane() {
		return dataPane;
	}
	
	private void initDataPane() {

		this.dataPane = new JPanel();
		dataPane.setLayout(new GridBagLayout());
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
	
	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}
	
	private void initDataButtonPane() {
		this.dataButtonPane = new JPanel();
		
		this.testTableButton = new JButton("TestTable");
		testTableButton.addActionListener(gatewayActionListener);

		dataButtonPane.add(testTableButton, BorderLayout.WEST);

		dataPane.add(dataButtonPane, gbc);
	}

	private void initDataTextAreaPane() {
		this.dataTextArea = new JTextArea(5, 20);
		this.dataTextAreaPane = new JScrollPane(dataTextArea);

		dataTextArea.setText("\n");

		dataPane.add(dataTextAreaPane, gbc);
	}
	
	private void initDataTable() {

		this.alertTableModel = gatewayView.getAlertTableModel();
		this.alertTable = new JTable(alertTableModel.getTableModel());
		this.tableScrollPane = new JScrollPane(alertTable);

		alertTable.setEnabled(true);
		alertTable.getSelectionModel().addListSelectionListener(gatewayActionListener);
		dataPane.add(tableScrollPane, gbc);
	}

	public void setDataTextArea() {
		if(alertTable.getSelectedRow() > -1) {
			String identifier = alertTableModel.getTableModel().getValueAt(alertTable.getSelectedRow(), 2).toString();
			String message = gatewayView.getAlertMessage(identifier);
			dataTextArea.setText(message);
		}
	}
	
	public boolean isAlertTableSelected() {
		if(alertTable.getSelectedRow() > -1) {
			return true;
		}
		return false;
	}
}
