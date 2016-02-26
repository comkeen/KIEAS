package kr.or.kpew.kieas.issuer.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasConstant;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.issuer.controller.Controller;
import kr.or.kpew.kieas.issuer.view.resource.TableModel;


public class AlertLogPanel 
{
	private static final String TEXT_AREA = "TextArea";
	private static final String CLEAR_BUTTON = "Clear";
	
	
	private View topView;
	private Controller controller;

	private JPanel mainPanel;
	
	private JScrollPane textAreaPanel;
	private JTextArea mTextArea;

	private JPanel buttonPanel;
	private JButton mClearButton;

	private JScrollPane alertLogTableScrollPanel;
	private JTable alertLogTable;
	private TableModel alertLogTableModel;
	
	private Vector<Object> mViewComponents;
	private Map<String, Component> mPanelComponents;
	private Map<String, String> mAlertElementMap;
	private Map<String, String> mAlertMessageMap;
	
	
	public AlertLogPanel(View topView)
	{
		this.topView = topView;
//		this.viewActionListener = controller;

		initAlertElementMap();
		initPanel();
	}
	
	private void initAlertElementMap()
	{
		this.mAlertElementMap = new HashMap<String, String>();
		
		mAlertElementMap.put(KieasMessageBuilder.SENDER, KieasMessageBuilder.SENDER);
		mAlertElementMap.put(KieasMessageBuilder.IDENTIFIER, KieasMessageBuilder.IDENTIFIER);
		mAlertElementMap.put(KieasMessageBuilder.SENT, KieasMessageBuilder.SENT);
		mAlertElementMap.put(KieasMessageBuilder.STATUS, KieasMessageBuilder.STATUS);
		mAlertElementMap.put(KieasMessageBuilder.EVENT, KieasMessageBuilder.EVENT);
		mAlertElementMap.put(KieasMessageBuilder.RESTRICTION, KieasMessageBuilder.RESTRICTION);
		mAlertElementMap.put(KieasMessageBuilder.GEO_CODE, KieasMessageBuilder.GEO_CODE);
		mAlertElementMap.put(KieasMessageBuilder.NOTE, KieasMessageBuilder.NOTE);
	}
	
	public JPanel getPanel()
	{
		return this.mainPanel;
	}
		
	private void initPanel()
	{		
		this.mViewComponents = new Vector<>();
		this.mPanelComponents = new HashMap<>();
		this.mAlertElementMap = new HashMap<String, String>();
		this.mAlertMessageMap = new HashMap<String, String>();
		
		this.mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(initTextAreaPanel());
		mainPanel.add(initButtonPanel());
		mainPanel.add(initTablePanel());
		
		mViewComponents.addElement(mPanelComponents);
	}

	private Component initTextAreaPanel()
	{
		this.mTextArea = new JTextArea(20, 20);
		mTextArea.setText("\n");
		mPanelComponents.put(TEXT_AREA, mTextArea);
		
		this.textAreaPanel = new JScrollPane(mTextArea);
		textAreaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return textAreaPanel;
	}

	private Component initButtonPanel()
	{
		this.buttonPanel = new JPanel();

		this.mClearButton = new JButton(CLEAR_BUTTON);
		mClearButton.setActionCommand(CLEAR_BUTTON);
		mClearButton.addActionListener(controller);
		buttonPanel.add(mClearButton, BorderLayout.WEST);
		
		return buttonPanel;
	}
	private Component initTablePanel()
	{
		String[] columnNames =
			{
				KieasMessageBuilder.IDENTIFIER,
				KieasMessageBuilder.SENDER,
				KieasMessageBuilder.SENT,
				KieasConstant.ACK
			};
		
		this.alertLogTableModel = new TableModel(columnNames);
		this.alertLogTable = new JTable(alertLogTableModel.getTableModel());
		this.alertLogTableScrollPanel = new JScrollPane(alertLogTable);
		alertLogTable.setEnabled(true);
		
		alertLogTable.getSelectionModel().addListSelectionListener(controller);
		
		
		return alertLogTableScrollPanel;
	}

	public void setTextArea(String message)
	{
		mTextArea.setText(message);
	}
	
	public void setDataTextArea()
	{
		if(alertLogTable.getSelectedRow() > -1)
		{
			String identifier = alertLogTableModel.getTableModel().getValueAt(alertLogTable.getSelectedRow(), 2).toString();
//			String message = topView.getAlertMessage(identifier);
//			mTextArea.setText(message);
		}
	}

	public void addAlertTableRow(String message)
	{				
		alertLogTableModel.addTableRowData(message);
		
//		kieasMessageBuilder.setMessage(message);
//		putAlertMessageMap(kieasMessageBuilder.getIdentifier(), message);
	}

//	public void putAlertMessageMap(String key, String message)
//	{
//		mAlertMessageMap.put(key, message);
//	}
	
//	public Map<String, String> getAlertElementMap(String message)
//	{
//		kieasMessageBuilder.setMessage(message);
//
//		mAlertElementMap.replace(KieasMessageBuilder.SENDER, kieasMessageBuilder.getSender());
//		mAlertElementMap.replace(KieasMessageBuilder.IDENTIFIER, kieasMessageBuilder.getIdentifier());
//		mAlertElementMap.replace(KieasMessageBuilder.STATUS, kieasMessageBuilder.getStatus());
//		mAlertElementMap.replace(KieasMessageBuilder.SENT, kieasMessageBuilder.getSent());
//		mAlertElementMap.replace(KieasMessageBuilder.EVENT, kieasMessageBuilder.getEvent(0));
//		mAlertElementMap.replace(KieasMessageBuilder.NOTE, kieasMessageBuilder.getNote());
//		if(kieasMessageBuilder.getRestriction() != null)
//		{
//			mAlertElementMap.replace(KieasMessageBuilder.RESTRICTION, kieasMessageBuilder.getRestriction());			
//		}
//		alertElementMap.replace(GEO_CODE, kieasMessageBuilder.getSent());
//
//		return mAlertElementMap;
//	}
	
	public void addController(Controller controller)
	{
		this.controller = controller;
	}

	public void removeController(Controller controller)
	{
		// TODO Auto-generated method stub
		
	}
}
