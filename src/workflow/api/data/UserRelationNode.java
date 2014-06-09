package workflow.api.data;

import java.io.Serializable;

public class UserRelationNode implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7326153572767433815L;
	private long trustee;
	private float rate;
	private float fullrate;
	
	
	public UserRelationNode() {
		super();
	}
	public UserRelationNode(long trustee, float rate, float fullrate) {
		super();
		this.trustee = trustee;
		this.rate = rate;
		this.fullrate = fullrate;
	}
	public float getTrustee() {
		return trustee;
	}
	public void setTrustee(long trustee) {
		this.trustee = trustee;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getFullrate() {
		return fullrate;
	}
	public void setFullrate(float fullrate) {
		this.fullrate = fullrate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(fullrate);
		result = prime * result + Float.floatToIntBits(rate);
		result = prime * result + (int) (trustee ^ (trustee >>> 32));
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
		UserRelationNode other = (UserRelationNode) obj;
		if (Float.floatToIntBits(fullrate) != Float
				.floatToIntBits(other.fullrate))
			return false;
		if (Float.floatToIntBits(rate) != Float.floatToIntBits(other.rate))
			return false;
		if (trustee != other.trustee)
			return false;
		return true;
	}
	
	
}
