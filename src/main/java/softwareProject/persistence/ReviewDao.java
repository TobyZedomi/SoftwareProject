package softwareProject.persistence;

import softwareProject.business.Review;
import java.util.List;

public interface ReviewDao {
    void saveReview(Review review);
    List<Review> getAllReviews();
}
