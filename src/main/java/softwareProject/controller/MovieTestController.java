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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MovieTestController {

    @Autowired
    private MovieService movieService;



    /**
     * Getting movie videos based on the movie id entered
     *
     * @param model   holds the attributes for the view
     * @param id      is teh movie id being searched
     * @param session holds the logged in users details
     * @return videos page if te videos exist and noVideo page if there are no videos for that movie
     */


    @GetMapping("/movieTrailer")
    public String getMovieTrailer(Model model, @RequestParam(name = "id") String id, HttpSession session) {

        if (session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");


            int movieId = Integer.parseInt(id);

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            session.setAttribute("chat_room_id",id);

            List<MovieTrailer> trailers = movieService.getTrailer(movieId);

            if (trailers.isEmpty()) {

                return "noVideo";
            }
            model.addAttribute("trailers", trailers);

            MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieId);
            model.addAttribute("movieName", movieDbByMovieId.getTitle());

            log.info("User {} clicked to watch movie videos on {}", u.getUsername(), movieDbByMovieId.getTitle());

            return "videos";

        }

        return "notValidUser";
    }




    /**
     * View movies by the genre id
     *
     * @param session holds the users logged information
     * @param model   holds the information for the view
     * @param id      is the genre id being searched
     * @return
     */

    @GetMapping("/viewMovieByGenre")
    public String viewMovieGenre(HttpSession session, Model model, @RequestParam(name = "id") int id) {

        if (session.getAttribute("loggedInUser") != null) {

            String genre_id = Integer.toString(id);

            session.setAttribute("genreId2", genre_id);

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);


            List<GenreTest> genres = movieService.getGenres();
            model.addAttribute("genres", genres);

            List<MovieTest> movieByGenres = movieService.getMoviesByGenre(genre_id);

            List<MovieTest> newMovie = new ArrayList<>();

            for (int i = 0; i < movieByGenres.size() - 2; i++) {

                if (movieByGenres.get(i).getBackdrop_path() != null) {
                    newMovie.add(movieByGenres.get(i));
                    model.addAttribute("movieByGenres", newMovie);
                }
            }

            GenreDao genreDao = new GenreDaoImpl("database.properties");

            GenreTest genre = genreDao.getGenreById(id);

            model.addAttribute("genreName", genre.getName());

            viewMoviesByGenre(session, model);

            return "movie_index";

        }

        return "notValidUser";
    }


    @GetMapping("/viewMovieBySearchOnParticularGenre")
    public String viewMovieBySearchOnParticularGenre(HttpSession session, Model model, @RequestParam(name = "query") String query) {


        if (session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            session.setAttribute("query", query);

            List<MovieTest> movieBySearch = movieService.getMoviesBySearch(query);

            System.out.println(movieBySearch);

            List<MovieTest> newMovieBySearch = new ArrayList<>();

            for (int i = 0; i < movieBySearch.size(); i++) {
                if (movieBySearch.get(i).getBackdrop_path() != null) {
                    newMovieBySearch.add(movieBySearch.get(i));
                    model.addAttribute("movieBySearchGenre", newMovieBySearch);
                }

            }

        model.addAttribute("query", query);

        log.info("User {} searched for movies on {}", u.getUsername(), query);

            viewMoviesByGenre(session, model);
            favouriteListForMovieBySearch(model, session, u);

        return "searchMovie_index";

    }

        return"notValidUser";
}


    /**
     * View movies by what is searched in the search bar
     *
     * @param session holds the users logged information
     * @param model   holds the information for the view
     * @param query   is the movie being searched
     * @return the search index page
     */
    @GetMapping("/viewMovieBySearch")
    public String viewMovieBySearch(HttpSession session, Model model, @RequestParam(name = "query") String query) {


        if (session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");


            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            session.setAttribute("query1", query);

            List<MovieTest> movieBySearch = movieService.getMoviesBySearch(query);

            List<MovieTest> newMovieBySearch = new ArrayList<>();

            for (int i = 0; i < movieBySearch.size() - 2; i++) {

                if (movieBySearch.get(i).getBackdrop_path() != null) {
                    newMovieBySearch.add(movieBySearch.get(i));
                    model.addAttribute("movieBySearch", newMovieBySearch);
                }
            }

            model.addAttribute("query", query);

            log.info("User {} searched for movies on {}", u.getUsername(), query);

            favouriteListForGeneralSearchOnNavBar(model, session, u);

            return "search_index";

        }

        return "notValidUser";
    }



    // view Similar

    @GetMapping("/viewSimilar")
    public String viewSimilar(HttpSession session, Model model, @RequestParam(name = "movieId") String movieId) {


        if (session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");


            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            int movieId2 = Integer.parseInt(movieId);

            session.setAttribute("similar", movieId2);

            List<MovieTest> movieBySearch = movieService.getSimilarMovies(movieId2);

            List<MovieTest> newMovieBySearch = new ArrayList<>();

            for (int i = 0; i < movieBySearch.size() - 2; i++) {

                if (movieBySearch.get(i).getBackdrop_path() != null) {
                    newMovieBySearch.add(movieBySearch.get(i));
                    model.addAttribute("movieBySearch", newMovieBySearch);
                }
            }

            MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieId2);
            model.addAttribute("movieName", movieDbByMovieId.getTitle());


            log.info("User {} viewed similar for movies on {}", u.getUsername(), movieDbByMovieId.getTitle());

            similarMovies(model, session, u);

            return "similar_index";

        }

        return "notValidUser";
    }


    /**
     * Get the total amount of cart items in the cart for the user
     *
     * @param session holds the users logged information
     * @param model   holds the information for the view
     */

    // total amount of items in cart
    public void getTotalAmountOfItemsInCart(HttpSession session, Model model) {

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }


    private void viewMoviesByGenre(HttpSession session, Model model) {
        User u = (User) session.getAttribute("loggedInUser");


        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session, model);

        //

        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao.getAllFavouriteListByUsername(u.getUsername());

        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        GenreDao genreDao = new GenreDaoImpl("database.properties");

        String genreId = (String) session.getAttribute("genreId2");

        if (genreId != null) {

            List<MovieTest> movieByGenres = movieService.getMoviesByGenre(genreId);

            List<MovieTest> newMovie = new ArrayList<>();

            for (int i = 0; i < movieByGenres.size() - 2; i++) {

                if (movieByGenres.get(i).getBackdrop_path() != null && movieByGenres.get(i).getGenre_ids().length > 0) {
                    movieByGenres.get(i).setGenreName(genreDao.getGenreById(Integer.parseInt(genreId)).getName());
                    newMovie.add(movieByGenres.get(i));
                    model.addAttribute("movieByGenres", newMovie);
                }

                for (int j = 0; j < favoriteLists.size(); j++) {

                    if (favoriteLists.get(j).getMovieDb_id() == movieByGenres.get(i).getId()) {

                        movieByGenres.get(i).setFavourite(true);
                    }
                }
            }

            // genre by id and get the name

            // use a session for this based on the controller method view movie by genre, testing branch


            GenreTest genre = genreDao.getGenreById(Integer.parseInt(genreId));

            model.addAttribute("genreName", genre.getName());

        } else {

            toViewMoviesByGenreMovieIndex(model, session);
        }

    }


    private void toViewMoviesByGenreMovieIndex(Model model, HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");


        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session, model);

        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao.getAllFavouriteListByUsername(u.getUsername());

        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);


        List<MovieTest> movieByGenres = movieService.getMoviesByGenre("878");

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

        // genre by id and get the name

        GenreDao genreDao = new GenreDaoImpl("database.properties");

        GenreTest genre = genreDao.getGenreById(878);

        model.addAttribute("genreName", genre.getName());
    }



    private void favouriteListForMovieBySearch(Model model, HttpSession session, User user) {
        FavoriteListDao favoriteListDao1 = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao1.getAllFavouriteListByUsername(user.getUsername());

        String query = (String) session.getAttribute("query");

        List<MovieTest> movieBySearch = movieService.getMoviesBySearch(query);

        System.out.println(movieBySearch);

        List<MovieTest> newMovieBySearch = new ArrayList<>();

        GenreDaoImpl genreDao = new GenreDaoImpl("database.properties");

        for (int i = 0; i < movieBySearch.size(); i++) {


            if (movieBySearch.get(i).getBackdrop_path() != null && movieBySearch.get(i).getGenre_ids().length > 0) {
                movieBySearch.get(i).setGenreName(genreDao.getGenreById(Integer.parseInt(movieBySearch.get(i).getGenre_ids()[0])).getName());
                newMovieBySearch.add(movieBySearch.get(i));
                model.addAttribute("movieBySearchGenre", newMovieBySearch);
            }

            for (int j = 0; j < favoriteLists.size(); j++) {

                if (favoriteLists.get(j).getMovieDb_id() == movieBySearch.get(i).getId()) {

                    movieBySearch.get(i).setFavourite(true);
                }
            }

        }

        model.addAttribute("query", query);
    }



    private void favouriteListForGeneralSearchOnNavBar(Model model, HttpSession session, User user) {

        FavoriteListDao favoriteListDao1 = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao1.getAllFavouriteListByUsername(user.getUsername());
        String query = (String) session.getAttribute("query1");

        List<MovieTest> movieBySearch = movieService.getMoviesBySearch(query);

        List<MovieTest> newMovieBySearch = new ArrayList<>();

        GenreDaoImpl genreDao = new GenreDaoImpl("database.properties");

        for (int i = 0; i < movieBySearch.size() - 2; i++) {

            if (movieBySearch.get(i).getBackdrop_path() != null && movieBySearch.get(i).getGenre_ids().length > 0) {
                movieBySearch.get(i).setGenreName(genreDao.getGenreById(Integer.parseInt(movieBySearch.get(i).getGenre_ids()[0])).getName());
                newMovieBySearch.add(movieBySearch.get(i));
                model.addAttribute("movieBySearch", newMovieBySearch);
            }

            for (int j = 0; j < favoriteLists.size(); j++) {

                if (favoriteLists.get(j).getMovieDb_id() == movieBySearch.get(i).getId()) {

                    movieBySearch.get(i).setFavourite(true);
                }
            }

        }

        model.addAttribute("query", query);

        log.info("User {} searched for movies on {}", user.getUsername(), query);
    }


    private void similarMovies(Model model, HttpSession session, User user){


        FavoriteListDao favoriteListDao1 = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao1.getAllFavouriteListByUsername(user.getUsername());

        int movieId2 =  (int) session.getAttribute("similar");

        List<MovieTest> movieBySearch = movieService.getSimilarMovies(movieId2);

        List<MovieTest> newMovieBySearch = new ArrayList<>();

        GenreDaoImpl genreDao = new GenreDaoImpl("database.properties");

        for (int i = 0; i < movieBySearch.size() - 2; i++) {

            if (movieBySearch.get(i).getBackdrop_path() != null && movieBySearch.get(i).getGenre_ids().length > 0) {
                movieBySearch.get(i).setGenreName(genreDao.getGenreById(Integer.parseInt(movieBySearch.get(i).getGenre_ids()[0])).getName());
                newMovieBySearch.add(movieBySearch.get(i));
                model.addAttribute("movieBySearch", newMovieBySearch);
            }

            for (int j = 0; j < favoriteLists.size(); j++) {

                if (favoriteLists.get(j).getMovieDb_id() == movieBySearch.get(i).getId()) {

                    movieBySearch.get(i).setFavourite(true);
                }
            }
        }

        MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieId2);
        model.addAttribute("movieName", movieDbByMovieId.getTitle());


    }

}
