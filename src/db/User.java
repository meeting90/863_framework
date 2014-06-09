package db;

import java.sql.Timestamp;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Long id;
	private String username;
	private String password;
	private String email;
	private Timestamp createTime;
	private Timestamp lastLoginTime;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	/** full constructor */
	public User(String username, String password, String email,
			Timestamp createTime, Timestamp lastLoginTime) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.createTime = createTime;
		this.lastLoginTime = lastLoginTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

}