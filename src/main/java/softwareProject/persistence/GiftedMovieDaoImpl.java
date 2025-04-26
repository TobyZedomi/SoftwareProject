package softwareProject.persistence;

import softwareProject.business.GiftedMovie;
import java.sql.*;
import java.util.*;

public class GiftedMovieDaoImpl extends MySQLDao implements GiftedMovieDao {

    public GiftedMovieDaoImpl() {
        super();
    }

    @Override
    public void giftMovie(String senderUsername, String recipientUsername, int movieId) {
        String sql = "INSERT INTO gifted_movies (sender_username, recipient_username, movie_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, senderUsername);
            stmt.setString(2, recipientUsername);
            stmt.setInt(3, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GiftedMovie> getGiftsReceived(String recipientUsername) {
        List<GiftedMovie> gifts = new ArrayList<>();
        String sql = "SELECT * FROM gifted_movies WHERE recipient_username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipientUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                gifts.add(GiftedMovie.builder()
                        .giftId(rs.getInt("gift_id"))
                        .senderUsername(rs.getString("sender_username"))
                        .recipientUsername(rs.getString("recipient_username"))
                        .movieId(rs.getInt("movie_id"))
                        .giftedAt(rs.getTimestamp("gifted_at"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gifts;
    }
}
