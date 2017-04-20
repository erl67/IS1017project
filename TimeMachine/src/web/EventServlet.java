package web;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import model.UrlManager;

/**
 * Servlet implementation class EventServlet. The servlet queries the history API
 */
@WebServlet({ "/EventServlet", "/e" })
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//variables that used to construct the history API call URL
	private final String mlUrl = "http://history.muffinlabs.com/date/";
	private String mlMonth = "";
	private String mlDay = "";
	private long ut = System.currentTimeMillis() / 1000L; //current epoch time, in seconds 

	
	public EventServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log(request.toString()); log(response.toString());
		response.setContentType("application/json"); 
		response.getWriter().println("\nEventServletTest\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath()+"\n\n");
		response.addHeader("SERVLET_STATUS", "ok");
		response.setStatus(200);

		//create a Calendar object to hold the date, Calendar is then used to deconstruct the Month and Day as the API needs it
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ut * 1000L);	//have to convert unix time from seconds back to millis. Calendar doesn't allow setting time in pure seconds.
		response.getWriter().println("Calendar=" +  cal.toString() + "\n");

		mlMonth = String.valueOf(cal.get(Calendar.MONTH)+1);	//Months start at 0 so must +1
		mlDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		String mlAPI = mlUrl + mlMonth + "/" + mlDay;

		String events = UrlManager.URLConnectionReader(mlAPI);

		response.getWriter().println("ml URL= " +  mlAPI +"\n\nml API json=\n"+ events);
		response.addHeader("json", events);
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log(request.toString()); log(response.toString());
		response.setContentType("application/json"); 

		JsonObject queryJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());

		String mlMonth = queryJson.getString("month");
		String mlDay = queryJson.getString("day");

		String mlAPI = mlUrl + mlMonth + "/" + mlDay;
		log ("mlAPI= " + mlAPI);

		//Send the API URL to the URL reader and receive a String of Json in return
		String events = UrlManager.URLConnectionReader(mlAPI);

		//If API worked then send the results back else send an error message
		if (events.length() > 8) {
			response.setStatus(200);
			response.getWriter().println(events);
		} else {
			response.setStatus(500);
			response.getWriter().println("{ \"error\": \"Event API call did not complete successfully\" }");
		}
	}

}
