package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import model.UserFacade;
import model.UserManager;
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
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log(request.toString()); log(response.toString());

		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();  

		JsonObject loginJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());
		String username = loginJson.getString("username");
		String password = loginJson.getString("password");

		WxUser u = LoginBean(username, password);

		if (u != null) {
			// Create a session object if it is already not  created.
			HttpSession session = request.getSession(true);
			Date createTime = new Date(session.getCreationTime());
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
			
//			String domain = request.getServerName() + ":" + request.getServerPort();
			String domain = request.getServerName();
			log("\n\nDomain=" + domain + "\n\n");
			response.addCookie(UserManager.makeCookie ("TimeMachine_cookie", u.getUserName(), domain));
			response.addCookie(UserManager.makeCookie ("TimeMachine_uid", String.valueOf(u.getId()), domain));

			out.print("{\"success\": true, \"message\": \"Login Success for user: " + u.getUserName() + "\"}");  

			//response.addHeader("LOGIN_STATUS", "SUCCESS");
			//response.sendRedirect("/TimeMachine/");
			request.setAttribute("user", u);
			request.setAttribute("session", session);
			response.setStatus(200);
		} else {
			out.print("{\"success\": false, \"message\": \"Username or password is incorrect!\"}");  
			response.setStatus(401);
			request.setAttribute("user", "");
			//response.getWriter().print("fail");
			//response.addHeader("LOGIN_STATUS", "FAILURE");
			//response.sendRedirect("/TimeMachine/login/");
		}
	}
}
