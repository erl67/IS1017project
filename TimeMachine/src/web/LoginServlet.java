package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

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

	/**
	 * Login user via EntityManager based on username and password
	 */
	public WxUser LoginBean (String u, String p){
		System.out.println("Login Bean, u="+u+" p="+p);
		return uf.checkLogin(u, p);
	}

	/**
	 * Login user via JDBC connection. Not currently used but available as a backup.
	 */
	public String LoginBean2 (String u, String p){
		System.out.println("Login Bean2:  u="+u+" p="+p);
		return uf.checkLogin2(u, p);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("/n/n/nLoginServletTest/n/n/n");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("SERVLET_STATUS", "ok");
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log(request.toString()); log(response.toString());

		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();  

		//read username and password sent via Json
		JsonObject loginJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());
		String username = loginJson.getString("username");
		String password = loginJson.getString("password");

		WxUser u = LoginBean(username, password);	//call method to login user
		
		//if login returns a valid user entity then session is created and cookies are set, otherwise returns an error message
		if (u != null) {
			// Create a session object, then iterate through it 
			HttpSession session = request.getSession(true);
			session.setAttribute("Username", u.getUserName());
			session.setAttribute("uid", u.getId());
			Enumeration<String> s = session.getAttributeNames();
			while (s.hasMoreElements()){
				String a = s.nextElement().toString();
				log("\ta:" + a + " v:" + session.getAttribute(a));
			}

			//call method to create Cookies for the user entity returned
			response.addCookie(UserManager.makeCookie ("TimeMachine_cookie", u.getUserName()));
			response.addCookie(UserManager.makeCookie ("TimeMachine_uid", String.valueOf(u.getId())));

			out.print("{\"success\": true, \"message\": \"Login Success for user: " + u.getUserName() + "\"}");  

			//response.addHeader("LOGIN_STATUS", "SUCCESS");	//the commented response attributes were used prior to using AJAX when it was just a html form submission
			//response.sendRedirect("/TimeMachine/");
			request.setAttribute("user", u);
			request.setAttribute("session", session);	//adds JSESSION cookie
			response.setStatus(200);
		} else {
			out.print("{\"success\": false, \"message\": \"Username or password is incorrect!\"}");  
			response.setStatus(401);
			request.setAttribute("user", "");
			//response.addHeader("LOGIN_STATUS", "FAILURE");	//these were used prior to JS login
			//response.sendRedirect("/TimeMachine/login/");
		}
		out.close();
	}
}
