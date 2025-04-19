package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.FavoriteList;
import softwareProject.business.GenreForMovie;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GenreForMovieDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Integration test to add to genreForMovie table
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addGenreForMovie() throws SQLException {

        System.out.println("Integration test to add a genreForMovie");

        GenreForMovie tester = new GenreForMovie(4, "Toby", 86331, 45 );
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        GenreForMovieDaoImpl genreForMovieDao = new GenreForMovieDaoImpl(conn);

        int result = genreForMovieDao.addGenreForMovie(tester);
        assertEquals(correctResult, result);
    }


    /**
     * Integration test to add to genreForMovie table when username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addGenreForMovieWhenUsernameDoesntExist() throws SQLException {

        System.out.println("Integration test to add a genreForMovie when username doesnt exist");

        GenreForMovie tester = new GenreForMovie(4, "Yellow", 86331, 45 );
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        GenreForMovieDaoImpl genreForMovieDao = new GenreForMovieDaoImpl(conn);

        int result = genreForMovieDao.addGenreForMovie(tester);
        assertEquals(correctResult, result);
    }


    /**
     * Integration test but adding genreForMovies but its null
     *
     * @throws SQLException if something goes wrong with database
     */
    @Test
    void addGenreForMovieButItsNull() throws SQLException {

        System.out.println("Add to genreForMovie but its null");

        GenreForMovie tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        GenreForMovieDaoImpl genreForMovieDao = new GenreForMovieDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            genreForMovieDao.addGenreForMovie(tester);
        });
    }

    /**
     * Test to get all genreForMovie by username
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllGenreForMovieByUsername() throws SQLException {

        System.out.println("Test to Get all genreForMovie by username");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        GenreForMovieDaoImpl genreForMovieDao = new GenreForMovieDaoImpl(conn);

        ArrayList<GenreForMovie> expected = new ArrayList<>();
        expected.add(new GenreForMovie(3, "Toby", 1373723,28));

        ArrayList<GenreForMovie> result = genreForMovieDao.getAllGenreForMovieByUsername("Toby");

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertEquals(expected.get(i), result.get(i));
        }

    }


    /**
     * Test to get all genreForMovie by username but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllGenreForMovieByUsernameButUsernameDoesntExist() throws SQLException {

        System.out.println("Test to Get all genreForMovie by username but username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        GenreForMovieDaoImpl genreForMovieDao = new GenreForMovieDaoImpl(conn);

        ArrayList<GenreForMovie> expected = new ArrayList<>();
        expected.add(new GenreForMovie(3, "Yellow", 1373723,28));

        ArrayList<GenreForMovie> result = genreForMovieDao.getAllGenreForMovieByUsername("Toby");

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertEquals(expected.get(i), result.get(i));
        }

    }
}