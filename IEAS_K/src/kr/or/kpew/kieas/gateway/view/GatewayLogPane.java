package kr.or.kpew.kieas.gateway.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kr.or.kpew.kieas.gateway.controller.GatewayController;

public class GatewayLogPane
{
	private GatewayController controller;
	
	private JPanel logPane;
	
	private GridBagConstraints gbc;

	private JScrollPane logTextAreaPane;
	private JTextArea logTextArea;
	
	private JPanel logButtonPane;	
	private JButton openGatewayButton;
	private JButton closeGatewayButton;
	private JButton clearLogButton;


	public GatewayLogPane(GatewayController controller)
	{		
		this.controller = controller;
		this.gbc = new GridBagConstraints();

		initGatewayLogPane();
	}
	
	public JPanel getLogPane()
	{
		return logPane;
	}
	
	private void initGatewayLogPane()
	{
		this.logPane = new JPanel();
		logPane.setLayout(new GridBagLayout());
		gbc.anchor = GridBagConstraints.SOUTH;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 1, 3, 1, 10, 1);
		initLogButtonPane();

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 3, 1, 10, 10);
		initLogTextAreaPane();
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
	

	private void initLogTextAreaPane()
	{
		this.logTextArea = new JTextArea(5, 20);
		this.logTextAreaPane = new JScrollPane(logTextArea);

		logTextArea.setText("\n");

		logPane.add(logTextAreaPane, gbc);
	}
	
	private void initLogButtonPane()
	{
		this.logButtonPane = new JPanel();

		this.openGatewayButton = new JButton("OpenGateway");
		openGatewayButton.addActionListener(controller);

		this.closeGatewayButton = new JButton("CloseGateway");
		closeGatewayButton.addActionListener(controller);

		this.clearLogButton = new JButton("ClearLog");
		clearLogButton.addActionListener(controller);

		logButtonPane.add(clearLogButton, BorderLayout.WEST);
		logButtonPane.add(openGatewayButton, BorderLayout.EAST);
		logButtonPane.add(closeGatewayButton, BorderLayout.EAST);

		logPane.add(logButtonPane, gbc);
	}

	public void setLogTextArea(String message)
	{
		logTextArea.setText(message);
	}

	public void appendLog(String text)
	{
		logTextArea.append(new Date().toString() + " : " + text+"\n");
		logTextAreaPane.getVerticalScrollBar().setValue(logTextAreaPane.getVerticalScrollBar().getMaximum());
	}

	public void clearLog() 
	{
		logTextArea.setText("");
	}
}
