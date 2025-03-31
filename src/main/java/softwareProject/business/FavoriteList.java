package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteList {


    private String username;

    private int movieDb_id;

    private String backdrop_path;
    private String overview;
    private String title;
}
