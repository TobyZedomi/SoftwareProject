package softwareProject.persistence;

import softwareProject.business.Review;
import java.util.List;

public interface ReviewDao {
    void saveReview(Review review);
    int updateReview(Review review);
    int deleteReview(String username, int movieId);
    List<Review> getAllReviews();
    List<Review> getReviewsByUser(String username); // Fetch reviews by username
}
