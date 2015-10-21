package kr.ac.uos.ai.ieas.main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import kr.ac.uos.ai.ieas.gateway.gatewayController.GatewayController;

public class IEAS_Main
{
	public IEAS_Main()
	{
//		initGUI();
		new AlerterMain();
//		GatewayController.getInstance();
	}

	public static void main(String[] args) 
	{
		new IEAS_Main();
	}
	
	private void initGUI()
	{
		JFrame frame = new JFrame();
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ActionListener actionListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String event = e.getActionCommand();
				switch (event)
				{
				case "alerter":
					new AlerterMain();
					break;
				case "gateway":
					System.out.println("doit");
					break;
				case "alertsystem":
					System.out.println("doit");
					break;
				default:
					break;
				}
			}
		};
		
		Container mainPane = frame.getContentPane();
		mainPane.setLayout(new FlowLayout());
		
		JButton alerterButton = new JButton("alerter");
		alerterButton.addActionListener(actionListener);
		JButton gatewayButton = new JButton("gateway");
		gatewayButton.addActionListener(actionListener);
		JButton alertsystemButton = new JButton("alertsystem");
		alertsystemButton.addActionListener(actionListener);
		
		mainPane.add(alerterButton);
		mainPane.add(gatewayButton);
		mainPane.add(alertsystemButton);
		
		frame.setVisible(true);
	}
}
