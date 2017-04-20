package model;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class HistoryFacade {


	@PersistenceContext(unitName = "TimeMachine")
	EntityManager em;

	public HistoryFacade(){
		super();
	}

	/**
	 * Receives UID and pulls the history for that user from the database
	 * List of History Entities is returned to servlet for conversion to Json
	 */
	@SuppressWarnings("unchecked")
	public List<WxHist> getHistory (int uid){

		List<WxHist> results = null;

		try {

			//This is an alternate method of finding the user in the database based on UID, you could also use a SELECT query
			WxUser user = em.find(WxUser.class, uid);

			//Build query to select history based on the above user
			Query q = em.createQuery("SELECT u FROM WxHist u WHERE (u.wxUser=:user)");
			q.setParameter("user", user);

			//print query to console for troubleshooting purposes
			System.out.println(q.toString());

			//EntityManager can return a List of results. Since we are storing and return history as List<WxHist> we don't have to do anything special
			results = q.getResultList();

			//iterate through the resulting list and print to console for reasons
			for (WxHist i : results) System.out.println("hf.gH: "+i.getId() + " " + i.getWxUser() + " " + i.getTitle() + " " + i.getDate() + " " + i.getLatitude() + " " + i.getLongitude());			

			return results;
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
	 * @param history : WxHist object created in HistoryServlet based on Json extraction
	 * @param uid : UID received from the cookie
	 * @return True or False
	 * This method persists the WxHist to the database
	 */
	public boolean addHistory (WxHist history, int uid) {

		try {
			//tried to prevent clicking on history item from being added
			//if (em.find(WxHist.class, history) == null) {
				em.persist(history);
				return true;
			//}
		}
		catch (NoResultException d) {
			d.printStackTrace();
			return false;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (Exception f) {
			f.printStackTrace();
			return false;
		}
		//return false;
	}

	/**
	 * @param uid
	 * @return WxUser Object based on uid derived from cookies
	 * This method is only called from the GetUserBean where it is need to set the WxUser object in a new history object for future peristance
	 */
	public WxUser getUser (int uid) {

		try {
			WxUser userResult = em.find(WxUser.class, uid);

			//the below 3 lines do the same thing as above
			//Query q = em.createQuery("SELECT u FROM WxUser u WHERE (u.id= :uid)");
			//q.setParameter("uid", uid);
			//WxUser userResult = (WxUser) q.getSingleResult();

			System.out.println("hf.gU: " + userResult.getId() + " " + userResult.getUserName());
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


}
