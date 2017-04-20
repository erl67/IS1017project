package web;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import model.UrlManager;
import model.WeatherFacade;

/**
 * Servlet implementation class WeatherServlet
 */
@WebServlet({ "/WeatherServlet", "/w" })
public class WeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//variables used to construct API call
	private final String dsKey = "472f1ba38a5f3d13407fdb589d975c8c/";
	private final String dsUrl = "https://api.darksky.net/forecast/";
	private final String exclude = "?exclude=minutely,hourly,flags";
	private String dsLat = "40.447347";
	private String dsLon = "-79.952746";
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
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log(request.toString()); log(response.toString());
		response.setContentType("application/json"); 
		response.getWriter().println("\n\n\nWeatherServletTest\n\nnow="+ ut +"\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath()+"\n\n");
		response.addHeader("SERVLET_STATUS", "ok");
		response.setStatus(200);
		
		dsTime = Long.valueOf(new Date().getTime()/1000L);	//returns current unix epoch time

		String dsAPI = dsUrl + dsKey + dsLoc + dsTime;
		
		String wx = UrlManager.URLConnectionReader(dsAPI);		//call method to parse API URL return to string

		response.getWriter().println("ds URL= " + dsAPI +"\n\nds API json=\n"+ wx);
		response.addHeader("json", wx);
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log(request.toString()); log(response.toString());
		response.setContentType("application/json"); 

		//take Json input from server and add to API variables
		JsonObject queryJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());
		dsTime = queryJson.getLong("date");
		dsLat = queryJson.getString("latitude");
		dsLon = queryJson.getString("longitude");
		dsLoc = dsLat + "," + dsLon + ",";

		String dsAPI = dsUrl + dsKey + dsLoc + 	dsTime + exclude;	//URL string
		log ("dsAPI= " + dsAPI);

		String wx = UrlManager.URLConnectionReader(dsAPI);	//call API

		//verify Api returns data and return to browser, or send error if API fails
		if (wx.length() > 8) {
			response.setStatus(200);
			response.getWriter().println(wx);
		} else {
			response.setStatus(500);
			response.getWriter().println("{ \"error\": \"Weather API call did not complete successfully\" }");
		}
	}

}
