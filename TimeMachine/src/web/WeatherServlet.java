package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import model.WeatherFacade;

/**
 * Servlet implementation class WeatherServlet
 */
@WebServlet({ "/WeatherServlet", "/w" })
public class WeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final String dsKey = "472f1ba38a5f3d13407fdb589d975c8c/";
	private final String dsUrl = "https://api.darksky.net/forecast/";
	private String dsLat = "37.8267";
	private String dsLon = "-122.4233";
	private String dsLoc = dsLat + "," + dsLon + ",";
	private long dsTime = -1L; 
	private long ut = System.currentTimeMillis() / 1000L; //current epoch time

	@EJB
	WeatherFacade wf;

	public WeatherServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log(request.toString()); log(response.toString());
		response.setContentType("application/json"); 
		response.getWriter().println("\n\n\nWeatherServletTest\n\nnow="+ ut +"\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath()+"\n\n");
		response.addHeader("SERVLET_STATUS", "ok");
		response.setStatus(200);

		String date = "1916-04-11T16:38:58.393Z";  //format of JSON input for testing
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
			ZonedDateTime zdt = ZonedDateTime.parse(date,dtf);        
			dsTime = zdt.toEpochSecond();
			log("date string=" + date + "dsTime string=" + dsTime);
			response.getWriter().println("date string=" + date + "\tepoch=" + dsTime + "\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		dsTime = Long.valueOf(new Date().getTime()/1000L);

		String wx = null;
		String dsAPI = dsUrl + dsKey + dsLoc + dsTime;

		wx = URLConnectionReader(dsAPI);

		response.getWriter().println("ds URL= " + dsAPI +"\n\nds API json=\n"+ wx);
		response.addHeader("json", wx);
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

		log(request.toString()); log(response.toString());
		response.setContentType("application/json"); 

		JsonObject queryJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());

		String date = queryJson.getString("date");
		
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
			ZonedDateTime zdt = ZonedDateTime.parse(date,dtf);        
			dsTime = zdt.toEpochSecond();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dsLat = queryJson.getString("latitude");
		dsLon = queryJson.getString("longitude");
		dsLoc = dsLat + "," + dsLon + ",";

		String dsAPI = dsUrl + dsKey + dsLoc + dsTime;
		log ("dsAPI= " + dsAPI);

		String wx = URLConnectionReader(dsAPI);

		if (wx.length() > 8) {
			response.setStatus(200);
			response.getWriter().println(wx);
		} else {
			response.setStatus(500);
			response.getWriter().println("{ \"error\": \"API call did not complete successfully\" }");
		}
	}

	public String URLConnectionReader(String urlS) {
		URL url;
		URLConnection yc;
		String inputLine;
		String wx = "";
		try {
			url = new URL(urlS);
			yc = url.openConnection();

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					wx += inputLine;
					log("inputLine= " + inputLine + " / wx = " + wx);
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
		return wx;
	}

}
