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
    private String username;

    private int movieId;
    private String movieTitle;
    private String content;
    private int rating;
    private LocalDateTime createdAt;
}
