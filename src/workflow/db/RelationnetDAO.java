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
 * Home object for domain model class Relationnet.
 * @see workflow.db.Relationnet
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class RelationnetDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(RelationnetDAO.class);

	
	private static String TRUSTOR_ID= "trustorId";
	private static String TRUSTEE_ID=" trusteeId";
	private static String TRUST_VALUE="trustValue";


	public void save(Relationnet transientInstance) {
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void persist(Relationnet transientInstance) {
		log.debug("persisting Relationnet instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Relationnet instance) {
		log.debug("attaching dirty Relationnet instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Relationnet instance) {
		log.debug("attaching clean Relationnet instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Relationnet persistentInstance) {
		log.debug("deleting Relationnet instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Relationnet merge(Relationnet detachedInstance) {
		log.debug("merging Relationnet instance");
		try {
			Relationnet result = (Relationnet) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Relationnet findById(java.lang.Long id) {
		log.debug("getting Relationnet instance with id: " + id);
		try {
			Relationnet instance = (Relationnet) getSession().get("workflow.db.Relationnet", id);
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

	public List findByExample(Relationnet instance) {
		log.debug("finding Relationnet instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow.db.Relationnet")
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
		log.debug("finding all Relationnet instances");
		try {
			String queryString = "from Relationnet";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Relationnet instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Relationnet where "
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}finally{
			getSession().clear();
		}
	}

	

	public List findByTrustorId(Object trustorId) {
		return findByProperty(TRUSTOR_ID, trustorId);
	}

	public List findByTrusteeId(Object trusteeId) {
		return findByProperty(TRUSTEE_ID, trusteeId);
	}
	
	public List findByTrustValue(Object trustValue) {
		return findByProperty(TRUST_VALUE, trustValue);
	}
	public List findByTrustorAndTrusteeId(Object trustorId,Object trusteeId){
		log.debug("finding Relationnet instance with property: " + TRUSTOR_ID
				+ ", value: " + trustorId + ";"+TRUSTEE_ID+",value: " +trusteeId);
		try {
			String queryString = "from Relationnet where "
					+ TRUSTOR_ID + "= ?"+ " and "+TRUSTEE_ID +"=?";
			
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, trustorId);
			queryObject.setParameter(1, trusteeId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}finally{
			getSession().clear();
		}
	}
	
	
}
