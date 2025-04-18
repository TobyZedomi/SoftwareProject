package softwareProject.persistence;

import softwareProject.business.ResetPasswordToken;

import java.time.LocalDateTime;

public interface ResetPasswordDao {

    public int insertToken(String email, String token, LocalDateTime date);
    public ResetPasswordToken findToken(String token);
    public int deleteByEmail(String email);

}
