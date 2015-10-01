package kr.ac.uos.ai.ieas.gatewayModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class GatewayAlertTableModel {

	private DefaultTableModel tableModel;

	private ArrayList<String> columnNames;
	private ArrayList<String> rowData;

	private int alertCount;


	public GatewayAlertTableModel() {

		initTable();
	}

	private void initTable() {
		this.columnNames = new ArrayList<String>();

		columnNames.add("No.");
		columnNames.add("Sender");
		columnNames.add("Identifier");
		columnNames.add("Sent");
		columnNames.add("Event");
		columnNames.add("Addresses");
		columnNames.add("Ack");

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void addTableRowData(HashMap<String, String> alertElementMap) {
		alertCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alertCount));
		rowData.set(1, alertElementMap.get("sender"));
		rowData.set(2, alertElementMap.get("identifier"));
		rowData.set(3, alertElementMap.get("sent"));
		rowData.set(4, alertElementMap.get("event"));
		rowData.set(5, alertElementMap.get("addresses"));
		rowData.set(6, "NACK");

		tableModel.addRow(rowData.toArray());
	}

	public void receiveAck(String identifier) {

		for (int i=0; i<tableModel.getRowCount(); i++) {
			if(tableModel.getValueAt(i, 6).toString().equals("NACK")) {
				if(tableModel.getValueAt(i, 2).toString().equals(identifier)) {
					getTableModel().setValueAt("COMP", i, 6);
					return;
				}
			}
		}
	}
}
