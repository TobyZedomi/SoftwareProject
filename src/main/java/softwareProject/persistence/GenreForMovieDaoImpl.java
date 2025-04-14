package softwareProject.persistence;

import softwareProject.business.FavoriteList;
import softwareProject.business.GenreForMovie;

import java.sql.*;

public class GenreForMovieDaoImpl extends MySQLDao implements GenreForMovieDao{

    public GenreForMovieDaoImpl(String databaseName){
        super(databaseName);
    }

    public GenreForMovieDaoImpl(Connection conn){
        super(conn);
    }
    public GenreForMovieDaoImpl(){
        super();
    }


    @Override
    public int addGenreForMovie(GenreForMovie genreForMovie){
        // DATABASE CODE
        //
        // Create variable to hold the result of the operation
        // Remember, where you are NOT doing a select, you will only ever get
        // a number indicating how many things were changed/affected
        int rowsAffected = 0;


        Connection conn = super.getConnection();

        // TRY to prepare a statement from the connection
        // When you are parameterizing the update, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try(PreparedStatement ps = conn.prepareStatement("insert into genreForMovie values(?, ?, ?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, genreForMovie.getGenreForMovie_id());
            ps.setString(2, genreForMovie.getUsername());
            ps.setInt(3, genreForMovie.getMovie_id());
            ps.setInt(4, genreForMovie.getGenre_id());

            // Execute the update and store how many rows were affected/changed
            // when inserting, this number indicates if the row was
            // added to the database (>0 means it was added)
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


    /**
     * Search through each row in the favouriteList
     * @param rs is the rating query being searched
     * @return the rating information
     * @throws SQLException is username
     */

    private GenreForMovie mapRow(ResultSet rs)throws SQLException {

        GenreForMovie g = new GenreForMovie(
                rs.getInt("genreForMovie_id"),
                rs.getString("username"),
                rs.getInt("movieDb_id"),
                rs.getInt("genre_id")
        );
        return g;
    }

}
