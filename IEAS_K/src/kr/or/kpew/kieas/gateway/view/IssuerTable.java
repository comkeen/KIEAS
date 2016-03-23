package kr.or.kpew.kieas.gateway.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import kr.or.kpew.kieas.common.IssuerProfile;

public class IssuerTable {

	private DefaultTableModel tableModel;

	private List<String> columnNames;
	private List<String> rowData;

	private int alerterCount;


	public IssuerTable()
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

	public void addTableRowData(IssuerProfile issuer)
	{
		String issuerId = issuer.getSender();
		for(int i = 0; i < tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 1).toString().equals(issuerId)){
				return;
			}
		}
		
		alerterCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alerterCount));
		rowData.set(1, issuerId);
		
		tableModel.addRow(rowData.toArray());
	}

	public DefaultTableModel getTableModel()
	{		
		return tableModel;
	}
}

