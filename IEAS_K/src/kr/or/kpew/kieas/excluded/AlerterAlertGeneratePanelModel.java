package kr.or.kpew.kieas.excluded;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

	private Map<String, String> mBasicComponents;
	private String mTextArea;
	private String mLoadTextField;
	private String mSaveTextField;

	private Map<String, String> mAlerterValues;
	private String mOrganization;
	private String mDomain;
	private String mUser;
	private String mUserDuty;

	private Vector<Map<String, String>> mInfoValues;
	private Map<String, String> mInfoIndexValues;
	private int infoIndex;
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
	
	private Vector<Map<String, String>> mResourceValues;
	private Map<String, String> mResourceIndexValues;
	private int mResourceCounter;
	private String mResourceDesc;
	private String mMimeType;
	private String mUri;
	
	private Vector<Map<String, String>> mAreaValues;
	private Map<String, String> mAreaIndexValues;
	private int mAreaCounter;
	private String mAreaDesc;
	private String mGeoCode;
	private String message;
	
	private boolean hasEnglish;
	private boolean hasRestriction;

	
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
		
		this.hasEnglish = false;
		
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

	
	private Map<String, String> initSaveLoadPanelComponents()
	{
		this.mBasicComponents = new HashMap<>();
		mBasicComponents.put(AlerterCapGeneratePanel.LOAD_TEXT_FIELD, mLoadTextField);
		mBasicComponents.put(AlerterCapGeneratePanel.SAVE_TEXT_FIELD, mSaveTextField);
		
		return mBasicComponents;
	}

	private Map<String, String> initAlertPanelComponents()
	{
		this.mAlerterValues = new HashMap<>();
		mAlerterValues.put(AlerterAlertGeneratePanel.ORGANIZAION, mOrganization);
		mAlerterValues.put(AlerterAlertGeneratePanel.DOMAIN, mDomain);
		mAlerterValues.put(AlerterAlertGeneratePanel.USER, mUser);
		mAlerterValues.put(AlerterAlertGeneratePanel.USER_DUTY, mUserDuty);
		
		return mAlerterValues;
	}

	private Vector<Map<String, String>> initInfoPanelComponents()
	{
		if(mInfoValues != null)
		{
			this.mInfoValues.clear();			
		}
		else
		{
			this.mInfoValues = new Vector<>();
		}
		this.infoIndex = 0;
		
		mInfoValues.addElement(addInfoIndexValues(infoIndex));
		
		return mInfoValues;
	}

	/**
	 * InfoIndex의 추가.	 * 
	 * @param index 추가되는 Info의 Index.
	 * @return HashMap<String, String>
	 */	
	private Map<String, String> addInfoIndexValues(int index)
	{
		this.mInfoIndexValues = new HashMap<>();
		mInfoIndexValues.put(KieasMessageBuilder.LANGUAGE + index, mLanguage);
		mInfoIndexValues.put(KieasMessageBuilder.CATEGORY + index, mCategory);
		mInfoIndexValues.put(KieasMessageBuilder.EVENT + index, mEvent);
		mInfoIndexValues.put(KieasMessageBuilder.URGENCY + index, mUrgency);
		mInfoIndexValues.put(KieasMessageBuilder.SEVERITY + index, mSeverity);
		mInfoIndexValues.put(KieasMessageBuilder.CERTAINTY + index, mCertainty);
		mInfoIndexValues.put(KieasMessageBuilder.EVENT_CODE + index, mEventCode);
		mInfoIndexValues.put(KieasMessageBuilder.EFFECTIVE + index, mEffective);
		mInfoIndexValues.put(KieasMessageBuilder.SENDER_NAME + index, mSenderName);
		mInfoIndexValues.put(KieasMessageBuilder.HEADLINE + index, mHeadline);
		mInfoIndexValues.put(KieasMessageBuilder.DESCRIPTION + index, mDescription);
		mInfoIndexValues.put(KieasMessageBuilder.WEB + index, mWeb);
		mInfoIndexValues.put(KieasMessageBuilder.CONTACT + index, mContact);

		return mInfoIndexValues;
	}
	
	public void addInfoIndex()
	{
		infoIndex++;
		addInfoIndexValues(infoIndex);
	}
	
	private Vector<Map<String, String>> initResourcePanelComponents()
	{
		this.mResourceValues = new Vector<>();
		this.mResourceCounter = 0;
		
		mResourceValues.addElement(addResourceIndexValues(mResourceCounter));
		
		return mResourceValues;
	}
	
	private Map<String, String> addResourceIndexValues(int index)
	{
		this.mResourceIndexValues = new HashMap<>();
		mResourceIndexValues.put(KieasMessageBuilder.RESOURCE_DESC + index, mResourceDesc);
		mResourceIndexValues.put(KieasMessageBuilder.MIME_TYPE + index, mMimeType);
		mResourceIndexValues.put(KieasMessageBuilder.URI + index, mUri);

		return mResourceIndexValues;
	}
	
	private Vector<Map<String, String>> initAreaPanelComponents()
	{
		this.mAreaValues = new Vector<>();
		this.mAreaCounter = 0;
		
		mAreaValues.addElement(addAreaIndexValues(mAreaCounter));
		
		return mAreaValues;
	}
	
	private Map<String, String> addAreaIndexValues(int index)
	{
		this.mAreaIndexValues = new HashMap<>();
		mAreaIndexValues.put(KieasMessageBuilder.AREA_DESC + index, mAreaDesc);
		mAreaIndexValues.put(KieasMessageBuilder.GEO_CODE + index, mGeoCode);

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
				if (component instanceof Map<?, ?>)
				{
//					System.out.println("Vector target = " + target);
//					System.out.println("Vector memberName = " + memberName);
//					System.out.println("Vector value = " + value);

					((Map<String, String>) component).replace(target, value);
					alerterModelManager.updateView(mViewName, target, value);
					return;
				}
				if (component instanceof Vector<?>)
				{
					
					for (Map<String, String> hashMap : (Vector<Map<String, String>>) component)
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
			System.out.println("There is no such a memberName : " + memberName);
			e.printStackTrace();
			return;
		}
		System.out.println("There is no such a ModelPropertyName : " + target);
	}
	
	private void setAlertPanel(KieasMessageBuilder kieasMessageBuilder)
	{
//		setModelProperty(KieasMessageBuilder.IDENTIFIER, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.IDENTIFIER));
//		setModelProperty(KieasMessageBuilder.SENDER, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.SENDER));
//		setModelProperty(KieasMessageBuilder.SENT, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.SENT));
//		setModelProperty(KieasMessageBuilder.STATUS, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.STATUS));
//		setModelProperty(KieasMessageBuilder.MSG_TYPE, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.MSG_TYPE));
//		setModelProperty(KieasMessageBuilder.SCOPE, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.SCOPE));
//		setModelProperty(KieasMessageBuilder.CODE, kieasMessageBuilder.getAlertElement(KieasMessageBuilder.CODE));		
		
		setInfoPanel(kieasMessageBuilder);
		setResourcePanel(kieasMessageBuilder);
		setAreaPanel(kieasMessageBuilder);
	}

	private void setInfoPanel(KieasMessageBuilder kieasMessageBuilder)
	{
		initInfoPanelComponents();
		infoIndex = kieasMessageBuilder.getInfoCount();
		alerterModelManager.updateView(mViewName, AlerterCapGeneratePanel.INFO_INDEX, Integer.toString(infoIndex));
		for(int i = 0; i < infoIndex; i++)
		{
//			setModelProperty(KieasMessageBuilder.LANGUAGE + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.LANGUAGE));
//			setModelProperty(KieasMessageBuilder.CATEGORY + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.CATEGORY));
//			setModelProperty(KieasMessageBuilder.EVENT + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.EVENT));
//			setModelProperty(KieasMessageBuilder.URGENCY + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.URGENCY));
//			setModelProperty(KieasMessageBuilder.SEVERITY + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.SEVERITY));
//			setModelProperty(KieasMessageBuilder.CERTAINTY + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.CERTAINTY));
//			setModelProperty(KieasMessageBuilder.EVENT_CODE + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.EVENT_CODE));
//			setModelProperty(KieasMessageBuilder.EFFECTIVE + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.EFFECTIVE));
//			setModelProperty(KieasMessageBuilder.SENDER_NAME + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.SENDER_NAME));
//			setModelProperty(KieasMessageBuilder.HEADLINE + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.HEADLINE));
//			setModelProperty(KieasMessageBuilder.DESCRIPTION + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.DESCRIPTION));
//			setModelProperty(KieasMessageBuilder.WEB + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.WEB));
//			setModelProperty(KieasMessageBuilder.CONTACT + i, kieasMessageBuilder.getInfoElement(infoIndex, KieasMessageBuilder.CONTACT));
		}
	}

	private void setResourcePanel(KieasMessageBuilder kieasMessageBuilder)
	{
		mResourceCounter = kieasMessageBuilder.getResourceCount(0);
//		System.out.println("resourceCount : " + mResourceCounter);
		for(int i = 0; i < mResourceCounter; i++)
		{
//			setModelProperty(KieasMessageBuilder.RESOURCE_DESC + i, kieasMessageBuilder.getResourceElement(0, 0, KieasMessageBuilder.RESOURCE_DESC));
//			setModelProperty(KieasMessageBuilder.MIME_TYPE + i, kieasMessageBuilder.getResourceElement(0, 0, KieasMessageBuilder.MIME_TYPE));
//			setModelProperty(KieasMessageBuilder.URI + i, kieasMessageBuilder.getResourceElement(0, 0, KieasMessageBuilder.URI));
		}
	}
	
	private void setAreaPanel(KieasMessageBuilder kieasMessageBuilder)
	{
		mAreaCounter = kieasMessageBuilder.getAreaCount(0);
//		System.out.println("areaCount : " + mAreaCounter);
		for(int i = 0; i < mAreaCounter; i++)
		{
//			setModelProperty(KieasMessageBuilder.AREA_DESC + i, kieasMessageBuilder.getAreaElement(0, 0, KieasMessageBuilder.AREA_DESC));
//			setModelProperty(KieasMessageBuilder.GEO_CODE + i, kieasMessageBuilder.getAreaElement(0, 0, KieasMessageBuilder.GEO_CODE));
		}
	}
	
	private String transformToMemberName(String target)
	{
		target = "m" + target.replaceAll("[0-9]+", "").trim();
		return target;
	}

	public void setQueryResult(ArrayList<String> result)
	{
		String content = capLoader("cap/HRW");
		
		kieasMessageBuilder.setMessage(content);
		setModelProperty("TextArea", content);
		setAlertPanel(kieasMessageBuilder);
	}
	
	public String buildCapMessage()
	{
//		kieasMessageBuilder.setAlert();
//		kieasMessageBuilder.setInfo();
//		kieasMessageBuilder.setArea();
		return message;
	}
	
	private String capLoader(String path)
	{
		String temp = "";
		String content = "";

		try
		{
			FileInputStream fileInputStream = new FileInputStream(new File(path));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			BufferedReader 	bufferedReader = new BufferedReader(inputStreamReader);

			while((temp = bufferedReader.readLine()) != null)
			{
				content += temp + "\n";
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}
}
