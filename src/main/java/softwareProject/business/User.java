package softwareProject.business;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {

    @EqualsAndHashCode.Include
    private String username;
    private String displayName;
    @ToString.Exclude
    private String email;
    private String password;
    private String address;
    private LocalDateTime dateOfBirth;
    private boolean isAdmin;
    private LocalDateTime createdAt;


}