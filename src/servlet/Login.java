package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.NameValuePair;
import org.hibernate.Session;
import org.hibernate.Transaction;

import util.PhpConfig;

import db.History;
import db.HistoryDAO;
import db.User;
import db.UserDAO;

public class Login extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		req.setCharacterEncoding("UTF-8");
		// TODO Auto-generated method stub
		UserDAO userDAO = new UserDAO();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		resp.setContentType("text/html;charset=UTF-8");
		List<User> userList = userDAO.findByUsername(username);
		if (userList == null || userList.isEmpty()) {
			req.getSession().setAttribute("error", "用户名不存在");// 用户名不存在
			resp.sendRedirect("index.jsp");
			return;
		} else if (!userList.get(0).getPassword().equals(MD5.getMD5(password.getBytes()))) {
			req.getSession().setAttribute("error", "密码错误");// 密码错误
			resp.sendRedirect("index.jsp");
			return;
		}

		long userid=userList.get(0).getId();
		req.getSession().setAttribute("error", null);
		req.getSession().setAttribute("username", username);
		req.getSession().setAttribute("user", username + "/");
		req.getSession().setAttribute("userid", userid);
		req.getSession().setAttribute("php_host", PhpConfig.getPhpHost());
		

		// 将session放入
		session.getServletContext().setAttribute("internetware_session",session);
		
		//php 后台session
//		setPhpSession(userid, username,req.getRequestURL().toString());

		/*
		 * Login记录
		 */
		History history = new History();
		HistoryDAO historyDAO = new HistoryDAO();
		history.setUserId(userList.get(0).getId());
		history.setServiceId((long) 3);
		history.setParam("");
		history.setDescription("从" + getRealIPAddress(req) + "登陆");
		history.setStatus(0);
		history.setAccessTime(new Timestamp(System.currentTimeMillis()));

		// Session hiberSession = historyDAO.getSession();
		// Transaction tx = null;
		// try{
		// tx = hiberSession.beginTransaction(); // 开启session
		// hiberSession.save(history);
		// tx.commit(); // 事务提交
		// }catch(Exception e){
		// // 异常处理
		// e.printStackTrace();
		// }finally{
		// hiberSession.close( ); // 关闭session
		// }

		HistoryDAO hDAO = new HistoryDAO();
		User user = userList.get(0);
		List<History> hList = hDAO.findByUserId(user.getId());
		req.getSession().setAttribute("history", hList);
		resp.sendRedirect("main.jsp");
		return;
	}

	/**
	 * 获取真实的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public String getRealIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
//	private void setPhpSession(long uid,String name,String redirect) throws IOException{
//		String urlStr=PhpConfig.getPhpHost()+"/login.php";
//		String params="uid="+uid+"&name="+name+"&redirect="+redirect;
//		URL url=new URL(urlStr);
//		URLConnection conn=url.openConnection();
//		HttpURLConnection httpUrlConnection = (HttpURLConnection) conn; 
//		httpUrlConnection.setDoOutput(true); 
//		httpUrlConnection.setRequestMethod("POST"); 
//		OutputStream os = httpUrlConnection.getOutputStream();
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//		writer.write(params);
//		writer.flush();
//		writer.close();
//		httpUrlConnection.connect();
//		BufferedReader reader=new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(),"UTF-8"));
//		System.out.println(reader.readLine());
//		
//		
//	}
}
