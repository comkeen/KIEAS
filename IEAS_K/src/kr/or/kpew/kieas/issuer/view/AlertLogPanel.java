package kr.or.kpew.kieas.issuer.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import kr.or.kpew.kieas.common.KieasConfiguration.KieasConstant;
import kr.or.kpew.kieas.common.KieasMessageBuilder.AlertElementNames;
import kr.or.kpew.kieas.issuer.controller.IssuerController;
import kr.or.kpew.kieas.issuer.model.AlertLogger.MessageAckPair;
import kr.or.kpew.kieas.issuer.view.resource.TableModel;


public class AlertLogPanel 
{
	private static final String CLEAR_BUTTON = "Clear";
	
	private Box mainBox;
	
	private JScrollPane textAreaPanel;
	private JTextArea mTextArea;

	private Box buttonBox;
	private JButton mClearButton;

	private JScrollPane alertLogTableScrollPanel;
	private JTable alertLogTable;
	private TableModel alertLogTableModel;
	
	private Vector<Object> mViewComponents;
	private Map<String, Component> mPanelComponents;

	
	
	public AlertLogPanel()
	{
		initPanel();
	}
	
	public JComponent getPanel()
	{
		return this.mainBox;
	}
		
	private void initPanel()
	{		
		this.mViewComponents = new Vector<>();
		this.mPanelComponents = new HashMap<>();
		
		this.mainBox = Box.createVerticalBox();

		mainBox.add(initTextAreaPanel());
		mainBox.add(initButtonPanel());
		mainBox.add(initTablePanel());
		
		mViewComponents.addElement(mPanelComponents);
	}

	private JComponent initTextAreaPanel()
	{
		this.mTextArea = new JTextArea(10, 10);
		mTextArea.setPreferredSize(new Dimension(500, 300));
		mTextArea.setText("\n");
		mPanelComponents.put(IssuerView.TEXT_AREA, mTextArea);
		
		this.textAreaPanel = new JScrollPane(mTextArea);
		textAreaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return textAreaPanel;
	}

	private JComponent initButtonPanel()
	{
		this.buttonBox = Box.createHorizontalBox();

		this.mClearButton = new JButton(CLEAR_BUTTON);
		mClearButton.setActionCommand(CLEAR_BUTTON);
		buttonBox.add(mClearButton, BorderLayout.WEST);
		
		return buttonBox;
	}
	
	private JComponent initTablePanel()
	{
		String[] columnNames =
			{
				AlertElementNames.Identifier.toString(),
				AlertElementNames.Sender.toString(),
				AlertElementNames.Sent.toString(),
				KieasConstant.ACK
			};
		
		this.alertLogTableModel = new TableModel(columnNames);
		this.alertLogTable = new JTable(alertLogTableModel.getTableModel());
		this.alertLogTableScrollPanel = new JScrollPane(alertLogTable);
		alertLogTable.setEnabled(true);
				
		return alertLogTableScrollPanel;
	}
	
	public String getSelectedRowIdentifier()
	{
		String identifier = "";
		if(alertLogTable.getSelectedRow() >= 0)
		{
			identifier = alertLogTableModel.getTableModel().getValueAt(alertLogTable.getSelectedRow(), 1).toString();
		}
		return identifier;
	}
	
	public void setTextArea(String message)
	{
		mTextArea.setText(message);
		mTextArea.setCaretPosition(0);
	}
	
	public void addController(IssuerController controller)
	{
		mClearButton.addActionListener(controller);
		alertLogTable.getSelectionModel().addListSelectionListener(controller);
	}

	public void removeController(IssuerController controller)
	{
		mClearButton.removeActionListener(controller);
		alertLogTable.getSelectionModel().removeListSelectionListener(controller);
	}

	public void updateTable(MessageAckPair value)
	{
		alertLogTableModel.updateTable(value);		
	}
}
