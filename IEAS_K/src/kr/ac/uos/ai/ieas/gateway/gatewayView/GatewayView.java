package kr.ac.uos.ai.ieas.gateway.gatewayView;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.ac.uos.ai.ieas.gateway.gatewayController.GatewayActionListener;
import kr.ac.uos.ai.ieas.gateway.gatewayController.GatewayController;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertSystemInfoTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlertTableModel;
import kr.ac.uos.ai.ieas.gateway.gatewayModel.GatewayAlerterInfoTableModel;


public class GatewayView
{
	private static GatewayView gatewayView;

	private GatewayController gatewayController;
	private GatewayActionListener gatewayActionListener;
	private GatewayLogPane gatewayLogPane;
	private GatewayDataPane gatewayDataPane;

	private JFrame frame;
	private JTabbedPane mainTabbedPane;

	private GatewayInfoPane gatewayInfoPane;
	
	
	public static GatewayView getInstance(GatewayController gatewayController, GatewayActionListener gatewayActionListener)
	{
		if (gatewayView == null) 
		{
			gatewayView = new GatewayView(gatewayController, gatewayActionListener);
		}
		return gatewayView;
	}

	private GatewayView(GatewayController gatewayController, GatewayActionListener gatewayActionListener)
	{
		this.gatewayActionListener = gatewayActionListener;
		this.gatewayController = gatewayController;
		initLookAndFeel();
		initFrame(gatewayController.getName());
	}

	private void initLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
	}

	private void initFrame(String name)
	{
		this.frame = new JFrame(name);
		frame.setSize(1024, 512);
		frame.setPreferredSize(new Dimension(400,200));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(gatewayActionListener);
		
		this.mainTabbedPane = new JTabbedPane();
		Container container = frame.getContentPane();
		container.add(mainTabbedPane);

		this.gatewayLogPane = GatewayLogPane.getInstance(gatewayActionListener);
		mainTabbedPane.addTab("경보로그", gatewayLogPane.getLogPane());
		
		this.gatewayDataPane = GatewayDataPane.getInstance(this, gatewayActionListener);		
		mainTabbedPane.addTab("경보메시지", gatewayDataPane.getDataPane());
		
		this.gatewayInfoPane = GatewayInfoPane.getInstance(this, gatewayActionListener);		
		mainTabbedPane.addTab("정보", gatewayInfoPane.getInfoPane());
	}


	public void setLogTextArea(String message)
	{		
		gatewayLogPane.setLogTextArea(message);
	}

	public void appendLog(String text)
	{
		gatewayLogPane.appendLog(text);
	}

	public void clearLog()
	{
		gatewayLogPane.clearLog();
	}

	public GatewayAlertTableModel getAlertTableModel()
	{
		return gatewayController.getAlertTableModel();
	}

	public String getAlertMessage(String identifier)
	{
		return gatewayController.getAlertMessage(identifier);
	}

	public void selectTableEvent()
	{
		gatewayDataPane.setDataTextArea();
	}

	public GatewayAlerterInfoTableModel getAlerterInfoTableModel()
	{
		return gatewayController.getAlerterInfoTableModel();
	}

	public GatewayAlertSystemInfoTableModel getAlertSystemInfoTableModel()
	{
		return gatewayController.getAlertSystemInfoTableModel();
	}

	public Component getFrame()
	{
		return frame;
	}	
}
