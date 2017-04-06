package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the wx_user database table.
 * 
 */
@Entity
@Table(name="wx_user")
@NamedQuery(name="WxUser.findAll", query="SELECT w FROM WxUser w")
public class WxUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="user_name")
	private String userName;

	@Column(name="user_pass")
	private String userPass;

	//bi-directional many-to-one association to WxData
	@OneToMany(mappedBy="wxUser")
	private List<WxData> wxData;

	//bi-directional many-to-one association to WxHist
	@OneToMany(mappedBy="wxUser")
	private List<WxHist> wxHists;

	public WxUser() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return this.userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public List<WxData> getWxData() {
		return this.wxData;
	}

	public void setWxData(List<WxData> wxData) {
		this.wxData = wxData;
	}

	public WxData addWxData(WxData wxData) {
		getWxData().add(wxData);
		wxData.setWxUser(this);

		return wxData;
	}

	public WxData removeWxData(WxData wxData) {
		getWxData().remove(wxData);
		wxData.setWxUser(null);

		return wxData;
	}

	public List<WxHist> getWxHists() {
		return this.wxHists;
	}

	public void setWxHists(List<WxHist> wxHists) {
		this.wxHists = wxHists;
	}

	public WxHist addWxHist(WxHist wxHist) {
		getWxHists().add(wxHist);
		wxHist.setWxUser(this);

		return wxHist;
	}

	public WxHist removeWxHist(WxHist wxHist) {
		getWxHists().remove(wxHist);
		wxHist.setWxUser(null);

		return wxHist;
	}

}