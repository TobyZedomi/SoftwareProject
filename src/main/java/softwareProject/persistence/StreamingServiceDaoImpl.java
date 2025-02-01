package softwareProject.persistence;

import softwareProject.business.Movie;
import softwareProject.business.StreamingService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StreamingServiceDaoImpl  extends MySQLDao implements StreamingServiceDao{

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public StreamingServiceDaoImpl(String databaseName){
        super(databaseName);
    }

    public StreamingServiceDaoImpl(Connection conn){
        super(conn);
    }
    public StreamingServiceDaoImpl(){
        super();
    }

    /**
     * Get all the streaming services based on the movie id
     * @param movieID is the movie being searched
     * @return list of all streaming services
     */
    @Override
    public ArrayList<StreamingService> getAllStreamingServicesByMovie(int movieID){

        ArrayList<StreamingService> streamingServices = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("Select * from streamingService where movie_id = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, movieID);


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){

                    StreamingService s = mapRow(rs);
                    streamingServices.add(s);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return streamingServices;
    }


    /**
     * Search through each row in the streaming service
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private StreamingService mapRow(ResultSet rs)throws SQLException {

        StreamingService s = new StreamingService(

                rs.getInt("streaming_service_id"),
                rs.getInt("movie_id"),
                rs.getString("streaming_service_name"),
                rs.getString("streaming_service_link"),
                rs.getDouble("cost")
        );
        return s;
    }


}
