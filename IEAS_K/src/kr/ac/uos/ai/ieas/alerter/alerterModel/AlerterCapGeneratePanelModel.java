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

	private String oldString;
	
	
	public AlerterCapGeneratePanelModel(_AlerterController alerterController) 
	{
		this.kieasMessage = new KieasMessageBuilder();
		
		this.m_TextAreaText = "";
		this.m_LoadTextFieldText = "cap/HRA.xml";
		this.m_SaveTextFieldText = "cap/out.xml";
	}
	
	public void setTextAreaText(String text)
	{
		oldString = this.m_TextAreaText;
		this.m_TextAreaText = text;
		firePropertyChange(_AlerterController.ALERTER_CAPGENERATEPANEL_TEXTAREA_PROPERTY, oldString, m_TextAreaText);
	}
	
	public void setIdentifierText(String text)
	{
		oldString = this.m_IdentifierText;
		this.m_IdentifierText = text;
		
		firePropertyChange(_AlerterController.ALERTER_IDENTIFIER_PROPERTY, oldString, m_IdentifierText);
	}
	
	public void setSenderText(String text)
	{
		oldString = this.m_SenderText;
		this.m_SenderText = text;
		
		firePropertyChange(_AlerterController.ALERTER_SENDER_PROPERTY, oldString, m_SenderText);
	}
	
	public void setStatusText(String text)
	{
		oldString = this.m_StatusText;
		this.m_StatusText = text;
		
		firePropertyChange(_AlerterController.ALERTER_STATUS_PROPERTY, oldString, m_StatusText);
	}
	
	private void setAlertPanel(KieasMessageBuilder ieasMessage)
	{
		setIdentifierText(ieasMessage.getIdentifier());
		setSenderText(ieasMessage.getSender());	
		setStatusText(ieasMessage.getStatus());
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

			setTextAreaText(content);
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
