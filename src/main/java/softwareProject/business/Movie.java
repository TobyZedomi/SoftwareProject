package softwareProject.business;

import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
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
    private int movie_id;
    private String movie_name;
    private int genre_id;
    private LocalDateTime date_of_release;
    private Time movie_length;
    private String movie_info;
    private String movie_mage;


}
