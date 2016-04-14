package kr.or.kpew.kieas.issuer.view;

import java.awt.Dimension;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.or.kpew.kieas.common.Item;
import kr.or.kpew.kieas.issuer.controller.IssuerController;
import kr.or.kpew.kieas.issuer.model.AlertLogger.MessageAckPair;

public class IssuerView implements Observer {
	public static final String TEXT_AREA = "TextArea";
	public static final String TEXT_FIELD = "TextField";
	public static final String COMBO_BOX = "ComboBox";
	public static final String TABLE = "Table";
	public static final String CAP_ELEMENT_PANEL = "CapElementPanel";
	public static final String IDENTIFIER = "Identifier";
	public static final String ACK = "Ack";

	private AlertGeneratorPanel alertGeneratorPanel;

	private JFrame mainFrame;
	private JTabbedPane mainTabbedPane;

	/**
	 * Main Frame과 각 포함되는 View Component 초기화.
	 * 
	 * @param alerterActionListener
	 *            이벤트 리스너
	 */
	public IssuerView()
	{
		initLookAndFeel();
		this.alertGeneratorPanel = new AlertGeneratorPanel();

		init();
	}

	private void init() {
		this.mainFrame = new JFrame();
		mainFrame.setTitle("표준경보발령대");
		mainFrame.setPreferredSize(new Dimension(800, 800));
		mainFrame.setLocation(0, 0);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.mainTabbedPane = new JTabbedPane();
		mainFrame.getContentPane().add(mainTabbedPane);

		mainTabbedPane.addTab("CAP Generator Panel", alertGeneratorPanel.getPanel());

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public String showFileOpenDialog() {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./cap"));
		fc.setDialogTitle("파일 선택");
		FileFilter filter = new FileFilter() {

			@Override
			public String getDescription() {
				return "통합경보시스템을 위한 공통경보프로토콜 파일(.xml | .cap)";
			}

			@Override
			public boolean accept(File f) {
				String extension = getExtension(f);
				return "xml".equals(extension) | "cap".equals(extension);
			}
			private String getExtension(File f) {
				String ext = null;
				String s = f.getName();
				int i = s.lastIndexOf('.');

				if (i > 0 &&  i < s.length() - 1) {
					ext = s.substring(i+1).toLowerCase();
				}
				return ext;
			}
		};
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(mainFrame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public void addInfoIndexPanel() {
		alertGeneratorPanel.addInfoIndexPanel();
	}

	public String getLoadTextField() {
		return alertGeneratorPanel.getLoadTextField();
	}

	public String getSaveTextField() {
		return alertGeneratorPanel.getSaveTextField();
	}

	public void setId(String name) {
		mainFrame.setTitle(name + "발령대");
	}

	public JFrame getFrame() {
		return this.mainFrame;
	}

	public void setTextArea(String message) {
		alertGeneratorPanel.setTextArea(message);
	}

	public String getTextArea()
	{
		return alertGeneratorPanel.getTextArea();
	}

	public String getCapElement()
	{
		return alertGeneratorPanel.getCapElement();
	}

	@Override
	public void update(Observable observable, Object value)
	{
		if(value instanceof Item)
		{
			Item item = (Item) value;			

			switch (item.getKey())
			{
			case CAP_ELEMENT_PANEL:
				alertGeneratorPanel.updateCapElementView(item.getValue());
				break;
			case TEXT_AREA:
				alertGeneratorPanel.setTextArea(item.getValue());
				break;
			case IDENTIFIER:
				alertGeneratorPanel.updateIdentifier(item.getValue());
				break;
			case ACK:
				createDialog(item.getValue());
			default:
				break;
			}
		}
		else if(value instanceof MessageAckPair)
		{
			alertGeneratorPanel.updateTable((MessageAckPair) value);
		}
	}
	
	private void createDialog(String identifier)
	{
		JOptionPane.showMessageDialog(mainFrame, "Receive Ack : " + identifier);
	}

	public void addController(IssuerController controller) {
		mainFrame.addWindowListener(controller);
		alertGeneratorPanel.addController(controller);
	}

	public void removeController(IssuerController controller) {
		mainFrame.removeWindowListener(controller);
		alertGeneratorPanel.removeController(controller);
	}

	public String getSelectedRowIdentifier()
	{
		return alertGeneratorPanel.getSelectedRowIdentifier();
	}
}