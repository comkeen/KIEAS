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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import kr.ac.uos.ai.ieas.abstractClass.AbstractModel;
import kr.ac.uos.ai.ieas.alerter.alerterController._AlerterController;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;


public class AlerterCapGeneratePanelModel extends AbstractModel
{	
	private KieasMessageBuilder kieasMessage;
	
	private FileInputStream fileInputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private StringReader stringReader;
	
	private String m_TextAreaText;
	private String m_LoadTextFieldText;
	private String m_SaveTextFieldText;
	
	private String m_IdentifierText;
	private String m_SenderText;
	private String m_SentText;
	private String m_StatusText;
	private String m_MsgTypeText;
	private String m_ScopeText;
	private String m_CodeText;
	
	private String[][] m_InfoValueTexts;

	private int m_infoCounter;
	private String oldString;
	
	
	public AlerterCapGeneratePanelModel(_AlerterController alerterController)
	{
		this.kieasMessage = new KieasMessageBuilder();
		
		this.m_TextAreaText = "";
		this.m_LoadTextFieldText = "cap/HRA.xml";
		this.m_SaveTextFieldText = "cap/out.xml";
		this.m_infoCounter = 0;
	}
	
	public void setLoadTextField(String text)
	{
		oldString = this.m_LoadTextFieldText;
		this.m_LoadTextFieldText = text;
		firePropertyChange(_AlerterController.CGPANEL_LOAD_TEXT_FEILD_PROPERTY, oldString, m_LoadTextFieldText);
	}
	
	public void setSaveTextField(String text)
	{
		oldString = this.m_SaveTextFieldText;
		this.m_SaveTextFieldText = text;
		firePropertyChange(_AlerterController.CGPANEL_SAVE_TEXT_FEILD_PROPERTY, oldString, m_SaveTextFieldText);
	}
	
	public void setTextArea(String text)
	{
		oldString = this.m_TextAreaText;
		this.m_TextAreaText = text;
		firePropertyChange(_AlerterController.CGPANEL_TEXT_AREA_PROPERTY, oldString, m_TextAreaText);
	}
	
	public void setIdentifier(String text)
	{
		oldString = this.m_IdentifierText;
		this.m_IdentifierText = text;
		
		firePropertyChange(_AlerterController.CGPANEL_IDENTIFIER_PROPERTY, oldString, m_IdentifierText);
	}
	
	public void setSender(String text)
	{
		oldString = this.m_SenderText;
		this.m_SenderText = text;
		
		firePropertyChange(_AlerterController.CGPANEL_SENDER_PROPERTY, oldString, m_SenderText);
	}
	
	public void setSent(String text)
	{
		oldString = this.m_SentText;
		this.m_SentText = text;
		
		firePropertyChange(_AlerterController.CGPANEL_SENT_PROPERTY, oldString, m_SentText);
	}
	
	public void setStatus(String text)
	{
		oldString = this.m_StatusText;
		this.m_StatusText = text;
		
		firePropertyChange(_AlerterController.CGPANEL_STATUS_PROPERTY, oldString, m_StatusText);
	}
	
	public void setMsgType(String text)
	{
		oldString = this.m_MsgTypeText;
		this.m_MsgTypeText = text;
		
		firePropertyChange(_AlerterController.CGPANEL_MSG_TYPE_PROPERTY, oldString, m_MsgTypeText);
	}
	
	public void setScope(String text)
	{
		oldString = this.m_ScopeText;
		this.m_ScopeText = text;
		
		firePropertyChange(_AlerterController.CGPANEL_SCOPE_PROPERTY, oldString, m_ScopeText);
	}
	
	public void setCode(String text)
	{
		oldString = this.m_CodeText;
		this.m_CodeText = text;
		
		firePropertyChange(_AlerterController.CGPANEL_CODE_PROPERTY, oldString, m_CodeText);
	}
	
	private void setAlertPanel(KieasMessageBuilder ieasMessage)
	{
		setIdentifier(ieasMessage.getIdentifier());
		setSender(ieasMessage.getSender());	
		setSent(ieasMessage.getSent());	
		setStatus(ieasMessage.getStatus());
		setMsgType(ieasMessage.getMsgType());
		setScope(ieasMessage.getScope());
		setCode(ieasMessage.getCode());
	}


	/*
	private void setInfoPanel(KieasMessageBuilder ieasMessage)
	{
		for(int infoCounter = 0; infoCounter < ieasMessage.getInfoCount(); infoCounter++)
		{
			for(int i = 0; i < infoEnumValues[infoCounter][0].getItemCount(); i++)
			{
				if(infoEnumValues[infoCounter][0].getItemAt(i).equals(ieasMessage.getLanguage()))
				{
					infoEnumValues[infoCounter][0].setSelectedIndex(i);
				}				
			}
			for(int i = 0; i < infoEnumValues[infoCounter][1].getItemCount(); i++)
			{
				if(infoEnumValues[infoCounter][1].getItemAt(i).equals(ieasMessage.getCategory()))
				{
					infoEnumValues[infoCounter][1].setSelectedIndex(i);
				}				
			}
			infoValues[infoCounter][2].setText(ieasMessage.getEvent());
			for(int i = 0; i < infoEnumValues[infoCounter][3].getItemCount(); i++)
			{
				if(infoEnumValues[infoCounter][3].getItemAt(i).equals(ieasMessage.getUrgency()))
				{
					infoEnumValues[infoCounter][3].setSelectedIndex(i);
				}				
			}
			for(int i = 0; i < infoEnumValues[infoCounter][4].getItemCount(); i++)
			{
				if(infoEnumValues[infoCounter][4].getItemAt(i).equals(ieasMessage.getSeverity()))
				{
					infoEnumValues[infoCounter][4].setSelectedIndex(i);
				}				
			}
			for(int i = 0; i < infoEnumValues[infoCounter][5].getItemCount(); i++)
			{
				if(infoEnumValues[infoCounter][5].getItemAt(i).equals(ieasMessage.getCertainty()))
				{
					infoEnumValues[infoCounter][5].setSelectedIndex(i);
				}				
			}
			for(int i = 0; i < infoEnumValues[infoCounter][6].getItemCount(); i++)
			{
				String[] tokens = infoEnumValues[infoCounter][6].getItemAt(i).split("\\(");

				if(tokens[0].equals(ieasMessage.getEventCode()))
				{
					infoEnumValues[infoCounter][6].setSelectedIndex(i);
				}				
			}
			infoValues[infoCounter][7].setText(ieasMessage.transformToYmdhms(ieasMessage.getEffective()));
			infoValues[infoCounter][8].setText(ieasMessage.getSenderName());
			infoValues[infoCounter][9].setText(ieasMessage.getWeb());
			infoValues[infoCounter][10].setText(ieasMessage.getContact());
			infoValues[infoCounter][11].setText(ieasMessage.getHeadline());
		}
		infoDescriptionValues[0].setText(ieasMessage.getDescrpition());
	}
*/
	public void capLoader()
	{
		try
		{
			String path = m_LoadTextFieldText;

			String temp = "";	         
			String content = "";

			fileInputStream = new FileInputStream(new File(path));
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			while((temp = bufferedReader.readLine()) != null)
			{
				content += temp + "\n";
			}

			setTextArea(content);
			kieasMessage.setMessage(content);

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
		this.stringReader = new StringReader(m_TextAreaText);
		bufferedReader = new BufferedReader(stringReader);

		//		String extension= ".xml";
		String path = m_SaveTextFieldText;
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
