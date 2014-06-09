package db;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * UserService entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see db.UserService
 * @author MyEclipse Persistence Tools
 */

public class UserServiceDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UserServiceDAO.class);
	// property constants
	public static final String USER_ID = "userId";
	public static final String SERVICE_ID = "serviceId";

	public void save(UserService transientInstance) {
		log.debug("saving UserService instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UserService persistentInstance) {
		log.debug("deleting UserService instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserService findById(java.lang.Long id) {
		log.debug("getting UserService instance with id: " + id);
		try {
			UserService instance = (UserService) getSession().get(
					"db.UserService", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UserService instance) {
		log.debug("finding UserService instance by example");
		try {
			List results = getSession().createCriteria("db.UserService")
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
		log.debug("finding UserService instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserService as model where model."
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

	public List findAll() {
		log.debug("finding all UserService instances");
		try {
			String queryString = "from UserService";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UserService merge(UserService detachedInstance) {
		log.debug("merging UserService instance");
		try {
			UserService result = (UserService) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UserService instance) {
		log.debug("attaching dirty UserService instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserService instance) {
		log.debug("attaching clean UserService instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}