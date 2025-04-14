package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreForMovie {

    @EqualsAndHashCode.Include
    private int genreForMovie_id;

    private String username;

    private int movie_id;

    private int genre_id;

}
