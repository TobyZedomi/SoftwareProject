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
public class MovieProduct {

    @EqualsAndHashCode.Include
    private int movie_id;
    private String movie_name;
    private LocalDate date_of_release;
    private Time movie_length;
    private String movie_info;
    private String movie_image;
    private double listPrice;
}
