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

	@SuppressWarnings("unchecked")
	public List<WxHist> getHistory (int uid){

		List<WxHist> results = null;

		try {
			WxUser user = em.find(WxUser.class, uid);
			Query q = em.createQuery("SELECT u FROM WxHist u WHERE (u.wxUser=:id)");
			q.setParameter("id", user);
			System.out.println(q.toString());
			results = q.getResultList();
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

	public boolean addHistory (WxHist history, int uid) {
		
		try {
			em.persist(history);
			return true;
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
	}
	
	/**
	 * @param uid
	 * @return WxUser Object based on uid
	 * This method is similar to the code: WxUser user = em.find(WxUser.class, uid);
	 */
	public WxUser getUser (int uid) {
		
		try {
			//Query q = em.createQuery("SELECT u FROM WxUser u WHERE (u.id= :uid)");
			//q.setParameter("uid", uid);
			//WxUser userResult = (WxUser) q.getSingleResult();
			WxUser userResult = em.find(WxUser.class, uid);
			//System.out.println("hf.gU: " + userResult.getId() + " " + userResult.getUserName());
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
