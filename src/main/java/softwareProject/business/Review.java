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
public class Review {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private String email;
    private String content;
    private LocalDateTime createdAt;
}
