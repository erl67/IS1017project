package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import model.UserFacade;
import model.WxUser;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet({ "/LoginServlet", "/l" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	UserFacade uf;

	public LoginServlet() {
		super();
	}

	public WxUser LoginBean (String u, String p){
		System.out.println("Login Bean, u="+u+" p="+p);
		return uf.checkLogin(u, p);
	}

	public String LoginBean2 (String u, String p){
		System.out.println("Login Bean2:  u="+u+" p="+p);
		return uf.checkLogin2(u, p);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("\n\n\nLoginServletTest\n\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("SERVLET_STATUS", "ok");
//		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log(request.toString()); log(response.toString());
		
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();  
		//RequestDispatcher rd=request.getRequestDispatcher("index.html");  
		
		final Boolean useSecureCookie = false;
		final int expiryTime = 60 * 60 * 8;  // 1h in seconds
		final String cookiePath = "/";
		
		JsonObject loginJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());
		
		String username = loginJson.getString("username");
		String password = loginJson.getString("password");

		//String username = request.getParameter("username");
		//String password = request.getParameter("password");

		WxUser u = LoginBean(username, password);

		if (u != null) {
			// Create a session object if it is already not  created.
			HttpSession session = request.getSession(true);
			// Get session creation time.
			Date createTime = new Date(session.getCreationTime());
			// Get last access time of this web page.
			Date lastAccessTime = new Date(session.getLastAccessedTime());

			Integer visitCount = new Integer(0);
			String visitCountKey = new String("visitCount");
			String userIDKey = new String(u.getUserName()); 
			int userID = u.getId();

			// Check if this is new comer on your web page.
			if (session.isNew()){
				session.setAttribute(userIDKey, userID);
				log("NewSession//UIDKey:"+userIDKey+" UID:"+userID+" VisitCount:"+visitCount+" createTime:"+ createTime+ " lastAccess:"+lastAccessTime+" Session:"+session.toString());
			} else {
				visitCount = (Integer)session.getAttribute(visitCountKey);
				visitCount = visitCount + 1;
				userID = (int) session.getAttribute(userIDKey);
				log("OldSession//UIDKey:"+userIDKey+" UID:"+userID+" VisitCount:"+visitCount+" createTime:"+ createTime+ " lastAccess:"+lastAccessTime+" Session:"+session.toString());
			}
			session.setAttribute(visitCountKey,  visitCount);

			Cookie tmc = new Cookie("TimeMachine_cookie", u.getUserName());
			Cookie uid = new Cookie("TimeMachine_uid", String.valueOf(u.getId()));
			tmc.setSecure(useSecureCookie);  // determines whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL
			tmc.setMaxAge(expiryTime);  // A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
			tmc.setPath(cookiePath);  // The cookie is visible to all the pages in the directory you specify, and all the pages in that directory's subdirectories
			uid.setSecure(useSecureCookie); uid.setMaxAge(expiryTime); uid.setPath(cookiePath);
			response.addCookie(tmc);
			response.addCookie(uid);

			out.print("{\"success\": true, \"message\": \"Login Success for user: " + u.getUserName() + "\"}");  

			//response.getWriter().print("success");
			//response.addHeader("LOGIN_STATUS", "SUCCESS");
			request.setAttribute("user", u);
			//response.sendRedirect("/TimeMachine/");
			request.setAttribute("session", session);
			response.setStatus(200);
		} else {
//			Cookie tmc = new Cookie("TimeMachine_cookie", "Failed Login");
//			Cookie uid = new Cookie("TimeMachine_uid", "0");
//			tmc.setSecure(useSecureCookie);
//			tmc.setMaxAge(expiryTime);	//set to 0 to delete cookie
//			tmc.setPath(cookiePath);
//			response.addCookie(tmc);
//			uid.setSecure(useSecureCookie); uid.setMaxAge(expiryTime); uid.setPath(cookiePath);
//			response.addCookie(tmc); response.addCookie(uid);

			out.print("{\"success\": false, \"message\": \"Username or password is incorrect!\"}");  

			//response.getWriter().print("fail");
			//response.addHeader("LOGIN_STATUS", "FAILURE");
			response.setStatus(401);
			request.setAttribute("user", "");
			//response.sendRedirect("/TimeMachine/login/");

		}
		//rd.include(request,response);  

	}

}
