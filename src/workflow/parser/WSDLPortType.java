package workflow.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.wsdl.PortType;

public class WSDLPortType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7964045850684630393L;
	public WSDLPortType(PortType pt) {
		this.portTypeName=pt.getQName().getLocalPart();
		operationName=new ArrayList<String>();
		for(Object obj: pt.getOperations()){
			javax.wsdl.Operation op=(javax.wsdl.Operation)obj;
			operationName.add(op.getName());
		}
	}
	private String portTypeName;
	private List<String> operationName;
	public String getPortTypeName() {
		return portTypeName;
	}
	public void setPortTypeName(String portTypeName) {
		this.portTypeName = portTypeName;
	}
	public List<String> getOperationName() {
		return operationName;
	}
	public void setOperationName(List<String> operationName) {
		this.operationName = operationName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result
				+ ((portTypeName == null) ? 0 : portTypeName.hashCode());
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
		WSDLPortType other = (WSDLPortType) obj;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		if (portTypeName == null) {
			if (other.portTypeName != null)
				return false;
		} else if (!portTypeName.equals(other.portTypeName))
			return false;
		return true;
	}
	
	
	

}
