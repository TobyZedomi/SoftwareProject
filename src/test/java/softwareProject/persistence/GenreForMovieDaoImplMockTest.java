package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.FavoriteList;
import softwareProject.business.GenreForMovie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreForMovieDaoImplMockTest {


    /**
     * Mock test to add genre for movies mock test
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addGenreForMovie() throws SQLException {

        System.out.println("Adding GenreForMovies mock Test");

        GenreForMovie tester = new GenreForMovie(4, "Yellow", 86331, 45 );

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into genreForMovie values(?, ?, ?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        GenreForMovieDao genreForMovieDao = new GenreForMovieDaoImpl(dbConn);
        int result = genreForMovieDao.addGenreForMovie(tester);

        System.out.println(result);


        assertEquals(expected, result);

    }

    @Test
    void getAllGenreForMovieByUsername() throws SQLException {

        System.out.println("Mock testing to get FavouriteList by username");

        GenreForMovie g1 = new GenreForMovie(4, "Yellow", 86331, 45 );
        GenreForMovie g2 = new GenreForMovie(5, "Yellow", 8633134, 45 );

        ArrayList<GenreForMovie> expectedResults = new ArrayList<>();
        expectedResults.add(g1);
        expectedResults.add(g2);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("SELECT * FROM genreForMovie where username = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultset, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getInt("genreForMovie_id")).thenReturn(g1.getGenreForMovie_id(), g2.getGenreForMovie_id());
        when(rs.getString("username")).thenReturn(g1.getUsername(), g2.getUsername());
        when(rs.getInt("movieDb_id")).thenReturn(g1.getMovie_id(), g2.getMovie_id());
        when(rs.getInt("genre_id")).thenReturn(g1.getGenre_id(), g2.getGenre_id());


        int numInGenreForMoviesTable = 2;

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        GenreForMovieDao genreForMovieDao = new GenreForMovieDaoImpl(dbConn);
        ArrayList<GenreForMovie> result = genreForMovieDao.getAllGenreForMovieByUsername("Yellow");
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numInGenreForMoviesTable, result.size());

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);

    }
}