package model;

import javax.servlet.http.Cookie;

/**
 * This class is to consolidate the cookie functionality as it is used in several servlets
 * Other user functionality may also be added here 
 */
public class UserManager {

	final static Boolean useSecureCookie = false;		// determines whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL
	final static int expiryTime = 60 * 60 * 8;			// A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
	final static String cookiePath = "/";				 // The cookie is visible to all the pages in the directory you specify, and all the pages in that directory's subdirectories

	/**
	 * Receive a Cookie Array from the Servlet, and iterate through the cookies until finding our UID cookie
	 * Return the UID integer to the Servlet, which shows us the currently logged-in user
	 */
	public static int checkUidCookie (Cookie[] cookies){
		int uid = -1;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("TimeMachine_uid"))	uid = Integer.valueOf(cookie.getValue());
			}
		}
		return uid;
	}


	/**
	 * @param name : Name for the cookie being created as String
	 * @param value : Value of the cookie as String
	 * @param domain : Domain of the server being used. By default it is the current host so it doesn't really matter except for testing. Would be more useful with a static host.
	 * @return	The new Cookie Object is returned to the Servlet to be written to the browser
	 */
	public static Cookie makeCookie (String name, String value){
		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(useSecureCookie);
		cookie.setMaxAge(expiryTime);  
		cookie.setPath(cookiePath); 
		return cookie;
	}
}
