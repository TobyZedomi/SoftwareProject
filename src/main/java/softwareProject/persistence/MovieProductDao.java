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


}


