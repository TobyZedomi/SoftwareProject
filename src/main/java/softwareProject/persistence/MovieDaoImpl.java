package softwareProject.persistence;

import lombok.extern.slf4j.Slf4j;
import softwareProject.business.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
public class MovieDaoImpl extends MySQLDao implements MovieDao {

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public MovieDaoImpl(String databaseName){
        super(databaseName);
    }

    public MovieDaoImpl(Connection conn){
        super(conn);
    }
    public MovieDaoImpl(){
        super();
    }

    @Override
    public ArrayList<Movie> findMovieByGenre(int genre){

        ArrayList<Movie> movies = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("Select * from movies where genre_id = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, genre);


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if(rs.next()){

                    Movie m = mapRow(rs);
                    movies.add(m);
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
        return movies;
    }


    /**
     * Search through each row in the movie
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private Movie mapRow(ResultSet rs)throws SQLException {

        Movie m = new Movie(

                rs.getInt("movie_id"),
                rs.getString("movie_name"),
                rs.getInt("genre_id"),
                rs.getTimestamp("date_of_release").toLocalDateTime(),
                rs.getTime("movie_length"),
                rs.getString("movie_info"),
                rs.getString("movie_image")
        );
        return m;
    }
}
