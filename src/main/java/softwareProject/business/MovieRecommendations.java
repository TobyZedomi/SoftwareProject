package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRecommendations {


    private String backdrop_path;

    private int id;

    private String title;

    private String overview;

    private String [] genre_ids;

    @Setter
    private boolean favourite = false;
}
