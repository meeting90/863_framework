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
 * Home object for domain model class Webservices.
 * @see workflow.db.Webservices
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class WebservicesDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(WebservicesDAO.class);


	private static String WS_NAME="wsName";
	private static String WS_PARH="wsPath";
	
	public void save(Webservices transientInstance) {
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void persist(Webservices transientInstance) {
		log.debug("persisting Webservices instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Webservices instance) {
		log.debug("attaching dirty Webservices instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Webservices instance) {
		log.debug("attaching clean Webservices instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Webservices persistentInstance) {
		log.debug("deleting Webservices instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Webservices merge(Webservices detachedInstance) {
		log.debug("merging Webservices instance");
		try {
			Webservices result = (Webservices) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Webservices findById(java.lang.Long id) {
		log.debug("getting Webservices instance with id: " + id);
		try {
			Webservices instance = (Webservices) 
					getSession().get("workflow.db.Webservices", id);
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

	public List findByExample(Webservices instance) {
		log.debug("finding Webservices instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow.db.Webservices")
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
		log.debug("finding all Webservices instances");
		try {
			String queryString = "from Webservices";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List getAllIds() {
		try {
			
			Query query = getSession().createQuery("select id from Webservices ");
			
			return query.list();
			
		} catch (RuntimeException re) {
			log.error("find all id failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Webservices instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Webservices as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	

	public List findByWsName(Object wsName) {
		return findByProperty(WS_NAME, wsName);
	}

	public List findByWsPath(Object wsPath) {
		return findByProperty(WS_PARH, wsPath);
	}
	
    public List getAllWS(int size,int offset){
		try {	
			Query query = getSession().createQuery("from  Webservices ");
			query.setFirstResult(offset);
			query.setMaxResults(size);
			return query.list();
			
		} catch (RuntimeException re) {
			log.error("find all id failed", re);
			throw re;
		}
		
	}

	public List searchService(String name) {
		try {	
		Query query = getSession().createQuery("from  Webservices where wsName like '"+name+"%'");
		
			return query.list();
			
		} catch (RuntimeException re) {
			log.error("find all id failed", re);
			throw re;
		}
	}
 

}
