package kr.ac.uos.ai.ieas.gateway.gatewayModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class GatewayAlerterInfoTableModel {

	private DefaultTableModel tableModel;

	private ArrayList<String> columnNames;
	private ArrayList<String> rowData;

	private int alerterCount;


	public GatewayAlerterInfoTableModel()
	{
		initTable();
	}

	private void initTable()
	{
		this.columnNames = new ArrayList<String>();

		columnNames.add("No.");
		columnNames.add("AlerterID");

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
	}

	public void addTableRowData(HashMap<String, String> alertElementMap)
	{
		for(int i = 0; i < tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 1).toString().equals(alertElementMap.get(GatewayModelManager.SENDER))){
				return;
			}
		}
		
		alerterCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alerterCount));
		rowData.set(1, alertElementMap.get(GatewayModelManager.SENDER));

		tableModel.addRow(rowData.toArray());
	}

	public DefaultTableModel getTableModel()
	{		
		return tableModel;
	}
}

