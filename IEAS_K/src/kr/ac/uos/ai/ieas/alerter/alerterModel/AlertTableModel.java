package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class AlertTableModel {

	private DefaultTableModel tableModel;

	private ArrayList<String> columnNames;
	private ArrayList<String> rowData;

	private int alertCount;


	public AlertTableModel() {

		initTable();
	}

	private void initTable() {
		this.columnNames = new ArrayList<String>();

		columnNames.add("No.");
		columnNames.add("Sender");
		columnNames.add("Identifier");
		columnNames.add("Sent");
		columnNames.add("Event");
//		columnNames.add("Ack");
		
		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void addTableRowData(HashMap<String, String> alertElementMap) {
		alertCount = tableModel.getRowCount()+1;

		rowData.set(0, Integer.toString(alertCount));
//		for (int i = 1; i < columnNames.size(); i++)
//		{
//			System.out.println("column = " + columnNames.get(i));
//			addRowData(i, columnNames.get(i), alertElementMap);			
//		}		
		rowData.set(1, alertElementMap.get("sender"));
		rowData.set(2, alertElementMap.get("identifier"));
		rowData.set(3, alertElementMap.get("sent"));
		rowData.set(4, alertElementMap.get("event"));
//		rowData.set(5, "NACK");

		tableModel.addRow(rowData.toArray());
	}
	
	private void addRowData(int index, String key, HashMap<String, String> alertElementMap)
	{
		if(alertElementMap.get(key) == null)
		{
			rowData.set(index, "");
		}
		else
		{
			rowData.set(index, alertElementMap.get(key));			
		}
	}

	public void receiveAck(String identifier) {

		for (int i=0; i<tableModel.getRowCount(); i++) {
			if(tableModel.getValueAt(i, 5).toString().equals("NACK")) {
				if(tableModel.getValueAt(i, 2).toString().equals(identifier)) {
					getTableModel().setValueAt("COMP", i, 5);
					return;
				}
			}
		}
	}
}
