package kr.or.kpew.kieas.gateway.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class GatewayAlerterInfoTableModel {

	private DefaultTableModel tableModel;

	private List<String> columnNames;
	private List<String> rowData;

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

	public void addTableRowData(Map<String, String> alertElementMap)
	{
		for(int i = 0; i < tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 1).toString().equals(alertElementMap.get(_GatewayModel.SENDER))){
				return;
			}
		}
		
		alerterCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alerterCount));
		rowData.set(1, alertElementMap.get(_GatewayModel.SENDER));

		tableModel.addRow(rowData.toArray());
	}

	public DefaultTableModel getTableModel()
	{		
		return tableModel;
	}
}

