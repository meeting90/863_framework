package workflow.db;

// Generated 2014-5-15 19:06:46 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;

import db.BaseHibernateDAO;

/**
 * Home object for domain model class Workflownodes.
 * @see workflow2.db.Workflownodes
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class WorkflownodesDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(WorkflownodesDAO.class);

	

	public void persist(Workflownodes transientInstance) {
		log.debug("persisting Workflownodes instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Workflownodes instance) {
		log.debug("attaching dirty Workflownodes instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Workflownodes instance) {
		log.debug("attaching clean Workflownodes instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Workflownodes persistentInstance) {
		log.debug("deleting Workflownodes instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Workflownodes merge(Workflownodes detachedInstance) {
		log.debug("merging Workflownodes instance");
		try {
			Workflownodes result = (Workflownodes) 
					getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Workflownodes findById(long id) {
		log.debug("getting Workflownodes instance with id: " + id);
		try {
			Workflownodes instance = (Workflownodes) getSession().get("workflow.db.Workflownodes", id);
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

	
	public List findByExample(Workflownodes instance) {
		log.debug("finding Workflownodes instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow2.db.Workflownodes")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
