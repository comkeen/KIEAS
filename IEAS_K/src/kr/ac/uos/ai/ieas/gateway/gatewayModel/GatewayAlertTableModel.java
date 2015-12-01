package kr.ac.uos.ai.ieas.gateway.gatewayModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class GatewayAlertTableModel {

	private DefaultTableModel tableModel;

	private ArrayList<String> columnNames;
	private ArrayList<String> rowData;

	private int alertCount;


	public GatewayAlertTableModel()
	{
		initTable();
	}

	private void initTable()
	{
		this.columnNames = new ArrayList<String>();

		columnNames.add(GatewayModelManager.NO);
		columnNames.add(GatewayModelManager.SENDER);
		columnNames.add(GatewayModelManager.IDENTIFIER);
		columnNames.add(GatewayModelManager.SENT);
		columnNames.add(GatewayModelManager.EVENT);
		columnNames.add(GatewayModelManager.RESTRICTION);
//		columnNames.add(GatewayModelManager.GEO_CODE);
		columnNames.add(GatewayModelManager.ACK);

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
	}

	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}

	public void addTableRowData(HashMap<String, String> alertElementMap)
	{
		alertCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alertCount));
		rowData.set(1, alertElementMap.get(GatewayModelManager.SENDER));
		rowData.set(2, alertElementMap.get(GatewayModelManager.IDENTIFIER));
		rowData.set(3, alertElementMap.get(GatewayModelManager.SENT));
		rowData.set(4, alertElementMap.get(GatewayModelManager.EVENT));
		rowData.set(5, alertElementMap.get(GatewayModelManager.RESTRICTION));
//		rowData.set(6, alertElementMap.get(GatewayModelManager.GEO_CODE));
		rowData.set(6, GatewayModelManager.NACK);

		tableModel.addRow(rowData.toArray());
	}

	public void receiveAck(String identifier)
	{
		for (int i = 0; i < tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 6).toString().equals(GatewayModelManager.NACK))
			{
				if(tableModel.getValueAt(i, 2).toString().equals(identifier))
				{
					getTableModel().setValueAt(GatewayModelManager.COMP, i, 6);
					return;
				}
			}
		}
	}
}
