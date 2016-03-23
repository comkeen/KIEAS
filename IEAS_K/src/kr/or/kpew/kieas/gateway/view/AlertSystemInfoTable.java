package kr.or.kpew.kieas.gateway.view;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import kr.or.kpew.kieas.common.AlertSystemProfile;

public class AlertSystemInfoTable {

	private DefaultTableModel tableModel;

	private ArrayList<String> columnNames;
	private ArrayList<String> rowData;

	private int alertSystemCount;


	public AlertSystemInfoTable() {

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

	public void addTableRowData(AlertSystemProfile profile)
	{
		for(int i=0; i<tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 1).toString().equals(profile.getSender()))
			{
				return;
			}
		}
		
		alertSystemCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alertSystemCount));

		rowData.set(1, profile.getSender());
		rowData.set(2, profile.getName());
		rowData.set(3, profile.getGeoCode());
		
		tableModel.addRow(rowData.toArray());
	}

	public DefaultTableModel getTableModel() {		
		return tableModel;
	}
}

