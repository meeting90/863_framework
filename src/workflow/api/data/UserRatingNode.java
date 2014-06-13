package workflow.api.data;

import java.util.List;

import workflow.db.Rating;

public class UserRatingNode {
	private float fulRateValue;
	private List<Rating> ratings;

	
	public UserRatingNode() {
		super();
	}
	public UserRatingNode(float fulRateValue, List<Rating> ratings) {
		super();
		this.fulRateValue = fulRateValue;
		this.ratings=ratings;
		
	}
	public float getFulRateValue() {
		return fulRateValue;
	}
	public void setFulRateValue(float fulRateValue) {
		this.fulRateValue = fulRateValue;
	}
	public List<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	
	

}
