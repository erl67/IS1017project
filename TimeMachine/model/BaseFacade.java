package model;
 
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class BaseFacade {
	
	@PersistenceContext(unitName = "TimeMachine")
	static EntityManager em;
	//protected EntityManager em = EntityManagerFactory.createEntityManager();
	
	public BaseFacade(){
		super();
	}
	
	public String checkLogin (String user, String pass){
		
		String q = "SELECT user_name FROM wx_user WHERE ((user_name=" + user + ") AND (user_pass="+pass+"))";
		System.out.println(q);
		
		Query q1 = null;
		String u = null;
		
		try {
			q1 = em.createQuery(q);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//String u = ((BaseEntity)<> q.getSingleResult())
			//String u = (BaseEntity)q.getSingleResult().toString();
			//String u = (String) q1.getSingleResult();
			System.out.println(q1.toString() + " == " + u);
			return u;
		}
		catch (NoResultException e) {
			return null;
		}
		
	}

}
