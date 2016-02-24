package kr.or.kpew.kieas.issuer.view.resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.table.DefaultTableModel;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;

public class TableModel
{
	private DefaultTableModel tableModel;
	private IKieasMessageBuilder kieasMessageBuilder;
	
	private String[] indexedColumnNames;
	
	private static final String GET = "get";
	private static final String NO = "No.";

	
	public TableModel(String[] comlumnNames)
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		initTable(comlumnNames);
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

	public void addTableRowData(String message)
	{
		kieasMessageBuilder.setMessage(message);
		
		String[] rowData = new String[indexedColumnNames.length];
		
		rowData[0] = Integer.toString(tableModel.getRowCount() + 1);
		for(int i = 1; i < indexedColumnNames.length; i++)
		{		
			try
			{
				String methodName = GET + indexedColumnNames[i];
				Method method = kieasMessageBuilder.getClass().getMethod(methodName.trim());
				Object object = method.invoke(kieasMessageBuilder);
				System.out.println("methodName : " + methodName);
				System.out.println("invoke : " + object.toString());
				
				rowData[i] = object.toString();
			}
			catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException ex)
			{
				continue;
			}			
		}		
		tableModel.addRow(rowData);
	}
		
	public DefaultTableModel getTableModel()
	{
		return tableModel;
	}
}
