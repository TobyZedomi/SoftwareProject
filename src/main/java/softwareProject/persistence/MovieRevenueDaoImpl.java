package softwareProject.persistence;

import softwareProject.business.AuditPurchasedItems;
import softwareProject.business.MovieRevenue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MovieRevenueDaoImpl extends MySQLDao implements MovieRevenueDao {

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public MovieRevenueDaoImpl(String databaseName){
        super(databaseName);
    }

    public MovieRevenueDaoImpl(Connection conn){
        super(conn);
    }
    public MovieRevenueDaoImpl(){
        super();
    }
    @Override
    public ArrayList<MovieRevenue> getTotalMovieRevenue(){

        ArrayList<MovieRevenue> requests = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("CALL getMovieRevenueStats()")) {
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){

                    MovieRevenue m = mapRow(rs);
                    requests.add(m);
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
        return requests;
    }
    private MovieRevenue mapRow(ResultSet rs)throws SQLException {

        MovieRevenue ap = new MovieRevenue(
                rs.getString("movie_name"),
                rs.getDouble("totalRevenue")
        );
        return ap;
    }
}
