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
 * Home object for domain model class Workflows.
 * @see workflow.db.Workflows
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class WorkflowsDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(WorkflowsDAO.class);

	private static String WF_NAME="wfName";
	private static String WF_PATH="wfPath";

	public void persist(Workflows transientInstance) {
		log.debug("persisting Workflows instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Workflows instance) {
		log.debug("attaching dirty Workflows instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Workflows instance) {
		log.debug("attaching clean Workflows instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Workflows persistentInstance) {
		log.debug("deleting Workflows instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Workflows merge(Workflows detachedInstance) {
		log.debug("merging Workflows instance");
		try {
			Workflows result = (Workflows) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Workflows findById(java.lang.Long id) {
		log.debug("getting Workflows instance with id: " + id);
		try {
			Workflows instance = (Workflows) getSession()
					.get("workflow.db.Workflows", id);
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

	
	public List findByExample(Workflows instance) {
		log.debug("finding Workflows instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow.db.Workflows")
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
		log.debug("finding all Workflows instances");
		try {
			String queryString = "from Workflows";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Workflows instance with property: " + propertyName
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

	

	public List findByWfName(Object wfName) {
		return findByProperty(WF_NAME, wfName);
	}

	public List findByWfPath(Object wfPath) {
		return findByProperty(WF_PATH, wfPath);
	}

}
