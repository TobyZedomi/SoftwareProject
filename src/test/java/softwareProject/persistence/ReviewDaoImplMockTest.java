package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import softwareProject.business.Review;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewDaoImplMockTest {

    public ReviewDaoImplMockTest() {
    }

    /**
     * Mock test to save a Review
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void saveReview() throws SQLException {
        System.out.println("Mock Test: Save Review");

        Review review = new Review("john_doe", 101, "Inception", "Amazing movie!", 5, LocalDateTime.now());

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(dbConn.prepareStatement(
                "INSERT INTO reviews (username, movie_id, movie_title, content, rating, createdAt) VALUES (?, ?, ?, ?, ?, ?)")
        ).thenReturn(ps);

        when(ps.executeUpdate()).thenReturn(1);

        // Execute
        ReviewDao reviewDao = new ReviewDaoImpl(dbConn);
        int result = reviewDao.saveReview(review);

        assertEquals(1, result, "Review insertion failed!");
    }

    /**
     * Mock test to update a Review
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void updateReview() throws SQLException {
        System.out.println("Mock Test: Update Review");

        Review updatedReview = new Review("john_doe", 101, "Inception", "Updated: Even better on second watch!", 5, LocalDateTime.now());

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(dbConn.prepareStatement(
                "UPDATE reviews SET content = ?, rating = ?, createdAt = ? WHERE username = ? AND movie_id = ?")
        ).thenReturn(ps);

        when(ps.executeUpdate()).thenReturn(1);

        // Execute
        ReviewDao reviewDao = new ReviewDaoImpl(dbConn);
        int result = reviewDao.updateReview(updatedReview);

        assertEquals(1, result, "Review update failed!");
    }

    /**
     * Mock test to delete a Review
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void deleteReview() throws SQLException {
        System.out.println("Mock Test: Delete Review");

        String username = "jane_smith";
        int movieId = 102;

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(dbConn.prepareStatement("DELETE FROM reviews WHERE username = ? AND movie_id = ?")).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        // Execute
        ReviewDao reviewDao = new ReviewDaoImpl(dbConn);
        int result = reviewDao.deleteReview(username, movieId);

        assertEquals(1, result, "Review deletion failed!");
    }

    /**
     * Mock test to get all Reviews
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void getAllReviews() throws SQLException {
        System.out.println("Mock Test: Get All Reviews");

        List<Review> expectedReviews = new ArrayList<>();
        expectedReviews.add(new Review("john_doe", 101, "Inception", "Mind-blowing!", 5, LocalDateTime.now()));
        expectedReviews.add(new Review("jane_smith", 102, "The Dark Knight", "Amazing movie!", 5, LocalDateTime.now()));

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(dbConn.prepareStatement("SELECT * FROM reviews ORDER BY createdAt DESC")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("username")).thenReturn(expectedReviews.get(0).getUsername(), expectedReviews.get(1).getUsername());
        when(rs.getInt("movie_id")).thenReturn(expectedReviews.get(0).getMovieId(), expectedReviews.get(1).getMovieId());
        when(rs.getString("movie_title")).thenReturn(expectedReviews.get(0).getMovieTitle(), expectedReviews.get(1).getMovieTitle());
        when(rs.getString("content")).thenReturn(expectedReviews.get(0).getContent(), expectedReviews.get(1).getContent());
        when(rs.getInt("rating")).thenReturn(expectedReviews.get(0).getRating(), expectedReviews.get(1).getRating());
        when(rs.getTimestamp("createdAt")).thenReturn(
                Timestamp.valueOf(expectedReviews.get(0).getCreatedAt()),
                Timestamp.valueOf(expectedReviews.get(1).getCreatedAt())
        );

        // Execute
        ReviewDao reviewDao = new ReviewDaoImpl(dbConn);
        List<Review> result = reviewDao.getAllReviews();

        assertEquals(expectedReviews.size(), result.size());
        assertEquals(expectedReviews, result);
    }

    /**
     * Mock test to get Reviews by Username
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void getReviewsByUsername() throws SQLException {
        System.out.println("Mock Test: Get Reviews By Username");

        String username = "michael_j";
        List<Review> expectedReviews = new ArrayList<>();
        expectedReviews.add(new Review(username, 103, "Interstellar", "Great visuals!", 4, LocalDateTime.now()));

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Ensure prepareStatement() returns the mock PreparedStatement
        when(dbConn.prepareStatement("SELECT * FROM reviews WHERE username = ?")).thenReturn(ps);

        // Ensure executeQuery() returns the mock ResultSet
        when(ps.executeQuery()).thenReturn(rs);


        when(rs.next()).thenReturn(true, false);
        when(rs.getString("username")).thenReturn(expectedReviews.get(0).getUsername());
        when(rs.getInt("movie_id")).thenReturn(expectedReviews.get(0).getMovieId());
        when(rs.getString("movie_title")).thenReturn(expectedReviews.get(0).getMovieTitle());
        when(rs.getString("content")).thenReturn(expectedReviews.get(0).getContent());
        when(rs.getInt("rating")).thenReturn(expectedReviews.get(0).getRating());
        when(rs.getTimestamp("createdAt")).thenReturn(Timestamp.valueOf(expectedReviews.get(0).getCreatedAt()));

        // Execute the test
        ReviewDao reviewDao = new ReviewDaoImpl(dbConn);
        List<Review> result = reviewDao.getReviewsByUsername(username);

        // Validate the result
        assertEquals(expectedReviews.size(), result.size());
        assertEquals(expectedReviews.get(0), result.get(0));
    }

}
