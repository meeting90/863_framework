package workflow.task;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TaskTimerListner  implements ServletContextListener{
	
	
	private Timer timer = null;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		timer=null;
		
	}
	@Override
	public void contextInitialized(ServletContextEvent event) {
		timer=new Timer(true);
		event.getServletContext().log("timer started!");
		timer.schedule(new UpdateDataBaseTask(event.getServletContext()), 0,1*60*60*1000);
	}
	

}
