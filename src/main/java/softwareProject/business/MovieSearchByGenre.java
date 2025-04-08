package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieSearchByGenre {

private String backdrop_path;

private String id;

private String overview;

private String title;

    @Setter
    private boolean favourite = false;

}
