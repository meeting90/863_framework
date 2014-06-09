package workflow.db;

// Generated 2014-5-16 13:16:00 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import db.BaseHibernateDAO;

/**
 * Home object for domain model class Userwokflow.
 * @see workflow.db.Userwokflow
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class UserwokflowDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(UserwokflowDAO.class);

	private static String USER_ID="userId";
	private	static String WF_ID="wfId";
	
	
	public void save(Userwokflow transientInstance) {
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void persist(Userwokflow transientInstance) {
		log.debug("persisting Userwokflow instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Userwokflow instance) {
		log.debug("attaching dirty Userwokflow instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Userwokflow instance) {
		log.debug("attaching clean Userwokflow instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Userwokflow persistentInstance) {
		log.debug("deleting Userwokflow instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Userwokflow merge(Userwokflow detachedInstance) {
		log.debug("merging Userwokflow instance");
		try {
			Userwokflow result = (Userwokflow) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Userwokflow findById(java.lang.Long id) {
		log.debug("getting Userwokflow instance with id: " + id);
		try {
			Userwokflow instance = (Userwokflow) getSession().get("workflow.db.Userwokflow", id);
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

	public List findByExample(Userwokflow instance) {
		log.debug("finding Userwokflow instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow.db.Userwokflow")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Userwokflow instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Userwokflow as model where model."
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

	public List findByWfId(Object wfId) {
		return findByProperty(WF_ID, wfId);
	}
	
	
}
