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

import kr.ac.uos.ai.ieas.alerter.alerterView.AlerterAlertGeneratePanel;
import kr.ac.uos.ai.ieas.alerter.alerterView.AlerterCapGeneratePanel;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterAlertGeneratePanelModel
{	
	private _AlerterModelManager alerterModelManager;
	private KieasMessageBuilder kieasMessageBuilder;

	private String mViewName;

	private Vector<Object> mViewComponentProperties;

	private HashMap<String, String> mBasicComponents;
	private String mTextArea;
	private String mLoadTextField;
	private String mSaveTextField;

	private HashMap<String, String> mAlerterValues;
	private String mOrganization;
	private String mDomain;
	private String mUser;
	private String mUserDuty;

	private Vector<HashMap<String, String>> mInfoValues;
	private HashMap<String, String> mInfoIndexValues;
	private int mInfoIndex;
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
	
	private Vector<HashMap<String, String>> mResourceValues;
	private HashMap<String, String> mResourceIndexValues;
	private int mResourceCounter;
	private String mResourceDesc;
	private String mMimeType;
	private String mUri;
	
	private Vector<HashMap<String, String>> mAreaValues;
	private HashMap<String, String> mAreaIndexValues;
	private int mAreaCounter;
	private String mAreaDesc;
	private String mGeoCode;

	
	public AlerterAlertGeneratePanelModel(_AlerterModelManager _AlerterModelManager)
	{
		this.alerterModelManager = _AlerterModelManager;
		this.kieasMessageBuilder = new KieasMessageBuilder();

		init();
		initComponents();
	}

	private void init()
	{
		this.mViewName = this.getClass().getSimpleName().toString().replace("Model", "");
		
		this.mTextArea = "";		
		this.mLoadTextField = "cap/HRA.xml";
		this.mSaveTextField = "cap/out.xml";

		this.mOrganization = "";
		this.mDomain = "";
		this.mUser = "";
		this.mUserDuty = "";

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

		this.mResourceDesc = "";
		this.mMimeType = "";
		this.mUri = "";

		this.mAreaDesc = "";
		this.mGeoCode = "";
	}

	/**
	 * View 적용될 데이터 값을 가지고있는 Model Vector 생성 및 초기화.
	 * HashMap<String name, String value> name - 데이터 이름, value - 값	
	 */
	private void initComponents()
	{
		this.mViewComponentProperties = new Vector<>();
		mViewComponentProperties.addElement(AlerterCapGeneratePanel.TEXT_AREA);
		mViewComponentProperties.addElement(initSaveLoadPanelComponents());
		mViewComponentProperties.addElement(initAlertPanelComponents());
		mViewComponentProperties.addElement(initInfoPanelComponents());
		mViewComponentProperties.addElement(initResourcePanelComponents());
		mViewComponentProperties.addElement(initAreaPanelComponents());
	}

	
	private HashMap<String, String> initSaveLoadPanelComponents()
	{
		this.mBasicComponents = new HashMap<>();
		mBasicComponents.put(AlerterCapGeneratePanel.LOAD_TEXT_FIELD, mLoadTextField);
		mBasicComponents.put(AlerterCapGeneratePanel.SAVE_TEXT_FIELD, mSaveTextField);
		
		return mBasicComponents;
	}

	private HashMap<String, String> initAlertPanelComponents()
	{
		this.mAlerterValues = new HashMap<>();
		mAlerterValues.put(AlerterAlertGeneratePanel.ORGANIZAION, mOrganization);
		mAlerterValues.put(AlerterAlertGeneratePanel.DOMAIN, mDomain);
		mAlerterValues.put(AlerterAlertGeneratePanel.USER, mUser);
		mAlerterValues.put(AlerterAlertGeneratePanel.USER_DUTY, mUserDuty);
		
		return mAlerterValues;
	}

	private Vector<HashMap<String, String>> initInfoPanelComponents()
	{
		if(mInfoValues != null)
		{
			this.mInfoValues.clear();			
		}
		else
		{
			this.mInfoValues = new Vector<>();
		}
		this.mInfoIndex = 0;
		
		mInfoValues.addElement(addInfoIndexValues(mInfoIndex));
		
		return mInfoValues;
	}

	/**
	 * InfoIndex의 추가.	 * 
	 * @param index 추가되는 Info의 Index.
	 * @return HashMap<String, String>
	 */	
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
	
	public void addInfoIndex()
	{
		mInfoIndex++;
		addInfoIndexValues(mInfoIndex);
	}
	
	private Vector<HashMap<String, String>> initResourcePanelComponents()
	{
		this.mResourceValues = new Vector<>();
		this.mResourceCounter = 0;
		
		mResourceValues.addElement(addResourceIndexValues(mResourceCounter));
		
		return mResourceValues;
	}
	
	private HashMap<String, String> addResourceIndexValues(int index)
	{
		this.mResourceIndexValues = new HashMap<>();
		mResourceIndexValues.put(AlerterCapGeneratePanel.RESOURCE_DESC + index, mResourceDesc);
		mResourceIndexValues.put(AlerterCapGeneratePanel.MIME_TYPE + index, mMimeType);
		mResourceIndexValues.put(AlerterCapGeneratePanel.URI + index, mUri);

		return mResourceIndexValues;
	}
	
	private Vector<HashMap<String, String>> initAreaPanelComponents()
	{
		this.mAreaValues = new Vector<>();
		this.mAreaCounter = 0;
		
		mAreaValues.addElement(addAreaIndexValues(mAreaCounter));
		
		return mAreaValues;
	}
	
	private HashMap<String, String> addAreaIndexValues(int index)
	{
		this.mAreaIndexValues = new HashMap<>();
		mAreaIndexValues.put(AlerterCapGeneratePanel.AREA_DESC + index, mAreaDesc);
		mAreaIndexValues.put(AlerterCapGeneratePanel.GEO_CODE + index, mGeoCode);

		return mAreaIndexValues;
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
					System.out.println("Vector target = " + target);
					System.out.println("Vector memberName = " + memberName);
//					System.out.println("Vector value = " + value);
					
//					if(memberName.equals("mCategory"))
//					{
//						((HashMap<String, String>) component).replace(target, value);
//						alerterModelManager.updateView(mViewName, target, value);
//						return;
//					}
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
	
	private void setAlertPanel(KieasMessageBuilder kieasMessageBuilder)
	{
		setModelProperty(AlerterCapGeneratePanel.IDENTIFIER, kieasMessageBuilder.getIdentifier());
		setModelProperty(AlerterCapGeneratePanel.SENDER, kieasMessageBuilder.getSender());
		setModelProperty(AlerterCapGeneratePanel.SENT, kieasMessageBuilder.getSent());
		setModelProperty(AlerterCapGeneratePanel.STATUS, kieasMessageBuilder.getStatus());
		setModelProperty(AlerterCapGeneratePanel.MSG_TYPE, kieasMessageBuilder.getMsgType());
		setModelProperty(AlerterCapGeneratePanel.SCOPE, kieasMessageBuilder.getScope());
		setModelProperty(AlerterCapGeneratePanel.CODE, kieasMessageBuilder.getCode());		
		
		setInfoPanel(kieasMessageBuilder);
		setResourcePanel(kieasMessageBuilder);
		setAreaPanel(kieasMessageBuilder);
	}

	private void setInfoPanel(KieasMessageBuilder ieasMessage)
	{
		initInfoPanelComponents();
		mInfoIndex = ieasMessage.getInfoCount();	
		alerterModelManager.updateView(mViewName, AlerterCapGeneratePanel.INFO_INDEX, Integer.toString(mInfoIndex));
		for(int i = 0; i < mInfoIndex; i++)
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

	private void setResourcePanel(KieasMessageBuilder ieasMessage)
	{
		mResourceCounter = ieasMessage.getResourceCount(0);
		System.out.println("resourceCount : " + mResourceCounter);
		for(int i = 0; i < mResourceCounter; i++)
		{
			setModelProperty(AlerterCapGeneratePanel.RESOURCE_DESC + i, ieasMessage.getResourceDesc(0, i));
			setModelProperty(AlerterCapGeneratePanel.MIME_TYPE + i, ieasMessage.getMimeType(0, i));
			setModelProperty(AlerterCapGeneratePanel.URI + i, ieasMessage.getUri(0, i));
		}
	}
	
	private void setAreaPanel(KieasMessageBuilder ieasMessage)
	{
		mAreaCounter = ieasMessage.getAreaCount(0);
		System.out.println("areaCount : " + mAreaCounter);
		for(int i = 0; i < mAreaCounter; i++)
		{
			setModelProperty(AlerterCapGeneratePanel.AREA_DESC + i, ieasMessage.getAreaDesc(0, i));
			setModelProperty(AlerterCapGeneratePanel.GEO_CODE + i, ieasMessage.getGeoCode(0, i));
		}
	}
	

	private String transformToMemberName(String target)
	{
		target = "m" + target.replaceAll("[0-9]+", "").trim();
		return target;
	}
}
