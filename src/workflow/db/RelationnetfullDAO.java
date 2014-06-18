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
 * Home object for domain model class Relationnetfull.
 * @see workflow.db.Relationnetfull
 * @author Hibernate Tools
 */
@SuppressWarnings("rawtypes")
public class RelationnetfullDAO extends BaseHibernateDAO{

	private static final Log log = LogFactory.getLog(RelationnetfullDAO.class);

	private static String TRUSTOR_ID= "trustorId";
	private static String TRUSTEE_ID=" trusteeId";
	private static String TRUST_VALUE="trustValue";

	public void save(Relationnetfull transientInstance) {
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void persist(Relationnetfull transientInstance) {
		log.debug("persisting Relationnetfull instance");
		try {
			getSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Relationnetfull instance) {
		log.debug("attaching dirty Relationnetfull instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Relationnetfull instance) {
		log.debug("attaching clean Relationnetfull instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Relationnetfull persistentInstance) {
		log.debug("deleting Relationnetfull instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Relationnetfull merge(Relationnetfull detachedInstance) {
		log.debug("merging Relationnetfull instance");
		try {
			Relationnetfull result = (Relationnetfull) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Relationnetfull findById(java.lang.Long id) {
		log.debug("getting Relationnetfull instance with id: " + id);
		try {
			Relationnetfull instance = (Relationnetfull) getSession().get("workflow.db.Relationnetfull", id);
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

	public List findByExample(Relationnetfull instance) {
		log.debug("finding Relationnetfull instance by example");
		try {
			List results = getSession()
					.createCriteria("workflow.db.Relationnetfull")
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
		log.debug("finding all Relationnetfull instances");
		try {
			String queryString = "from Relationnetfull";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Relationnetfull instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Relationnetfull as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
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
		log.debug("finding Relationnetfull instance with property: " + TRUSTOR_ID
				+ ", value: " + trustorId + ";"+TRUSTEE_ID+",value: " +trusteeId);
		try {
			String queryString = "from Relationnetfull as model where model."
					+ TRUSTOR_ID + "= ?"+ " and model."+TRUSTEE_ID +"=?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, trustorId);
			queryObject.setParameter(1, trusteeId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	public List findByTrustorAndThreshold(Object trustorId,int threshold){
		log.debug("finding Relationnetfull instance with property: " + TRUSTOR_ID
				+ ", value: " + trustorId + ";"+TRUST_VALUE+",value: " +threshold);
		try {
			String queryString = "from Relationnetfull as model where model."
					+ TRUSTOR_ID + "= ? order by model.trustValue desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, trustorId);
			queryObject.setMaxResults(threshold);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public int deleteAll(){
		log.debug("delete all");
		try {
			String queryString = "delete from Relationnetfull";
			Query queryObject = getSession().createQuery(queryString);
		
			return queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}
