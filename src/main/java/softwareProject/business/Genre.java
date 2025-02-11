package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Genre {
    @EqualsAndHashCode.Include
    private int genre_id;
    private String genre_name;

}
