package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import kr.ac.uos.ai.ieas.alerter.alerterView.AlerterCapGeneratePanel;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterCapGeneratePanelModel
{	
	private _AlerterModelManager alerterModelManager;
	private KieasMessageBuilder kieasMessage;
	
	private FileInputStream fileInputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private StringReader stringReader;

	private String mViewName;

	private Vector<Object> mViewComponents;
	
	private String mTextArea;
	
	private HashMap<String, String> mSaveLoadPanelComponents;
	private String mLoadTextField;
	private String mSaveTextField;
	
	private HashMap<String, String> mAlertValues;
	private String mIdentifier;
	private String mSender;
	private String mSent;
	private String mStatus;
	private String mMsgType;
	private String mScope;
	private String mCode;
	
	private Vector<HashMap<String, String>> mInfoValues;
	private HashMap<String, String> mInfoIndexValues;
	private int mInfoCounter;
	private String mLanguage;
	private String mCategory;
	private String mEvent;
	private String mUrgency;
	private String mSeverity;
	private String mCertainty;
	private String mEventCode;
	private String mEffective;
	private String mSenderName;
	private String mHeadline;
	private String mDescription;
	private String mWeb;
	private String mContact;
	

	private String memberTarget;
	private String mInfoElementName;
	
	
	public AlerterCapGeneratePanelModel(_AlerterModelManager _AlerterModelManager)
	{
		this.alerterModelManager = _AlerterModelManager;
		this.kieasMessage = new KieasMessageBuilder();
		
		initComponentVector();
		init();
	}
	
	private void initComponentVector()
	{
		this.mViewComponents = new Vector<>();
		mViewComponents.addElement(AlerterCapGeneratePanel.TEXT_AREA);
		mViewComponents.addElement(initSaveLoadPanelComponents());
		mViewComponents.addElement(initAlertPanelComponents());
		mViewComponents.addElement(initInfoPanelComponents());
	}

	private HashMap<String, String> initSaveLoadPanelComponents()
	{
		this.mSaveLoadPanelComponents = new HashMap<>();
		mSaveLoadPanelComponents.put(AlerterCapGeneratePanel.LOAD_TEXT_FIELD, "");
		mSaveLoadPanelComponents.put(AlerterCapGeneratePanel.SAVE_TEXT_FIELD, "");
		return mSaveLoadPanelComponents;
	}
	
	private HashMap<String, String> initAlertPanelComponents() {
		this.mAlertValues = new HashMap<>();
		mAlertValues.put(AlerterCapGeneratePanel.IDENTIFIER, "");
		mAlertValues.put(AlerterCapGeneratePanel.SENDER, "");
		mAlertValues.put(AlerterCapGeneratePanel.SENT, "");
		mAlertValues.put(AlerterCapGeneratePanel.STATUS, "");
		mAlertValues.put(AlerterCapGeneratePanel.MSG_TYPE, "");
		mAlertValues.put(AlerterCapGeneratePanel.SCOPE, "");
		mAlertValues.put(AlerterCapGeneratePanel.CODE, "");
		return mAlertValues;
	}

	private Vector<HashMap<String, String>> initInfoPanelComponents() {
		this.mInfoValues = new Vector<>();
		this.mInfoCounter = 0;
		mInfoValues.addElement(addInfoIndexValues(mInfoCounter));
		return mInfoValues;
	}
	
	private HashMap<String, String> addInfoIndexValues(int index)
	{
		this.mInfoIndexValues = new HashMap<>();
		mInfoIndexValues.put(AlerterCapGeneratePanel.LANGUAGE + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.CATEGORY + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.EVENT + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.URGENCY + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.SEVERITY + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.CERTAINTY + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.EVENT_CODE + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.EFFECTIVE + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.HEADLINE + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.DESCRIPTION + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.WEB + index, "");
		mInfoIndexValues.put(AlerterCapGeneratePanel.CONTACT + index, "");
		
		return null;
	}

	private void init()
	{
		this.mViewName = this.getClass().getSimpleName().toString().replace("Model", "");
		this.mInfoCounter = 0;
		
		setProperty(AlerterCapGeneratePanel.LOAD_TEXT_FIELD, "cap/HRA.xml");
		setProperty(AlerterCapGeneratePanel.SAVE_TEXT_FIELD, "cap/out.xml");
		
		this.mIdentifier = "";
		this.mSender = "";
		this.mSent = "";
		this.mStatus = "";
		this.mMsgType = "";
		this.mScope = "";
		this.mCode = "";
		
		this.mLanguage = "";
		this.mCategory = "";
		this.mEvent = "";
		this.mUrgency = "";
		this.mSeverity = "";
		this.mCertainty = "";
		this.mEventCode = "";
		this.mEffective = "";
		this.mSenderName = "";
		this.mHeadline = "";
		this.mDescription = "";
		this.mWeb = "";
		this.mContact = "";				
	}
	
	public void setProperty(String target, String value)
	{
		memberTarget = "m" + target;
		try 
		{
			this.getClass().getDeclaredField(memberTarget).set(this, value);
			alerterModelManager.updateView(mViewName, target, this.getClass().getDeclaredField(memberTarget).get(this).toString());
		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
		{
			System.out.println("there is no such a field " + memberTarget);
			e.printStackTrace();
		}
	}
	
	public void setProperty(String target, String value, int index)
	{
		memberTarget = "m" + target;
//		System.out.println("memberTarget = " + memberTarget);
//		System.out.println("value = " + value);
		try 
		{
			this.getClass().getDeclaredField(memberTarget).set(this, value);
			alerterModelManager.updateView(mViewName, target, this.getClass().getDeclaredField(memberTarget).get(this).toString());
		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
		{
			System.out.println("there is no such a field " + memberTarget);
			e.printStackTrace();
		}
	}
	
	private void setAlertPanel(KieasMessageBuilder ieasMessage)
	{
		setProperty(AlerterCapGeneratePanel.IDENTIFIER, ieasMessage.getIdentifier());
		setProperty(AlerterCapGeneratePanel.SENDER, ieasMessage.getSender());
		setProperty(AlerterCapGeneratePanel.SENT, ieasMessage.getSent());
		setProperty(AlerterCapGeneratePanel.STATUS, ieasMessage.getStatus());
		setProperty(AlerterCapGeneratePanel.MSG_TYPE, ieasMessage.getMsgType());
		setProperty(AlerterCapGeneratePanel.SCOPE, ieasMessage.getScope());
		setProperty(AlerterCapGeneratePanel.CODE, ieasMessage.getCode());		
	}
	
	private void setInfoPanel(KieasMessageBuilder ieasMessage)
	{
		for(int i = 0; i < ieasMessage.getInfoCount(); i++)
		{
			setProperty(AlerterCapGeneratePanel.LANGUAGE, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.CATEGORY, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.EVENT, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.URGENCY, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.SEVERITY, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.CERTAINTY, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.EVENT_CODE, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.EFFECTIVE, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.SENDER_NAME, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.HEADLINE, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.DESCRIPTION, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.WEB, ieasMessage.getCategory(i));
			setProperty(AlerterCapGeneratePanel.CONTACT, ieasMessage.getCategory(i));
		}
	}

	public void capLoader()
	{
		try
		{
			String path = mLoadTextField;

			String temp = "";	         
			String content = "";

			fileInputStream = new FileInputStream(new File(path));
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			while((temp = bufferedReader.readLine()) != null)
			{
				content += temp + "\n";
			}

			setProperty("TextArea", content);
			kieasMessage.setMessage(content);
//
			setAlertPanel(kieasMessage);
//			setInfoPanel(kieasMessage);
		}
		catch (IOException e)
		{	
			e.printStackTrace();
		}
	}

	public void capWriter()
	{
		this.stringReader = new StringReader(mTextArea);
		bufferedReader = new BufferedReader(stringReader);

		//		String extension= ".xml";
		String path = mSaveTextField;
		String temp = "";

		try
		{
			this.bufferedWriter = new BufferedWriter(new FileWriter(path));

			while((temp = bufferedReader.readLine()) != null)
			{
				bufferedWriter.write(temp);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
