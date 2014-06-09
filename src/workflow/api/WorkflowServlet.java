package workflow.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.xml.sax.SAXException;

import workflow.db.Userwokflow;
import workflow.db.UserwokflowDAO;
import workflow.db.Workflows;
import workflow.db.WorkflowsDAO;
import workflow.parser.BPELLazyParser;

@SuppressWarnings("unchecked")
public class WorkflowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
 
    public WorkflowServlet() {
        super();
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		InputStream stream=WorkflowServlet.class.getResourceAsStream("usage.html");
//
//        final byte data[] = new byte[1024];
//        int count;
//        while ((count = stream.read(data, 0, 1024)) != -1) {
//            resp.getOutputStream().write(data, 0, count);
//        }
//        resp.flushBuffer();
    	
    	doPost(req,resp);
	}
    
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		String query=req.getParameter("query");
		Long uid=(Long)req.getSession().getAttribute("userid");
		
//		if(uid==null){
//			resp.getWriter().append(ServletConstants.SESSION_TIMEOUT_ERROR);
//			return;
//		}
		
	
		
		if(query.equals("getWorkflow")){
			resp.getWriter().append(getWorkflow(uid));
		}
		else if(query.equals("getWorkflowInfo")){
			long wfId=Long.parseLong(req.getParameter("wfid"));
			//resp.getWriter().append(getWorkflowInfo(wfId));

			InputStream stream=WorkflowServlet.class.getResourceAsStream("getWorkflowInfo.json");

	        final byte data[] = new byte[1024];
	        int count;
	        while ((count = stream.read(data, 0, 1024)) != -1) {
	            resp.getOutputStream().write(data, 0, count);
	        }
	        resp.flushBuffer();
	        
	        return;
		}
		
		else if(query.equals("uploadWorkflow")){
			
		}
		else{
			resp.getWriter().append(ServletConstants.UNKOWN_REQUEST_ERROR);
		}
		 resp.getWriter().flush();
		 resp.getWriter().close();
	}


	

	private String getWorkflowInfo(long wfId) {
		WorkflowsDAO wfDAO=new WorkflowsDAO();
		Workflows wf=wfDAO.findById(wfId);
		BPELLazyParser parser=new BPELLazyParser(getServletContext());
		return parser.lazyParse(wf);
	
			
		
	}

	private String getWorkflow(long uid) {
		UserwokflowDAO  uwfDAO=new UserwokflowDAO();
		List<Userwokflow> result=uwfDAO.findByUserId(uid);
		JSONArray jsonArray = new JSONArray(result);
		return jsonArray.toString();
	}








}
