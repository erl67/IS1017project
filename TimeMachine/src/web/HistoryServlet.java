package web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import model.HistoryFacade;
import model.UserManager;
import model.WxHist;
import model.WxUser;

/**
 * Servlet implementation class HistoryServlet
 */
/**
 * @author E
 *
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
	 * @param uid
	 * @return String of JSON history
	 */
	public String jsonHistory (int uid) {

		List<WxHist> historyList = GetHistoryBean(uid);		//List of history data from database based on logged-in user
		JsonArray ja = new JsonArray();		//Json array to store info from List

		//iterate through List adding each item to a new JsonObject, then store the object in Array at end
		for (WxHist i : historyList) {
			JsonObject jo = new JsonObject();
			jo.put("Id", i.getId());
			jo.put("Name", i.getWxUser().getUserName());
			jo.put("UID", i.getWxUser().getId());
			jo.put("Title", i.getTitle());
			jo.put("Date", i.getDate().toString());
			jo.put("Latitude", i.getLatitude());
			jo.put("Longitude", i.getLongitude());
			log("Json Object="+ jo.toJson());
			//log("\njo="+jo.toString());		
			ja.add(jo);
		}
		log("\nja=" + ja.toJson());		

		return ja.toJson();	//return JsonArray as Json, can also be returned using .toString() but that excludes the quotation marks
	}

	/**
	 * @param uid
	 * @return String (Manually created JSON)
	 * This method is a backup to created prior simple JSON method
	 */
	public String jsonHistoryManual (int uid) {
		List<WxHist> historyList = GetHistoryBean(uid);
		String jh = "[";
		for (WxHist i : historyList) {
			jh += "\n\t{";
			jh += "\n\t\"Id\": " + i.getId() +",";
			jh += "\n\t\"Name\": \""+ i.getWxUser().getUserName()+ "\",";
			jh += "\n\t\"UID\": " + i.getWxUser().getId() +",";
			jh += "\n\t\"Title\": \"" + i.getTitle() + "\",";
			jh += "\n\t\"Date\": \"" + i.getDate() +"\",";
			jh += "\n\t\"Latitude\": "+ i.getLatitude() + ",";
			jh += "\n\t\"Longitude\": " + i.getLongitude() + "";
			jh += "\n\t},";	
		}
		jh = jh.substring(0, jh.length() - 1); //remove last comma
		jh += "\n]";
		log(jh.toString());	
		return jh;
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");  
		response.addHeader("SERVLET_STATUS", "ok");

		int uid = UserManager.checkUidCookie (request.getCookies());

		String jh = jsonHistory (uid);
		response.getWriter().println(jh);

		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log(request.toString()); log(response.toString()); 
		response.setContentType("application/json");  

		int uid = UserManager.checkUidCookie (request.getCookies());

		JsonObject queryJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());
		WxHist history = new WxHist();

		String date = queryJson.getString("date"); log("Date=" + date);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		try {
			history.setDate(df.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		history.setTitle(queryJson.getString("title"));
		history.setLatitude(queryJson.getString("latitude"));
		history.setLongitude(queryJson.getString("longitude"));
		history.setWxUser(GetUserBean(uid));

		boolean checkAddition = hf.addHistory(history, uid);

		if (checkAddition == true) {
			response.setStatus(200);
			String jh = jsonHistory(uid);
			response.getWriter().println(jh);
		} else {
			response.setStatus(500);
			response.getWriter().println("{ \"error\": \"Query could not be saved to the database\" }");
		}
	}

}
