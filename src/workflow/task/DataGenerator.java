package workflow.task;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import workflow.db.Rating;
import workflow.db.Ratingfull;
import workflow.db.RatingfullDAO;
import workflow.db.Relationnet;
import workflow.db.Relationnetfull;
import workflow.db.RelationnetfullDAO;


/*
 * this class is to generate the relation data and rating data
 */
public class DataGenerator 
{
	private List<Long> userIds;
	private List<Long> wsIds;
	private List<Rating> ratingOriginal;
	private List<Relationnet> relationOriginal;
	private HashMap<Long, Integer> userIdMap;
	private HashMap<Long, Integer> wsIdMap;
	private double[][] oriRatingArray;
	private double[][] oriRelationArray;
	
	public DataGenerator(List<Long> userIds, List<Long> wsIds, List<Rating> ratingOriginal, List<Relationnet> relationOriginal)
	{
		this.userIds = userIds;
		this.wsIds = wsIds;
		this.ratingOriginal = ratingOriginal;
		this.relationOriginal = relationOriginal;
		this.initData();
	}
	
	private void initData()
	{
		int userSize = this.userIds.size();
		int wsSize = this.wsIds.size();
		
		this.userIdMap = new HashMap<Long, Integer>(userSize);
		this.wsIdMap = new HashMap<Long, Integer>(wsSize);
		
		for (int index=0; index<userSize; index++)
			this.userIdMap.put(this.userIds.get(index), index);
		
		for (int index=0; index<wsSize; index++)
			this.wsIdMap.put(this.wsIds.get(index), index);
		
		////////////////init relation/////////////////
		this.oriRelationArray = new double[userSize][userSize];
		for (int index1=0; index1<userSize; index1++)
			for (int index2=0; index2<userSize; index2++)
				this.oriRelationArray[index1][index2] = 0;
		
		for (int index=0; index<this.relationOriginal.size(); index++)
		{
			Relationnet currentRelation = this.relationOriginal.get(index);
			Long trustorId = currentRelation.getTrustorId();
			Long trusteeId = currentRelation.getTrusteeId();
			Float trustValue = currentRelation.getTrustValue();
			this.oriRelationArray[this.userIdMap.get(trustorId)][this.userIdMap.get(trusteeId)] = trustValue;
		}
		
		/////////////////init rating//////////////////
		this.oriRatingArray = new double[userSize][wsSize];
		for (int index1=0; index1<userSize; index1++)
			for (int index2=0; index2<wsSize; index2++)
				this.oriRatingArray[index1][index2] = 0;
		
		for (int index=0; index<this.ratingOriginal.size(); index++)
		{
			Rating currentRating = this.ratingOriginal.get(index);
			Long userId = currentRating.getUserId();
			Long wsId = currentRating.getWsId();
			Float rateValue = currentRating.getRateValue();
			this.oriRatingArray[this.userIdMap.get(userId)][this.wsIdMap.get(wsId)] = rateValue;
		}
	
	}
	
	public void updateData()
	{
		/////////////get full relation and rating///////////
		NmfTrust nmfTrust = new NmfTrust(this.oriRelationArray);
		double[][] fullRelationArray = nmfTrust.getFullTrust();
		
		RSTE rste = new RSTE(this.oriRatingArray, fullRelationArray);
		double[][] fullRatingArray = rste.getFullRating();
		
		/////////import into database///////////////////////
		this.saveFullRelation(fullRelationArray);
		this.saveFullRating(fullRatingArray);
		
	}
	
	private void saveFullRelation(double[][] fullRelationArray)
	{
		int userSize = fullRelationArray.length;
		
		RelationnetfullDAO rnfDAO=new RelationnetfullDAO();
		Session session = rnfDAO.getSession();
		Transaction tx = null;
		
		
		long count=0;
		try{
			tx = session.beginTransaction();	// open session 
			rnfDAO.deleteAll();
			tx.commit();
			tx=session.beginTransaction();
			for (int index1=0; index1<userSize; index1++){
				for (int index2=0; index2<userSize; index2++)
				{
					Long trustorId = this.userIds.get(index1);
					Long trusteeId = this.userIds.get(index2);
					Float trustValue = (float) fullRelationArray[index1][index2];
					
					if(trustValue!=0){
						Relationnetfull rnf=new Relationnetfull();
						rnf.setNid(count);
						rnf.setTrustorId(trustorId);
						rnf.setTrusteeId(trusteeId);
						rnf.setTrustValue(trustValue);
						count++;
						session.save(rnf);
					}
					
				}
			}
		
			tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			
		     session.close( );	// close session
		}
		
	}
	
	private void saveFullRating(double[][] fullRatingArray)
	{
		int userSize = fullRatingArray.length;
		int wsSize = fullRatingArray[0].length;
		
		RatingfullDAO rfDAO=new RatingfullDAO();
		Session session = rfDAO.getSession();
		Transaction tx = null;
		
		
		long count=0;
	
		try{
			tx = session.beginTransaction();	// open session 
			rfDAO.deleteAll();
			tx.commit();
			tx=session.beginTransaction();
			for (int index1=0; index1<userSize; index1++){
				for (int index2=0; index2<wsSize; index2++)
				{
					Long userId = this.userIds.get(index1);
					Long wsId = this.wsIds.get(index2);
					Float rateValue = (float) fullRatingArray[index1][index2];
					if(rateValue!=0 ){
						
						Ratingfull rf=new Ratingfull();
						rf.setRid(count);
						rf.setUserId(userId);
						rf.setWsId(wsId);
						rf.setRateValue(rateValue);
						count++;
						session.save(rf);
					}	
					
				}
			}
		
			
			tx.commit();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
			
		     session.close( );	// close session
		}
		
	}
	
}
