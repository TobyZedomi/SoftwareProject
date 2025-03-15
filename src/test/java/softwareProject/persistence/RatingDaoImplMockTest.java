package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingDaoImplMockTest {

    public RatingDaoImplMockTest() {
    }

    /**
     * Mock test to get highest-rated movies
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void getHighestRatedMovies() throws SQLException {
        System.out.println("Mock Test: Get Highest Rated Movies");

        List<Rating> expectedRatings = new ArrayList<>();
        expectedRatings.add(new Rating(201, "Test Movie 1", 5.0, 10));
        expectedRatings.add(new Rating(202, "Test Movie 2", 4.5, 8));

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Mock the SQL query
        when(dbConn.prepareStatement(
                "SELECT movie_id, movie_title, AVG(rating) AS avg_rating, COUNT(*) AS total_reviews " +
                        "FROM reviews GROUP BY movie_id, movie_title ORDER BY avg_rating DESC LIMIT 10"
        )).thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);


        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("movie_id")).thenReturn(expectedRatings.get(0).getMovieId(), expectedRatings.get(1).getMovieId());
        when(rs.getString("movie_title")).thenReturn(expectedRatings.get(0).getMovieTitle(), expectedRatings.get(1).getMovieTitle());
        when(rs.getDouble("avg_rating")).thenReturn(expectedRatings.get(0).getAverageRating(), expectedRatings.get(1).getAverageRating());
        when(rs.getInt("total_reviews")).thenReturn(expectedRatings.get(0).getTotalReviews(), expectedRatings.get(1).getTotalReviews());

        // Execute test
        RatingDao ratingDao = new RatingDaoImpl(dbConn);
        List<Rating> result = ratingDao.getHighestRatedMovies();

        assertEquals(expectedRatings.size(), result.size(), "Highest rated movies count mismatch");
        assertEquals(expectedRatings, result, "Highest rated movies data mismatch");
    }

    /**
     * Mock test to get lowest-rated movies
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void getLowestRatedMovies() throws SQLException {
        System.out.println("Mock Test: Get Lowest Rated Movies");

        List<Rating> expectedRatings = new ArrayList<>();
        expectedRatings.add(new Rating(301, "Test Movie 3", 1.5, 5));
        expectedRatings.add(new Rating(302, "Test Movie 4", 2.0, 3));

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Mock the SQL query
        when(dbConn.prepareStatement(
                "SELECT movie_id, movie_title, AVG(rating) AS avg_rating, COUNT(*) AS total_reviews " +
                        "FROM reviews GROUP BY movie_id, movie_title ORDER BY avg_rating ASC LIMIT 10"
        )).thenReturn(ps);

        when(ps.executeQuery()).thenReturn(rs);


        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("movie_id")).thenReturn(expectedRatings.get(0).getMovieId(), expectedRatings.get(1).getMovieId());
        when(rs.getString("movie_title")).thenReturn(expectedRatings.get(0).getMovieTitle(), expectedRatings.get(1).getMovieTitle());
        when(rs.getDouble("avg_rating")).thenReturn(expectedRatings.get(0).getAverageRating(), expectedRatings.get(1).getAverageRating());
        when(rs.getInt("total_reviews")).thenReturn(expectedRatings.get(0).getTotalReviews(), expectedRatings.get(1).getTotalReviews());

        // Execute test
        RatingDao ratingDao = new RatingDaoImpl(dbConn);
        List<Rating> result = ratingDao.getLowestRatedMovies();

        assertEquals(expectedRatings.size(), result.size(), "Lowest rated movies count mismatch");
        assertEquals(expectedRatings, result, "Lowest rated movies data mismatch");
    }
}
