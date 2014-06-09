package workflow.db;

// Generated 2014-5-14 15:43:26 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import db.BaseHibernateDAO;

/**
 * Home object for domain model class Ratingfull.
 * @see workflow.db.Ratingfull
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class RatingfullDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(RatingfullDAO.class);

	private static String USER_ID="userId";
	private static String WS_ID= "wsId";
	private static String RATE_VALUE="rateValue";
	

	
	
	public void save(Ratingfull transientInstance) {
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void persist(Ratingfull transientInstance) {
		log.debug("persisting Ratingfull instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Ratingfull instance) {
		log.debug("attaching dirty Ratingfull instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Ratingfull instance) {
		log.debug("attaching clean Ratingfull instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Ratingfull persistentInstance) {
		log.debug("deleting Ratingfull instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Ratingfull merge(Ratingfull detachedInstance) {
		log.debug("merging Ratingfull instance");
		try {
			Ratingfull result = (Ratingfull) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Ratingfull findById(java.lang.Long id) {
		log.debug("getting Ratingfull instance with id: " + id);
		try {
			Ratingfull instance = (Ratingfull) 
					getSession().get("workflow.db.Ratingfull", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Ratingfull instance) {
		log.debug("finding Ratingfull instance by example");
		try {
			List results =getSession()
					.createCriteria("workflow.db.Ratingfull")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	
	public List findAll() {
		log.debug("finding all Ratingfull instances");
		try {
			String queryString = "from Rating";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Ratingfull instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Ratingfull as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByWsId(Object wsId) {
		return findByProperty(WS_ID, wsId);
	}
	
	public List findByRateValue(Object rateValue) {
		return findByProperty(RATE_VALUE, rateValue);
	}
	
	
	public List findByUserAndWsId(Object userId,Object wsId){
		log.debug("finding Ratingfull instance with property: " + USER_ID
				+ ", value: " + userId + ";"+WS_ID+",value: " +wsId);
		try {
			String queryString = "from Ratingfull as model where model."
					+ USER_ID + "= ?"+ " and model."+WS_ID +"=?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, userId);
			queryObject.setParameter(1, wsId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public int deleteAll(){
		log.debug("delete all");
		try {
			String queryString = "delete from Ratingfull";
			Query queryObject = getSession().createQuery(queryString);
		
			return queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}
	
	
