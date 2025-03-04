package softwareProject.persistence;

import softwareProject.business.MovieProduct;

import java.util.ArrayList;

public interface MovieProductDao {

    public ArrayList<MovieProduct> getAllMovieProducts();

    public MovieProduct getMovieById(int movieId);

    public int addMovieProduct(MovieProduct movieProduct);

    public int deleteMovieProductByMovieId(int movieId);
}
