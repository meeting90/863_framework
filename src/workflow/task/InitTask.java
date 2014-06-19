package workflow.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import servlet.MD5;
import workflow.db.Rating;
import workflow.db.RatingDAO;
import workflow.db.Relationnet;
import workflow.db.RelationnetDAO;
import workflow.db.Userwokflow;
import workflow.db.UserwokflowDAO;
import workflow.db.Webservices;
import workflow.db.WebservicesDAO;
import workflow.db.Workflows;
import workflow.db.WorkflowsDAO;

import db.User;
import db.UserDAO;

public class InitTask {
	
	
	public static void initUsers() throws FileNotFoundException{
		
		UserDAO userDAO=new UserDAO();
		Session session = userDAO.getSession();
		Transaction tx = null;
		try{
		      tx = session.beginTransaction();	// 开启session 
		      Scanner scanner =new Scanner(new File("./one_side/userMapping.txt"));
		      while(scanner.hasNext()){
		    	  String name=scanner.next();
		    	  scanner.next();
		    	  User user=new User();
				  user.setUsername(name);
				  user.setPassword(MD5.getMD5(name.getBytes()));
				  user.setEmail(name+"@ics.nju.edu.cn");
				  user.setCreateTime(new Timestamp(System.currentTimeMillis()));      
			      session.save(user);
		      }
		      scanner.close();
		      tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			 
		     session.close( );	// 关闭session
		}
			  
			
		
		
	}
	public static void initWebService(){
		WebservicesDAO wsDAO=new WebservicesDAO();
		Session session = wsDAO.getSession();
		Transaction tx = null;
		try{
		      tx = session.beginTransaction();	// 开启session 
		      Scanner scanner =new Scanner(new File("./one_side/wsMapping.txt"));
		      while(scanner.hasNext()){
		    	  String name=scanner.nextLine().split("\t")[0];
		    	  System.out.println(name);
		    	  
		    	  Webservices ws=new Webservices();
				  ws.setWsName(name);
				  ws.setWsPath("/wsdls/"+name);
			      session.save(ws);
		      }
		      scanner.close();
		      tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			 
		     session.close( );	// 关闭session
		}
			  
		
	}
	
	private static Map<Integer,String> userMap=new HashMap<Integer,String>();
	private static Map<Integer,String> wsMap=new HashMap<Integer,String>();
	public static void initUserMap() throws FileNotFoundException{
		Scanner userScanner=new Scanner(new File("./one_side/userMapping.txt"));
		while(userScanner.hasNext()){
			String name=userScanner.next();
			int oldId=userScanner.nextInt();
			userMap.put(oldId, name);
		}
		
		userScanner.close();
	}
	public static void initWsMap() throws FileNotFoundException{
		Scanner wsScanner=new Scanner(new File("./one_side/wsMapping.txt"));
		while(wsScanner.hasNext()){
			String line=wsScanner.nextLine();
			String name=line.split("\t")[0];
			int oldId=Integer.parseInt(line.split("\t")[1]);
			wsMap.put(oldId, name);
		}
		wsScanner.close();
	}
	public static void initRating() throws FileNotFoundException{
		
		
		UserDAO userDAO=new UserDAO();
		WebservicesDAO wsDAO=new WebservicesDAO();
		RatingDAO ratingDAO=new RatingDAO();
		Session session = ratingDAO.getSession();
		Transaction tx = null;
		try{
		      tx = session.beginTransaction();	// 开启session 
		      Scanner ratingScanner=new Scanner(new File("./one_side/rating_original.txt"));
		      while(ratingScanner.hasNext()){
				int oldUserId=ratingScanner.nextInt();
				int oldWsId=ratingScanner.nextInt();
				float rateValue=ratingScanner.nextFloat();
				String userName="tzh";
				if(userMap.containsKey(oldUserId))
					userName=userMap.get(oldUserId);
				if(!wsMap.containsKey(oldWsId)){// skip rating of unknown ws
					System.out.println("skip:"+oldUserId+"\t"+oldWsId+"\t"+rateValue);
					
					continue;
				}
				String wsName=wsMap.get(oldWsId);
				
				User user=(User) userDAO.findByUsername(userName).get(0);
				Webservices ws= (Webservices) wsDAO.findByWsName(wsName).get(0);
				Rating rating=new Rating();
				rating.setUserId(user.getId());
				rating.setWsId(ws.getWsId());
				rating.setRateValue(getRateValue(rateValue));
				session.save(rating);			
			  }
			
		      tx.commit();
			  ratingScanner.close();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			
		     session.close( );	// 关闭session
		}
		
	}
	
	
	
	private static float getRateValue(float oldValue){
		if(oldValue==5)
			return (float) 1.0;
		else if(oldValue==4)
			return (float) 0.9;
		else if(oldValue==3)
			return (float) 0.7;
		else if(oldValue==2)
			return (float) 0.5;
		else if(oldValue==1)
			return (float) 0.3;
		else 
			return 0;
	}
	public static void initTrust(){
		UserDAO userDAO=new UserDAO();
		RelationnetDAO rnDAO=new RelationnetDAO();
		
		Session session = rnDAO.getSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			Scanner scanner=new Scanner(new File("./one_side/trust_original.txt"));
			while(scanner.hasNext()){
				
				int oldTorId=scanner.nextInt();
				int oldTeeId=scanner.nextInt();
				float trustValue=scanner.nextFloat();
				if(oldTorId==oldTeeId){
					System.out.println("skip: trustor=trustee,"+oldTorId);
					continue;
				}
				String tor="tzh";
				if(userMap.containsKey(oldTorId))
					tor=userMap.get(oldTorId);
				String tee="tzh";
				if(userMap.containsKey(oldTeeId))
					tee=userMap.get(oldTeeId);
				
				User torUser=(User) userDAO.findByUsername(tor).get(0);
				User teeUser=(User) userDAO.findByUsername(tee).get(0);
				
				Relationnet rn=new Relationnet();
				rn.setTrustorId(torUser.getId());
				rn.setTrusteeId(teeUser.getId());
				rn.setTrustValue(trustValue);
			
				session.save(rn);
			}
			scanner.close();
			tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			
		     session.close( );	// 关闭session
		}
	}
	public static void initWorkflow(){
		WorkflowsDAO wfDAO=new WorkflowsDAO();
		UserwokflowDAO uwfDAO=new UserwokflowDAO();
		
		Session session = wfDAO.getSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			Workflows wf=new Workflows();
			wf.setWfId(1L);
			wf.setWfName("travel.bpel");
			wf.setWfPath("/bpels/travel.bpel");
			session.save(wf);
			
			tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			
		     session.close( );	// 关闭session
		}
		
		session= uwfDAO.getSession();
		tx=null;
		try{
			tx=session.beginTransaction();
			Userwokflow uwf=new Userwokflow();
			uwf.setUserId(316);
			uwf.setWfId(1L);
			session.save(uwf);
			tx.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
	
	public static void updateTrust(){
		RelationnetDAO rnDAO=new RelationnetDAO();
		UserDAO uDAO=new UserDAO();
		
		List<User> users= uDAO.findAll();
		Random random=new Random();
		User[] userList=users.toArray(new User[users.size()]);
		System.out.println(userList.length);
		Session session=rnDAO.getSession();
		Transaction tx=null;
		try{
			tx=session.beginTransaction();
			for(User u:userList){
				
				Collections.shuffle(users);
				int count=random.nextInt(2);
				System.out.println(count);
				for(int i=0;i<count;i++){
					List<Relationnet> result= rnDAO.findByTrustorAndTrusteeId(u.getId(), users.get(i).getId());
					if(result==null||result.isEmpty()){
						Relationnet newR=new Relationnet();
						newR.setTrustorId(u.getId());
						newR.setTrusteeId(users.get(i).getId());
						newR.setTrustValue(1.0f);
						JSONObject ob=new JSONObject(newR);
						System.out.println(ob.toString());
						session.save(newR);
					}
					
				}
			}
			tx.commit();
		}catch(Exception e){
			
		}finally{
			session.close();
		}
		
	}
	public static float ratingValue(double r){
		if(r<=0.1)
			return 0.3f;
		else if(r<=0.3)
			return 0.5f;
		else if(r<=0.5)
			return 0.7f;
		else if(r<=0.8)
			return 0.9f;
		else 
			return 1.0f;
	}
	public static void updateRating(){
		RatingDAO rDAO=new RatingDAO();
		UserDAO uDAO=new UserDAO();
		WebservicesDAO wsDAO=new WebservicesDAO();
		
		List<User> users= uDAO.findAll();
		List<Webservices> services=wsDAO.findAll();
		Random random=new Random();
		
		Session session=rDAO.getSession();
		Transaction tx=null;
		try{
			tx=session.beginTransaction();
			for(Webservices ws: services){
				
				Collections.shuffle(users);
				int count=random.nextInt(3);
				
				System.out.println(count);
				for(int i=0;i<count;i++){
					List<Rating> result= rDAO.findByUserAndWsId(users.get(i).getId(), ws.getWsId());
					if(result==null||result.isEmpty()){
						double r=random.nextDouble();
					
						Rating rating=new Rating();
						rating.setRateValue(ratingValue(r));
						rating.setUserId(users.get(i).getId());
						rating.setWsId(ws.getWsId());
						JSONObject ob=new JSONObject(rating);
						System.out.println(ob.toString());
						session.save(rating);
						
					}
					
				}
			}
			tx.commit();
		}catch(Exception e){
			
		}finally{
			session.close();
		}
	}
	
	
	public static void updateWs(){
		WebservicesDAO wsDAO=new WebservicesDAO();
		RatingDAO rDAO=new RatingDAO();
		UserDAO uDAO=new UserDAO();
		List<User> users=uDAO.findAll();
		Session session = wsDAO.getSession();
		Transaction tx = null;
		Random random=new Random();
		try{
		      tx = session.beginTransaction();	// 开启session 
		     
		      File folder=new File("./wsdls/");
		      File []files=folder.listFiles();
		      for(File file: files){
		    	 int count= random.nextInt(10);
		    	 Collections.shuffle(users);
		    	  Webservices ws=new Webservices();
		    	  ws.setWsName(file.getName());
		    	  ws.setWsPath("/wsdls/"+file.getName());
		    	  session.save(ws);
		    	  for(int i=0;i<count;i++){
						double r=random.nextDouble();
					
						Rating rating=new Rating();
						rating.setRateValue(ratingValue(r));
						rating.setUserId(users.get(i).getId());
						rating.setWsId(ws.getWsId());
						JSONObject ob=new JSONObject(rating);
						System.out.println(ob.toString());
						session.save(rating);
							
						
						
		    	  }
		      }  
		      tx.commit();
		     
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			 
		     session.close( );	// 关闭session
		}
	}
	
	public static void updateWorkflow(){
		WorkflowsDAO wfDAO=new WorkflowsDAO();
		UserwokflowDAO uwfDAO=new UserwokflowDAO();
		
		Session session = wfDAO.getSession();
		Transaction tx = null;
		File folder=new File("./bpels/");
		File [] files=folder.listFiles();
	
			try{
				tx=session.beginTransaction();
				for(File file :files){
					Workflows wf=new Workflows();
					
					wf.setWfName(file.getName());
					wf.setWfPath("/bpels/"+file.getName().replace("bpel", "xml"));
					session.save(wf);
					
					Userwokflow uwf=new Userwokflow();
					uwf.setUserId(316);
					uwf.setWfId(wf.getWfId());
					session.save(uwf);
					
				}
			
			tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			
		     session.close( );	// 关闭session
		}
		
	}
	public static void main(String []args) throws FileNotFoundException{
		//InitTask.initWorkflow();
		//InitTask.initUsers();
		//InitTask.initWebService();
		//InitTask.initUserMap();
		//InitTask.initWsMap();
		//InitTask.initRating();
		//InitTask.initTrust();
		//InitTask.updateTrust();
		//InitTask.updateRating();
		//InitTask.updateWs();
		//InitTask.updateWorkflow();
	}

}
