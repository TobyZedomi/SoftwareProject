package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.Genre;
import softwareProject.business.GenreTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreDaoImplMockTest {

    public GenreDaoImplMockTest() {

    }


    /**
     * Mock test to get genre by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getGenreById() throws SQLException {

        System.out.println("Mock Test to get Genre by id");

        Genre expected = new Genre(787, "Science Fiction");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * FROM genre where id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 1 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getInt("genre_id")).thenReturn(expected.getGenre_id());
        when(rs.getString("genre_name")).thenReturn(expected.getGenre_name());

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        GenreDao genreDao = new GenreDaoImpl(dbConn);
        GenreTest genre = genreDao.getGenreById(expected.getGenre_id());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(expected, genre);


    }
}