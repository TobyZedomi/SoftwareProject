package softwareProject.business;

import lombok.*;

import java.sql.Time;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    @EqualsAndHashCode.Include
    private int movie_id;
    private String movie_name;
    private int genre_id;
    private int age_id;
    private LocalDate date_of_release;
    private Time movie_length;
    private String movie_info;
    private String movie_image;


    public Movie(String movieName, int genreId, int ageId, LocalDate releaseDate, Time length, String movieInfo, String movieImage) {
    }
}
