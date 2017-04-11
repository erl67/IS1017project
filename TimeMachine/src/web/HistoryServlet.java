package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.json.JsonValue;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JsonObject;
import org.json.simple.parser.JSONParser;

import model.HistoryFacade;
import model.WxHist;
import model.WxUser;

/**
 * Servlet implementation class HistoryServlet
 */
@SuppressWarnings("deprecation")
@WebServlet({ "/HistoryServlet", "/h" })
public class HistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	HistoryFacade hf;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HistoryServlet() {
		super();
	}

	public WxUser GetUserBean (int uid){
		System.out.println("GetUser Bean, u="+uid);
		return hf.getUser(uid);
	}

	public List<WxHist> GetHistoryBean (int uid){
		System.out.println("GetUser Bean, u="+uid);
		return hf.getHistory(uid);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().println("\n\n\nHistoryServletTest\n\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath() + "\n\n\n");
		response.addHeader("SERVLET_STATUS", "ok");

		int uid = -1;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("TImeMachine_uid")) {
					uid = Integer.valueOf(cookie.getValue());
				}
			}
		}
		
		List<WxHist> historyList = GetHistoryBean(uid);
		Object jHistory = "Object of nothing";
		
		for (WxHist i : historyList) {
			
			response.getWriter().println(i.getId() + " " + i.getWxUser().getUserName() + " " + i.getTitle() + " " + i.getDate() + " " + i.getLatitude() + " " + i.getLongitude()+"\n");
			
//			jHistory.put("date", (JsonValue) i.getDate());
//			jHistory.put("latitude",i.getLatitude());
//			jHistory.put("longitude",  String.valueOf(i.getLongitude()));
//			jHistory.put("title", i.getTitle());
//			jHistory.put("user", i.getWxUser().getUserName());
//			jHistory.put("id", i.getId());	
//			log(jHistory.toString());
			
		}
		response.getWriter().println("json created= "+ jHistory.toString());
		
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		log(request.toString()); log(response.toString());
		PrintWriter out = response.getWriter();  
		RequestDispatcher rd=request.getRequestDispatcher("index.html");  
		response.setContentType("text/html");  
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(request.getParameter("json"));
			log("json= " + json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<WxHist> historyList = null;

		final String cookieName = "TimeMachine_history";
		final Boolean useSecureCookie = false;
		final int expiryTime = 60 * 60 * 24;  // 24h in seconds
		final String cookiePath = "/";

		int uid = -1;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("TImeMachine_uid")) {
					uid = Integer.valueOf(cookie.getValue());
				}
			}
		}

		WxHist history = new WxHist();
		history.setDate(new Date());
		history.setTitle("Testing HistoryServlet");
		history.setLatitude("0");
		history.setLongitude("0");
		history.setWxUser(GetUserBean(uid));

		boolean checkAddition = hf.addHistory(history, uid);

		if (checkAddition == true) {
			Cookie tmc = new Cookie(cookieName, "History Added");
			tmc.setSecure(useSecureCookie);
			tmc.setMaxAge(expiryTime);
			tmc.setPath(cookiePath); 
			response.addCookie(tmc);
			response.getWriter().print("history found");
			response.addHeader("HISTORY_SERVLET", "OK");
			response.setStatus(200);
			
			historyList = GetHistoryBean(uid);

//			JsonObject jHistory = new JsonObject();
			for (WxHist i : historyList) {
				
				log(i.getId() + " " + i.getWxUser() + " " + i.getTitle() + " " + i.getDate() + " " + i.getLatitude() + " " + i.getLongitude());
				
//				jHistory.put("date", i.getDate());
//				jHistory.put("latitude",i.getLatitude());
//				jHistory.put("longitude",  String.valueOf(i.getLongitude()));
//				jHistory.put("title", i.getTitle());
//				jHistory.put("user", i.getWxUser().getUserName());
//				jHistory.put("id", i.getId());	
			}
			
//			log(jHistory.toString());
			response.addHeader("history", historyList.toString());
			response.addHeader("json", historyList.toString());

		} else {
			Cookie tmc = new Cookie(cookieName, "History Error");
			tmc.setSecure(useSecureCookie);
			tmc.setMaxAge(expiryTime);
			tmc.setPath(cookiePath);
			response.addCookie(tmc);
			response.setStatus(418);
		}


//		//Read previous history
//
//		JsonObject newHist = new JsonObject();
//		String s = "[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
//
//		try{
//
//			JSONParser parser = new JSONParser();
//			JSONObject json = (JSONObject) parser.parse(jHistory.toString());
//
//			//	    	  Object obj = p.deserialize(jHistory.toString());
//			Jsoner p = null;
//			Object obj = p.deserialize(jHistory.toString(), newHist);
//
//			JsonObject jsonObject = (JsonObject) obj;
//			JsonArray array = (JsonArray)obj;
//
//			System.out.println("The 2nd element of array");
//			System.out.println(array.get(1));
//			System.out.println();
//
//			JsonObject obj2 = (JsonObject)array.get(1);
//			System.out.println("Field \"1\"");
//			System.out.println(obj2.get("1"));    
//
//			s = "{}";
//			obj = parser.parse(s);
//			System.out.println(obj);
//
//			s = "[5,]";
//			obj = parser.parse(s);
//			System.out.println(obj);
//
//			s = "[5,,2]";
//			obj = parser.parse(s);
//			System.out.println(obj);
//		}catch(ParseException pe){
//
//			System.out.println("position: " + pe.getPosition());
//			System.out.println(pe);
//		}
//

		
		//		try {
		//			JsonParser parser;
		//			JsonObject parse = parser.parse(request.getReader());
		//		} catch (Exception e) {
		//	
		//		}

		//		JsonObject returnValue;
		//		JsonValue jv = "success";
		//		returnValue.put("user", new JsonValue("sucess"));
		//		returnValue.put("user", "success");
		//		response.getWriter().print(returnValue);

		rd.include(request,response);  
	}

}
