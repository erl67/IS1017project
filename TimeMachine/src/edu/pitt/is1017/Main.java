package src.edu.pitt.is1017;

public class Main {

	public static void main(String[] args) {

		String dsKey = "472f1ba38a5f3d13407fdb589d975c8c/";
		String dsUrl = "https://api.darksky.net/forecast/";
		String dsLoc = "37.8267,-122.4233,";
		String dsTime = "946684800";

		String testURL = dsUrl + dsKey + dsLoc + dsTime;

		System.out.println("TEST");
		WeatherServlet.URLConnectionReader(testURL);
		//WeatherServlet.URLConnectionReader("http://google.com");

	}

}
