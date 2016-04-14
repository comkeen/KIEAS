package kr.or.kpew.kieas.gateway.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IssuerProfile;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.gateway.controller.GatewayController;
import kr.or.kpew.kieas.gateway.model.GatewayModel.Pair;

public class GatewayView implements Observer
{
	private GatewayController controller;

	private GatewayLogPane gatewayLogPane;
	private GatewayDataPane gatewayDataPane;

	private JFrame frame;
	private JTabbedPane mainTabbedPane;

	private GatewayInfoPane gatewayInfoPane;

	private IssuerTable issuersTable;
	private AlertSystemInfoTable alertsystemsTable;
	private AlertMessageTable alertMessageTable;

	private boolean initialized = false;

	
	public void init()
	{
		issuersTable = new IssuerTable();
		alertsystemsTable = new AlertSystemInfoTable();
		alertMessageTable = new AlertMessageTable();

		initLookAndFeel();

		gatewayLogPane = new GatewayLogPane(controller);
		gatewayDataPane = new GatewayDataPane(this, controller);
		gatewayInfoPane = new GatewayInfoPane(this, controller);

		initFrame(/*model.getId()*/);

		initialized = true;
	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initFrame(/*String name*/) {
		String name = "Gateway";
		this.frame = new JFrame(name);
		frame.setSize(600, 400);
		frame.setLocation(800, 0);
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(controller);

		this.mainTabbedPane = new JTabbedPane();
		Container container = frame.getContentPane();
		container.add(mainTabbedPane);

		mainTabbedPane.addTab("경보로그", gatewayLogPane.getLogPane());
		mainTabbedPane.addTab("경보메시지", gatewayDataPane.getDataPane());
		mainTabbedPane.addTab("정보", gatewayInfoPane.getInfoPane());
	}

	public void setLogTextArea(String message) {
		gatewayLogPane.setLogTextArea(message);
	}

	public void appendLog(String text) {
		gatewayLogPane.appendLog(text);
	}

	public void clearLog() {
		gatewayLogPane.clearLog();
	}

	public AlertMessageTable getAlertTableModel() {
		return alertMessageTable;
		// return model.getAlertTableModel();
	}

//	public String getAlertMessage(String identifier) {
//		return model.getAlertMessage(identifier);
//	}

	public void selectTableEvent() {
		gatewayDataPane.setDataTextArea();
	}

	public IssuerTable getAlerterInfoTableModel() {
		return issuersTable;
		// return model.getAlerterInfoTableModel();
	}

	public AlertSystemInfoTable getAlertSystemInfoTableModel() {
		return alertsystemsTable;
		// return model.getAlertSystemInfoTableModel();
	}

	public Component getFrame() {
		return frame;
	}

	public void setId(String gatewayId) {
		frame.setTitle(gatewayId);
	}

	public void systemExit() {
		String question = "통합게이트웨이 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";

		if (JOptionPane.showConfirmDialog(this.getFrame(), question, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
//			model.exit();
			System.exit(0);
		} else {
			System.out.println("GW: cancel exit program");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (!initialized)
			return;

		Pair item = (Pair) arg;
		

		switch (item.type) {
		case Log:
			appendLog((String)item.object);
			break;
		case GatewayId:
			setId((String)item.object);
			break;
		case AlertMessage:
			alertMessageTable.addTableRowData((KieasMessageBuilder)item.object);
			break;
		case RegisterAlertSystem:
			alertsystemsTable.addTableRowData((AlertSystemProfile)item.object);
			break;
		case RegisterIssuer:
			issuersTable.addTableRowData((IssuerProfile)item.object);
			break;
		case Ack:
			alertMessageTable.receiveAck((String)item.object);
		default:
			break;
		}
	}

	public void setController(GatewayController controller) {
		this.controller = controller;

	}

}
