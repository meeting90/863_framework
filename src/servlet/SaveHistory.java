package servlet;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import db.History;
import db.HistoryDAO;
import db.User;
import db.UserDAO;




public class SaveHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SaveHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String username=(String) request.getSession().getAttribute("username");
    	Long userid=(Long)request.getSession().getAttribute("userid");
    
    	
    	String serviceId=new String(request.getParameter("serviceId").getBytes(), "UTF-8");
    	String document=new String(request.getParameter("document").getBytes(), "UTF-8");
    	String left=request.getParameter("left");
    	String right=request.getParameter("right");
    	String center=request.getParameter("center");
    	
    	String bottom=request.getParameter("bottom");   
    	String usersRoot = getServletContext().getRealPath("/users");
        File path = new File(usersRoot + "/user"+userid+"/tool"+serviceId);
        if (!path.exists()) {
            path.mkdirs();
        }
        long currentTime=System.currentTimeMillis();
        String filename=currentTime+"";
        File saveFile = new File(path + "/" + filename);
      
        PrintWriter writer;
        if(left!=null){
        	left=new String(left.getBytes(),"utf-8");
        	writer=new PrintWriter(saveFile+".left"); 
        	writer.write(left);
        	writer.flush();
        	writer.close();
        }
    	if(right!=null){
    		right=new String(right.getBytes(),"utf-8");
    		writer=new PrintWriter(saveFile+".right");
    		writer.write(right);
    		writer.flush();
    		writer.close();
    	}
    	if(center!=null){
    		center=new String(center.getBytes(),"utf-8");
    		writer=new PrintWriter(saveFile+".center");
    		writer.write(center);
    		writer.flush();
    		writer.close();
    	}
    	if(bottom!=null){
    		bottom=new String(bottom.getBytes(),"utf-8");
    		writer=new PrintWriter(saveFile+".bottom");
    		writer.write(bottom);
    		writer.flush();
    		writer.close();
    	}
     	
     	

    	History history=new History();
    	HistoryDAO historyDAO=new HistoryDAO();
    	UserDAO userDAO=new UserDAO();
		List<User> userList=userDAO.findByUsername(username);
    	history.setUserId(userList.get(0).getId());
    	history.setServiceId((long) Integer.parseInt(serviceId));
    	history.setStatus(0);
    	history.setAccessTime(new Timestamp(currentTime));
    	history.setDescription(document);
    	history.setParam("users/user"+userid+"/tool"+serviceId+"/"+saveFile.getName());
    	Session hiberSession= historyDAO.getSession();
    	Transaction tx=null;
    	try{
    		tx=hiberSession.beginTransaction();
    		hiberSession.save(history);
    		tx.commit();
    		response.getWriter().append("OK");
    	}catch(Exception e){
    		e.printStackTrace();
    		response.getWriter().append("Error");
    	}finally{
    		hiberSession.close();
    	}
    	response.getWriter().flush();
    	response.getWriter().close();
	}
    
    

}
