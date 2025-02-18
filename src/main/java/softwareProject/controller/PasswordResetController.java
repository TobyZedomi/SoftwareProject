package softwareProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import softwareProject.business.PasswordResetToken;
import softwareProject.persistence.PasswordResetTokenDao;
import softwareProject.persistence.UserDao;
import softwareProject.business.EmailService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

@Controller
public class PasswordResetController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenDao tokenDao;

    @Autowired
    private UserDao userDao;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(email, token, LocalDateTime.now().plusMinutes(30));
        tokenDao.saveToken(resetToken);
        emailService.sendResetEmail(email, token);
        model.addAttribute("message", "A password reset link has been sent to your email.");
        return "forgot_password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(name = "token", required = false) String token, Model model) {
        if (token == null || token.isEmpty()) {
            model.addAttribute("message", "Invalid or missing reset token.");
            return "error";
        }

        Optional<PasswordResetToken> tokenOpt = tokenDao.findByToken(token);

        if (tokenOpt.isEmpty() || tokenOpt.get().getExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "Invalid or expired token.");
            return "error";
        }

        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password, Model model)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        Optional<PasswordResetToken> tokenOpt = tokenDao.findByToken(token);
        if (tokenOpt.isPresent()) {
            String email = tokenOpt.get().getEmail();

            // Hash the password before i update it
            String hashedPassword = hashPassword(password);

            if (userDao.updatePassword(email, hashedPassword)) {
                tokenDao.deleteByEmail(email);
                return "reset_success";
            }
        }
        model.addAttribute("message", "Invalid token.");
        return "error";
    }

    // It Hashess password using PBKDF2
    public String hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = "NotSoSecretSalt".getBytes();
        int iterations = 65536;
        int keySize = 256;

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, keySize);
        SecretKey key = factory.generateSecret(spec);

        String keyAsString = Base64.getEncoder().encodeToString(key.getEncoded());
        return keyAsString;
    }
}
