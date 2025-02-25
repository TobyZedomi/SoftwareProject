package softwareProject.persistence;

import org.springframework.stereotype.Repository;
import softwareProject.business.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewDaoImpl extends MySQLDao implements ReviewDao {

    public ReviewDaoImpl() {
        super("database.properties");
    }

    @Override
    public void saveReview(Review review) {
        String sql = "INSERT INTO reviews (name, email, content, createdAt) VALUES (?, ?, ?, ?)";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, review.getName());
            ps.setString(2, review.getEmail());
            ps.setString(3, review.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(review.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private Review mapRow(ResultSet rs) throws SQLException {
        Review r = new Review(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("content"),
                rs.getTimestamp("createdAt").toLocalDateTime()
        );
        return r;
    }

}
