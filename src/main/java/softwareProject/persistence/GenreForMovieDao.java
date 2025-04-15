package softwareProject.persistence;

import softwareProject.business.GenreForMovie;

import java.util.ArrayList;

public interface GenreForMovieDao {

    public int addGenreForMovie(GenreForMovie genreForMovie);

    public ArrayList<GenreForMovie> getAllGenreForMovieByUsername(String username);
}
