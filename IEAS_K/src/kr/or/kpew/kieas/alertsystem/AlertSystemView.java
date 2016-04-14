package kr.or.kpew.kieas.alertsystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.main.IntegratedAlertSystemMain;

public class AlertSystemView implements Observer {
	private AlertSystemController controller;

	private JFrame frame;
	private Container alertPane;
	private GridBagConstraints gbc;
	private JTextArea alertArea;
	private JScrollPane alertAreaPane;
	private JPanel buttonPane;
	private JTabbedPane mainTabbedPane;

	JTextField systemType;

	public AlertSystemView() {
	}
	
	public void show() {
		frame.setVisible(true);
	}

	public void init() {
		initLookAndFeel();
		initFrame();
		gbc = new GridBagConstraints();
		initAlertPane();
		initButtonPane();
		mainTabbedPane.addTab("경보메시지", alertPane);
	}

	public void setController(AlertSystemController controller) {
		this.controller = controller;
	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initFrame() {
		this.frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(controller);

		this.mainTabbedPane = new JTabbedPane();
		Container container = frame.getContentPane();
		container.add(mainTabbedPane);

		frame.setSize(400, 400);
		frame.setLocation(IntegratedAlertSystemMain.xLocation, 400);
		IntegratedAlertSystemMain.xLocation += IntegratedAlertSystemMain.xIncrement;
		frame.setPreferredSize(new Dimension(512, 256));
	}

	private void initAlertPane() {
		alertPane = new JPanel();
		alertPane.setLayout(new GridBagLayout());

		alertArea = new JTextArea(5, 20);
		alertAreaPane = new JScrollPane(alertArea);

		alertArea.setText("");

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 1, 1, 1, 8);
		alertPane.add(alertAreaPane, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 1, 1, 1, 1, 2);

	}

	private void initButtonPane() {
		this.buttonPane = new JPanel();
		
		systemType = new JTextField(15);
		systemType.setEnabled(false);
		systemType.setText("<경보시스템 종류>");

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(controller);
		buttonPane.add(clearButton, BorderLayout.WEST);

		buttonPane.add(systemType, BorderLayout.WEST);

		alertPane.add(buttonPane, gbc);
	}


	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}

	public void systemExit() {
		String question = "표준경보시스템 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";

		if (JOptionPane.showConfirmDialog(frame, question, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else {
			System.out.println("AS: cancel exit program");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof String) {		
			alertArea.setText((String)arg);
		}
		else if(arg instanceof AlertSystemProfile) {
			AlertSystemProfile profile = (AlertSystemProfile)arg;
			frame.setTitle(profile.getSender());
			systemType.setText(profile.getType().getDescription());
		}
	}

	public void clear() {
		alertArea.setText("");
	}
}
