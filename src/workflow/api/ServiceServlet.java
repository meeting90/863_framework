package workflow.api;

import java.io.File;
import java.io.IOException;
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

import workflow.api.data.UserRatingNode;
import workflow.db.Focusws;
import workflow.db.FocuswsDAO;
import workflow.db.Rating;
import workflow.db.RatingDAO;
import workflow.db.Ratingfull;
import workflow.db.RatingfullDAO;
import workflow.db.Userwokflow;
import workflow.db.UserwokflowDAO;
import workflow.db.Webservices;
import workflow.db.WebservicesDAO;
import workflow.db.Workflows;
import workflow.db.WorkflowsDAO;
import workflow.parser.WSDLazyParser;

@SuppressWarnings("unchecked")
public class ServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    public ServiceServlet() {
        super();
    }
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		 if(query.equals("getServiceName")){
			long wsid=Long.parseLong(req.getParameter("wsid"));
			resp.getWriter().append(getWsNameById(wsid));
		}else if(query.equals("getServiceId")){
			String wsName=req.getParameter("wsName");
			resp.getWriter().append(getServiceId(wsName));
		}
		 else if(query.equals("getService")){
			long wsid=Long.parseLong(req.getParameter("wsid"));
			resp.getWriter().append(getWebServiceById(wsid));
		}else if(query.equals("searchService")){
			String name=req.getParameter("name");
			resp.getWriter().append(searchWebService(name));
		}
		else if(query.equals("getFocusedService")){
			long uid=Long.parseLong(req.getParameter("uid"));
			resp.getWriter().append(getFocusedWS(uid));
		}else if(query.equals("getServiceInfo")){
			long wsId=Long.parseLong(req.getParameter("wsid"));
			resp.getWriter().append(getWSInfo(wsId));
		}else if(query.equals("getAllService")){
			int size=Integer.parseInt(req.getParameter("size"));
			int offset=Integer.parseInt(req.getParameter("offset"));
			resp.getWriter().append(getAllWS(offset,size));
		}else if(query.equals("addFocusService")){
			long uid=Long.parseLong(req.getParameter("uid"));
			long wsId=Long.parseLong(req.getParameter("wsid"));
			resp.getWriter().append(addFocusService(uid,wsId));
		}else if(query.equals("removeFocusService")){
			long uid=Long.parseLong(req.getParameter("uid"));
			long wsId=Long.parseLong(req.getParameter("wsid"));
			resp.getWriter().append(removeFocusService(uid,wsId));
		}else if(query.equals("getRating")){
			long uid=Long.parseLong(req.getParameter("uid"));
			long wsId=Long.parseLong(req.getParameter("wsid"));
			resp.getWriter().append(getRating(uid,wsId));
		}else if(query.equals("updateRating")){
			long uid=Long.parseLong(req.getParameter("uid"));
			
			long wsId=Long.parseLong(req.getParameter("wsid"));
			float rateValue=Float.parseFloat(req.getParameter("ratevalue"));
			resp.getWriter().append(updateRating(uid,wsId,rateValue));
		}else if(query.equals("getfullRating")){
			long uid=Long.parseLong(req.getParameter("uid"));
			long wsId=Long.parseLong(req.getParameter("wsid"));
			resp.getWriter().append(getfullRating(uid,wsId));
		}
		else if(query.equals("uploadService")){
			
			//System.out.printf("upload\n");
			
			String temppath = getServletContext().getRealPath("./workflow")+"/wsdls/temp/";
			String path = getServletContext().getRealPath("./workflow")+"/wsdls/";
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
	            String wsName = filename;
	            while(f.exists()){
	            	wsName = firstname+"_"+fileIndex+lastname;
	            	f = new File(path,wsName);
	            	fileIndex++;
	            }
	            
	            item.write(f);
	            
	            //System.out.printf("add to database,%s\n",wsName);

	            String ret = insertWsRecord(wsName, path+"/"+wsName, Long.parseLong(req.getParameter("uid")));
	            
	            System.out.printf("%s\n",ret);
	            
	            resp.getWriter().append(ret);
	            
	      
	          
	        } catch (FileUploadException e) {
	        	//System.out.printf("error1\n");
	        	resp.getWriter().append(ServletConstants.ERROR_MSG); 
	        } catch (Exception e) {
	        	e.printStackTrace();
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
		
		return filename;
	}
	
	private String getServiceId(String wsName) {
		WebservicesDAO wsDAO=new WebservicesDAO();
		List<Webservices> services=wsDAO.findByWsName(wsName);
		JSONArray json=new JSONArray(services);
		return json.toString();
	}
	private String getfullRating(long uid, long wsId) {
		RatingfullDAO rfDAO=new RatingfullDAO();
		List<Ratingfull> rateful=rfDAO.findByUserAndWsId(uid, wsId);
		JSONArray json=new JSONArray(rateful);
		return json.toString();
	}
	private String searchWebService(String name) {
		WebservicesDAO wsDAO=new WebservicesDAO();
		List<?> result=wsDAO.searchService(name);
		JSONArray jsonArray = new JSONArray(result);
		return jsonArray.toString();
	}
	private String getWebServiceById(long wsid) {
		WebservicesDAO wsDAO=new WebservicesDAO();
		Webservices ws=wsDAO.findById(wsid);
		JSONObject json=new JSONObject(ws);
		return json.toString();
	}
	private String getAllWS(int offset, int size) {
		WebservicesDAO wsDAO=new WebservicesDAO();
		List<Webservices> result=wsDAO.getAllWS(size, offset);
		JSONArray jsonArray = new JSONArray(result);
		return jsonArray.toString();
	}

	private String getWSInfo(long wsId) {
		WSDLazyParser parser=new WSDLazyParser(getServletContext());
		WebservicesDAO wsDAO=new WebservicesDAO();
		Webservices ws=wsDAO.findById(wsId);
		return parser.lazyParse(ws);
		
	}
	
	private String getWsNameById(long wsid) {
		WebservicesDAO wsDAO=new WebservicesDAO();
		Webservices ws=wsDAO.findById(wsid);
		return ws.getWsName();
	}
	private String getFocusedWS(long uid) {
		FocuswsDAO fDAO=new FocuswsDAO();
		WebservicesDAO wsDAO=new WebservicesDAO();
		List<Focusws> result=fDAO.findByUserId(uid);
		List<Webservices> wsList=new ArrayList<Webservices>();
		for(Focusws fws: result){
			Webservices ws=wsDAO.findById(fws.getWsid());
			if(ws!=null)
				wsList.add(ws);
		}
		JSONArray jsonArray = new JSONArray(wsList);
		return jsonArray.toString();
	}
	private String removeFocusService(long uid, long wsId) {
		FocuswsDAO fDAO=new FocuswsDAO();	
		try{
			List<Focusws> fws=fDAO.findByUserIdAndWsId(uid, wsId);
		
			if(fws.size()!=0){
				Focusws item=fws.get(0);
				Session session=fDAO.getSession();
				Transaction tx=null;
				try{
					tx=session.beginTransaction();
					session.delete(item);
					tx.commit();
					return getFocusedWS(uid);
					
				}catch(Exception e){
					e.printStackTrace();
				    throw e;
				}finally{
					
				     session.close( );	// 关闭session
				}
			}
		}catch(Exception e){
			return ServletConstants.ERROR_MSG;
		}
		return ServletConstants.ERROR_MSG;
		
		
	}
	private String addFocusService(long uid, long wsId) {
		FocuswsDAO fDAO=new FocuswsDAO();	
		try{
			
			List<Focusws> fws=fDAO.findByUserIdAndWsId(uid, wsId);
			if(fws.size()==0){
				Focusws item=new Focusws();
				item.setUserid(uid);
				item.setWsid(wsId);
				Session session=fDAO.getSession();
				Transaction tx=null;
				try{
					tx=session.beginTransaction();
					session.save(item);
					tx.commit();
					return getFocusedWS(uid);
				}catch(Exception e){
					e.printStackTrace();
				    throw e;
				}finally{
				     session.close( );	// 关闭session
				}
			}
		}catch(Exception e){
			return ServletConstants.ERROR_MSG;
		}
		return  ServletConstants.ERROR_MSG;
	}
	private String updateRating(long uid, long wsId, float rateValue) {
		RatingDAO rDAO=new RatingDAO();
		try{
			List<Rating> ratings=rDAO.findByUserAndWsId(uid, wsId);
			Rating rating=null;
			if(ratings.size()==0){
				rating=new Rating();
				rating.setUserId(uid);
				rating.setWsId(wsId);
			
			}else{
				rating=ratings.get(0);
			}
			rating.setRateValue(rateValue);
			Session session=rDAO.getSession();
			Transaction tx=null;
			try{
				tx=session.beginTransaction();
				session.saveOrUpdate(rating);
				tx.commit();
			}catch(Exception e){
				e.printStackTrace();
			    throw e;
			}finally{
			     session.close( );	// 关闭session
			}
		}catch(Exception e){
			return  ServletConstants.ERROR_MSG;
		}
		return  ServletConstants.SUCCESS_MSG;
	}

	private String getRating(long userId,long wsId) {
		RatingDAO rDAO=new RatingDAO();
		RatingfullDAO rfDAO=new RatingfullDAO();
		UserRatingNode rNode=new UserRatingNode();
		List<Ratingfull> rateful=rfDAO.findByUserAndWsId(userId, wsId);
		List<Rating> rates=rDAO.findByWsId(wsId);
		rNode.setRatings(rates);
		if(rateful==null||rateful.isEmpty())
			rNode.setFulRateValue(0);
		else
			rNode.setFulRateValue(rateful.get(0).getRateValue());
		JSONObject json=new JSONObject(rNode);
		return json.toString();

	}
	
	
	public  String  insertWsRecord(String wsName,String wsPath,long uid){
		/**
		 * wsName: eg "account.wsdl"
		 * wsPath: eg:"/wsdls/account.wsdl"
		 * uid: eg: 316 
		 */
		
		
		
		
		WebservicesDAO wsDAO=new WebservicesDAO();
	
		
		Session session = wsDAO.getSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			Webservices ws=new Webservices();
			ws.setWsName(wsName);
			ws.setWsPath(wsPath);
			session.save(ws);
			Focusws fws=new Focusws();
			fws.setUserid(uid);
			fws.setWsid(ws.getWsId());
			session.save(fws);
			
			tx.commit();
			return getFocusedWS(uid);
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
		     session.close( );	// 关闭session
		}
		
		return ServletConstants.ERROR_MSG;
	}

}
