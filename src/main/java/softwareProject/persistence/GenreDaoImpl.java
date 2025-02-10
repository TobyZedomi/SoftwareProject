package softwareProject.persistence;

import softwareProject.business.Genre;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GenreDaoImpl extends MySQLDao implements GenreDao{

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public GenreDaoImpl(String databaseName){
        super(databaseName);
    }

    public GenreDaoImpl(Connection conn){
        super(conn);
    }
    public GenreDaoImpl(){
        super();
    }

    /**
     * Get all the movies by genre
     * @return an arrayList of movies based on the genre
     */
    @Override
    public ArrayList<Genre> getAllGenres(){

        ArrayList<Genre> genres = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("Select * from genre")) {
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){

                    Genre g = mapRow(rs);
                    genres.add(g);
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
        return genres;
    }
    private Genre mapRow(ResultSet rs)throws SQLException {

        Genre g = new Genre(

                rs.getInt("genre_id"),
                rs.getString("genre_name")
        );
        return g;
    }

}
