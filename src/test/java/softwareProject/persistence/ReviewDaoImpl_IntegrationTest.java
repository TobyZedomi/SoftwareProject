package softwareProject.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import softwareProject.business.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReviewDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");
    private ReviewDao reviewDao;

    @BeforeEach
    void setUp() throws SQLException {
        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        reviewDao = new ReviewDaoImpl(conn);

        // This makes sure test_user exists in the users table
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO users (username, displayName, email, password, dateOfBirth, isAdmin, createdAt, user_image) " +
                        "VALUES ('test_user', 'Test User', 'testuser@example.com', 'hashedpassword', '2000-01-01', false, NOW(), 'DefaultUserImage.jpg')"
        )) {
            ps.executeUpdate();
        }
    }


    @Test
    void testSaveReview() throws SQLException {
        System.out.println("Integration test to save a Review");

        Review review = new Review("test_user", 111, "Test Movie", "Great Test Review!", 5, LocalDateTime.now());
        int result = reviewDao.saveReview(review);

        assertEquals(1, result, "Review insertion failed!");
    }

    @Test
    void testUpdateReview() throws SQLException {
        System.out.println("Integration test to update a Review");

        Review updatedReview = new Review("john_doe", 101, "Inception", "Updated: Even better on second watch!", 5, LocalDateTime.now());
        int result = reviewDao.updateReview(updatedReview);

        assertEquals(1, result, "Review update failed - Review might not exist in the DB");
    }

    @Test
    void testDeleteReview() throws SQLException {
        System.out.println("Integration test to delete a Review");

        int result = reviewDao.deleteReview("jane_smith", 102);

        assertEquals(1, result, "Review deletion failed");
    }

    @Test
    void testGetAllReviews() throws SQLException {
        System.out.println("Integration test to get all Reviews");

        List<Review> reviews = reviewDao.getAllReviews();

        assertNotNull(reviews);
        assertTrue(reviews.size() > 0, "No reviews found in database ");
    }

    @Test
    void testGetReviewsByUsername() throws SQLException {
        System.out.println("Integration test to get Reviews by Username");

        List<Review> reviews = reviewDao.getReviewsByUsername("michael_j");

        assertNotNull(reviews);
        assertTrue(reviews.size() > 0, "User has no reviews!");
    }
}
