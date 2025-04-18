package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.*;
import softwareProject.service.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class ReccomendationsController {


    @Autowired
    private MovieService movieService;

    @GetMapping("/movieRecs")
    public String movieRecs(HttpSession session, Model model) {

        if (session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");


            //  int id = Integer.parseInt(movieId);

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            int[] arr = new int[]{822119, 429617, 939243, 661539, 539972, 1084199, 1412113, 51482, 1357633, 1020414, 210577, 146233};
            System.out.print("Random number from the array = " + arr[new Random().nextInt(arr.length)]);

            // creating a session to hold random number

            session.setAttribute("randomNumber", arr[new Random().nextInt(arr.length)]);

            List<MovieRecommendations> movieRecs = movieService.getRecommendations(arr[new Random().nextInt(arr.length)]);

            List<MovieRecommendations> newMovie = new ArrayList<>();

            FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");


            for (int i = 0; i < 15; i++) {

                if (movieRecs.get(i).getBackdrop_path() != null) {
                    newMovie.add(movieRecs.get(i));
                    model.addAttribute("movieRecs", newMovie);
                }

            }

            String recs = "Random Movie Recommendations";
            model.addAttribute("recs1", recs);

            String favList = "Favourite List";
            model.addAttribute("favList", favList);

            String recs2 = "Most Common Genre In FavouriteList";
            model.addAttribute("recs2", recs2);


            favListForRandomRecs(model, session, favoriteListDao, u);


            return "movie_recs";

        }

        return "notValidUser";
    }

    @GetMapping("/movieRecsGenreCommon")
    public String movieRecsGenreCommon(HttpSession session, Model model) {


            User u = (User) session.getAttribute("loggedInUser");

            FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl("database.properties");
            ArrayList<FavoriteList> favoriteLists = favouriteListDao.getAllFavouriteListByUsername(u.getUsername());

            GenreForMovieDaoImpl genreForMovieDao = new GenreForMovieDaoImpl("database.properties");
            ArrayList<GenreForMovie> genreForMovies = genreForMovieDao.getAllGenreForMovieByUsername(u.getUsername());


            // Array to store genreId


            ArrayList<Integer> genreIds = new ArrayList();

            for (int i = 0; i < favoriteLists.size(); i++) {

                for (int j = 0; j < genreForMovies.size(); j++) {
                    if (favoriteLists.get(i).getMovieDb_id() == genreForMovies.get(j).getMovie_id()) {
                        genreIds.add(genreForMovies.get(j).getGenre_id());
                    }
                }
            }

            // remove duplicates, reference link - https://www.studytonight.com/java-examples/how-to-remove-duplicates-from-arraylist

            genreIds = (ArrayList<Integer>)genreIds.stream().distinct().collect(Collectors.toList());

            if (genreIds.isEmpty()){

                String recs = "Random Movie Recommendations";
                model.addAttribute("recs1", recs);

                String favList = "Favourite List";
                model.addAttribute("favList", favList);

                String recs2 = "Most Common Genre In FavouriteList";
                model.addAttribute("recs2", recs2);

                return "goToMoviesPage";
            }

            int maxCount = 0;
            int mostCommonGenreId = 0;

            for (int i = 0; i < genreIds.size(); i++) {

                int count = 0;
                for (int j = 0; j < genreIds.size(); j++) {
                    if (genreIds.get(i) == genreIds.get(j)) {
                        count++;
                    }
                }

                if (count > maxCount) {
                    maxCount = count;
                    mostCommonGenreId = genreIds.get(i);
                }
            }

        System.out.println(mostCommonGenreId);

        session.setAttribute("mostCommonGenreId", mostCommonGenreId);

        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        List<MovieTest> movieByGenres = movieService.getMoviesByGenre(String.valueOf(mostCommonGenreId));

        List<MovieTest> newMovie = new ArrayList<>();

        for (int i = 0; i < movieByGenres.size() - 2; i++) {

            if (movieByGenres.get(i).getBackdrop_path() != null) {
                newMovie.add(movieByGenres.get(i));
                model.addAttribute("movieByGenres", newMovie);
            }
        }

        GenreDao genreDao = new GenreDaoImpl("database.properties");

        GenreTest genre = genreDao.getGenreById(mostCommonGenreId);
        model.addAttribute("genreName", genre.getName());


        String recs = "Random Movie Recommendations";
        model.addAttribute("recs1", recs);

        String favList = "Favourite List";
        model.addAttribute("favList", favList);

        String recs2 = "Most Common Genre In FavouriteList";
        model.addAttribute("recs2", recs2);

        favListForRecsBasedOnMostCommonMovieInFavList(model, session);

        return "movie_recsGenre";

        }


    private void favListForRandomRecs(Model model, HttpSession session, FavoriteListDao favoriteListDao, User user) {
        int randomNumber = (Integer) session.getAttribute("randomNumber");

        List<MovieRecommendations> movieRecs = movieService.getRecommendations(randomNumber);

        List<MovieRecommendations> newMovie = new ArrayList<>();


        ArrayList<FavoriteList> favoriteLists = favoriteListDao.getAllFavouriteListByUsername(user.getUsername());

        for (int i = 0; i < 15; i++) {

            if (movieRecs.get(i).getBackdrop_path() != null) {
                newMovie.add(movieRecs.get(i));
                model.addAttribute("movieRecs",newMovie);
            }

            for (int j = 0; j < favoriteLists.size(); j++) {

                if (favoriteLists.get(j).getMovieDb_id() == movieRecs.get(i).getId()) {

                    movieRecs.get(i).setFavourite(true);
                }
            }

        }
    }

    private void favListForRecsBasedOnMostCommonMovieInFavList(Model model, HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl("database.properties");
        ArrayList<FavoriteList> favoriteLists = favouriteListDao.getAllFavouriteListByUsername(u.getUsername());

        GenreForMovieDaoImpl genreForMovieDao = new GenreForMovieDaoImpl("database.properties");
        ArrayList<GenreForMovie> genreForMovies = genreForMovieDao.getAllGenreForMovieByUsername(u.getUsername());


        // Array to store genreId


        ArrayList<Integer> genreIds = new ArrayList();

        for (int i = 0; i < favoriteLists.size(); i++) {

            for (int j = 0; j < genreForMovies.size(); j++) {
                if (favoriteLists.get(i).getMovieDb_id() == genreForMovies.get(j).getMovie_id()) {
                    genreIds.add(genreForMovies.get(j).getGenre_id());
                }
            }
        }

        int maxCount = 0;
        int mostCommonGenreId = 0;

        for (int i = 0; i < genreIds.size(); i++) {

            int count = 0;

            for (int j = 0; j < genreIds.size(); j++) {
                if (genreIds.get(i) == genreIds.get(j)) {
                    count++;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                mostCommonGenreId = genreIds.get(i);
            }
        }

        System.out.println(mostCommonGenreId);

        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        List<MovieTest> movieByGenres = movieService.getMoviesByGenre(String.valueOf(mostCommonGenreId));

        List<MovieTest> newMovie = new ArrayList<>();

        for (int i = 0; i < movieByGenres.size() - 2; i++) {

            if (movieByGenres.get(i).getBackdrop_path() != null) {
                newMovie.add(movieByGenres.get(i));
                model.addAttribute("movieByGenres", newMovie);
            }

            for (int j = 0; j < favoriteLists.size(); j++) {

                if (favoriteLists.get(j).getMovieDb_id() == movieByGenres.get(i).getId()) {

                    movieByGenres.get(i).setFavourite(true);
                }
            }
        }

        GenreDao genreDao = new GenreDaoImpl("database.properties");

        GenreTest genre = genreDao.getGenreById(mostCommonGenreId);
        model.addAttribute("genreName", genre.getName());
    }


    public void getTotalAmountOfItemsInCart(HttpSession session,Model model){

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }

}
