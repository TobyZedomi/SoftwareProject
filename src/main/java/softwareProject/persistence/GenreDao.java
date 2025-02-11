package softwareProject.persistence;

import softwareProject.business.Genre;

import java.util.ArrayList;

public interface GenreDao {
    public ArrayList<Genre> getAllGenres();
}
