package workflow.api.data;

import java.io.Serializable;

public class RelationGraphEdge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2871969787939803198L;
	private long source;
	private long target;
	private float weight;
	
	public RelationGraphEdge() {
		super();
	}
	
	public RelationGraphEdge(long source, long target, float weight) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public long getSource() {
		return source;
	}
	public void setSource(long source) {
		this.source = source;
	}
	public long getTarget() {
		return target;
	}
	public void setTarget(long target) {
		this.target = target;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight){
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (source ^ (source >>> 32));
		result = prime * result + (int) (target ^ (target >>> 32));
		result = prime * result + Float.floatToIntBits(weight);
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
		RelationGraphEdge other = (RelationGraphEdge) obj;
		if (source != other.source)
			return false;
		if (target != other.target)
			return false;
		if (Float.floatToIntBits(weight) != Float.floatToIntBits(other.weight))
			return false;
		return true;
	}
	
	
	
	
	
}
