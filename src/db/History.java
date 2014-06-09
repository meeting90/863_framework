package db;

import java.sql.Timestamp;

/**
 * History entity. @author MyEclipse Persistence Tools
 */

public class History implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userId;
	private Long serviceId;
	private String param;
	private String description;
	private Timestamp accessTime;
	private Integer status;

	// Constructors

	/** default constructor */
	public History() {
	}

	/** full constructor */
	public History(Long userId, Long serviceId, String param,
			String description, Timestamp accessTime, Integer status) {
		this.userId = userId;
		this.serviceId = serviceId;
		this.param = param;
		this.description = description;
		this.accessTime = accessTime;
		this.status = status;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getParam() {
		return this.param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getAccessTime() {
		return this.accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}