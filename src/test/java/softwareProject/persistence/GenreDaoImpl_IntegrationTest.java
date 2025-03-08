package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.CartItem;
import softwareProject.business.GenreTest;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GenreDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Get genre by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getGenreById() throws SQLException {

        System.out.println("Integration Test to get genre by id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        GenreDao genreDao = new GenreDaoImpl(conn);

        GenreTest genreTest = new GenreTest(12, "Adventure");

        GenreTest result = genreDao.getGenreById(genreTest.getId());

        //check if the billingAddresses are the same
        assertEquals(genreTest, result);
    }

    /**
     * Get genre by id but id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getGenreByIdButIdDoesntExist() throws SQLException {

        System.out.println("Integration Test to get genre by id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        GenreDao genreDao = new GenreDaoImpl(conn);

        GenreTest genreTest = new GenreTest(1222321, "Adventure");

        GenreTest result = genreDao.getGenreById(genreTest.getId());

        //check if the billingAddresses are the same
        assertNotEquals(genreTest, result);
    }

}