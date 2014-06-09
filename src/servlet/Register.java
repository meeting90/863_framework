package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import db.User;
import db.UserDAO;

public class Register extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		// TODO Auto-generated method stub
		UserDAO userDAO = new UserDAO();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String password2 = req.getParameter("password2");
		String email = req.getParameter("email");
		
		if (userDAO.findByUsername(username).size() != 0) {
			req.getSession().setAttribute("error", "用户名已存在");
			resp.sendRedirect("register.jsp");
			return;
		}
		req.getSession().setAttribute("error", null);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(MD5.getMD5(password.getBytes()));
		user.setEmail(email);
		user.setCreateTime(new Timestamp(System.currentTimeMillis()));

		Session session = userDAO.getSession();
		Transaction tx = null;
		try{
		      tx = session.beginTransaction();	// 开启session             
		      session.save(user);
		      tx.commit();	// 事务提交
		}catch(Exception e){
		      // 异常处理
		}finally{
		     session.close( );	// 关闭session
		}
		
//		req.getSession().setAttribute("error", null);
//		req.getSession().setAttribute("user", username);
		resp.sendRedirect("registerSuccess.jsp");
	}
}
