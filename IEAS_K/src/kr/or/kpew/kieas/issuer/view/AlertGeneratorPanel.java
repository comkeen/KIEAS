package kr.or.kpew.kieas.issuer.view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.or.kpew.kieas.issuer.controller.Controller;


public class AlertGeneratorPanel implements Observer
{	
	private static final String LOAD_TEXT_FIELD = "LoadTextField";
	private static final String SAVE_TEXT_FIELD = "SaveTextField";
	private static final String LOAD_CAP_BUTTON = "Load Cap";
	private static final String SAVE_CAP_BUTTON = "Save Cap";
	private static final String REGISTER_BUTTON = "Register";
	private static final String SET_ID = "Set Id";

	private JScrollPane alertGenerateScrollPanel;
	private JPanel mainPanel;

	private Vector<Object> mViewComponents;
	private HashMap<String, Component> panelComponenets;
	private List<JButton> buttons;
	private JScrollPane textAreaPane;
	private JTextArea mTextArea;
	
	private JPanel buttonPane;
	private JButton saveCapButton;
	private JButton loadCapDraftButton;
	private JTextField mSaveTextField;
	private JTextField mLoadTextField;

	private JButton alertApplyButton;
	private JButton sendButton;
	private JButton registerButton;
	private JButton setIdButton;



	public AlertGeneratorPanel()
	{
		init();
	}

	private void init()
	{		
		this.mViewComponents = new Vector<>();
		this.panelComponenets = new HashMap<>();
		this.buttons = new ArrayList<>();
		this.mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		CapElementPanel capPane = new CapElementPanel();
		
		mainPanel.add(initTextArea());
		mainPanel.add(initButtonPanel());
		mainPanel.add(capPane.getPanel());
		
		mViewComponents.addElement(panelComponenets);

		this.alertGenerateScrollPanel = new JScrollPane(mainPanel);
	}

	private Component initTextArea()
	{
		this.mTextArea = new JTextArea(20, 20);
		mTextArea.setEditable(false);
		
		this.textAreaPane = new JScrollPane(mTextArea);
		panelComponenets.put("TextArea", mTextArea);
		textAreaPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		return textAreaPane;
	}

	private Component initButtonPanel()
	{
		this.buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

		Box loadBox = Box.createHorizontalBox();		
		this.loadCapDraftButton = createButton(LOAD_CAP_BUTTON);
		loadBox.add(loadCapDraftButton);		
		this.mLoadTextField = new JTextField();
		panelComponenets.put(LOAD_TEXT_FIELD, mLoadTextField);
		mLoadTextField.setText("cap/HRA.xml");
		loadBox.add(mLoadTextField);
		loadBox.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPane.add(loadBox);

		Box saveBox = Box.createHorizontalBox();
		this.saveCapButton = createButton(SAVE_CAP_BUTTON);
		saveBox.add(saveCapButton);
		this.mSaveTextField = new JTextField();
		panelComponenets.put(SAVE_TEXT_FIELD, mSaveTextField);
		mSaveTextField.setText("cap/out.xml");
		saveBox.add(mSaveTextField);
		saveBox.setBorder(BorderFactory.createLoweredBevelBorder());
		buttonPane.add(saveBox);

		
		this.alertApplyButton = createButton("Apply");
		buttonPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		buttonPane.add(alertApplyButton);
		this.sendButton = createButton("Send");
		sendButton.setActionCommand("Send");
		buttonPane.add(sendButton);
		this.registerButton = createButton(REGISTER_BUTTON);
		registerButton.setActionCommand(REGISTER_BUTTON);
		buttonPane.add(registerButton);
		this.setIdButton = createButton(SET_ID);
		setIdButton.setActionCommand(SET_ID);
		buttonPane.add(setIdButton);
		
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return buttonPane;
	}

	private JButton createButton(String name)
	{
		JButton button = new JButton(name);
		buttons.add(button);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		return button;
	}

	public JScrollPane getPanel()
	{
		return this.alertGenerateScrollPanel;
	}
	
	public void updateView(String target, String value)
	{		
		if(target.equals(View.TEXT_AREA))
		{
			this.mTextArea.setText(value);
			return;
		}
	}
	
	public String getLoadTextField()
	{
		return mLoadTextField.getText();
	}

	public String getSaveTextField()
	{
		return mSaveTextField.getText();
	}

	public String getTextArea()
	{
		return ((JTextArea) panelComponenets.get(View.TEXT_AREA)).getText();
	}

	public void setTextArea(String message)
	{
		mTextArea.setText(message);
	}

	@Override
	public void update(Observable observable, Object value)
	{
		setTextArea(value.toString());
	}

	public void addController(Controller controller)
	{
		for (JButton button : buttons)
		{
			button.addActionListener(controller);
		}
	}
	
	public void removeController(Controller controller)
	{
		for (JButton button : buttons)
		{
			button.removeActionListener(controller);
		}
	}

	public void addInfoIndexPanel()
	{
		// TODO Auto-generated method stub
		
	}

	public void addResourceIndexPanel() {
		// TODO Auto-generated method stub
		
	}

	public void addAreaIndexPanel() {
		// TODO Auto-generated method stub
		
	}
}

