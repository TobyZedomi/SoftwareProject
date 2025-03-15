package softwareProject.persistence;


import softwareProject.business.BillingAddress;
import softwareProject.business.Review;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ReviewDaoImpl extends MySQLDao implements ReviewDao {

    public ReviewDaoImpl(String databaseName){
        super(databaseName);
    }

    public ReviewDaoImpl(Connection conn){
        super(conn);
    }
    public ReviewDaoImpl(){super("database.properties");
    }

    /**
     * Saves a new movie review in the database.
     * This method inserts a review into the `reviews` table with details like
     *   username, movie ID, title, review content, rating, and timestamp.
     * @param review  the review object
     * @return the number of rows affected
     */
    @Override
    public int saveReview(Review review){
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
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO reviews (username, movie_id, movie_title, content, rating, createdAt) VALUES (?, ?, ?, ?, ?, ?)"
        )) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, review.getUsername());
            ps.setInt(2, review.getMovieId());
            ps.setString(3, review.getMovieTitle());
            ps.setString(4, review.getContent());
            ps.setInt(5, review.getRating());
            ps.setTimestamp(6, Timestamp.valueOf(review.getCreatedAt()));

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
     * Updates an existing review in the database.
     * Returns the number of rows affected.
     *
     * @param review The review with updated content and rating.
     * @return Rows affected (1 if successful, 0 if no update occurred).
     */

    @Override
    public int updateReview(Review review) {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try {
            con = getConnection();

            String query = "UPDATE reviews SET content = ?, rating = ?, createdAt = ? WHERE username = ? AND movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, review.getContent());
            ps.setInt(2, review.getRating());
            ps.setTimestamp(3, Timestamp.valueOf(review.getCreatedAt()));
            ps.setString(4, review.getUsername());
            ps.setInt(5, review.getMovieId());

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Exception occurred in the updateReview() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occurred in the finally section of the method: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return rowsAffected;
    }


    /**
     * Deletes a review from the database based on the given username and movie ID.
     * Returns the number of rows affected.
     *
     * @param username The username of the reviewer.
     * @param movieId The ID of the movie being reviewed.
     * @return Rows affected (1 if deleted successfully, 0 if no review was found).
     */

    @Override
    public int deleteReview(String username, int movieId) {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try {
            con = getConnection();

            String query = "DELETE FROM reviews WHERE username = ? AND movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setInt(2, movieId);

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Exception occurred in the deleteReview() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occurred in the finally section of the deleteReview() method");
                e.printStackTrace();
            }
        }

        return rowsAffected;
    }


    /**
     * Retrieves all reviews from the database, ordered by the most recent first.
     *
     * @return A list of all reviews in descending order of creation date.
     */

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            con = getConnection();

            String query = "SELECT * FROM reviews ORDER BY createdAt DESC";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next())
            {
                Review r = mapRow(rs);
                reviews.add(r);
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

        return reviews;
    }



    /**
     * Retrieves all reviews written by a specific user.
     *
     * @param username The username of the user whose reviews should be retrieved.
     * @return A list of reviews written by the specified user.
     */

    @Override
    public ArrayList<Review> getReviewsByUsername(String username){

        ArrayList<Review> reviews = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * FROM reviews where username = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);
            rs = ps.executeQuery();



            while(rs.next()){

                Review r = mapRow(rs);
                reviews.add(r);
            }


        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
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
                System.out.println("Exception occurred in the finally section of the method: " + e.getMessage());
            }
        }
        return reviews;
    }


    private Review mapRow(ResultSet rs) throws SQLException {
        Review r = new Review(
                rs.getString("username"),
                rs.getInt("movie_id"),
                rs.getString("movie_title"),
                rs.getString("content"),
                rs.getInt("rating"),
                rs.getTimestamp("createdAt").toLocalDateTime()
        );
        return r;
    }

}
