package db;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * History entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see db.History
 * @author MyEclipse Persistence Tools
 */

public class HistoryDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(HistoryDAO.class);
	// property constants
	public static final String USER_ID = "userId";
	public static final String SERVICE_ID = "serviceId";
	public static final String PARAM = "param";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";

	public void save(History transientInstance) {
		log.debug("saving History instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(History persistentInstance) {
		log.debug("deleting History instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public History findById(java.lang.Long id) {
		log.debug("getting History instance with id: " + id);
		try {
			History instance = (History) getSession().get("db.History", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(History instance) {
		log.debug("finding History instance by example");
		try {
			List results = getSession().createCriteria("db.History")
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
		log.debug("finding History instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from History as model where model."
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

	public List findByServiceId(Object serviceId) {
		return findByProperty(SERVICE_ID, serviceId);
	}

	public List findByParam(Object param) {
		return findByProperty(PARAM, param);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all History instances");
		try {
			String queryString = "from History";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public History merge(History detachedInstance) {
		log.debug("merging History instance");
		try {
			History result = (History) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(History instance) {
		log.debug("attaching dirty History instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(History instance) {
		log.debug("attaching clean History instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}