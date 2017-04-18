package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;


@Stateless
public class UserFacade {

	@PersistenceContext(unitName = "TimeMachine")
	EntityManager em;

	static Query q;

	public UserFacade(){
		super();
	}

	/**
	 * Receives username and password from the Login form and queries the database for a match
	 * Returns the User Object on success or null if the info doesn't match the database
	 */
	public WxUser checkLogin (String user, String pass){

		WxUser userResult = null;

		try {
			Query q = em.createQuery("SELECT u FROM WxUser u WHERE ((u.userName= :name) AND (u.userPass= :pass))");
			q.setParameter("name", user);
			q.setParameter("pass", pass);
			System.out.println(q.toString());	//print Query to console to check for errors
			userResult = (WxUser) q.getSingleResult();
			return userResult;
		}
		catch (NoResultException d) {
			d.printStackTrace();
			return null;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (Exception f) {
			f.printStackTrace();
			return null;
		}
	}

	/**
	 * Receives username and password from registration form via servlet
	 * Creates a new User object with the passed strings and attempts to persist it to the database
	 * The database constraints control whether the information is acceptable, so if the username is not unique or the password is too short
	 * then we receive a PersistenceException. Any error returns a null user, which will show an error in the browser and tell the user to try again
	 */
	public WxUser registerUser (String user, String pass) {

		WxUser r = new WxUser ();
		r.setUserName(user);
		r.setUserPass(pass);

		try {
			em.persist(r);
			r = checkLogin(r.getUserName(), r.getUserPass()); //pulls the object so we update & know what the autoincrement UID is for the cookie
			return r;
		}
		catch (NoResultException d) {
			d.printStackTrace();
			return null;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		catch (PersistenceException g) {
			g.printStackTrace();
			return null;
		} catch (Exception f) {
			f.printStackTrace();
			return null;
		}
	}


	/**
	 * Alternate method to access database via JDBC connection. Not currently used as EntityManager works 
	 */
	@SuppressWarnings("static-method")
	public String checkLogin2 (String user, String pass) {

		Connection con = null;
		final String DB_URL = "jdbc:mysql://sis-teach-01.sis.pitt.edu:3306/erl67is1017";
		final String DB_DRIVER = "com.mysql.jdbc.Driver";
		final String DB_USERNAME = "erl67is1017";
		final String DB_PASSWORD = "erl67@pitt.edu";
		String u = null;

		try {
			Class.forName(DB_DRIVER).newInstance();
			con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement stmt = con.prepareStatement("SELECT user_name FROM erl67is1017.wx_user WHERE ((user_name=?) AND (user_pass=?));");
			stmt.setString(1, user);
			stmt.setString(2, pass);
			ResultSet rs = stmt.executeQuery();
			System.out.println(stmt.toString().substring(49));

			while (rs.next()) u = rs.getString(1);

			rs.close(); stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return u;
	}

}
