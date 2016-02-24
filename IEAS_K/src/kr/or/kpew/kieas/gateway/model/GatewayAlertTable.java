package kr.or.kpew.kieas.gateway.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class GatewayAlertTable
{
	private DefaultTableModel tableModel;

	private List<String> columnNames;
	private List<String> rowData;

	private int alertCount;


	public GatewayAlertTable()
	{
		initTable();
	}

	private void initTable()
	{
		this.columnNames = new ArrayList<String>();

		columnNames.add(_GatewayModel.NO);
		columnNames.add(_GatewayModel.SENDER);
		columnNames.add(_GatewayModel.IDENTIFIER);
		columnNames.add(_GatewayModel.SENT);
		columnNames.add(_GatewayModel.EVENT);
		columnNames.add(_GatewayModel.RESTRICTION);
//		columnNames.add(GatewayModelManager.GEO_CODE);
		columnNames.add(_GatewayModel.ACK);

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
	}

	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}

	public void addTableRowData(Map<String, String> alertElementMap)
	{
		alertCount = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(alertCount));
		rowData.set(1, alertElementMap.get(_GatewayModel.SENDER));
		rowData.set(2, alertElementMap.get(_GatewayModel.IDENTIFIER));
		rowData.set(3, alertElementMap.get(_GatewayModel.SENT));
		rowData.set(4, alertElementMap.get(_GatewayModel.EVENT));
		rowData.set(5, alertElementMap.get(_GatewayModel.RESTRICTION));
//		rowData.set(6, alertElementMap.get(GatewayModelManager.GEO_CODE));
		rowData.set(6, _GatewayModel.NACK);

		tableModel.addRow(rowData.toArray());
	}

	public void receiveAck(String identifier)
	{
		for (int i = 0; i < tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 6).toString().equals(_GatewayModel.NACK))
			{
				if(tableModel.getValueAt(i, 2).toString().equals(identifier))
				{
					getTableModel().setValueAt(_GatewayModel.COMP, i, 6);
					return;
				}
			}
		}
	}
}
