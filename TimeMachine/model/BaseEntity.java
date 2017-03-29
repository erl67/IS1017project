package model;

import java.io.Serializable;
import java.lang.Double;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: BaseEntity
 *
 */
@Entity

public class BaseEntity implements Serializable {

	
	private int id;
	private Double temp;
	private static final long serialVersionUID = 1L;

	public BaseEntity() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public Double getTemp() {
		return this.temp;
	}

	public void setTemp(Double temp) {
		this.temp = temp;
	}
   
}
