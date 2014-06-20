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
		context.log("开始用户之间信任度预测以及服务的评价预测任务");
		long startTime = System.currentTimeMillis();
		
		updateDataBase();
		
		
		long endTime = System.currentTimeMillis();
		
		context.log("预测任务耗时 " + (endTime-startTime)/1000 + "s");
		
		context.log("预测任务完成");
		
	}
	
	public  void updateDataBase(){
		
		//get all webservices ids
		WebservicesDAO webservicesDAO=new WebservicesDAO();
		List<Long> wsIds=webservicesDAO.getAllIds();
		context.log("Web服务数量："+wsIds.size());
	
		
		//get all user ids
		UserDAO userDAO=new UserDAO();
		List<Long> userIds=userDAO.getAllIds();
		context.log("用户数量："+userIds.size());
		
		
		//get rate
		RatingDAO ratingDAO = new RatingDAO();
		List<Rating> ratingOringal = ratingDAO.findAll();
		
		context.log("原始服务评价数量："+ratingOringal.size());
		
		
		
		//get relationship
		RelationnetDAO relationnetDAO=new RelationnetDAO();
		
		List<Relationnet> relationOringal= relationnetDAO.findAll();
		
		context.log("原始用户关系数量："+relationOringal.size());
		
	
		///////////////update database using DataGenerator///////////////////
	
		
		DataGenerator dataGenerator = new DataGenerator(userIds, wsIds, ratingOringal, relationOringal);
		dataGenerator.updateData();
		
	}
	
	
}
