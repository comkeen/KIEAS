package kr.ac.uos.ai.ieas.alerter.alerterView;

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

import kr.ac.uos.ai.ieas.alerter.alerterController.AleterViewActionListener;
import kr.ac.uos.ai.ieas.alerter.alerterView.resource.AlertLogTableModel;
import kr.ac.uos.ai.ieas.resource.IKieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterLogPanel 
{
	private static final String TEXT_AREA = "TextArea";
	private static final String CLEAR_BUTTON = "Clear";
	
	
	private _AlerterTopView topView;
	private AleterViewActionListener viewActionListener;
	private IKieasMessageBuilder kieasMessageBuilder;

	private JPanel mainPanel;
	
	private JScrollPane textAreaPanel;
	private JTextArea mTextArea;

	private JPanel buttonPanel;
	private JButton mClearButton;

	private JScrollPane alertLogTableScrollPanel;
	private JTable alertLogTable;
	private AlertLogTableModel alertLogTableModel;
	
	private Vector<Object> mViewComponents;
	private Map<String, Component> mPanelComponents;
	private Map<String, String> mAlertElementMap;
	private Map<String, String> mAlertMessageMap;
	
	
	public AlerterLogPanel(_AlerterTopView topView, AleterViewActionListener alerterActionListener)
	{
		this.topView = topView;
		this.viewActionListener = alerterActionListener;
		this.kieasMessageBuilder = new KieasMessageBuilder();

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
		
	public void setActionListener(AleterViewActionListener alerterActionListener)
	{
		this.viewActionListener = alerterActionListener;
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
		mClearButton.addActionListener(viewActionListener);
		buttonPanel.add(mClearButton, BorderLayout.WEST);
		
		return buttonPanel;
	}
	private Component initTablePanel()
	{
		this.alertLogTableModel = new AlertLogTableModel();
		this.alertLogTable = new JTable(alertLogTableModel.getTableModel());
		this.alertLogTableScrollPanel = new JScrollPane(alertLogTable);
		
		alertLogTable.setEnabled(true);
		alertLogTable.getSelectionModel().addListSelectionListener(viewActionListener);

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
			String message = topView.getAlertMessage(identifier);
			mTextArea.setText(message);
		}
	}

	public void addAlertTableRow(String message)
	{				
		alertLogTableModel.addTableRowData(getAlertElementMap(message));

		kieasMessageBuilder.setMessage(message);
		putAlertMessageMap(kieasMessageBuilder.getIdentifier(), message);
	}

	public void putAlertMessageMap(String key, String message)
	{
		mAlertMessageMap.put(key, message);
	}
	
	public Map<String, String> getAlertElementMap(String message)
	{
		kieasMessageBuilder.setMessage(message);

		mAlertElementMap.replace(KieasMessageBuilder.SENDER, kieasMessageBuilder.getSender());
		mAlertElementMap.replace(KieasMessageBuilder.IDENTIFIER, kieasMessageBuilder.getIdentifier());
		mAlertElementMap.replace(KieasMessageBuilder.STATUS, kieasMessageBuilder.getStatus());
		mAlertElementMap.replace(KieasMessageBuilder.SENT, kieasMessageBuilder.getSent());
		mAlertElementMap.replace(KieasMessageBuilder.EVENT, kieasMessageBuilder.getEvent(0));
		mAlertElementMap.replace(KieasMessageBuilder.NOTE, kieasMessageBuilder.getNote());
		if(kieasMessageBuilder.getRestriction() != null)
		{
			mAlertElementMap.replace(KieasMessageBuilder.RESTRICTION, kieasMessageBuilder.getRestriction());			
		}
//		alertElementMap.replace(GEO_CODE, kieasMessageBuilder.getSent());

		return mAlertElementMap;
	}
}
