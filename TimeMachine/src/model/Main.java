package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.json.JsonObject;

import web.*;
import model.*;

public class Main {
	
	static LoginServlet l;
	WeatherServlet w;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		String dsKey = "472f1ba38a5f3d13407fdb589d975c8c/";
		String dsUrl = "https://api.darksky.net/forecast/";
		String dsLoc = "37.8267,-122.4233,";
		String dsTime = "946684800";

		String testURL = dsUrl + dsKey + dsLoc + dsTime;

		WeatherServlet.URLConnectionReader(testURL);
		
//		testLogin() ;
		
		String u = l.LoginBean2("ERIC", "123");
		System.out.println("User name returns: " + u);
		
//		u = LoginServlet.LoginBean("1","1");
		System.out.println("EM returns: " + u);


//		JsonObject json = readJsonFromUrl(testURL);
//		System.out.println(json.toString());
//		System.out.println(json.get("id"));

	}

	private static String readAll(Reader rd)  {
		StringBuilder sb = new StringBuilder();
		int cp;
		try {
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static JsonObject readJsonFromUrl(String url) {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			
			JsonObject json = null;
//			JsonReader jsonReader = Json.createReader(jsonText);
//			JsonArray array1 = jsonReader.readArray();
//			jsonReader.close();
			
//			JsonArray array = new JsonArray("[1,2,3,4,5]");
//			
//			System.out.println("jsonText= " + jsonText);
//			JsonObject json = new JsonObject(jsonText);
//			JsonArray array = new JsonArray(jsonText);
//			
			
			return json;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testLogin (){
		
		String u = null;
		
		u = l.LoginBean2("1", "1");
		System.out.println("User name returns: " + u);
		
		u = l.LoginBean2("1", "0");
		System.out.println("User name returns: " + u);
		
//		u = l.LoginBean("1","1");
//		System.out.println("EM returns: " + u);
		
		u = l.LoginBean("1","1");
		System.out.println("EM returns: " + u);
		
	}

}