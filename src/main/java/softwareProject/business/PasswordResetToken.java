package softwareProject.business;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PasswordResetToken {
    private String email;
    private String token;
    private LocalDateTime expiry;
}
