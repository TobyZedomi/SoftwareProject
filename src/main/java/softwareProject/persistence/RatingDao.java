package softwareProject.persistence;

import softwareProject.business.Rating;
import java.util.List;

public interface RatingDao {
    List<Rating> getHighestRatedMovies();
    List<Rating> getLowestRatedMovies();
}
