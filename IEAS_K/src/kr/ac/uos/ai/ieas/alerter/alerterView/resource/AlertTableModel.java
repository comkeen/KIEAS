package kr.ac.uos.ai.ieas.alerter.alerterView.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class AlertTableModel
{
	private DefaultTableModel tableModel;

	private List<String> columnNames;
	private List<String> rowData;

	private int alertCount;

	public static final String NO = "No.";
	public static final String ACK = "ACK";
	public static final String NACK = "NACK";
	public static final String COMP = "COMP";

	public AlertTableModel() { initTable(); }
	public DefaultTableModel getTableModel() { return tableModel; }

	private void initTable()
	{
		this.columnNames = new ArrayList<String>();

		columnNames.add(NO);
		columnNames.add(KieasMessageBuilder.SENDER);
		columnNames.add(KieasMessageBuilder.IDENTIFIER);
		columnNames.add(KieasMessageBuilder.SENT);
		columnNames.add(KieasMessageBuilder.EVENT);

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
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
