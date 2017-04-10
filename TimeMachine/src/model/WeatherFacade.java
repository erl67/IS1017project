package model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class WeatherFacade {

	@PersistenceContext(unitName = "TimeMachine")
	EntityManager em;

	private Query q;

	public WeatherFacade(){
		super();
	}

	
}
