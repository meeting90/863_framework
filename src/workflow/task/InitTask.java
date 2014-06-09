package workflow.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;

import servlet.MD5;
import workflow.db.Rating;
import workflow.db.RatingDAO;
import workflow.db.Relationnet;
import workflow.db.RelationnetDAO;
import workflow.db.Webservices;
import workflow.db.WebservicesDAO;

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
	public static void main(String []args) throws FileNotFoundException{
		//InitTask.initUsers();
		//InitTask.initWebService();
		//InitTask.initUserMap();
		//InitTask.initWsMap();
		//InitTask.initRating();
		//InitTask.initTrust();
		
	}

}
