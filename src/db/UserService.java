package db;

/**
 * UserService entity. @author MyEclipse Persistence Tools
 */

public class UserService implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userId;
	private Long serviceId;

	// Constructors

	/** default constructor */
	public UserService() {
	}

	/** full constructor */
	public UserService(Long userId, Long serviceId) {
		this.userId = userId;
		this.serviceId = serviceId;
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

}