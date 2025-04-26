package softwareProject.persistence;

import softwareProject.business.MovieProduct;

import java.time.LocalDate;
import java.util.ArrayList;

public interface MovieProductDao {

    public ArrayList<MovieProduct> getAllMovieProducts();

    public MovieProduct getMovieById(int movieId);

    public int addMovieProduct(MovieProduct movieProduct);

    public int deleteMovieProductByMovieId(int movieId);

    public int updateMovieProductNameByMovieId(String movieName, int movieId);

    public int updateMovieProductDateOfReleaseByMovieId(LocalDate dateOfRelease, int movieId);

    public int updateMovieProductInfoByMovieId(String movieInfo, int movieId);

    public int updateMovieProductPriceByMovieId(double price, int movieId);


    public ArrayList<MovieProduct> searchForMovieProductBYMovieName(String movieName);
    public ArrayList<MovieProduct> filterMovieProductBetweenMinAndMax(double min, double max);
    public ArrayList<MovieProduct> filterMovieProductAboveMin(double min);
    public ArrayList<MovieProduct> filterMovieProductBelowMax(double max);
}


