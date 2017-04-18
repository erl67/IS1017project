package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class is for handling URL calls from the servlets
 */
public class UrlManager {

	/**
	 * Receive a String of a URL and return String of results Useful for making
	 * API calls that return Json (which is a string)
	 */
	public static String URLConnectionReader(String urlS) {

		URL url;
		URLConnection con;
		String inputLine;
		String results = "";

		try {

			url = new URL(urlS);
			con = url.openConnection();

			try {

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while ((inputLine = in.readLine()) != null) {
					results += inputLine;
				}

				in.close();
			}

			catch (Exception e) {
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
