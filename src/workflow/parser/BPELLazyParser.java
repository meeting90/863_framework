/**
 * 
 */
package workflow.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.ode.bpel.compiler.bom.Activity;
import org.apache.ode.bpel.compiler.bom.AssignActivity;
import org.apache.ode.bpel.compiler.bom.BpelObjectFactory;
import org.apache.ode.bpel.compiler.bom.EmptyActivity;
import org.apache.ode.bpel.compiler.bom.FlowActivity;
import org.apache.ode.bpel.compiler.bom.ForEachActivity;
import org.apache.ode.bpel.compiler.bom.IfActivity;
import org.apache.ode.bpel.compiler.bom.Import;
import org.apache.ode.bpel.compiler.bom.InvokeActivity;
import org.apache.ode.bpel.compiler.bom.OnAlarm;
import org.apache.ode.bpel.compiler.bom.OnMessage;
import org.apache.ode.bpel.compiler.bom.PickActivity;
import org.apache.ode.bpel.compiler.bom.Process;
import org.apache.ode.bpel.compiler.bom.ReceiveActivity;
import org.apache.ode.bpel.compiler.bom.RepeatUntilActivity;
import org.apache.ode.bpel.compiler.bom.ReplyActivity;
import org.apache.ode.bpel.compiler.bom.RethrowActivity;
import org.apache.ode.bpel.compiler.bom.ScopeActivity;
import org.apache.ode.bpel.compiler.bom.SequenceActivity;
import org.apache.ode.bpel.compiler.bom.SwitchActivity;
import org.apache.ode.bpel.compiler.bom.SwitchActivity.Case;
import org.apache.ode.bpel.compiler.bom.TerminateActivity;
import org.apache.ode.bpel.compiler.bom.ThrowActivity;
import org.apache.ode.bpel.compiler.bom.Variable;
import org.apache.ode.bpel.compiler.bom.WaitActivity;
import org.apache.ode.bpel.compiler.bom.WhileActivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import workflow.db.Workflownodes;
import workflow.db.WorkflownodesDAO;
import workflow.db.Workflows;

/**
 * @author meeting
 *
 */
public class BPELLazyParser {
	private static final String ERROR_MSSAGE="error:cannot parse bpel to nodes";
	private static final String NO_SOURCE="cannot find bpel source";
	
	private Process bpelProcess;
	private Map<String,String> var2Wsdl;
	private ServletContext servletContext;
	public BPELLazyParser(){
		
	}
	public BPELLazyParser(ServletContext servletContext) {
		this.servletContext=servletContext;
	}

	public  String lazyParse(Workflows wf){
		WorkflownodesDAO wfnDAO=new WorkflownodesDAO();
		Workflownodes nodes=wfnDAO.findById(wf.getWfId());
		if(nodes==null){
			nodes=new Workflownodes();
			nodes.setWfId(wf.getWfId());
			try {
				String nodeJSon = coreParse(servletContext.getRealPath("/")+wf.getWfPath());
				nodes.setWfNodes(nodeJSon.getBytes());
			} catch (Exception e1) {
				
				nodes.setWfNodes(ERROR_MSSAGE.getBytes());
			} 
			Session hiberSession= wfnDAO.getSession();
	    	Transaction tx=null;
	    	try{
	    		tx=hiberSession.beginTransaction();
	    		hiberSession.save(nodes);
	    		tx.commit();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	
	    	}finally{
	    		hiberSession.close();
	    	}
		}	
		return new String(nodes.getWfNodes());
		
	}

	public String coreParse(String wfPath) throws IOException, SAXException{
		BpelObjectFactory bof = new BpelObjectFactory();
		
		InputSource is = new InputSource(wfPath);
	    this.bpelProcess = bof.parse(is, null);
	    
	    Activity rootActivity=bpelProcess.getRootActivity();
	    
	    BPELActivity bact=parseActivity(rootActivity);
	    JSONObject json=new JSONObject(bact);
	    return json.toString();
		
	}
	private BPELActivity parseActivity(Activity activity){
		
		BPELActivity act=new BPELActivity();
		act.setName(activity.getName());
		if(activity instanceof SequenceActivity){
			act.setNodeType("sequence");
			SequenceActivity sequence=(SequenceActivity)activity;
			List<Activity> children=sequence.getActivities();
			for(Activity child : children){
			    addActivity(act, child);
			//	act.getBPELActivity().add(parseActivity(child));
			}
		}else if(activity instanceof FlowActivity){
			act.setNodeType("flow");
			FlowActivity flow=(FlowActivity)activity;
			List<Activity> children=flow.getActivities();
			for(Activity child: children){
				addActivity(act,child);
			//	act.getBPELActivity().add(parseActivity(child));
			}
		}else if(activity instanceof ForEachActivity){
			act.setNodeType("forEach");
			ForEachActivity foreach=(ForEachActivity)activity;
			List<Activity> children=foreach.getActivities();
			for(Activity child : children){
				addActivity(act,child);
				//act.getBPELActivity().add(parseActivity(child));
			}
		}else if(activity instanceof PickActivity){
			act.setNodeType("pick");
			PickActivity pick=(PickActivity)activity;
			List<OnMessage> onMsgs=pick.getOnMessages();
			for(OnMessage onMsg: onMsgs){
				act.getHeader().add(onMsg.getTextValue());
				act.getBPELActivity().add(parseActivity(onMsg.getActivity()));
			}
			List<OnAlarm> onAlarms=pick.getOnAlarms();
			for(OnAlarm onAlarm: onAlarms){
				act.getHeader().add(onAlarm.getTextValue());
				act.getBPELActivity().add(parseActivity(onAlarm.getActivity()));
			}
			
		}else if(activity instanceof SwitchActivity){
			act.setNodeType("switch");
			SwitchActivity _switch=(SwitchActivity)activity;
			List<Case> cases=_switch.getCases();
			for(Case _case: cases){
				act.getHeader().add(_case.getTextValue());
				act.getBPELActivity().add(parseActivity(_case.getActivity()));
			}
			
		}else if(activity instanceof IfActivity ){
			act.setNodeType("if");
			IfActivity _if=(IfActivity)activity;
			
			act.getHeader().add(_if.getCondition().getTextValue());
			act.getBPELActivity().add(parseActivity(_if.getActivity()));
			List<org.apache.ode.bpel.compiler.bom.IfActivity.Case> cases=_if.getCases();
			for(org.apache.ode.bpel.compiler.bom.IfActivity.Case _case :cases){
				if(_case.getCondition()==null)
					act.getHeader().add("otherwise");
				else
					act.getHeader().add(_case.getCondition().getTextValue());
				act.getBPELActivity().add(parseActivity(_case.getActivity()));
			}
		}else if(activity instanceof RepeatUntilActivity){
			act.setNodeType("repeatUtil");
			RepeatUntilActivity repeatUtil=(RepeatUntilActivity)activity;
			act.getBPELActivity().add(parseActivity(repeatUtil.getActivity()));
		}else if(activity instanceof WhileActivity){
			act.setNodeType("while");
			WhileActivity _while=(WhileActivity)activity;
			act.getBPELActivity().add(parseActivity(_while.getActivity()));
		}else if(activity instanceof ScopeActivity){
			act.setNodeType("Scope");
			ScopeActivity scope=(ScopeActivity)activity;
			act.getBPELActivity().add(parseActivity(scope.getChildActivity()));
		}else if(activity instanceof WaitActivity){  // atomic activity
			act.setNodeType("wait");
		}else if(activity instanceof ThrowActivity){
			act.setNodeType("throw");
		}else if(activity instanceof TerminateActivity){
			act.setNodeType("terminate");
		}else if(activity instanceof RethrowActivity){
			act.setNodeType("rethrow");
		}else if(activity instanceof EmptyActivity){
			act.setNodeType("empty");
		}else if(activity instanceof AssignActivity){
			act.setNodeType("assign");
		}else if(activity instanceof ReceiveActivity){
			act.setNodeType("receive");
			ReceiveActivity receive=(ReceiveActivity)activity;
			String var=receive.getVariable();
			
			
		
			String operation=receive.getOperation();
			act.getExtra().add(operation);
			try {
				String wsdlName=getWSDLFileByVar(var);
				act.getExtra().add(wsdlName);
			} catch (Exception e) {
				act.getExtra().add(NO_SOURCE);
			} 	
		}else if(activity instanceof ReplyActivity){
			act.setNodeType("reply");
			ReplyActivity reply=(ReplyActivity)activity;
			String var=reply.getVariable();
			String operation=reply.getOperation();
			act.getExtra().add(operation);
			try {
				String wsdlName=getWSDLFileByVar(var);
				
				act.getExtra().add(wsdlName);
			} catch (Exception e) {
				act.getExtra().add(NO_SOURCE);
			} 	
		}else if(activity instanceof InvokeActivity){
			act.setNodeType("invoke");
			InvokeActivity invoke=(InvokeActivity)activity;
			String var=invoke.getInputVar();
			if(var==null)
				var=invoke.getOutputVar();
		
			String operation=invoke.getOperation();
			act.getExtra().add(operation);
			try {
				String wsdlName=getWSDLFileByVar(var);
				
				act.getExtra().add(wsdlName);
			} catch (Exception e) {
				act.getExtra().add(NO_SOURCE);
			} 	
		}
		return act;
		
	}
	
	
	private String getWSDLFileByVar(String var){
		if(var==null) return null;
		if(var2Wsdl==null){
			var2Wsdl=new HashMap<String,String>();
			List<Variable> variables=bpelProcess.getVariables();
			for(Variable v: variables){
				String namespace=v.getTypeName().getNamespaceURI();
				
				List<Import> imports=bpelProcess.getImports();
				for(Import _import: imports){
					String wsdlName=_import.getLocation().toString();
					
					if(namespace.equals(_import.getNamespace())){
							var2Wsdl.put(v.getName(), wsdlName);
					}
				
				}
			}
		}
		if(var2Wsdl.containsKey(var))
			return var2Wsdl.get(var);
		return NO_SOURCE;
	}
	

	private void addActivity(BPELActivity ba, Activity activity){
		if(activity instanceof AssignActivity){
			return;
		}else if(activity instanceof EmptyActivity){
			return;
		}//ignore this two kind of activity
		ba.getBPELActivity().add(parseActivity(activity));
	}

	
	public static void main(String []args) throws IOException, SAXException{
		BPELLazyParser parser=new BPELLazyParser();
		System.out.println(parser.coreParse("./bpels/travel.bpel"));
	}
}
