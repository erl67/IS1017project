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
import javax.servlet.http.HttpSession;

import model.BaseFacade;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet({ "/LoginServlet", "/l" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	BaseFacade bf;

	public LoginServlet() {
		super();
	}

//	public static String LoginBean (String u, String p){
//		System.out.println("Login Bean, u="+u+" p="+p);
//		return bf.checkLogin(u, p);
//	}

	public static String LoginBean2 (String u, String p){
		System.out.println("Login Bean2:  u="+u+" p="+p);
		return BaseFacade.checkLogin2(u, p);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("\n\n\nLoginServletTest\n\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("LOGIN_STATUS", "no");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		log(request.toString()); log(response.toString());
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String u = LoginBean2(username, password);
		
	    response.setContentType("text/html");  
	    PrintWriter out = response.getWriter();  

		if (u != null) {
			// Create a session object if it is already not  created.
			HttpSession session = request.getSession(true);
			// Get session creation time.
			Date createTime = new Date(session.getCreationTime());
			// Get last access time of this web page.
			Date lastAccessTime = new Date(session.getLastAccessedTime());

			String title = "Welcome Back to my website";
			Integer visitCount = new Integer(0);
			String visitCountKey = new String("visitCount");
			String userIDKey = new String(u);
			String userID = new String(u);

			// Check if this is new comer on your web page.
			if (session.isNew()){
				title = "Welcome to my website";
				session.setAttribute(userIDKey, userID);
				log("NewSession//UIDKey:"+userIDKey+" UID:"+userID+" VisitCount:"+visitCount+" createTime:"+ createTime+ " lastAccess:"+lastAccessTime+" Session:"+session.toString());
			} else {
				visitCount = (Integer)session.getAttribute(visitCountKey);
				visitCount = visitCount + 1;
				userID = (String)session.getAttribute(userIDKey);
				log("OldSession//UIDKey:"+userIDKey+" UID:"+userID+" VisitCount:"+visitCount+" createTime:"+ createTime+ " lastAccess:"+lastAccessTime+" Session:"+session.toString());
			}
			session.setAttribute(visitCountKey,  visitCount);
			
		    final String cookieName = "TimeMachine_cookie";
		    final String cookieValue = u;  // you could assign it some encoded value
		    final Boolean useSecureCookie = false;
		    final int expiryTime = 60 * 60 * 1;  // 1h in seconds
		    final String cookiePath = "/";
		    Cookie tmc = new Cookie(cookieName, cookieValue);
		    tmc.setSecure(useSecureCookie);  // determines whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL
		    tmc.setMaxAge(expiryTime);  // A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
		    tmc.setPath(cookiePath);  // The cookie is visible to all the pages in the directory you specify, and all the pages in that directory's subdirectories
		    response.addCookie(tmc);
		    
	        out.print("Login Success for user: " + u);  
	        RequestDispatcher rd=request.getRequestDispatcher("index.html");  
	        
			response.getWriter().print("success");
			response.addHeader("LOGIN_STATUS", "SUCCESS");
			request.setAttribute("user", u);
			response.sendRedirect("/TimeMachine/");
			
	        rd.include(request,response);  

			// Set response content type
			//		      response.setContentType("text/html");
			//		      PrintWriter out = response.getWriter();

			//		      String docType =
			//		      "<!doctype html public \"-//w3c//dtd html 4.0 " +
			//		      "transitional//en\">\n";
			//		      out.println(docType +
			//		                "<html>\n" +
			//		                "<head><title>" + title + "</title></head>\n" +
			//		                "<body bgcolor=\"#f0f0f0\">\n" +
			//		                "<h1 align=\"center\">" + title + "</h1>\n" +
			//		                 "<h2 align=\"center\">Session Infomation</h2>\n" +
			//		                "<table border=\"1\" align=\"center\">\n" +
			//		                "<tr bgcolor=\"#949494\">\n" +
			//		                "  <th>Session info</th><th>value</th></tr>\n" +
			//		                "<tr>\n" +
			//		                "  <td>id</td>\n" +
			//		                "  <td>" + session.getId() + "</td></tr>\n" +
			//		                "<tr>\n" +
			//		                "  <td>Creation Time</td>\n" +
			//		                "  <td>" + createTime + 
			//		                "  </td></tr>\n" +
			//		                "<tr>\n" +
			//		                "  <td>Time of Last Access</td>\n" +
			//		                "  <td>" + lastAccessTime + 
			//		                "  </td></tr>\n" +
			//		                "<tr>\n" +
			//		                "  <td>User ID</td>\n" +
			//		                "  <td>" + userID + 
			//		                "  </td></tr>\n" +
			//		                "<tr>\n" +
			//		                "  <td>Number of visits</td>\n" +
			//		                "  <td>" + visitCount + "</td></tr>\n" +
			//		                "</table>\n" +
			//		                "</body></html>");

		} else {
	        out.print("Sorry username or password error");  
	        RequestDispatcher rd=request.getRequestDispatcher("index.html");  
	        
			response.getWriter().print("fail");
			response.addHeader("LOGIN_STATUS", "FAILURE");
			request.setAttribute("user", "");
			response.sendRedirect("/TimeMachine/login/");
			
			rd.include(request,response);  
		}

	}

}
