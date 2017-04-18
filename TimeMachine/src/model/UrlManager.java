package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlManager {
	
	public static String URLConnectionReader(String urlS) {
		URL url;
		URLConnection yc;
		String inputLine;
		String results = "";
		try {
			url = new URL(urlS);
			yc = url.openConnection();

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					results += inputLine;
				}
				in.close();
			}
			catch (Exception e){
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

}
