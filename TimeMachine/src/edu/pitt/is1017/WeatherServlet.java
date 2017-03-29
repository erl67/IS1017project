package src.edu.pitt.is1017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WeatherServlet
 */
@WebServlet(description = "Process weather data from DarkSky API", urlPatterns = { "/WeatherServlet" })
public class WeatherServlet extends HttpServlet {

	private String dsKey = "472f1ba38a5f3d13407fdb589d975c8c/";
	private String dsUrl = "https://api.darksky.net/forecast/";
	private String dsLoc = "37.8267,-122.4233,";
	private String dsTime = "946684800";

	private String testURL = dsUrl + dsKey + dsLoc + dsTime;

	private static final long serialVersionUID = 1L;
	

	/**
	 * Default constructor. 
	 */
	public WeatherServlet() {
		// TODO Auto-generated constructor stub
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
				//inputLine = in.readLine();
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

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.getWriter().println("\n\n\n" + testURL);
		//response.getWriter().append("\n"  + testURL);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}