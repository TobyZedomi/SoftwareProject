package softwareProject.persistence;

import softwareProject.business.BillingAddress;
import softwareProject.business.CartItem;
import softwareProject.business.FavoriteList;
import softwareProject.business.MovieProduct;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FavouriteListDaoImpl extends MySQLDao implements FavoriteListDao {


    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public FavouriteListDaoImpl(String databaseName){
        super(databaseName);
    }

    public FavouriteListDaoImpl(Connection conn){
        super(conn);
    }
    public FavouriteListDaoImpl(){
        super();
    }



    /**
     * Add favorite list
     * @param favoriteList is the movie being added
     * @return 1 if added and 0 if not added
     */

    @Override
    public int addFavouriteList(FavoriteList favoriteList){
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
        try(PreparedStatement ps = conn.prepareStatement("insert into favouriteList values(?, ?, ?, ?,?,?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, favoriteList.getUsername());
            ps.setInt(2, favoriteList.getMovieDb_id());
            ps.setString(3, favoriteList.getBackdrop_path());
            ps.setString(4, favoriteList.getOverview());
            ps.setString(5, favoriteList.getTitle());
            ps.setString(6, favoriteList.getGenreName());
            ps.setInt(7, favoriteList.getGenreId());

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


    /// get all in favourite list by username

    /**
     * Get all favourite list by username
     * @param username is teh username being searched
     * @return arraylist of favourite list
     */

    @Override
    public ArrayList<FavoriteList> getAllFavouriteListByUsername(String username){

        ArrayList<FavoriteList> favoriteLists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{

            con = getConnection();

            String query = "SELECT * FROM favouritelist where username = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);
            rs = ps.executeQuery();

            while(rs.next()){
                FavoriteList f = mapRow(rs);
                favoriteLists.add(f);

            }


        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution" + e.getMessage());
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the getProductByCode() method: " + e.getMessage());
            }
        }
        return favoriteLists;
    }


    /**
     * Get all favourite list in the system
     * @return arraylist of favourite list
     */
    @Override
    public ArrayList<FavoriteList> getAllFavouriteList(){

        ArrayList<FavoriteList> favoriteLists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            con = getConnection();

            String query = "Select * from favouritelist";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next())
            {
               FavoriteList f = mapRow(rs);
                favoriteLists.add(f);
            }
        }catch(SQLException e){
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the  method: " + e.getMessage());
            }
        }

        return favoriteLists;
    }


    // delete favourite list

    /**
     * user can delete movies from favourite list
     * @param username is teh username being searched
     * @param movieId is the movie id being searched
     * @return 1 if deleted and 0 if not deleted
     */

    @Override
    public int deleteFroFavouriteList(String username, int movieId){
        int rowsAffected = 0;
        Connection con = null;
        PreparedStatement ps = null;

        try{

            con = getConnection();

            String query = "DELETE from favouritelist where username = ? and movieDb_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1,username);
            ps.setInt(2,movieId);

            rowsAffected = ps.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Exception occured in the updateProductName() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the updateProductName() method");
                e.getMessage();
            }
        }

        return rowsAffected;

    }


    /**
     * get favourite list by username and movie id
     * @param username is the username bieng searched
     * @param movieId is teh movie id being searched
     * @return movie from favourite list
     */

    @Override
    public FavoriteList getFavouriteListByUsernameAndMovieId(String username, int movieId) {

        FavoriteList favoriteList = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            con = getConnection();

            String query = "SELECT * FROM favouritelist where username = ? and movieDb_id = ?";
            ps = con.prepareStatement(query);
            ps.setString(1,username);
            ps.setInt(2,movieId);
            rs = ps.executeQuery();


            if (rs.next()) {

                favoriteList = mapRow(rs);
            }

        } catch (SQLException e) {
            System.out.println("Exception occured in the getMovieById() method: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the getProductByCode() method: " + e.getMessage());
            }
        }
        return favoriteList;
    }




    /**
     * Search through each row in the favouriteList
     * @param rs is the rating query being searched
     * @return the rating information
     * @throws SQLException is username
     */

    private FavoriteList mapRow(ResultSet rs)throws SQLException {

        FavoriteList f = new FavoriteList(
                rs.getString("username"),
                rs.getInt("movieDb_id"),
                rs.getString("backdrop_path"),
                rs.getString("overview"),
                rs.getString("title"),
                rs.getString("genreName"),
                rs.getInt("genreId")

        );
        return f;
    }

}
