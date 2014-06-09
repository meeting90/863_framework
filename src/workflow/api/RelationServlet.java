package workflow.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import workflow.api.data.RelationGraphEdge;
import workflow.api.data.RelationGraphNode;
import workflow.api.data.UserRelationNode;
import workflow.db.Rating;
import workflow.db.RatingDAO;
import workflow.db.Relationnet;
import workflow.db.RelationnetDAO;
import workflow.db.Relationnetfull;
import workflow.db.RelationnetfullDAO;
import workflow.db.UserDAO;
import db.User;
@SuppressWarnings("unchecked")
public class RelationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public RelationServlet() {
        super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		String query=req.getParameter("query");
		Long loginId=(Long)req.getSession().getAttribute("userid");
//		if(loginId==null){
//			resp.getWriter().append(ServletConstants.SESSION_TIMEOUT_ERROR);
//			return;
//		}
		if(query.equals("getAllRelation")){// get relation graph from database
			resp.getWriter().append(getRelation());
		}else if(query.equals("getTrustValue")){
			long trustorId=Long.parseLong(req.getParameter("trustorId"));
			long trusteeId=Long.parseLong(req.getParameter("trusteeId"));
			resp.getWriter().append(getTrustValue(trustorId,trusteeId));
		}
		else if(query.equals("getTrustRelation")){//get realtion where user.trustvalue > threshold
			long uid=Long.parseLong(req.getParameter("uid"));
			float threshold=Float.parseFloat(req.getParameter("threshold"));
			resp.getWriter().append(getTrustRelation(uid,threshold));
		}else if(query.equals("addRelation")){
			long trustorId=Long.parseLong(req.getParameter("trustorId"));
			long trusteeId=Long.parseLong(req.getParameter("trusteeId"));
			resp.getWriter().append(addRelation(trustorId,trusteeId));
		}else if(query.equals("removeRelation")){
			long trustorId=Long.parseLong(req.getParameter("trustorId"));
			long trusteeId=Long.parseLong(req.getParameter("trusteeId"));
			resp.getWriter().append(removeRelation(trustorId,trusteeId));
		}
	
		else{
			resp.getWriter().append(ServletConstants.UNKOWN_REQUEST_ERROR);
		}
		 resp.getWriter().flush();
		 resp.getWriter().close();
	}
	
	

	private String getTrustValue(long trustorId, long trusteeId) {
		RelationnetDAO rDAO=new RelationnetDAO();
		List<Relationnet> rn=rDAO.findByTrustorAndTrusteeId(trustorId, trusteeId);
		if(rn.size()!=0)
			return rn.get(0).getTrustValue()+"";
		else 
			return 0+"";
	}
	private String removeRelation(long uid, long trusteeId) {
		RelationnetDAO rDAO=new RelationnetDAO();	
		Session session=rDAO.getSession();
	
			List<Relationnet> rn=rDAO.findByTrustorAndTrusteeId(uid, trusteeId);
			if(rn.size()!=0){
				Relationnet item=rn.get(0);
				
				Transaction tx=null;
				try{
					tx=session.beginTransaction();
					session.delete(item);
					session.flush();
					tx.commit();
					UserDAO userDAO=new UserDAO();
					List<Relationnet> rns= rDAO.findByTrustorId(uid);
					
					List<List<?>> userNames=new ArrayList<List<?>>();
					for(Relationnet temp:rns){
						User user=userDAO.findById(temp.getTrusteeId());
						List<Object> u=new ArrayList<Object>();
						u.add(user.getId());
						u.add(user.getUsername());
						userNames.add(u);
					}
					
					JSONArray json=new JSONArray(userNames);
				
					//System.out.println(json.toString());
					return json.toString();
				}catch(Exception e){
					return ServletConstants.ERROR_MSG;
				}finally{
				     session.close( );	// 关闭session
				}
			}
			return ServletConstants.ERROR_MSG;
		
		
	}
	private String addRelation(long uid, long trusteeId) {
		RelationnetDAO rDAO=new RelationnetDAO();	
		Session session=rDAO.getSession();
		
			List<Relationnet> rn=rDAO.findByTrustorAndTrusteeId(uid, trusteeId);
			if(rn.size()==0){
				Relationnet item=new Relationnet();
				item.setTrustorId(uid);
				item.setTrusteeId(trusteeId);
				item.setTrustValue(1.0f);
				
				Transaction tx=null;
				try{
					tx=session.beginTransaction();
					session.save(item);
					session.flush();
					tx.commit();
					
					
					UserDAO userDAO=new UserDAO();
					List<Relationnet> rns= rDAO.findByTrustorId(uid);
					
					List<List<?>> userNames=new ArrayList<List<?>>();
					for(Relationnet temp:rns){
						User user=userDAO.findById(temp.getTrusteeId());
						List<Object> u=new ArrayList<Object>();
						u.add(user.getId());
						u.add(user.getUsername());
						userNames.add(u);
					}
					
					JSONArray json=new JSONArray(userNames);
				
					//System.out.println(json.toString());
					return json.toString();
					
				}catch(Exception e){
					return ServletConstants.ERROR_MSG;
				   
				}finally{
				     session.close( );	// 关闭session
				}
			}
			return ServletConstants.ERROR_MSG;
		
		
	}

	private String getTrustRelation(long uid, float threshold) {
		RelationnetfullDAO rnfDAO=new RelationnetfullDAO();
		RelationnetDAO rnDAO=new RelationnetDAO();
		List<Relationnet> rns=rnDAO.findByTrustorId(uid);
		
		List<Relationnetfull> rnfs=rnfDAO.findByTrustorAndThreshold(uid, threshold);
		
	
		Map<Long,Float> trustee2Rate=new HashMap<Long,Float>();
		Map<Long,Float> trustee2Ratefull=new HashMap<Long,Float>();
		Set<Long> trusteeSet=new HashSet<Long>();
		List<UserRelationNode> result=new ArrayList<UserRelationNode>();
		for(Relationnetfull rnf: rnfs){
			trusteeSet.add(rnf.getTrusteeId());
			trustee2Ratefull.put(rnf.getTrusteeId(),rnf.getTrustValue());
		}
		for(Relationnet rn:rns){
			trusteeSet.add(rn.getTrusteeId());
			trustee2Rate.put(rn.getTrusteeId(), rn.getTrustValue());
		}
		for(long trusteeId: trusteeSet){
			UserRelationNode node=new UserRelationNode();
			node.setTrustee(trusteeId);
			if(trustee2Rate.containsKey(trusteeId))
				node.setRate((float)trustee2Rate.get(trusteeId));
			else
				node.setRate(0);
			if(trustee2Ratefull.containsKey(trusteeId))
				node.setFullrate((float)trustee2Ratefull.get(trusteeId));
			else
				node.setFullrate(node.getRate());
			result.add(node);
		}
		List<Relationnet> otherRelation=new ArrayList<Relationnet>();
		for(long trusteeId : trusteeSet){
			
				RelationnetDAO relationnetDAO=new RelationnetDAO();
				List<Relationnet> rns2=relationnetDAO.findByTrustorId(trusteeId);
				for(Relationnet rn2: rns2){
					if(trusteeSet.contains(rn2.getTrusteeId()))
						otherRelation.add(rn2);
				}
			
		}
		JSONObject json=new JSONObject();
		try {
			json.put("relation", result );
			json.put("other", otherRelation);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
	
		return json.toString();
	}

	private String getRelation() {
		RelationnetDAO rnDAO=new RelationnetDAO();
		List<Relationnet> relation=rnDAO.findAll();
		RatingDAO rDAO=new RatingDAO();
		List<Rating> rating=rDAO.findAll();
		
		Set<Long> userSet=new HashSet<Long>(); 
		Set<Long> wsSet=new HashSet<Long>();
		List<RelationGraphNode> nodes=new ArrayList<RelationGraphNode>();
		List<RelationGraphEdge> edges=new ArrayList<RelationGraphEdge>();
		Map<Long,Long> user2Id=new HashMap<Long,Long>();
		Map<Long,Long> ws2Id=new HashMap<Long,Long>();
		for(Relationnet r: relation){
			userSet.add(r.getTrustorId());
			userSet.add(r.getTrusteeId());
		}
		for(Rating r: rating){
			userSet.add(r.getUserId());
			wsSet.add(r.getWsId());
		}
		long count=0;
		for(long uid: userSet){
			RelationGraphNode rgn=new RelationGraphNode();
			rgn.setId(count);
			rgn.setName(uid);
			rgn.setUser(true);
			nodes.add(rgn);
			user2Id.put(uid,  count);
			count++;
		}
		for(long wsid: wsSet){
			RelationGraphNode rgn=new RelationGraphNode();
			rgn.setId(count);
			rgn.setName(wsid);
			rgn.setUser(false);
			nodes.add(rgn);
			ws2Id.put(wsid, count);
			count++;
			
		}
		for(Relationnet r: relation){
			RelationGraphEdge edge=new RelationGraphEdge();
			edge.setSource(user2Id.get(r.getTrustorId()));
			edge.setTarget(user2Id.get(r.getTrusteeId()));
			edge.setWeight(r.getTrustValue());
			edges.add(edge);
		}
		for(Rating r: rating){
			RelationGraphEdge edge=new RelationGraphEdge();
			edge.setSource(user2Id.get(r.getUserId()));
			edge.setTarget(ws2Id.get(r.getWsId()));
			edge.setWeight(r.getRateValue());
			edges.add(edge);
		}
		
		JSONObject json=new JSONObject();
		try {
			json.put("nodes", nodes);
			json.put("edges", edges);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
		
	}
	
	

}
