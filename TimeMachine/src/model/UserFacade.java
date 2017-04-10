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
import javax.persistence.Query;


@Stateless
public class UserFacade {

	@PersistenceContext(unitName = "TimeMachine")
	EntityManager em;
	
	static Query q;

	public UserFacade(){
		super();
	}

	public WxUser checkLogin (String user, String pass){
		String u = null;
		WxUser userResult = null;

		try {
			Query q = em.createQuery("SELECT u FROM WxUser u WHERE ((u.userName= :name) AND (u.userPass= :pass))");
			q.setParameter("name", user);
			q.setParameter("pass", pass);
			System.out.println(q.toString());
			userResult = (WxUser) q.getSingleResult();
			u = userResult.getUserName();
			System.out.println(q.toString());
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
	
	public WxUser registerUser (String user, String pass) {
		
		WxUser r = new WxUser ();
		r.setUserName(user);
		r.setUserPass(pass);
		
		try {
//			Query q = em.createQuery("INSERT INTO WxUser(u.userName, u.userPass) VALUES (:name, :pass)");
//			q.setParameter("name", user);
//			q.setParameter("pass", pass);
//			System.out.println(q.toString());

//			WxUser userResult = em.find(WxUser.class, user);

			em.persist(r);
			r = checkLogin(r.getUserName(), r.getUserPass()); //pulls the object so we know what the autoincrement ID is
			return r;
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
		
		
//		return r;
	}

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

		//if (u==null) u="INVALID USER";
		return u;
	}

}
