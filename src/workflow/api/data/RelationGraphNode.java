package workflow.api.data;

import java.io.Serializable;

public class RelationGraphNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3323033942467180399L;
	private long id;
	private long name;

	private boolean isUser;
	
	public RelationGraphNode() {
		
	}
	
	public RelationGraphNode(long name, long id, boolean isUser) {
		this.name = name;
		this.id = id;
		this.isUser = isUser;
	}

	public long getName() {
		return name;
	}
	public void setName(long name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isUser() {
		return isUser;
	}
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isUser ? 1231 : 1237);
		result = prime * result + (int) (name ^ (name >>> 32));
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
		RelationGraphNode other = (RelationGraphNode) obj;
		if (id != other.id)
			return false;
		if (isUser != other.isUser)
			return false;
		if (name != other.name)
			return false;
		return true;
	}
	
	

}
