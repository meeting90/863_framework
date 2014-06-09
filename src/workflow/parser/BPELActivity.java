 package workflow.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BPELActivity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6561359013335453133L;
	private String name;
	private String nodeType;
	private List<String> extra=new ArrayList<String>();

	List<String> header=new ArrayList<String>();
	List<BPELActivity> BPELActivity=new ArrayList<BPELActivity>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public List<String> getHeader() {
		return header;
	}
	public void setHeader(List<String> header) {
		this.header = header;
	}
	public List<BPELActivity> getBPELActivity() {
		return BPELActivity;
	}
	public void setBPELActivity(List<BPELActivity> bPELActivity) {
		BPELActivity = bPELActivity;
	}
	public List<String> getExtra() {
		return extra;
	}
	public void setExtra(List<String> extra) {
		this.extra = extra;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((BPELActivity == null) ? 0 : BPELActivity.hashCode());
		result = prime * result + ((extra == null) ? 0 : extra.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((nodeType == null) ? 0 : nodeType.hashCode());
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
		BPELActivity other = (BPELActivity) obj;
		if (BPELActivity == null) {
			if (other.BPELActivity != null)
				return false;
		} else if (!BPELActivity.equals(other.BPELActivity))
			return false;
		if (extra == null) {
			if (other.extra != null)
				return false;
		} else if (!extra.equals(other.extra))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nodeType == null) {
			if (other.nodeType != null)
				return false;
		} else if (!nodeType.equals(other.nodeType))
			return false;
		return true;
	}
	
	
	
	

}
