package softwareProject.business;

import lombok.*;

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

    @ToString.Exclude
    private String email;
    private String password;
    private String address;
    private boolean isAdmin;


}