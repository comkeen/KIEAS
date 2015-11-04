package kr.ac.uos.ai.ieas.alerter.alerterModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Vector;

import kr.ac.uos.ai.ieas.alerter.alerterView.AlerterCapGeneratePanel;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterCapGeneratePanelModel
{	
	private _AlerterModelManager alerterModelManager;
	private KieasMessageBuilder kieasMessageBuilder;

	private FileInputStream fileInputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private StringReader stringReader;

	private String mViewName;

	private Vector<Object> mViewComponentProperties;

	private HashMap<String, String> mComponents;
	private String mTextArea;
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

	public AlerterCapGeneratePanelModel(_AlerterModelManager _AlerterModelManager)
	{
		this.alerterModelManager = _AlerterModelManager;
		this.kieasMessageBuilder = new KieasMessageBuilder();

		init();
		initComponents();
	}

	private void init()
	{
		this.mViewName = this.getClass().getSimpleName().toString().replace("Model", "");
		this.mInfoCounter = 0;

		this.mTextArea = "";		
		this.mLoadTextField = "cap/HRA.xml";
		this.mSaveTextField = "cap/out.xml";

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

	private void initComponents()
	{
		this.mViewComponentProperties = new Vector<>();
		mViewComponentProperties.addElement(AlerterCapGeneratePanel.TEXT_AREA);
		mViewComponentProperties.addElement(initSaveLoadPanelComponents());
		mViewComponentProperties.addElement(initAlertPanelComponents());
		mViewComponentProperties.addElement(initInfoPanelComponents());
	}

	private HashMap<String, String> initSaveLoadPanelComponents()
	{
		this.mComponents = new HashMap<>();
		mComponents.put(AlerterCapGeneratePanel.LOAD_TEXT_FIELD, mLoadTextField);
		mComponents.put(AlerterCapGeneratePanel.SAVE_TEXT_FIELD, mSaveTextField);
		
		return mComponents;
	}

	private HashMap<String, String> initAlertPanelComponents()
	{
		this.mAlertValues = new HashMap<>();
		mAlertValues.put(AlerterCapGeneratePanel.IDENTIFIER, mIdentifier);
		mAlertValues.put(AlerterCapGeneratePanel.SENDER, mSender);
		mAlertValues.put(AlerterCapGeneratePanel.SENT, mSent);
		mAlertValues.put(AlerterCapGeneratePanel.STATUS, mStatus);
		mAlertValues.put(AlerterCapGeneratePanel.MSG_TYPE, mMsgType);
		mAlertValues.put(AlerterCapGeneratePanel.SCOPE, mScope);
		mAlertValues.put(AlerterCapGeneratePanel.CODE, mCode);
		
		return mAlertValues;
	}

	private Vector<HashMap<String, String>> initInfoPanelComponents()
	{
		this.mInfoValues = new Vector<>();
		this.mInfoCounter = 0;
		
		mInfoValues.addElement(addInfoIndexValues(mInfoCounter));
		
		return mInfoValues;
	}

	private HashMap<String, String> addInfoIndexValues(int index)
	{
		this.mInfoIndexValues = new HashMap<>();
		mInfoIndexValues.put(AlerterCapGeneratePanel.LANGUAGE + index, mLanguage);
		mInfoIndexValues.put(AlerterCapGeneratePanel.CATEGORY + index, mCategory);
		mInfoIndexValues.put(AlerterCapGeneratePanel.EVENT + index, mEvent);
		mInfoIndexValues.put(AlerterCapGeneratePanel.URGENCY + index, mUrgency);
		mInfoIndexValues.put(AlerterCapGeneratePanel.SEVERITY + index, mSeverity);
		mInfoIndexValues.put(AlerterCapGeneratePanel.CERTAINTY + index, mCertainty);
		mInfoIndexValues.put(AlerterCapGeneratePanel.EVENT_CODE + index, mEventCode);
		mInfoIndexValues.put(AlerterCapGeneratePanel.EFFECTIVE + index, mEffective);
		mInfoIndexValues.put(AlerterCapGeneratePanel.SENDER_NAME + index, mSenderName);
		mInfoIndexValues.put(AlerterCapGeneratePanel.HEADLINE + index, mHeadline);
		mInfoIndexValues.put(AlerterCapGeneratePanel.DESCRIPTION + index, mDescription);
		mInfoIndexValues.put(AlerterCapGeneratePanel.WEB + index, mWeb);
		mInfoIndexValues.put(AlerterCapGeneratePanel.CONTACT + index, mContact);

		return mInfoIndexValues;
	}

	public void setModelProperty(String target, String value)
	{
		String memberName = transformToMemberName(target);

		try
		{			
			this.getClass().getDeclaredField(memberName).set(this, value);

			for (Object component : mViewComponentProperties)
			{
				if (component instanceof HashMap<?, ?>)
				{				
					((HashMap<String, String>) component).replace(target, value);
					alerterModelManager.updateView(mViewName, target, value);
					return;
				}
				if (component instanceof Vector<?>)
				{
					for (HashMap<String, String> hashMap : (Vector<HashMap<String, String>>) component)
					{
						if(hashMap.containsKey(target))
						{
							hashMap.replace(target, value);
							alerterModelManager.updateView(mViewName, target, value);
							return;
						}
					}
				}
			}
		}
		catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) 
		{
			System.out.println("there is no such a memberName " + memberName);
			e.printStackTrace();
			return;
		}
		System.out.println("there is no such a ModelPropertyName " + target);
	}

	private void setAlertPanel(KieasMessageBuilder ieasMessage)
	{
		setModelProperty(AlerterCapGeneratePanel.IDENTIFIER, ieasMessage.getIdentifier());
		setModelProperty(AlerterCapGeneratePanel.SENDER, ieasMessage.getSender());
		setModelProperty(AlerterCapGeneratePanel.SENT, ieasMessage.getSent());
		setModelProperty(AlerterCapGeneratePanel.STATUS, ieasMessage.getStatus());
		setModelProperty(AlerterCapGeneratePanel.MSG_TYPE, ieasMessage.getMsgType());
		setModelProperty(AlerterCapGeneratePanel.SCOPE, ieasMessage.getScope());
		setModelProperty(AlerterCapGeneratePanel.CODE, ieasMessage.getCode());		
		
		setInfoPanel(ieasMessage);
	}

	private void setInfoPanel(KieasMessageBuilder ieasMessage)
	{
		mInfoCounter = ieasMessage.getInfoCount();
		for(int i = 0; i < mInfoCounter; i++)
		{
			setModelProperty(AlerterCapGeneratePanel.LANGUAGE + i, ieasMessage.getLanguage(i));
			setModelProperty(AlerterCapGeneratePanel.CATEGORY + i, ieasMessage.getCategory(i));
			setModelProperty(AlerterCapGeneratePanel.EVENT + i, ieasMessage.getEvent(i));
			setModelProperty(AlerterCapGeneratePanel.URGENCY + i, ieasMessage.getUrgency(i));
			setModelProperty(AlerterCapGeneratePanel.SEVERITY + i, ieasMessage.getSeverity(i));
			setModelProperty(AlerterCapGeneratePanel.CERTAINTY + i, ieasMessage.getCertainty(i));
			setModelProperty(AlerterCapGeneratePanel.EVENT_CODE + i, ieasMessage.getEventCode(i));
			setModelProperty(AlerterCapGeneratePanel.EFFECTIVE + i, ieasMessage.getEffective(i));
			setModelProperty(AlerterCapGeneratePanel.SENDER_NAME + i, ieasMessage.getSenderName(i));
			setModelProperty(AlerterCapGeneratePanel.HEADLINE + i, ieasMessage.getHeadline(i));
			setModelProperty(AlerterCapGeneratePanel.DESCRIPTION + i, ieasMessage.getDescrpition(i));
			setModelProperty(AlerterCapGeneratePanel.WEB + i, ieasMessage.getWeb(i));
			setModelProperty(AlerterCapGeneratePanel.CONTACT + i, ieasMessage.getContact(i));
		}
	}

	public void loadCap(String path)
	{
		String content = capLoader(path);
		kieasMessageBuilder.setMessage(content);
		
		setModelProperty("TextArea", content);
		setAlertPanel(kieasMessageBuilder);
	}

	public void saveCap(String path)
	{
		capWriter(path);
	}

	private String capLoader(String path)
	{
		String temp = "";
		String content = "";

		try
		{
			fileInputStream = new FileInputStream(new File(path));
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			while((temp = bufferedReader.readLine()) != null)
			{
				content += temp + "\n";
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}

	private void capWriter(String path)
	{
		stringReader = new StringReader(mTextArea);
		bufferedReader = new BufferedReader(stringReader);

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

	private String transformToMemberName(String target)
	{
		target = "m" + target.replaceAll("[0-9]+", "").trim();
		return target;
	}
}
