package softwareProject.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class MovieRecommendationResponse {

    private int page;
    private List<MovieRecommendations> results;
}
