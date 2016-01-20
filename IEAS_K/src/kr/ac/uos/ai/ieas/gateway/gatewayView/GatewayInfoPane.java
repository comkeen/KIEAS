package kr.ac.uos.ai.ieas.gateway.gatewayView;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import kr.ac.uos.ai.ieas.gateway.gatewayController.GatewayActionListener;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertSystemInfoTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlerterInfoTableModel;

public class GatewayInfoPane
{
	private GatewayActionListener gatewayActionListener;
	private GatewayView gatewayView;
	private GridBagConstraints gbc;

	private JPanel infoPane;

	private JTable alerterInfoTable;

	private JScrollPane alerterInfoTableScrollPane;

	private GatewayAlerterInfoTableModel alerterInfoTableModel;

	private GatewayAlertSystemInfoTableModel alertSystemInfoTableModel;

	private JTable alertSystemInfoTable;

	private JScrollPane alertSystemInfoTableScrollPane;


	public GatewayInfoPane(GatewayView gatewayView, GatewayActionListener gatewayActionListener)
	{
		this.gatewayActionListener = gatewayActionListener;
		this.gatewayView = gatewayView;
		this.gbc = new GridBagConstraints();

		initInfoPane();
	}
	public JPanel getInfoPane()
	{
		return infoPane;
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
	
	private void initInfoPane()
	{
		this.infoPane = new JPanel();
		infoPane.setLayout(new GridBagLayout());

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 1, 1, 1, 1);
		initAlertSystemInfoTablePane();
		
		gbc.fill = GridBagConstraints.BOTH;
		setGbc(1, 0, 1, 1, 1, 1);
		initAlerterInfoTablePane();
		
	}

	private void initAlertSystemInfoTablePane() {
		this.alerterInfoTableModel = gatewayView.getAlerterInfoTableModel();
		this.alerterInfoTable = new JTable(alerterInfoTableModel.getTableModel());
		this.alerterInfoTableScrollPane = new JScrollPane(alerterInfoTable);

		alerterInfoTable.setEnabled(true);
		alerterInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		alerterInfoTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		alerterInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		alerterInfoTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		alerterInfoTable.getSelectionModel().addListSelectionListener(gatewayActionListener);
		
		infoPane.add(alerterInfoTableScrollPane, gbc);
	}

	private void initAlerterInfoTablePane()
	{
		this.alertSystemInfoTableModel = gatewayView.getAlertSystemInfoTableModel();
		this.alertSystemInfoTable = new JTable(alertSystemInfoTableModel.getTableModel());
		this.alertSystemInfoTableScrollPane = new JScrollPane(alertSystemInfoTable);

		alertSystemInfoTable.setEnabled(true);
		alertSystemInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		alertSystemInfoTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		alertSystemInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		alertSystemInfoTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		alertSystemInfoTable.getColumnModel().getColumn(2).setPreferredWidth(200);
		alertSystemInfoTable.getColumnModel().getColumn(3).setPreferredWidth(200);
		alertSystemInfoTable.getSelectionModel().addListSelectionListener(gatewayActionListener);
		
		infoPane.add(alertSystemInfoTableScrollPane, gbc);		
	}
}
