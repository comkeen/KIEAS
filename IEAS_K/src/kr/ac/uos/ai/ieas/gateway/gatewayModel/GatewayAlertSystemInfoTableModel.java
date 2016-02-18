package kr.ac.uos.ai.ieas.gateway.gatewayModel;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class GatewayAlertSystemInfoTableModel {

	private DefaultTableModel tableModel;

	private ArrayList<String> columnNames;
	private ArrayList<String> rowData;

	private int alertSystemCount;


	public GatewayAlertSystemInfoTableModel() {

		initTable();
	}

	private void initTable() {
		this.columnNames = new ArrayList<String>();

		columnNames.add("No.");
		columnNames.add("AlertSystemID");
		columnNames.add("AlertSystemType");
		columnNames.add("GeoCode");

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
	}

	public void addTableRowData(Map<String, String> alertElementMap)
	{
		for(int i=0; i<tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 1).toString().equals(alertElementMap.get(_GatewayModelManager.SENDER)))
			{
				return;
			}
		}
		
		alertSystemCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alertSystemCount));
		rowData.set(1, alertElementMap.get(_GatewayModelManager.SENDER));
		rowData.set(2, alertElementMap.get(_GatewayModelManager.RESTRICTION));
		rowData.set(3, alertElementMap.get(_GatewayModelManager.NOTE));

		tableModel.addRow(rowData.toArray());
	}
	
//	public void addTableRowData(HashMap<String, String> alertElementMap)
//	{
//		for(int i=0; i<tableModel.getRowCount(); i++)
//		{
//			if(tableModel.getValueAt(i, 1).toString().equals(alertElementMap.get(GatewayModelManager.SENDER)))
//			{
//				return;
//			}
//		}
//		
//		alertSystemCount = tableModel.getRowCount()+1;
//		rowData.set(0, Integer.toString(alertSystemCount));
//		rowData.set(1, alertElementMap.get(GatewayModelManager.SENDER));
//
//		tableModel.addRow(rowData.toArray());
//	}

	public DefaultTableModel getTableModel() {		
		return tableModel;
	}
}

