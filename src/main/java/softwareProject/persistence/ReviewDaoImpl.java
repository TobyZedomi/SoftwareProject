package softwareProject.persistence;


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

    @Override
    public void saveReview(Review review) {
        String sql = "INSERT INTO reviews (username, movie_id, movie_title, content, rating, createdAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, review.getUsername());
            ps.setInt(2, review.getMovieId());
            ps.setString(3, review.getMovieTitle());
            ps.setString(4, review.getContent());
            ps.setInt(5, review.getRating());
            ps.setTimestamp(6, Timestamp.valueOf(review.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int updateReview(Review review) {
        int rowsAffected = 0;


        Connection conn = super.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE reviews SET content = ?, rating = ?, createdAt = ? WHERE username = ? AND movie_id = ?"
        )) {
            ps.setString(1, review.getContent());
            ps.setInt(2, review.getRating());
            ps.setTimestamp(3, Timestamp.valueOf(review.getCreatedAt()));
            ps.setString(4, review.getUsername());
            ps.setInt(5, review.getMovieId());

            rowsAffected = ps.executeUpdate();
        }

        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            rowsAffected = -1;
        }

        catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;
    }


    @Override
    public int deleteReview(String username, int movieId) {
        int rowsAffected = 0;


        Connection conn = super.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM reviews WHERE username = ? AND movie_id = ?"
        )) {
            ps.setString(1, username);
            ps.setInt(2, movieId);

            rowsAffected = ps.executeUpdate();
        }

        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            rowsAffected = -1;
        }

        catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;
    }


    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews ORDER BY createdAt DESC";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reviews.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByUser(String username) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE username = ? ORDER BY createdAt DESC";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
