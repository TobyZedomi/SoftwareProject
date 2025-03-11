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

    @Override
    public List<Rating> getHighestRatedMovies() {
        List<Rating> highestRated = new ArrayList<>();
        String sql = "SELECT movie_id, movie_title, AVG(rating) AS avg_rating, COUNT(*) AS total_reviews " +
                "FROM reviews GROUP BY movie_id, movie_title ORDER BY avg_rating DESC LIMIT 10";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                highestRated.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highestRated;
    }

    @Override
    public List<Rating> getLowestRatedMovies() {
        List<Rating> lowestRated = new ArrayList<>();
        String sql = "SELECT movie_id, movie_title, AVG(rating) AS avg_rating, COUNT(*) AS total_reviews " +
                "FROM reviews GROUP BY movie_id, movie_title ORDER BY avg_rating ASC LIMIT 10";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lowestRated.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
