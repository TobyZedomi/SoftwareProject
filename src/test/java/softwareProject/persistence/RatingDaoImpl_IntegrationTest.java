package softwareProject.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import softwareProject.business.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RatingDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");
    private RatingDao ratingDao;

    @BeforeEach
    void setUp() throws SQLException {
        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ratingDao = new RatingDaoImpl(conn);

        PreparedStatement ps = null;
        try {
            // Ensure test user exists in the users table
            ps = conn.prepareStatement(
                    "INSERT IGNORE INTO users (username, displayName, email, password, dateOfBirth, isAdmin, createdAt, user_image) " +
                            "VALUES ('test_user', 'Test User', 'testuser@example.com', 'hashedpassword', '2000-01-01', false, NOW(), 'DefaultUserImage.jpg')"
            );
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        try {
            // Insert test reviews for multiple movies
            ps = conn.prepareStatement(
                    "INSERT IGNORE INTO reviews (username, movie_id, movie_title, content, rating) VALUES " +
                            "('test_user', 201, 'Test Movie 1', 'Great movie!', 5), " +
                            "('test_user', 202, 'Test Movie 2', 'Not bad', 4), " +
                            "('test_user', 203, 'Test Movie 3', 'Okay', 3), " +
                            "('test_user', 204, 'Test Movie 4', 'Bad', 2), " +
                            "('test_user', 205, 'Test Movie 5', 'Terrible', 1)"
            );
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Integration test to retrieve the highest-rated movies
     */
    @Test
    void testGetHighestRatedMovies() throws SQLException {
        System.out.println("Integration test to get highest-rated movies");

        List<Rating> highestRatedMovies = ratingDao.getHighestRatedMovies();

        assertNotNull(highestRatedMovies, "Highest rated movies list should not be null");
        assertTrue(highestRatedMovies.size() > 0, "There should be at least one highest-rated movie");

        if (highestRatedMovies.size() > 1) {
            Rating firstMovie = highestRatedMovies.get(0);
            Rating secondMovie = highestRatedMovies.get(1);

            double firstMovieRating = firstMovie.getAverageRating();
            double secondMovieRating = secondMovie.getAverageRating();

            if (firstMovieRating < secondMovieRating) {
                fail("Movies are not sorted in descending order by rating.");
            }
        }
    }

    /**
     * Integration test to retrieve the lowest-rated movies
     */
    @Test
    void testGetLowestRatedMovies() throws SQLException {
        System.out.println("Integration test to get lowest-rated movies");

        List<Rating> lowestRatedMovies = ratingDao.getLowestRatedMovies();

        assertNotNull(lowestRatedMovies, "Lowest rated movies list should not be null");
        assertTrue(lowestRatedMovies.size() > 0, "There should be at least one lowest-rated movie");

        if (lowestRatedMovies.size() > 1) {
            Rating firstMovie = lowestRatedMovies.get(0);
            Rating secondMovie = lowestRatedMovies.get(1);

            double firstMovieRating = firstMovie.getAverageRating();
            double secondMovieRating = secondMovie.getAverageRating();

            if (firstMovieRating > secondMovieRating) {
                fail("Movies are not sorted in ascending order by rating.");
            }
        }
    }
}
