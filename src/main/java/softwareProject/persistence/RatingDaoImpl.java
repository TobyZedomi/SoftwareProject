package softwareProject.persistence;

import softwareProject.business.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDaoImpl extends MySQLDao implements RatingDao {

    public RatingDaoImpl(String databaseName){
        super(databaseName);
    }

    public RatingDaoImpl(Connection conn){
        super(conn);
    }
    public RatingDaoImpl(){super("database.properties");
    }

    /**
     * Gets a list of the top 10 highest-rated movies.
     *
     * This method retrieves movies from the database, calculates their average ratings,
     * and sorts them in descending order. Only the top 10 movies.
     *
     * @return A list of ratings from  the highest to lowest.
     */

    @Override
    public List<Rating> getHighestRatedMovies() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Rating> highestRated = new ArrayList<>();

        try {
            con = getConnection();

            String query = "SELECT movie_id, movie_title, AVG(rating) AS avg_rating, COUNT(*) AS total_reviews " +
                    "FROM reviews GROUP BY movie_id, movie_title ORDER BY avg_rating DESC LIMIT 10";

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                highestRated.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Exception occurred in getHighestRatedMovies() method: " + e.getMessage());
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
                System.out.println("Exception occurred in the final section of the method: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return highestRated;
    }


    /**
     * Gets a list of the top 10 highest-rated movies.
     *
     * This method retrieves movies from the database, calculates their average ratings,
     * and sorts them in ascending order. Only the top 10 movies.
     *
     * @return A list of ratings from  the lowest to highest.
     */
    @Override
    public List<Rating> getLowestRatedMovies() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Rating> lowestRated = new ArrayList<>();

        try {
            con = getConnection();

            String query = "SELECT movie_id, movie_title, AVG(rating) AS avg_rating, COUNT(*) AS total_reviews " +
                    "FROM reviews GROUP BY movie_id, movie_title ORDER BY avg_rating ASC LIMIT 10";

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                lowestRated.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Exception occurred in getLowestRatedMovies() method: " + e.getMessage());
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
                System.out.println("Exception occurred in the finally section of getLowestRatedMovies() method");
                e.printStackTrace();
            }
        }

        return lowestRated;
    }



    private Rating mapRow(ResultSet rs) throws SQLException {
        Rating r = new Rating(
                rs.getInt("movie_id"),
                rs.getString("movie_title"),
                rs.getDouble("avg_rating"),
                rs.getInt("total_reviews")
        );
        return r;
    }
}
