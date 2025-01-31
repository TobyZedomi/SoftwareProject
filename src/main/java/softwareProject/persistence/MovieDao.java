package softwareProject.persistence;

import softwareProject.business.Movie;

import java.util.ArrayList;

public interface MovieDao {

    public ArrayList<Movie> findMovieByGenre(int genre);
}
