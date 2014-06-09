package workflow.db;

// Generated 2014-5-15 18:04:09 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import db.BaseHibernateDAO;

/**
 * Home object for domain model class Focusws.
 * @see workflow.db.Focusws
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class FocuswsDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(FocuswsDAO.class);

	private static String USER_ID="userid";
	private static String WS_ID= "wsId";

	public void persist(Focusws transientInstance) {
		log.debug("persisting Focusws instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Focusws instance) {
		log.debug("attaching dirty Focusws instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Focusws instance) {
		log.debug("attaching clean Focusws instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Focusws persistentInstance) {
		log.debug("deleting Focusws instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Focusws merge(Focusws detachedInstance) {
		log.debug("merging Focusws instance");
		try {
			Focusws result = (Focusws) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Focusws findById(java.lang.Long id) {
		log.debug("getting Focusws instance with id: " + id);
		try {
			Focusws instance = (Focusws) getSession()
					.get("workflow.db.Focusws", id);
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

	public List findByExample(Focusws instance) {
		log.debug("finding Focusws instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow.db.Focusws")
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
		log.debug("finding all Focusws instances");
		try {
			String queryString = "from Focusws";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Focusws instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Focusws as model where model."
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
	
	public List findByUserIdAndWsId(Object userId,Object wsId){
		log.debug("finding Focusws instance with property: " + USER_ID
				+ ", value: " + userId+","+WS_ID+",value:"+wsId);
		try {
			String queryString = "from Focusws as model where model."
					+ USER_ID + "= ?" + "and "+ WS_ID+ "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, userId);
			queryObject.setParameter(0, wsId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	




}
