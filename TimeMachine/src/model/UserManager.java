package model;

import javax.servlet.http.Cookie;

public class UserManager {
	
	final static Boolean useSecureCookie = false;
	final static int expiryTime = 60 * 60 * 8;  // 1h in seconds
	final static String cookiePath = "/";
	
	
	public static int checkUidCookie (Cookie[] cookies){
		int uid = -1;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("TimeMachine_uid"))	uid = Integer.valueOf(cookie.getValue());
			}
		}
		return uid;
	}
	
	public static Cookie makeCookie (String name, String value){
		
		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(useSecureCookie);  // determines whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL
		cookie.setMaxAge(expiryTime);  // A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
		cookie.setPath(cookiePath);  // The cookie is visible to all the pages in the directory you specify, and all the pages in that directory's subdirectories
		
		return cookie;
	}

}
