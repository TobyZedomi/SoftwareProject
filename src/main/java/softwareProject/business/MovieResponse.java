package softwareProject.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MovieResponse {

    private int page;
    private List<MovieTest> results;
}
