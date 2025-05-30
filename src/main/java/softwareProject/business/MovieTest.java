package softwareProject.business;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieTest {

    @EqualsAndHashCode.Include
    private int id;

    private String backdrop_path;

    private String overview;
    private String title;

    private String [] genre_ids;
    private String release_date;

    private String genreName;

    @Setter
    private boolean favourite = false;
}
