package softwareProject.persistence;

import org.springframework.stereotype.Repository;
import softwareProject.business.PasswordResetToken;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PasswordResetTokenDaoImpl extends MySQLDao implements PasswordResetTokenDao {

    public PasswordResetTokenDaoImpl() {
        super("database.properties");
    }

    @Override
    public void saveToken(PasswordResetToken token) {
        String sql = "INSERT INTO password_reset_tokens (email, token, expiry) VALUES (?, ?, ?)";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, token.getEmail());
            ps.setString(2, token.getToken());
            ps.setTimestamp(3, Timestamp.valueOf(token.getExpiry()));
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        String sql = "SELECT * FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PasswordResetToken resetToken = new PasswordResetToken(
                            rs.getString("email"),
                            rs.getString("token"),
                            rs.getTimestamp("expiry").toLocalDateTime()
                    );
                    return Optional.of(resetToken);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM password_reset_tokens WHERE email = ?";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


}
