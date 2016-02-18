package kr.ac.uos.ai.ieas.gateway.gatewayModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class GatewayAlertTableModel
{
	private DefaultTableModel tableModel;

	private List<String> columnNames;
	private List<String> rowData;

	private int alertCount;


	public GatewayAlertTableModel()
	{
		initTable();
	}

	private void initTable()
	{
		this.columnNames = new ArrayList<String>();

		columnNames.add(_GatewayModelManager.NO);
		columnNames.add(_GatewayModelManager.SENDER);
		columnNames.add(_GatewayModelManager.IDENTIFIER);
		columnNames.add(_GatewayModelManager.SENT);
		columnNames.add(_GatewayModelManager.EVENT);
		columnNames.add(_GatewayModelManager.RESTRICTION);
//		columnNames.add(GatewayModelManager.GEO_CODE);
		columnNames.add(_GatewayModelManager.ACK);

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
		rowData.set(1, alertElementMap.get(_GatewayModelManager.SENDER));
		rowData.set(2, alertElementMap.get(_GatewayModelManager.IDENTIFIER));
		rowData.set(3, alertElementMap.get(_GatewayModelManager.SENT));
		rowData.set(4, alertElementMap.get(_GatewayModelManager.EVENT));
		rowData.set(5, alertElementMap.get(_GatewayModelManager.RESTRICTION));
//		rowData.set(6, alertElementMap.get(GatewayModelManager.GEO_CODE));
		rowData.set(6, _GatewayModelManager.NACK);

		tableModel.addRow(rowData.toArray());
	}

	public void receiveAck(String identifier)
	{
		for (int i = 0; i < tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 6).toString().equals(_GatewayModelManager.NACK))
			{
				if(tableModel.getValueAt(i, 2).toString().equals(identifier))
				{
					getTableModel().setValueAt(_GatewayModelManager.COMP, i, 6);
					return;
				}
			}
		}
	}
}
