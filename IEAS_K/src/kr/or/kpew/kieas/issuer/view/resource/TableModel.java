package kr.or.kpew.kieas.issuer.view.resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.table.DefaultTableModel;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.gateway.view.AlertMessageTable.Responses;
import kr.or.kpew.kieas.issuer.model.AlertLogger.MessageAckPair;

public class TableModel
{
	private DefaultTableModel tableModel;
	private IKieasMessageBuilder kieasMessageBuilder;
	
	private String[] indexedColumnNames;
	
	private static final String GET = "get";
	private static final String NO = "No.";

	
	public TableModel(String[] columnNames)
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		initTable(columnNames);
	}

	public void initTable(String[] columnNames)
	{		
		this.indexedColumnNames = new String[columnNames.length + 1];
		
		indexedColumnNames[0] = NO;
		for(int i = 0; i < columnNames.length; i++)
		{
			indexedColumnNames[i + 1] = columnNames[i];
		}
		
		this.tableModel = new DefaultTableModel(indexedColumnNames, 0);
	}

	public void updateTable(Object object)
	{
		String message = null;
		String state = null;
		if(object instanceof MessageAckPair)
		{
			MessageAckPair pair = (MessageAckPair) object;
			message = pair.getMessage();
			state = pair.getState();
		}
		else if(object instanceof String)
		{
			message = (String) object;
		}		
		//ack update
		kieasMessageBuilder.parse(message);
		String identifier = kieasMessageBuilder.getIdentifier();
		for (int i = 0; i < tableModel.getRowCount(); i++)
		{		
			if(tableModel.getValueAt(i, 1).toString().equals(identifier))
			{
				getTableModel().setValueAt(state, i, 4);
				return;
			}
		}
		//addTableRow
		String[] rowData = new String[indexedColumnNames.length + 1];
		
		rowData[0] = Integer.toString(tableModel.getRowCount() + 1);
		for(int j = 1; j < rowData.length - 2; j++)
		{		
			String methodName = GET + indexedColumnNames[j];
			try
			{
				Method method = kieasMessageBuilder.getClass().getMethod(methodName.trim());
				Object returnObject = method.invoke(kieasMessageBuilder);
				
				rowData[j] = returnObject.toString();
			}
			catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException ex)
			{
				System.out.println("TableModel: there is no such a method name : " + methodName);
				continue;
			}		
		}	
		rowData[4] = Responses.NACK.toString();
		tableModel.addRow(rowData);
		return;		
	}
		
	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}
}
