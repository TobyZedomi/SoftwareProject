package softwareProject.persistence;

import softwareProject.business.Movie;
import softwareProject.business.MovieProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MovieProductDaoImpl extends MySQLDao implements MovieProductDao{

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public MovieProductDaoImpl(String databaseName){
        super(databaseName);
    }

    public MovieProductDaoImpl(Connection conn){
        super(conn);
    }
    public MovieProductDaoImpl(){
        super();
    }


    @Override
    public ArrayList<MovieProduct> getAllMovieProducts(){
        ArrayList<MovieProduct> movieProducts = new ArrayList<>();

        Connection conn = super.getConnection();

        try(PreparedStatement ps = conn.prepareStatement("SELECT * from movieProduct")){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){

                    MovieProduct m = mapRow(rs);
                    movieProducts.add(m);
                }
            }catch(SQLException e){
                System.out.println(LocalDateTime.now() + ": An SQLException  occurred while running the query" +
                        " or processing the result.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }catch(SQLException e){
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return movieProducts;
    }


    @Override
    public MovieProduct getMovieById(int movieId){

        MovieProduct movieProduct = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM movieProduct where movie_id = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, movieId);

            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if(rs.next()){

                    movieProduct = mapRow(rs);
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
        return movieProduct;
    }


    /**
     * Search through each row in the movie
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private MovieProduct mapRow(ResultSet rs)throws SQLException {

        MovieProduct m = new MovieProduct(

                rs.getInt("movie_id"),
                rs.getString("movie_name"),
                rs.getDate("date_of_release").toLocalDate(),
                rs.getTime("movie_length"),
                rs.getString("movie_info"),
                rs.getString("movie_image"),
                rs.getDouble("listPrice")
        );
        return m;
    }
}
