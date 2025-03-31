package softwareProject.persistence;

import softwareProject.business.PasswordResetToken;

import java.time.LocalDateTime;

public interface ResetPasswordDao {

    public int insertToken(String email, String token, LocalDateTime date);
    public PasswordResetToken findToken(String token);
    public int deleteByEmail(String email);

}
