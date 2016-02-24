package kr.ac.uos.ai.ieas.db.dbHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class DataFormatUtils {

	private DataFormatUtils() {
	}

	public static String convertDateObjectType(java.util.Date datetime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(datetime);
	}
	
	public static Date convertStringToDate(String date) {
		date = date.replace("T", " ");
		date = date.replace("+09:00", "");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

//	public static String buildJsonFromElements(String rootName, HashMap<String, String> elements){
//		
//	}
	
	public static String jsonToXML(String jsonInput) {
		String xmlContent = null;
		try {
			JSONObject jsonObj = new JSONObject(jsonInput);
			xmlContent = XML.toString(jsonObj);
		} catch (JSONException e) {
			System.err.println("Error while creating xmlContent object: String not convertable to XML string.");
			e.printStackTrace();
		}
		return xmlContent;
	}
	

	public static String xmlToJson(String xmlInput) {
		String jsonContent = null;
		try {
			JSONObject jsonObj = XML.toJSONObject(xmlInput);
			jsonContent = jsonObj.toString();
		} catch (JSONException e) {
			System.err.println("Error while creating xmlContent object: String not convertable to XML string.");
			e.printStackTrace();
		}
		return jsonContent;
	}
	
	
	public static String calendarToCAPDatetimeString(Calendar calendar) {
		Date calDate = calendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" + "HH:mm:ss");
		String formattedString = sdf.format(calDate);

		String date = formattedString.substring(0, 10);
		String time = formattedString.substring(10, 18);
		String result = date + "T" + time + "+09:00";
		return result;
	}

	public static String removeDashFromLanguageCode(String input) {
		String lang = input.substring(0, 2);
		String code = input.substring(3, 5);
		return lang + code;
	}

	public static String addDashToLanguageCode(String input) {
		String lang = input.substring(0, 2);
		String code = input.substring(2, 4);
		return lang + "-" + code;
	}
	
	/*
	public static void main(String[] args) {

		CAPDBUtils util = new CAPDBUtils();

		ArrayList<CAPAlert> alerts = util.getAlerts();
		ArrayList<Calendar> cal = new ArrayList<Calendar>();

		for (CAPAlert alert : alerts) {
			Date date = alert.getSent();
			Calendar calElement = Calendar.getInstance();
			calElement.setTime(date);
			cal.add(calElement);
			String a = DataFormatUtils.calendarToCAPDatetimeString(calElement);
			System.out.println(a);
		}

		String test = "koKR";
		System.out.println(DataFormatUtils.addDashToLanguageCode(test));
		String test2 = "ko-KR";
		System.out.println(DataFormatUtils.removeDashFromLanguageCode(test2));
	}
	*/
}
