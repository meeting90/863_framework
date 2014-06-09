package workflow.task;

import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import workflow.db.Rating;
import workflow.db.RatingDAO;
import workflow.db.Relationnet;
import workflow.db.RelationnetDAO;
import workflow.db.UserDAO;
import workflow.db.WebservicesDAO;
@SuppressWarnings("unchecked")
public class UpdateDataBaseTask extends TimerTask{

	
	private ServletContext context=null;
	public UpdateDataBaseTask(ServletContext servletContext) {
		this.context=servletContext;
	}

	@Override
	public void run() {
		context.log("start update database");
		
		updateDataBase();
		
		context.log("update ended");
		
	}
	
	public static void updateDataBase(){
		
		//get all webservices ids
		WebservicesDAO webservicesDAO=new WebservicesDAO();
		List<Long> wsIds=webservicesDAO.getAllIds();
		
		System.out.println(wsIds.size());
		
		//get all user ids
		UserDAO userDAO=new UserDAO();
		List<Long> userIds=userDAO.getAllIds();
		
		System.out.println(userIds.size());
		
		//get rate
		RatingDAO ratingDAO = new RatingDAO();
		List<Rating> ratingOringal = ratingDAO.findAll();
		
		System.out.println(ratingOringal.size());
		
		//get relationship
		RelationnetDAO relationnetDAO=new RelationnetDAO();
		
		List<Relationnet> relationOringal= relationnetDAO.findAll();
		
		System.out.println(relationOringal.size());
		
		///////////////update database using DataGenerator///////////////////
		long startTime = System.currentTimeMillis();
		
		DataGenerator dataGenerator = new DataGenerator(userIds, wsIds, ratingOringal, relationOringal);
		dataGenerator.updateData();
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total update time is " + (endTime-startTime) + " millisecond");
		
	
		

	}
	
	public static void main(String []args){
		UpdateDataBaseTask.updateDataBase();
	}

}
