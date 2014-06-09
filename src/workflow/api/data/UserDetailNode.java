package workflow.api.data;

import java.util.ArrayList;
import java.util.List;

import workflow.db.Webservices;

public class UserDetailNode {
	private long userId;
	private String username;
	private String email;
	private List<Webservices> focusedWebService=new ArrayList<Webservices>();
	private List<String> friends=new ArrayList<String>();
	private List<String> fans=new ArrayList<String>();
	public UserDetailNode() {
		super();
	}
	public UserDetailNode(long userId, String username, String email,
			List<Webservices> focusedWebService, List<String> friends,List<String> fans) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.focusedWebService = focusedWebService;
		this.friends = friends;
		this.fans=fans;
	}
	
	public List<String> getFans() {
		return fans;
	}
	public void setFans(List<String> fans) {
		this.fans = fans;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Webservices> getFocusedWebService() {
		return focusedWebService;
	}
	public void setFocusedWebService(List<Webservices> focusedWebService) {
		this.focusedWebService = focusedWebService;
	}
	public List<String> getFriends() {
		return friends;
	}
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fans == null) ? 0 : fans.hashCode());
		result = prime
				* result
				+ ((focusedWebService == null) ? 0 : focusedWebService
						.hashCode());
		result = prime * result + ((friends == null) ? 0 : friends.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDetailNode other = (UserDetailNode) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fans == null) {
			if (other.fans != null)
				return false;
		} else if (!fans.equals(other.fans))
			return false;
		if (focusedWebService == null) {
			if (other.focusedWebService != null)
				return false;
		} else if (!focusedWebService.equals(other.focusedWebService))
			return false;
		if (friends == null) {
			if (other.friends != null)
				return false;
		} else if (!friends.equals(other.friends))
			return false;
		if (userId != other.userId)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	} 
	
	

}
