package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the wx_hist database table.
 * 
 */
@Entity
@Table(name="wx_hist")
@NamedQuery(name="WxHist.findAll", query="SELECT w FROM WxHist w")
public class WxHist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String latitude;

	private String longitude;

	private String title;

	//bi-directional many-to-one association to WxUser
	@ManyToOne
	@JoinColumn(name="fk_user")
	private WxUser wxUser;

	public WxHist() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public WxUser getWxUser() {
		return this.wxUser;
	}

	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}

}