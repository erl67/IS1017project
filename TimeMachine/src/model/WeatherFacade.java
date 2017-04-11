package model;

import javax.ejb.Stateless;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class WeatherFacade {

	@PersistenceContext(unitName = "TimeMachine")
	EntityManager em;

	private Query q;

	public WeatherFacade(){
		super();
	}

	
}
