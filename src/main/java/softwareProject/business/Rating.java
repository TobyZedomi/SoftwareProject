package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {
    private int movieId;
    private String movieTitle;
    private double averageRating;
    private int totalReviews;
}
