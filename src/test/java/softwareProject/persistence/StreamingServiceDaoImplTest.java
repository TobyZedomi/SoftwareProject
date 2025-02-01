package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.Movie;
import softwareProject.business.StreamingService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class StreamingServiceDaoImplTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    /**
     * Get all streaming services based on Movie ID
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllStreamingServicesByMovie() throws SQLException {

        System.out.println("Test to Get all streaming services for movie");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        StreamingServiceDao streamingServiceDao = new StreamingServiceDaoImpl(conn);

        ArrayList<StreamingService> expected = new ArrayList<>();
        expected.add(new StreamingService(28,15, "CrunchyRoll", "https://www.crunchyroll.com/", 5.99));
        expected.add(new StreamingService(29, 15, "HBO Max", "https://www.max.com/", 10.99 ));

        ArrayList<StreamingService> result = streamingServiceDao.getAllStreamingServicesByMovie(15);

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertStreamingServiceEquals(expected.get(i), result.get(i));
        }


    }


    /**
     * Checking if two streaming Services have the same value
     * @param expected is the streamingService being searched
     * @param result is the streamingService being searched
     */
    void assertStreamingServiceEquals(StreamingService expected, StreamingService result){
        assertEquals(expected.getStreaming_service_id(), result.getStreaming_service_id());
        assertEquals(expected.getMovie_id(), result.getMovie_id());
        assertEquals(expected.getStreaming_service_name(), result.getStreaming_service_name());
        assertEquals(expected.getStreaming_service_link(), result.getStreaming_service_link());
        assertEquals(expected.getCost(), result.getCost());
    }
}