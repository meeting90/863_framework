package workflow.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import workflow.api.data.UserDetailNode;
import workflow.db.Focusws;
import workflow.db.FocuswsDAO;
import workflow.db.Relationnet;
import workflow.db.RelationnetDAO;
import workflow.db.UserDAO;
import workflow.db.Webservices;
import workflow.db.WebservicesDAO;
import db.User;
@SuppressWarnings("unchecked")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

    public UserServlet() {
        super();
    }
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		resp.setHeader("Access-Control-Allow-Origin", "*");
		String query=req.getParameter("query");
		Long loginId=(Long)req.getSession().getAttribute("userid");
//		if(loginId==null){
//			resp.getWriter().append(ServletConstants.SESSION_TIMEOUT_ERROR);
//			return;
//		}
		if(query.equals("getAllUser")){
			int size=Integer.parseInt(req.getParameter("size"));
			int offset=Integer.parseInt(req.getParameter("offset"));
			resp.getWriter().append(getAllUser(offset,size));
		}else if(query.equals("getUserName")){
			long userid=Long.parseLong(req.getParameter("uid"));
			resp.getWriter().append(getUserNameById(userid));
		}else if(query.equals("searchUser")){
			String name=req.getParameter("name");
			resp.getWriter().append(searchUser(name));
		}else if(query.equals("getFocusedUser")){
			long userid=Long.parseLong(req.getParameter("uid"));
			resp.getWriter().append(getFocusedUser(userid));
		}
		else if(query.equals("getUserDetail")){
			Long userid=Long.parseLong(req.getParameter("uid"));
			resp.getWriter().append(getUserDetail(userid));
		}	
		else{
			resp.getWriter().append(ServletConstants.UNKOWN_REQUEST_ERROR);
		}
		 resp.getWriter().flush();
		 resp.getWriter().close();
		
		
	}
	
	private String getFocusedUser(long userid) {
		RelationnetDAO rDAO=new RelationnetDAO();
		UserDAO userDAO=new UserDAO();
	
		
		List<Relationnet> rns= rDAO.findByTrustorId(userid);
	
		List<List<?>> userNames=new ArrayList<List<?>>();
		for(Relationnet rn:rns){
			User user=userDAO.findById(rn.getTrusteeId());
			List<Object> u=new ArrayList<Object>();
			u.add(user.getId());
			u.add(user.getUsername());
			userNames.add(u);
		}
		
		JSONArray json=new JSONArray(userNames);
	
		return json.toString();
		
	}
	private String getUserDetail(Long userid) {
		UserDAO userDAO=new UserDAO();
		User user=userDAO.findById(userid);
		UserDetailNode node=new UserDetailNode();
		if(user!=null){
			node.setUserId(user.getId());
			node.setUsername(user.getUsername());
			node.setEmail(user.getEmail());
			FocuswsDAO fDAO=new FocuswsDAO();
			List<Focusws> fwss=fDAO.findByUserId(userid);
			for(Focusws fws : fwss ){
				WebservicesDAO wsDAO=new WebservicesDAO();
				Webservices ws= wsDAO.findById(fws.getWsid());
				node.getFocusedWebService().add(ws);
			}
			RelationnetDAO rDAO=new RelationnetDAO();
			List<Relationnet> relations=rDAO.findByTrustorId(userid);
			for(Relationnet rn: relations){
				UserDAO uDAO=new UserDAO();
				User u=uDAO.findById(rn.getTrusteeId());
				node.getFriends().add(u.getUsername());
			}
			relations=rDAO.findByTrusteeId(userid);
			for(Relationnet rn: relations){
				UserDAO uDAO=new UserDAO();
				User u=uDAO.findById(rn.getTrustorId());
				node.getFans().add(u.getUsername());
			}
		}
		JSONObject json=new JSONObject(node);
		return json.toString();

	}

	private String searchUser(String query) {
		UserDAO userDAO=new UserDAO();
		List<?> result=userDAO.searchUser(query);
		JSONArray jsonArray = new JSONArray(result);
		return jsonArray.toString();
	}
	
	private String getAllUser(int offset, int size) {
		UserDAO userDAO=new UserDAO();
		List<?> result=userDAO.getAllUser(size, offset);
		JSONArray jsonArray = new JSONArray(result);
		return jsonArray.toString();

		
	}
	private String getUserNameById(long userid) {
		UserDAO userDAO=new UserDAO();
		User user=userDAO.findById(userid);
		if(user==null)
			return "unknown";
		else
			return user.getUsername();
	}


}
