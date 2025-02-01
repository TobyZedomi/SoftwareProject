package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import softwareProject.business.Movie;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieDaoImplTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    /**
     * Test to get all movies by certain genre
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findMovieByGenre() throws SQLException {
        System.out.println("Test to get all movies by certain genre");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieDao movieDao = new MovieDaoImpl(conn);

        Time t1 = Time.valueOf("02:07:00");
        Time t2 = Time.valueOf("02:32:00");
        Time t3 = Time.valueOf("02:27:00");
        ArrayList<Movie> expected = new ArrayList<>();
        expected.add(new Movie(1, "Spider-Man-2", 1, 3, LocalDateTime.of(2004,07,16,0,0,0),t1, "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman2.jpg" ));
        expected.add(new Movie(2, "The Dark Knight",1,3,LocalDateTime.of(2008,07,28,0,0,0), t2, "Batman must face Joker as he wants to destroy and control Gotham City. Batman struggles to face joker before its to late", "batman.jpg"));
        expected.add(new Movie(3, "Inception", 1, 2, LocalDateTime.of(2010,07,16,0,0,0), t3, "Cobb enters the dreams of people to steal information and wants to achieve an impossible task", "inception.jpg"));

        ArrayList<Movie> result = movieDao.findMovieByGenre(1);

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertMovieEquals(expected.get(i), result.get(i));
        }
    }


    void assertMovieEquals(Movie expected, Movie result){
        assertEquals(expected.getMovie_id(), result.getMovie_id());
        assertEquals(expected.getMovie_name(), result.getMovie_name());
        assertEquals(expected.getGenre_id(), result.getGenre_id());
        assertEquals(expected.getAge_id(), result.getAge_id());
        assertEquals(expected.getDate_of_release(), result.getDate_of_release());
        assertEquals(expected.getMovie_length(), result.getMovie_length());
        assertEquals(expected.getMovie_info(), result.getMovie_info());
        assertEquals(expected.getMovie_image(), result.getMovie_image());
    }
}