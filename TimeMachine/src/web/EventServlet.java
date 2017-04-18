package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

/**
 * Servlet implementation class EventServlet
 */
@WebServlet({ "/EventServlet", "/e" })
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ut * 1000L);
		response.getWriter().println("Calendar=" +  cal.toString() + "\n");

		mlMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
		mlDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		String events = null;
		String mlAPI = mlUrl + mlMonth + "/" + mlDay;

		events = URLConnectionReader(mlAPI);

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

		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(queryJson.getLong("date"));
		
		mlMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
		mlDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		String mlAPI = mlUrl + mlMonth + "/" + mlDay;
		log ("mlAPI= " + mlAPI);

		String events = URLConnectionReader(mlAPI);

		if (events.length() > 8) {
			response.setStatus(200);
			response.getWriter().println(events);
		} else {
			response.setStatus(500);
			response.getWriter().println("{ \"error\": \"Event API call did not complete successfully\" }");
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
