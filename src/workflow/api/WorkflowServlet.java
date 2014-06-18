package workflow.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import workflow.db.Focusws;
import workflow.db.Userwokflow;
import workflow.db.UserwokflowDAO;
import workflow.db.Webservices;
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
//		resp.setHeader("Access-Control-Allow-Origin", "*");
		String query=req.getParameter("query");
		Long loginId=(Long)req.getSession().getAttribute("userid");
		
		if(loginId==null){
			resp.getWriter().append(ServletConstants.SESSION_TIMEOUT_ERROR);
			return;
		}
		
	
		
		if(query.equals("getFoucsedWf")){
			long userId=Long.parseLong(req.getParameter("uid"));
			resp.getWriter().append(getFoucsedWf(userId));
		}
		else if(query.equals("getWorkflow")){
			long userId=Long.parseLong(req.getParameter("wfid"));
			resp.getWriter().append(getWorkflow(userId));
		}
		else if(query.equals("getWorkflowInfo")){
			long wfId=Long.parseLong(req.getParameter("wfid"));
			resp.getWriter().append(getWorkflowInfo(wfId));

		
		}
		
		else if(query.equals("uploadWorkflow")){
			
			String temppath = getServletContext().getRealPath("\\workflow")+"\\bpels\\temp\\";
			String path = getServletContext().getRealPath("\\workflow")+"\\bpels\\";
	        DiskFileItemFactory factory = new DiskFileItemFactory();  
	        factory.setRepository(new File(temppath));
	        factory.setSizeThreshold(1024 * 1024);
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        
	        try {
	            List<FileItem> list = upload.parseRequest(req);
	      
	            
	            FileItem item = getUploadFileItem(list);
	            String filename = getUploadFileName(item);
	            
	        
	            File f = new File(path,filename);
	            int p = filename.indexOf(".");
	            String firstname = filename.substring(0, p);
	            String lastname = filename.substring(p);
	            int fileIndex = 1;
	            while(f.exists()){
	            	f = new File(path,firstname+"_"+fileIndex+lastname);
	            	fileIndex++;
	            }
	            item.write(f);
	            
	              
	            resp.getWriter().append(ServletConstants.SUCCESS_MSG);
	          
	        } catch (FileUploadException e) {  
	        	  resp.getWriter().append(ServletConstants.ERROR_MSG);
	        } catch (Exception e) {  
	        	 resp.getWriter().append(ServletConstants.ERROR_MSG);
	        }
		}
		else{
			resp.getWriter().append(ServletConstants.UNKOWN_REQUEST_ERROR);
		}
		 resp.getWriter().flush();
		 resp.getWriter().close();
	}
	
	private FileItem getUploadFileItem(List<FileItem> list) {
		for (FileItem fileItem : list) {
			if(!fileItem.isFormField()) {
				return fileItem;
			}
		}
		return null;
	}
	private String getUploadFileName(FileItem item) {
		String value = item.getName();
		int start = value.lastIndexOf("/");
		String filename = value.substring(start + 1);
		int point = filename.lastIndexOf(".");
		filename = filename.substring(0, point)+".xml";
		return filename;
	}

	

	private String getWorkflow(long wfId) {
		WorkflowsDAO wfDAO=new WorkflowsDAO();
		Workflows wf=wfDAO.findById(wfId);
		JSONObject json=new JSONObject(wf);
		return json.toString();
	  
	}

	private String getWorkflowInfo(long wfId) {
		WorkflowsDAO wfDAO=new WorkflowsDAO();
		Workflows wf=wfDAO.findById(wfId);
		BPELLazyParser parser=new BPELLazyParser(getServletContext());
		return parser.lazyParse(wf);
	
			
		
	}

	private String getFoucsedWf(long uid) {
		UserwokflowDAO  uwfDAO=new UserwokflowDAO();
		WorkflowsDAO wfDAO=new WorkflowsDAO();
		List<Userwokflow> result=uwfDAO.findByUserId(uid);
		List<Workflows> wfList=new ArrayList<Workflows>();
		for(Userwokflow fwf: result){
			Workflows wf=wfDAO.findById(fwf.getWfId());
			if(wf!=null)
				wfList.add(wf);
		}
		JSONArray jsonArray = new JSONArray(wfList);
		return jsonArray.toString();
		
	}
	
	public  void  insertWfRecord(String wfName,String wfPath,long uid){
		/**
		 * wfName: eg "travel.bpel"
		 * wfPath: eg:"/bpels/travel.xml"
		 * uid: eg: 316 
		 */
		
		WorkflowsDAO wfDAO=new WorkflowsDAO();
		UserwokflowDAO uwfDAO=new UserwokflowDAO();
		
		Session session = wfDAO.getSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			Workflows wf=new Workflows();
			wf.setWfName(wfName);
			wf.setWfPath(wfPath);
			session.save(wf);
			
			Userwokflow uwf=new Userwokflow();
			uwf.setWfId(wf.getWfId());
			uwf.setUserId(uid);
			session.save(uwf);
			
			tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			
		     session.close( );	// 关闭session
		}
		
	
		
	}
	

}
