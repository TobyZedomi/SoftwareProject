package softwareProject.business;

import lombok.Data;

import java.util.List;

@Data
public class MovieReviewResponse {
    private int id;
    private List<MovieReview> results;
}
