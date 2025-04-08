package softwareProject.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MovieSearchByGenreResponse {

    private int page;
    private List<MovieSearchByGenre> results;

}
