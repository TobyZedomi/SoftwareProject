package softwareProject.business;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    @EqualsAndHashCode.Include
    private int movieId;
    private String movieName;
    private int genreId;
    private Date dateOfRelease;
    private double movieLength;
    private String movieInfo;
    private String whereToWatch;
    private String movieImage;


}
