package model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Facade for WxData table, which has been de-implemented. Facade on standby for future missions.
 */
@Stateless
public class WeatherFacade {

	@PersistenceContext(unitName = "TimeMachine")
	EntityManager em;

	//private Query q;

	public WeatherFacade(){
		super();
	}

	
}
