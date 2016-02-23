package kr.ac.uos.ai.ieas.alerter.alerterView.resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import kr.ac.uos.ai.ieas.resource.IKieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class TableModel
{
	private DefaultTableModel tableModel;
	private IKieasMessageBuilder kieasMessageBuilder;
	
	private String[] columnNames;
	private String message;

	private int alertCount;
	
	private static final String GET = "get";

	
	public TableModel(String[] comlumnNames)
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		initTable(comlumnNames);
	}

	public void initTable(String[] comlumnNames)
	{
		this.columnNames = comlumnNames;
		this.tableModel = new DefaultTableModel(columnNames, 0);
		
		this.alertCount = 0;
	}

	public void addTableRowData(String message)
	{
		kieasMessageBuilder.setMessage(message);
		alertCount = tableModel.getRowCount() + 1;

		Method method = null;
		try
		{
			for(int i = 0; i < columnNames.length; i++)
			{		
				String methodName = GET + columnNames[i];
				method = kieasMessageBuilder.getClass().getMethod(methodName.trim());
				method.invoke(kieasMessageBuilder);
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
//		rowData.set(0, Integer.toString(alertCount));
//		rowData.set(1, alertElementMap.get(KieasMessageBuilder.SENDER));
//		rowData.set(2, alertElementMap.get(KieasMessageBuilder.IDENTIFIER));
//		rowData.set(3, alertElementMap.get(KieasMessageBuilder.SENT));
//		rowData.set(4, alertElementMap.get(KieasMessageBuilder.EVENT));

//		tableModel.addRow(rowData.toArray());
	}
		
	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}
}
