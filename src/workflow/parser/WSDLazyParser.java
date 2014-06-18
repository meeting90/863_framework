/**
 * 
 */
package workflow.parser;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import workflow.db.Webservicenodes;
import workflow.db.WebservicenodesDAO;
import workflow.db.Webservices;

import com.ibm.wsdl.factory.WSDLFactoryImpl;

/**
 * @author meeting
 *
 */
public class WSDLazyParser {

	
	private static final String ERROR_MSSAGE="error:cannot parse wsdl to nodes";
	private ServletContext servletContext;
	public WSDLazyParser(ServletContext servletContext) {
		this.servletContext=servletContext;
	}
	public WSDLazyParser(){
		
	}


	public String lazyParse(Webservices ws)   {
		WebservicenodesDAO wsnDAO=new WebservicenodesDAO();
		Webservicenodes nodes=wsnDAO.findById(ws.getWsId());
		if(nodes==null){
			nodes=new Webservicenodes();
			nodes.setWsId(ws.getWsId());
			try {		
				String realPath=servletContext.getRealPath("/workflow")+ws.getWsPath();
				System.out.println(realPath);
				String nodeJson=coreParse(realPath);
				
				nodes.setWsNodes(nodeJson.getBytes());
			} catch (Exception e) {	
				
				nodes.setWsNodes(ERROR_MSSAGE.getBytes());
			} 
			Session hiberSession= wsnDAO.getSession();
	    	Transaction tx=null;
	    	try{
	    		tx=hiberSession.beginTransaction();
	    		hiberSession.saveOrUpdate(nodes);
	    		tx.commit();
	    	}catch(Exception e){
	    		//e.printStackTrace();
	    	
	    	}finally{
	    		hiberSession.close();
	    	}
		}	
		return new String(nodes.getWsNodes());
	}
	

	public String coreParse(String wsdlPath) throws WSDLException, JSONException{
		WSDLReader	reader=new WSDLFactoryImpl().newWSDLReader();
		
		Definition definition=reader.readWSDL(wsdlPath);
		Set<String> wsdlBinding=new HashSet<String>();
		Set<String> wsdlPort=new HashSet<String>();
		Set<WSDLPortType> wsdlPortType=new HashSet<WSDLPortType>();
		Map<String,String> port2Bindings=new HashMap<String,String>();
		Map<String,String> binding2portType=new HashMap<String,String>();
		String serviceName=null;
		for(Object obj:definition.getServices().values()){
			javax.wsdl.Service service=(javax.wsdl.Service)obj;
			serviceName=service.getQName().getLocalPart();
			for(Object port:service.getPorts().values()){
				javax.wsdl.Port p=(javax.wsdl.Port)port;
				javax.wsdl.Binding binding=p.getBinding();
				javax.wsdl.PortType pt=binding.getPortType();
				WSDLPortType wpt=new WSDLPortType(pt);
				
				String portName=p.getName();
				String bindingName=binding.getQName().getLocalPart();
				
				wsdlPort.add(portName);
				wsdlBinding.add(bindingName);
				port2Bindings.put(portName, bindingName);
				wsdlPortType.add(wpt);
				binding2portType.put(bindingName, wpt.getPortTypeName());
				
			}
			break;
			
			
		}
		JSONObject defJSON=new JSONObject();
		defJSON.put("service", serviceName);
		defJSON.put("port", wsdlPort);
		defJSON.put("binding", wsdlBinding);
		defJSON.put("portType",wsdlPortType);
		defJSON.put("p2b", port2Bindings);
		defJSON.put("b2pt",binding2portType);
		return defJSON.toString();

	}
	public static void main(String []args) throws WSDLException, JSONException{
		WSDLazyParser parser=new WSDLazyParser();
		File file=new File("./wsdls");
		File[] fs=file.listFiles();
		for(File wsdlf: fs){
			System.out.println(parser.coreParse(wsdlf.getAbsolutePath()));
		}
		String result=parser.coreParse("./wsdls/achws.asmx.wsdl");
		JSONObject json=new JSONObject(result);
		JSONObject portType=(JSONObject) json.getJSONArray("portType").get(0);
		JSONArray array=portType.getJSONArray("operationName");
		for(int i=0;i<array.length();i++){
			System.out.println(array.get(i));
		}
	}
	
	
}
