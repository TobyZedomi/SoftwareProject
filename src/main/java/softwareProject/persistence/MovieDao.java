package softwareProject.persistence;

import softwareProject.business.Movie;

import java.util.ArrayList;
import java.util.List;

public interface MovieDao {

    public ArrayList<Movie> findMovieByGenre(int genre);
    List<Movie> getAllMovies();
    public int deletingAMovie(int movieId);
    public int insertNewMovie(Movie newMovie);
    public Movie searchForMovieByName(String name);

}
