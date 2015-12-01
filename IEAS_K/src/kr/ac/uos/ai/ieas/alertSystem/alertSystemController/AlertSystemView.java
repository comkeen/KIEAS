package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.ac.uos.ai.ieas.resource.KieasConfiguration;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder.Item;

public class AlertSystemView {
	
	private AlertSystemController controller;
	private AlertSystemActionListener alertSystemActionListener;
	private KieasMessageBuilder kieasMessageBuilder;
	
	private JFrame frame;
	private Container alertPane;
	private GridBagConstraints gbc;
	private JTextArea alertArea;
	private JScrollPane alertAreaPane;
	private JPanel buttonPane;
	private JTabbedPane mainTabbedPane;
	
	private JComboBox<Item> geoCodeCombobox;
	private JComboBox<String> alertSystemTypeCombobox;
	
	
	public AlertSystemView(AlertSystemController alertSystemController)
	{		
		this.controller = alertSystemController;
		this.alertSystemActionListener = new AlertSystemActionListener(this);
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		this.initLookAndFeel();
		this.initFrame();
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

	private void initFrame()
	{
		this.frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(alertSystemActionListener);
		this.mainTabbedPane = new JTabbedPane();
		Container container = frame.getContentPane();
		container.add(mainTabbedPane);
		
		frame.setSize(1024, 512);
		frame.setPreferredSize(new Dimension(512, 256));
		
		this.gbc = new GridBagConstraints();
		
		initAlertPane();
		mainTabbedPane.addTab("경보메시지", alertPane);
					
		frame.setVisible(true);
	}

	private void initAlertPane()
	{
		this.alertPane = new JPanel();
		alertPane.setLayout(new GridBagLayout());
		
		this.alertArea = new JTextArea(5, 20);
		this.alertAreaPane = new JScrollPane(alertArea);	

		alertArea.setText("\n");
		
		gbc.fill = GridBagConstraints.BOTH;	
		setGbc(0, 0, 1, 1, 1, 8);
		alertPane.add(alertAreaPane, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 1, 1, 1, 1, 2);
		this.initButtonPane();
	}
	
	private void initButtonPane()
	{
		this.buttonPane = new JPanel();
		
		initComboBox();
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(alertSystemActionListener);

		buttonPane.add(alertSystemTypeCombobox, BorderLayout.WEST);
//		buttonPane.add(geoCodeCombobox, BorderLayout.WEST);
		buttonPane.add(clearButton, BorderLayout.WEST);
		
		alertPane.add(buttonPane, gbc);
	}
	
	private void initComboBox()
	{
		Vector<String> comboboxModel1 = new Vector<>();		
		for (String type : KieasConfiguration.KieasList.ALERT_SYSTEM_TYPE_LIST)
		{
			comboboxModel1.addElement(type);
		}
		this.alertSystemTypeCombobox = new JComboBox<>(comboboxModel1);
		alertSystemTypeCombobox.addItemListener(new ItemListener()
		{
	        public void itemStateChanged(ItemEvent e)
	        {
	        	controller.selectTopic(AlertSystemController.ALERT_SYSTEM_TYPE, e.getItem().toString());
	        }
	    });
		
//		Vector<Item> comboboxModel2 = new Vector<>();	
//		for (Item item : kieasMessageBuilder.getCapEnumMap().get(KieasMessageBuilder.GEO_CODE))
//		{
//			comboboxModel2.addElement(item);
//		}		
//		this.geoCodeCombobox = new JComboBox<>(comboboxModel2);
//		geoCodeCombobox.addItemListener(new ItemListener()
//		{
//	        public void itemStateChanged(ItemEvent e)
//	        {
//	        	controller.selectTopic(AlertSystemController.GEO_CODE, e.getItem().toString());
//	        }
//	    });		
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
	
	public void setTextArea(String message)
	{
		alertArea.setText(message);	
	}

	public void setAlertSystemId(String alertSystemId)
	{
		frame.setTitle(alertSystemId);
	}

	public void systemExit()
	{
		String question = "표준경보시스템 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";
		
		if (JOptionPane.showConfirmDialog(frame,
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
	    {	
			controller.closeConnection();
	        System.exit(0);
	    }
		else
		{
			System.out.println("cancel exit program");
		}
	}

	public String getSelectedGeoCode()
	{		
		return geoCodeCombobox.getSelectedItem().toString();
	}

	public String getSelectedAlertSystemType()
	{
		return alertSystemTypeCombobox.getSelectedItem().toString();
	}
}
