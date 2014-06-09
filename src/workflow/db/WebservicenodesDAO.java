package workflow.db;

// Generated 2014-5-15 19:06:46 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;

import db.BaseHibernateDAO;

/**
 * Home object for domain model class Webservicenodes.
 * @see workflow2.db.Webservicenodes
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class WebservicenodesDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(WebservicenodesDAO.class);

	public void save(Webservicenodes transientInstance) {
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void persist(Webservicenodes transientInstance) {
		log.debug("persisting Webservicenodes instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Webservicenodes instance) {
		log.debug("attaching dirty Webservicenodes instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Webservicenodes instance) {
		log.debug("attaching clean Webservicenodes instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Webservicenodes persistentInstance) {
		log.debug("deleting Webservicenodes instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Webservicenodes merge(Webservicenodes detachedInstance) {
		log.debug("merging Webservicenodes instance");
		try {
			Webservicenodes result = (Webservicenodes)getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Webservicenodes findById(long id) {
		log.debug("getting Webservicenodes instance with id: " + id);
		try {
			Webservicenodes instance = (Webservicenodes) getSession()
					.get("workflow.db.Webservicenodes", id);
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

	
	public List findByExample(Webservicenodes instance) {
		log.debug("finding Webservicenodes instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow.db.Webservicenodes")
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
