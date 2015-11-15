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

	/**
	 * CAP 메시지를 다루기 위해 사용되는 KieasMessageBuilder 객체 생성.
	 * @param _AlerterModelManager Model들을 관리하는 ModelManager. 
	 */
	public AlerterCapGeneratePanelModel(_AlerterModelManager _AlerterModelManager)
	{
		this.alerterModelManager = _AlerterModelManager;
		this.kieasMessageBuilder = new KieasMessageBuilder();

		init();
		initComponents();
	}

	/**
	 * 멤버변수 초기화.
	 * mViewName - 이 Model의 표적이 되는 View 클래스의 이름.
	 */
	private void init()
	{
		this.mViewName = this.getClass().getSimpleName().toString().replace("Model", "");
		
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
	
	/**
	 * ResourceIndex의 추가.
	 * @param index 추가되는 Resource Index.
	 * @return HashMap<String, String>
	 */
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
	
	/**
	 * AreaIndex의 추가.
	 * @param index 추가되는 Area Index.
	 * @return HashMap<String, String>
	 */
	private HashMap<String, String> addAreaIndexValues(int index)
	{
		this.mAreaIndexValues = new HashMap<>();
		mAreaIndexValues.put(AlerterCapGeneratePanel.AREA_DESC + index, mAreaDesc);
		mAreaIndexValues.put(AlerterCapGeneratePanel.GEO_CODE + index, mGeoCode);

		return mAreaIndexValues;
	}

	/**
	 * Model Vector의 값을 변경할 때 사용됨.
	 * 값이 변경되면 updateView 호출을 통해 View에 변경된 값을 갱신.
	 * @param target 변경될 데이터의 이름
	 * @param value 변경될 데이터의 값
	 */
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

	/**
	 * Cap메시지를 로드했을 시 호출됨.
	 * Alert패널의 데이터들을 일괄 변경, 갱신함.
	 * @param kieasMessageBuilder 갱신되는 Cap 메시지 객체.
	 */
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
	
	/**
	 * Cap 메시지를 불러올 때 호출됨.
	 * @param path 불러올 Cap의 경로
	 */
	public void loadCap(String path)
	{
		String content = capLoader(path);
		kieasMessageBuilder.setMessage(content);
		
		setModelProperty("TextArea", content);
		setAlertPanel(kieasMessageBuilder);
	}

	/**
	 * 작성된 Cap 메시지를 파일로 쓸 때 호출됨.
	 * @param path 파일이 저장될 경로
	 */
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
