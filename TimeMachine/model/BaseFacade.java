package model;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class BaseFacade {

	@PersistenceContext(unitName="TimeMachine")
	private static EntityManager em;
	
	public BaseFacade(){
		super();
	}
	
	public static String checkLogin (String user, String pass){
		
		String q = "SELECT user_name FROM wx_user WHERE ((user_name=" + user + ") AND (user_pass="+pass+"))";
		System.out.println(q);
		Query q1 = null;
		
		try {
			q1 = em.createQuery(q);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//Query q2 = em.createNativeQuery(q);
		
		try {
			//String u = ((BaseEntity)<> q.getSingleResult())
			//String u = (BaseEntity)q.getSingleResult().toString();
			String u = (String) q1.getSingleResult();
			System.out.println(q1.toString() + " == " + u);
			return u;
		}
		catch (NoResultException e) {
			return null;
		}
		
	}

}
