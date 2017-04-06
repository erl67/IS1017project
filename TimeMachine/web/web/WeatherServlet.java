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

import model.BaseFacade;

/**
 * Servlet implementation class WeatherServlet
 */
@WebServlet({ "/WeatherServlet", "/w" })
public class WeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	BaseFacade bf;
	
    public WeatherServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		

		doGet(request, response);
		log(request.toString()); log(response.toString());

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String u = null;// LoginBean2(username, password);

		final String cookieName = "TimeMachine_cookie";
		final String cookieValue = u;  // you could assign it some encoded value
		final Boolean useSecureCookie = false;
		final int expiryTime = 60 * 60 * 1;  // 1h in seconds
		final String cookiePath = "/";

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

//
//			Cookie tmc = new Cookie(cookieName, cookieValue);
//			tmc.setSecure(useSecureCookie);  // determines whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL
//			tmc.setMaxAge(expiryTime);  // A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
//			tmc.setPath(cookiePath);  // The cookie is visible to all the pages in the directory you specify, and all the pages in that directory's subdirectories
//			response.addCookie(tmc);

			out.print("Login Success for user: " + u);  
			RequestDispatcher rd=request.getRequestDispatcher("index.html");  

			response.getWriter().print("success");
			response.addHeader("LOGIN_STATUS", "SUCCESS");
			request.setAttribute("user", u);
			response.sendRedirect("/TimeMachine/");

			rd.include(request,response);  

		
		} else {

//			Cookie tmc = new Cookie(cookieName, "Failed Login");
//			tmc.setSecure(useSecureCookie);
//			tmc.setMaxAge(expiryTime);	//set to 0 to delete cookie
//			tmc.setPath(cookiePath);
//			response.addCookie(tmc);

			out.print("Sorry username or password error");  
			RequestDispatcher rd=request.getRequestDispatcher("index.html");  

			response.getWriter().print("fail");
			response.addHeader("LOGIN_STATUS", "FAILURE");
			request.setAttribute("user", "");
			response.sendRedirect("/TimeMachine/login/");

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
