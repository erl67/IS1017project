package src.edu.pitt.is1017;

import java.sql.Timestamp;
import java.util.Vector;

/**
 * Class ListItem / defines ListItems
 * @author ERL67
 * created: 19JAN17
 */

public class WeatherItem {

	private String description;
	private int id;
	private  Timestamp timestamp;
	


	/**
	 * @param description
	 * @param id
	 * @param timestamp
	 * This constructor can cause conflicts with the database, because we need to wait for the DB to assign the ID based on the DBengine
	 * Only used for tasks already in the DB, otherwise use the one below, and setID() based on what row the DB assigns
	 */
	public WeatherItem(String description, int id, Timestamp timestamp) {
		this.description = description;
		this.id = id;
		this.timestamp = timestamp;
	}
	

	/**
	 * @param description
	 * @param timestamp
	 */
	public WeatherItem(String description, Timestamp timestamp) {
		this.description = description;
		this.timestamp = timestamp;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return this.description;
	}
	
	public WeatherItem getContent() {
		return this;
	}

}
