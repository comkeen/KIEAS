package kr.or.kpew.kieas.gateway.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import kr.or.kpew.kieas.gateway.controller.GatewayController;

public class GatewayDataPane
{	
	private GatewayView topView;
	private GatewayController viewActionListener;
	private AlertMessageTable alertTableModel;

	private GridBagConstraints gbc;
	
	private JPanel dataPane;
	private JPanel dataButtonPane;

	private JTextArea dataTextArea;
	private JScrollPane dataTextAreaPane;

	private JScrollPane alertLogTableScrollPane;
	private JTable alertLogTable;


	public GatewayDataPane(GatewayView gatewayView, GatewayController gatewayActionListener)
	{		
		this.viewActionListener = gatewayActionListener;
		this.topView = gatewayView;
		this.gbc = new GridBagConstraints();

		initDataPane();
	}
	
	public JPanel getDataPane()
	{
		return dataPane;
	}
	
	private void initDataPane()
	{
		this.dataPane = new JPanel();
		dataPane.setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.SOUTH;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 2, 3, 1, 1, 1);
		initDataButtonPane();
		
		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 1, 3, 1, 1, 3);
		initDataTable();

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 3, 1, 1, 7);
		initDataTextAreaPane();
	}
	
	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}
	
	private void initDataButtonPane()
	{
		this.dataButtonPane = new JPanel();

		dataPane.add(dataButtonPane, gbc);
	}

	private void initDataTextAreaPane()
	{
		this.dataTextArea = new JTextArea(5, 20);
		this.dataTextAreaPane = new JScrollPane(dataTextArea);

		dataTextArea.setText("\n");

		dataPane.add(dataTextAreaPane, gbc);
	}
	
	private void initDataTable()
	{
		this.alertTableModel = topView.getAlertTableModel();
		this.alertLogTable = new JTable(alertTableModel.getTableModel());
		this.alertLogTableScrollPane = new JScrollPane(alertLogTable);

		alertLogTable.setEnabled(true);
		alertLogTable.getSelectionModel().addListSelectionListener(viewActionListener);
		dataPane.add(alertLogTableScrollPane, gbc);
	}

	public void setDataTextArea()
	{
		if(alertLogTable.getSelectedRow() > -1)
		{
			String identifier = alertTableModel.getTableModel().getValueAt(alertLogTable.getSelectedRow(), 2).toString();
			String message = alertTableModel.getAlertMessage(identifier);
			dataTextArea.setText(message);
		}
	}
	
	public boolean isAlertTableSelected()
	{
		if(alertLogTable.getSelectedRow() > -1)
		{
			return true;
		}
		return false;
	}
}
