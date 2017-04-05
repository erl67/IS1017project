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
public class BaseFacade {

	@PersistenceContext(unitName = "TimeMachine")
	static
	EntityManager em;
	//protected EntityManager em = EntityManagerFactory.createEntityManager();
	
	static Query q;

	public BaseFacade(){
		super();
	}

	public static String checkLogin (String user, String pass){

		String  q1 = "SELECT user_name FROM erl67is1017.wx_user WHERE ((user_name=" + user + ") AND (user_pass="+pass+"));";
		System.out.println("EM create:: "+q1);

		String u = null;

		try {
			q = em.createQuery(q1);
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}

		try {
			//String u = ((BaseEntity)<> q.getSingleResult())
			//String u = (BaseEntity)q.getSingleResult().toString();
			u = (String) q.getSingleResult();
			System.out.println(q1.toString() + " == " + u);
			return u;
		}
		catch (NoResultException e) {
			return null;
		}
	}

	public static String checkLogin2 (String user, String pass) {

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
			//Query q1 = em.createQuery(stmt.toString().substring(49));

			while (rs.next()) u = rs.getString(1);
			System.out.println(u);

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
