package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UserFacade;
import model.UserManager;
import model.WxUser;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet({ "/RegisterServlet", "/r" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	UserFacade uf;

	public RegisterServlet() {
		super();
	}

	public WxUser RegisterBean (String u, String p){
		WxUser r = null;
		System.out.println("Register Bean, u="+u+" p="+p);
		r = uf.registerUser(u, p);
		return r;
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("\n\n\nRegisterServletTest\n\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("SERVLET_STATUS", "ok");
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log(request.toString()); log(response.toString());
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter(); 
		
//		response.setContentType("application/json"); 
//		JsonObject loginJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());
//		String username = loginJson.getString("username");
//		String password = loginJson.getString("password");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		WxUser u = RegisterBean(username, password);

		if (u != null) {

			out.print("Register Success for user: " + u);  
			
			response.addCookie(UserManager.makeCookie ("TimeMachine_cookie", u.getUserName()));
			response.addCookie(UserManager.makeCookie ("TimeMachine_uid", String.valueOf(u.getId())));

			response.getWriter().print("success");
			response.addHeader("LOGIN_STATUS", "SUCCESS");
			request.setAttribute("user", u);
			response.sendRedirect("/TimeMachine/");
			response.setStatus(200);

		} else {
			response.getWriter().append("Error Duplicate Entry").append(request.getContextPath());
			out.print("{\"success\": false, \"message\": \"Error. Username already taken. Find another.\"}");  
			response.setStatus(401);
			request.setAttribute("user", "");
			response.addHeader("LOGIN_STATUS", "FAILURE");
			response.sendRedirect("/TimeMachine/register/");
		}
	}
}
