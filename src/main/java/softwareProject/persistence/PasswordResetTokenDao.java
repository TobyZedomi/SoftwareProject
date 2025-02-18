package softwareProject.persistence;

import softwareProject.business.PasswordResetToken;
import java.util.Optional;

public interface PasswordResetTokenDao {
    void saveToken(PasswordResetToken token);
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByEmail(String email);
}
