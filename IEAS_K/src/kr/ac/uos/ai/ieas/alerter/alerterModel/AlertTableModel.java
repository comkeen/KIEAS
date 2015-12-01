package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class AlertTableModel
{
	private DefaultTableModel tableModel;

	private ArrayList<String> columnNames;
	private ArrayList<String> rowData;

	private int alertCount;


	public AlertTableModel() 
	{
		initTable();
	}

	private void initTable()
	{
		this.columnNames = new ArrayList<String>();

		columnNames.add(_AlerterModelManager.NO);
		columnNames.add(_AlerterModelManager.SENDER);
		columnNames.add(_AlerterModelManager.IDENTIFIER);
		columnNames.add(_AlerterModelManager.SENT);
		columnNames.add(_AlerterModelManager.EVENT);
		columnNames.add(_AlerterModelManager.RESTRICTION);
//		columnNames.add(_AlerterModelManager.GEO_CODE);
		columnNames.add(_AlerterModelManager.ACK);

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
	}

	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}

	public void addTableRowData(HashMap<String, String> alertElementMap)
	{
		alertCount = tableModel.getRowCount() + 1;
		rowData.set(0, Integer.toString(alertCount));
		rowData.set(1, alertElementMap.get(_AlerterModelManager.SENDER));
		rowData.set(2, alertElementMap.get(_AlerterModelManager.IDENTIFIER));
		rowData.set(3, alertElementMap.get(_AlerterModelManager.SENT));
		rowData.set(4, alertElementMap.get(_AlerterModelManager.EVENT));
		rowData.set(5, alertElementMap.get(_AlerterModelManager.RESTRICTION));
//		rowData.set(6, alertElementMap.get(_AlerterModelManager.GEO_CODE));
		rowData.set(6, _AlerterModelManager.NACK);

		tableModel.addRow(rowData.toArray());
	}

	public void receiveAck(String identifier)
	{
		for (int i=0; i<tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 6).toString().equals(_AlerterModelManager.NACK))
			{
				if(tableModel.getValueAt(i, 2).toString().equals(identifier))
				{
					getTableModel().setValueAt(_AlerterModelManager.COMP, i, 6);
					return;
				}
			}
		}
	}
}
