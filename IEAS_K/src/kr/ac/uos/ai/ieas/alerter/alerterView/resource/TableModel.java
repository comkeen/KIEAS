package kr.ac.uos.ai.ieas.alerter.alerterView.resource;

import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class TableModel extends DefaultTableModel
{
	private DefaultTableModel tableModel;

	private String[] columnNames;
	private List<String> rowData;

	private int alertCount;

	public TableModel(String[] comlumnNames)
	{
		initTable(comlumnNames);
	}
	
	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}

	public void initTable(String[] comlumnNames)
	{
		this.columnNames = comlumnNames;

		this.tableModel = new DefaultTableModel(columnNames, 0);
	}

	public void addTableRowData(Map<String, String> alertElementMap)
	{
		alertCount = tableModel.getRowCount() + 1;
		rowData.set(0, Integer.toString(alertCount));
		rowData.set(1, alertElementMap.get(KieasMessageBuilder.SENDER));
		rowData.set(2, alertElementMap.get(KieasMessageBuilder.IDENTIFIER));
		rowData.set(3, alertElementMap.get(KieasMessageBuilder.SENT));
		rowData.set(4, alertElementMap.get(KieasMessageBuilder.EVENT));

		tableModel.addRow(rowData.toArray());
	}
}
