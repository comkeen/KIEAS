package kr.ac.uos.ai.ieas.alertSystem.alertSystemController;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.ac.uos.ai.ieas.resource.IeasConfiguration;

public class AlertSystemView {
	
	private AlertSystemController alertSystem;
	private AlertSystemActionListener alertSystemActionListener;
	
	private JFrame frame;
	private Container alertPane;
	private GridBagConstraints gbc;
	private JTextArea alertArea;
	private JScrollPane alertAreaPane;
	private JPanel buttonPane;
	private JComboBox<String> topicCombobox;
	
	private String id;
	private JTabbedPane mainTabbedPane;


	public AlertSystemView(AlertSystemController alertSystem, String id) {
		
		this.alertSystem = alertSystem;
		this.id = id;
		this.alertSystemActionListener = new AlertSystemActionListener(this);
		
		this.initLookAndFeel();
		this.initFrame();
	}
	
	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initFrame() {
		this.frame = new JFrame(id);
		
		this.mainTabbedPane = new JTabbedPane();
		Container container = frame.getContentPane();
		container.add(mainTabbedPane);
		
		frame.setSize(1024, 512);
		frame.setPreferredSize(new Dimension(400,200));
		
		this.gbc = new GridBagConstraints();
		
		initAlertPane();
		mainTabbedPane.addTab("경보메시지", alertPane);
					
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initAlertPane() {
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
	
	private void initButtonPane() {
		this.buttonPane = new JPanel();
		
		initComboBox();
		buttonPane.add(topicCombobox, BorderLayout.WEST);
		
		alertPane.add(buttonPane, gbc);
	}
	
	private void initComboBox() {
		this.topicCombobox = new JComboBox<String>();
		topicCombobox.addItemListener(alertSystemActionListener);
		
		for (String location : IeasConfiguration.IEAS_List.LOCATION_LIST) {

			topicCombobox.addItem(location);
		};		
	}
	
	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}
	
	public void setTextArea(String message) {
		alertArea.setText(message);	
	}

	public void selectTopic(String topic) {
		alertSystem.selectTopic(topic);
	}
}
