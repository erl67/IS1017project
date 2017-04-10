package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.HistoryFacade;
import model.WxHist;
import model.WxUser;

/**
 * Servlet implementation class HistoryServlet
 */
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
		// TODO Auto-generated constructor stub
	}

	public WxUser GetUserBean (int uid){
		System.out.println("GetUser Bean, u="+uid);
		return hf.getUser(uid);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("\n\n\nHistoryServletTest\n\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("SERVLET_STATUS", "something");
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		log(request.toString()); log(response.toString());
		PrintWriter out = response.getWriter();  
		RequestDispatcher rd=request.getRequestDispatcher("index.html");  
		response.setContentType("text/html");  

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
		} else {
			Cookie tmc = new Cookie(cookieName, "History Error");
			tmc.setSecure(useSecureCookie);
			tmc.setMaxAge(expiryTime);
			tmc.setPath(cookiePath);
			response.addCookie(tmc);
			response.setStatus(418);
		}



		//		JsonObject object = Json.createObjectBuilder().build();
		//		JsonObject jo = new JsonObject();
		//		JsonObject jo;
		//		JsonValue jv;
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



		//		if (u != null) {
		//			RequestDispatcher rd=request.getRequestDispatcher("index.html");  
		//			rd.include(request,response);  
		//		} else {
		//			out.print("Sorry username or password error");  
		//			RequestDispatcher rd=request.getRequestDispatcher("index.html");  	
		//			rd.include(request,response);  
		//		}

		rd.include(request,response);  
	}

}
