package kr.or.kpew.kieas.issuer.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * xml 파일을 읽거나 쓰기위해 사용되는 클래스. 현재는 사용하지 않는다.
 * @author byun-ai
 *
 */
public class XmlReaderAndWriter
{
	private static final String DEFAULT_LOAD_PATH = "cap/HRA.xml";
	private static final String DEFAULT_SAVE_PATH = "cap/out.xml";
	
	private FileInputStream fileInputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private StringReader stringReader;

	private String loadPath;
	private String savePath;
	
	
	public XmlReaderAndWriter()
	{
		this.loadPath = DEFAULT_LOAD_PATH;
		this.savePath = DEFAULT_SAVE_PATH;
	}
	
	public String getLoadPath()
	{
		return this.loadPath;
	}	
	
	public String getSavePath()
	{
		return this.savePath;
	}
	
	public String loadXml(String path)
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

	public void writerXml(String path, String message)
	{
		this.stringReader = new StringReader(message);
		this.bufferedReader = new BufferedReader(stringReader);

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
			bufferedReader.close();
			stringReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
