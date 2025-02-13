package softwareProject.persistence;

import lombok.extern.slf4j.Slf4j;
import softwareProject.business.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Get all the movies by genre
     * @param genre is the genre being searched
     * @return an arrayList of movies based on the genre
     */
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
                while(rs.next()){

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
     * Removes a movie from movies
     * @param movie movie that is going to be deleted
     * @return 1 if it has been removed, -1 if it has not been removed
     */
    @Override
    public int deletingAMovie(int movieId){
        int rowsAffected = 0;

        Connection conn = super.getConnection();

        try(PreparedStatement ps = conn.prepareStatement("DELETE from movies where movie_id = ?")){
            ps.setInt(1,movieId);
            rowsAffected = ps.executeUpdate();
        }// Add an extra exception handling block for where there is already an entry
        // with the primary key specified
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            // Set the rowsAffected to -1, this can be used as a flag for the display section
            rowsAffected = -1;
        }catch(SQLException e){
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;

    }

    @Override
    public int insertNewMovie(Movie newMovie){
        int rowsAffected = 0;

        Connection conn = super.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO movies (movie_name, genre_id, age_id, date_of_release, movie_length, movie_info, movie_image) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            // Set the values for the PreparedStatement
            ps.setString(1, newMovie.getMovie_name());
            ps.setInt(2, newMovie.getGenre_id());
            ps.setInt(3, newMovie.getAge_id());
            ps.setDate(4, Date.valueOf(newMovie.getDate_of_release()));
            ps.setTime(5, newMovie.getMovie_length());
            ps.setString(6, newMovie.getMovie_info());
            ps.setString(7, newMovie.getMovie_image());

            rowsAffected = ps.executeUpdate();
        }// Add an extra exception handling block for where there is already an entry
        // with the primary key specified
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            // Set the rowsAffected to -1, this can be used as a flag for the display section
            rowsAffected = -1;
        }catch(SQLException e){
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;

    }

    @Override
    public Movie searchForMovieByName(String name) {
        Movie movie = new Movie();
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM movies where movie_name = ?")) {
            ps.setString(1,name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movie = mapRow(rs);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.freeConnection(conn);
        }
        return movie;
    }


    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM movies")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movie movie = mapRow(rs);
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
                rs.getInt("age_id"),
                rs.getDate("date_of_release").toLocalDate(),
                rs.getTime("movie_length"),
                rs.getString("movie_info"),
                rs.getString("movie_image")
        );
        return m;
    }
}
