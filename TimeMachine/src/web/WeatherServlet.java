package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JsonObject;
import org.json.simple.Jsonable;

import model.WeatherFacade;

/**
 * Servlet implementation class WeatherServlet
 */
@WebServlet({ "/WeatherServlet", "/w" })
public class WeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	WeatherFacade wf;
	
    public WeatherServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("\n\n\nWeatherServletTest\n\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("SERVLET_STATUS", "ok");
//		response.setStatus(204);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		log(request.toString()); log(response.toString());

		
		JsonObject object;// = Json.createObjectBuilder().build();
		JsonObject jo = new JsonObject();
		Jsonable jv;
//		jv = "test";
//		
//		object.put("user", "test");
//		
//		try {
//			JsonParser parser;
//			JsonObject parse = parser.parse(request.getReader());
//		} catch (Exception e) {
//	
//		}
//		
//		JsonObject returnValue;
//		JsonValue jv = "success";
//		returnValue.put("user", new JsonValue("sucess"));
//		returnValue.put("user", "success");
//		response.getWriter().print(returnValue);
//		
		String u = "WeatherServlet for now";

		final String cookieName = "TimeMachine_weatherservlet";
		final String cookieValue = "This Does Nothing";  // you could assign it some encoded value
		final Boolean useSecureCookie = false;
		final int expiryTime = 60 * 60 * 24;  // 24h in seconds
		final String cookiePath = "/";

		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  

		if (u != null) {
			HttpSession session = request.getSession(true);
			Date createTime = new Date(session.getCreationTime());
			Date lastAccessTime = new Date(session.getLastAccessedTime());
			Integer visitCount = new Integer(0);
			String visitCountKey = new String("visitCount");
			String userIDKey = new String(u);
			String userID = new String(u);
			if (session.isNew()){
				session.setAttribute(userIDKey, userID);
			} else {
				visitCount = (Integer)session.getAttribute(visitCountKey);
				visitCount = visitCount + 1;
				userID = (String)session.getAttribute(userIDKey);
			}
			session.setAttribute(visitCountKey,  visitCount);

			Cookie tmc = new Cookie(cookieName, cookieValue);
			tmc.setSecure(useSecureCookie);
			tmc.setMaxAge(expiryTime);
			tmc.setPath(cookiePath); 
			response.addCookie(tmc);

			RequestDispatcher rd=request.getRequestDispatcher("index.html");  
			rd.include(request,response);  

		
		} else {

			Cookie tmc = new Cookie(cookieName, "Weather Servlet No Login");
			tmc.setSecure(useSecureCookie);
			tmc.setMaxAge(expiryTime);	//set to 0 to delete cookie
			tmc.setPath(cookiePath);
			response.addCookie(tmc);

			out.print("Sorry username or password error");  
			RequestDispatcher rd=request.getRequestDispatcher("index.html");  

			response.getWriter().print("fail");
			response.addHeader("WEATHER_STATUS", "OK");
//			request.setAttribute("user", "");

			rd.include(request,response);  
		}

	}
	

	public static void URLConnectionReader(String urlS) {
		URL url;
		URLConnection yc;
		String inputLine;
		try {
			url = new URL(urlS);
			yc = url.openConnection();

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				while ((inputLine = in.readLine()) != null) 
					System.out.println(inputLine);
				
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
	}

}
