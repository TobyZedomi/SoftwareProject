package softwareProject.persistence;

import softwareProject.business.OtpLogin;

import java.time.LocalDateTime;

public interface OtpLoginDaoImpl {
    public int insertOTP(String email, int number, LocalDateTime date);
    public OtpLogin findOtp(String email);
    public int deleteByEmailOtp(String email);
}
