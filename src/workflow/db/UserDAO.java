package workflow.db;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class UserDAO extends db.UserDAO{
	private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
	
	public List getAllIds(){
		
		try {
			
			Query query = getSession().createQuery("select id from User ");
			return query.list();
			
		} catch (RuntimeException re) {
			log.error("find all id failed", re);
			throw re;
		}finally{
			getSession().close();
		}
		
	}
	
	public List getAllUser(int size,int offset){
		
		try {
			
			Query query = getSession().createQuery("select id, username from User ");
			query.setMaxResults(size);
			query.setFirstResult(offset);
			return query.list();
			
		} catch (RuntimeException re) {
			log.error("find all id failed", re);
			throw re;
		}finally{
			getSession().close();
		}
		
	}

	public List getUserById(long uid){
		try {
			
			Query query = getSession().createQuery("select id, username from User where id =?");
			query.setParameter(0, uid);
			return query.list();
		} catch (RuntimeException re) {
			log.error("find all id failed", re);
			throw re;
		}finally{
			getSession().close();
		}
	}

	public List searchUser(String name){
		try {
			
			Query query = getSession().createQuery("select id, username from User where username like '"+name+"%'");
			return query.list();
		} catch (RuntimeException re) {
			log.error("find all id failed", re);
			throw re;
		}finally{
			getSession().close();
		}
	}

	
}
