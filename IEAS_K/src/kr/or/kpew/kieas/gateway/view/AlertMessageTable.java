package kr.or.kpew.kieas.gateway.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import kr.or.kpew.kieas.common.KieasMessageBuilder;

public class AlertMessageTable
{
	private DefaultTableModel tableModel;

	private List<String> columnNames;
	private List<String> rowData;
	
	private Map<String, String> alertMessages;
	
	public enum CapFields {
		No,
		Sender,
		Identifier,
		Status,
		Sent,
		Event,
		Restriction,
		GeoCode,
		Note,
	}
	
	public enum Responses {
		ACK,
		NACK,
		COMP
	}


	public AlertMessageTable()
	{
		initTable();
	}

	private void initTable()
	{
		this.columnNames = new ArrayList<String>();

		columnNames.add(CapFields.No.name());
		columnNames.add(CapFields.Sender.name());
		columnNames.add(CapFields.Identifier.name());
		columnNames.add(CapFields.Sent.name());
		columnNames.add(CapFields.Event.name());
		columnNames.add(CapFields.Restriction.name());
//		columnNames.add(GatewayModelManager.GEO_CODE);
		columnNames.add(Responses.ACK.name());

		this.rowData = columnNames;

		this.tableModel = new DefaultTableModel(columnNames.toArray(), 0);
		alertMessages = new HashMap<>();
	}

	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}

//	public void addTableRowData(Map<String, String> alertElementMap)
	public void addTableRowData(KieasMessageBuilder builder)
	{
		
		
		int count = tableModel.getRowCount()+1;
		rowData.set(0, Integer.toString(count));
		rowData.set(1, builder.getSender());
		rowData.set(2, builder.getIdentifier());
		rowData.set(3, builder.getSent());
		rowData.set(4, builder.getEvent(0));
		rowData.set(5, builder.getRestriction());
		rowData.set(6, Responses.NACK.name());
		
		alertMessages.put(builder.getIdentifier(), builder.build());

		tableModel.addRow(rowData.toArray());
	}

	public void receiveAck(String identifier)
	{
		for (int i = 0; i < tableModel.getRowCount(); i++)
		{
			if(tableModel.getValueAt(i, 6).toString().equals(Responses.NACK.name()))
			{
				if(tableModel.getValueAt(i, 2).toString().equals(identifier))
				{
					getTableModel().setValueAt(Responses.ACK.name(), i, 6);
					return;
				}
			}
		}
	}
	
	public String getAlertMessage(String identifier) {
		return alertMessages.get(identifier);
	}
}
