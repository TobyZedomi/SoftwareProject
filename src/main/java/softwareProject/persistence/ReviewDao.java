package softwareProject.persistence;

import softwareProject.business.Review;

import java.util.ArrayList;
import java.util.List;

public interface ReviewDao {
    int saveReview(Review review);
    int updateReview(Review review);
    int deleteReview(String username, int movieId);
    List<Review> getAllReviews();
    public ArrayList<Review> getReviewsByUsername(String username);
}
