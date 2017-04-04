package model;

import java.io.Serializable;
import javax.persistence.*;


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

}