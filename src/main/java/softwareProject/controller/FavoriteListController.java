package softwareProject.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softwareProject.business.*;
import softwareProject.persistence.*;
import softwareProject.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Controller
public class FavoriteListController {

    @Autowired
    private MovieService movieService;


    @GetMapping("/addMovieFavList")
    public String addMovieFavList(@RequestParam(name = "movieId") String movieId,
                                  @RequestParam(name = "backdrop_path") String backdrop_path,
                                  @RequestParam(name = "overview") String overview,
                                  @RequestParam(name = "title") String title, Model model, HttpSession session) {


        if(session.getAttribute("loggedInUser") != null) {

            int movieDB_Id = Integer.parseInt(movieId);


            FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");

            User user = (User) session.getAttribute("loggedInUser");

            int complete = favoriteListDao.addFavouriteList(new FavoriteList(user.getUsername(), movieDB_Id, backdrop_path, overview, title));

            String message;
            if(complete == -1){

                favoriteListDao.deleteFroFavouriteList(user.getUsername(), movieDB_Id);

                // for deleted movie
                //  model.addAttribute("movieId", movieDB_Id);


                FavoriteList favoriteListUser = favoriteListDao.getFavouriteListByUsernameAndMovieId(user.getUsername(), movieDB_Id);
                session.setAttribute("favouriteListUser", favoriteListUser);


                //model.addAttribute("movieId",950387);


                MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = movieDbByMovieId.getTitle() + " was deleted from your Favourite List";
                model.addAttribute("messageDelete", message);


                log.info(message);

                getTotalAmountOfItemsInCart(session, model);

                toViewMostPopularMovies(model, session);




                return "index";


            }else{


                MovieDbByMovieId  movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = "Movie: " +movieDbByMovieId.getTitle() + " was added to favouriteList";
                model.addAttribute("message", message);

                log.info(message);

                getTotalAmountOfItemsInCart( session,model);

                toViewMostPopularMovies(model, session);

                return "index";
            }

        }

        return "notValidUser";

    }

    // addMovieFavList for movies by genre

    @GetMapping("/addMovieFavList2")
    public String addMovieFavList2(@RequestParam(name = "movieId") String movieId,
                                  @RequestParam(name = "backdrop_path") String backdrop_path,
                                  @RequestParam(name = "overview") String overview,
                                  @RequestParam(name = "title") String title, Model model, HttpSession session) {


        if(session.getAttribute("loggedInUser") != null) {

            int movieDB_Id = Integer.parseInt(movieId);


            FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");

            User user = (User) session.getAttribute("loggedInUser");

            int complete = favoriteListDao.addFavouriteList(new FavoriteList(user.getUsername(), movieDB_Id, backdrop_path, overview, title));

            String message;
            if(complete == -1){

                favoriteListDao.deleteFroFavouriteList(user.getUsername(), movieDB_Id);


                FavoriteList favoriteListUser = favoriteListDao.getFavouriteListByUsernameAndMovieId(user.getUsername(), movieDB_Id);
                session.setAttribute("favouriteListUser", favoriteListUser);

                MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = movieDbByMovieId.getTitle() + " was deleted from your Favourite List";
                model.addAttribute("messageDelete", message);

                log.info(message);

                getTotalAmountOfItemsInCart(session, model);

                toViewMoviesByGenre(model, session);

                return "movie_index";


            }else{


                MovieDbByMovieId  movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = "Movie: " +movieDbByMovieId.getTitle() + " was added to favouriteList";
                model.addAttribute("message", message);

                log.info(message);

                getTotalAmountOfItemsInCart( session,model);


                toViewMoviesByGenre(model, session);

                return "movie_index";
            }

        }

        return "notValidUser";

    }


    // favourite list for search in movies

    @GetMapping("/addMovieFavList3")
    public String addMovieFavList3(@RequestParam(name = "movieId") String movieId,
                                   @RequestParam(name = "backdrop_path") String backdrop_path,
                                   @RequestParam(name = "overview") String overview,
                                   @RequestParam(name = "title") String title, Model model, HttpSession session) {


        if(session.getAttribute("loggedInUser") != null) {

            int movieDB_Id = Integer.parseInt(movieId);


            FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");

            User user = (User) session.getAttribute("loggedInUser");

            int complete = favoriteListDao.addFavouriteList(new FavoriteList(user.getUsername(), movieDB_Id, backdrop_path, overview, title));

            String message;
            if(complete == -1){

                favoriteListDao.deleteFroFavouriteList(user.getUsername(), movieDB_Id);


                FavoriteList favoriteListUser = favoriteListDao.getFavouriteListByUsernameAndMovieId(user.getUsername(), movieDB_Id);
                session.setAttribute("favouriteListUser", favoriteListUser);

                MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = movieDbByMovieId.getTitle() + " was deleted from your Favourite List";
                model.addAttribute("messageDelete", message);

                log.info(message);

                getTotalAmountOfItemsInCart(session, model);


                favouriteListForMovieBySearch(model, session, user);

                return "searchMovie_index";

            }else{


                MovieDbByMovieId  movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = "Movie: " +movieDbByMovieId.getTitle() + " was added to favouriteList";
                model.addAttribute("message", message);

                log.info(message);

                getTotalAmountOfItemsInCart( session,model);


                favouriteListForMovieBySearch(model, session, user);

                return "searchMovie_index";
            }

        }

        return "notValidUser";

    }


    // favourite list for general search

    @GetMapping("/addMovieFavList4")
    public String addMovieFavList4(@RequestParam(name = "movieId") String movieId,
                                   @RequestParam(name = "backdrop_path") String backdrop_path,
                                   @RequestParam(name = "overview") String overview,
                                   @RequestParam(name = "title") String title, Model model, HttpSession session) {


        if(session.getAttribute("loggedInUser") != null) {

            int movieDB_Id = Integer.parseInt(movieId);


            FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");

            User user = (User) session.getAttribute("loggedInUser");

            int complete = favoriteListDao.addFavouriteList(new FavoriteList(user.getUsername(), movieDB_Id, backdrop_path, overview, title));

            String message;
            if(complete == -1){

                favoriteListDao.deleteFroFavouriteList(user.getUsername(), movieDB_Id);


                FavoriteList favoriteListUser = favoriteListDao.getFavouriteListByUsernameAndMovieId(user.getUsername(), movieDB_Id);
                session.setAttribute("favouriteListUser", favoriteListUser);

                MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = movieDbByMovieId.getTitle() + " was deleted from your Favourite List";
                model.addAttribute("messageDelete", message);

                log.info(message);

                getTotalAmountOfItemsInCart(session, model);

                favouriteListForGeneralSearchOnNavBar(model, session, user);

                return "search_index";

            }else{


                MovieDbByMovieId  movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = "Movie: " +movieDbByMovieId.getTitle() + " was added to favouriteList";
                model.addAttribute("message", message);

                log.info(message);

                getTotalAmountOfItemsInCart( session,model);

                favouriteListForGeneralSearchOnNavBar(model, session, user);

                return "search_index";
            }

        }

        return "notValidUser";

    }


    // favourite list for random recommendations

    @GetMapping("/addMovieFavList5")
    public String addMovieFavList5(@RequestParam(name = "movieId") String movieId,
                                   @RequestParam(name = "backdrop_path") String backdrop_path,
                                   @RequestParam(name = "overview") String overview,
                                   @RequestParam(name = "title") String title, Model model, HttpSession session) {


        if(session.getAttribute("loggedInUser") != null) {

            int movieDB_Id = Integer.parseInt(movieId);


            FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");

            User user = (User) session.getAttribute("loggedInUser");

            int complete = favoriteListDao.addFavouriteList(new FavoriteList(user.getUsername(), movieDB_Id, backdrop_path, overview, title));

            String message;
            if(complete == -1){

                favoriteListDao.deleteFroFavouriteList(user.getUsername(), movieDB_Id);


                FavoriteList favoriteListUser = favoriteListDao.getFavouriteListByUsernameAndMovieId(user.getUsername(), movieDB_Id);
                session.setAttribute("favouriteListUser", favoriteListUser);

                MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = movieDbByMovieId.getTitle() + " was deleted from your Favourite List";
                model.addAttribute("messageDelete", message);

                log.info(message);

                getTotalAmountOfItemsInCart(session, model);

                String recs = "Random Movie Recommendations";
                model.addAttribute("recs1", recs);

                String favList = "Favourite List";
                model.addAttribute("favList", favList);

                favListForRandomRecs(model, session, favoriteListDao, user);

                return "movie_recs";

            }else{


                MovieDbByMovieId  movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = "Movie: " +movieDbByMovieId.getTitle() + " was added to favouriteList";
                model.addAttribute("message", message);

                log.info(message);

                getTotalAmountOfItemsInCart( session,model);

                // creating a session to hold random number

                String recs = "Random Movie Recommendations";
                model.addAttribute("recs1", recs);

                String favList = "Favourite List";
                model.addAttribute("favList", favList);

                favListForRandomRecs(model, session, favoriteListDao, user);

                return "movie_recs";
            }

        }

        return "notValidUser";

    }

    private void favListForRandomRecs(Model model, HttpSession session, FavoriteListDao favoriteListDao, User user) {

        int randomNumber = (int) session.getAttribute("randomNumber");

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


    // delete fav list

    @GetMapping("/deleteFavList")
    public String deleteFavList(HttpSession session,
                                @RequestParam(name = "movieId") String movieId, Model model) {

        if (session.getAttribute("loggedInUser") != null) {

            int movieDB_Id = Integer.parseInt(movieId);


            FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");

            User user = (User) session.getAttribute("loggedInUser");

            int delete = favoriteListDao.deleteFroFavouriteList(user.getUsername(), movieDB_Id);

            String message;
            if (delete > 0) {

                MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = movieDbByMovieId.getTitle() + " was deleted from your favourite list ";
                model.addAttribute("message", message);
                log.info(message);

                getTotalAmountOfItemsInCart(session, model);


                ArrayList<FavoriteList> favoriteLists = favoriteListDao.getAllFavouriteListByUsername(user.getUsername());

                String name = null;
                for (int i = 0; i < favoriteLists.size();i++) {

                    name = favoriteLists.get(i).getUsername();

                }

                session.setAttribute("favoriteLists", name);


                model.addAttribute("movies", favoriteLists);

                return "favList";

            } else {

                MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieDB_Id);
                message = movieDbByMovieId.getTitle() + " was not deleted from your favourite list";
                model.addAttribute("message", message);
                log.info(message);

                getTotalAmountOfItemsInCart(session, model);


                ArrayList<FavoriteList> favoriteLists = favoriteListDao.getAllFavouriteListByUsername(user.getUsername());


                model.addAttribute("movies", favoriteLists);

                return "favList";
            }

        }

        return "notValidUser";

    }



    private void toViewMostPopularMovies(Model model, HttpSession session) {

        User u = (User) session.getAttribute("loggedInUser");
        List<MovieTest> movies = movieService.getMovies();

        // create new list to add the movies from the movie db into
        List<MovieTest> newMovie = new ArrayList<>();

        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao.getAllFavouriteListByUsername(u.getUsername());

        // loop through the movie db list and reduce the size by 2
        for (int i = 0; i < movies.size() - 2; i++) {

            // if any backdrop image is unavailable it will not add it to the new arraylist
            if (movies.get(i).getBackdrop_path() != null) {
                // add the movies from the movie db into the new arraylist
                newMovie.add(movies.get(i));
                model.addAttribute("movies", newMovie);
            }

            for (int j = 0; j < favoriteLists.size(); j++) {

                if (favoriteLists.get(j).getMovieDb_id() == movies.get(i).getId()) {

                    movies.get(i).setFavourite(true);
                }
            }
        }

    }


    private void toViewMoviesByGenre(Model model, HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");


        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session, model);

        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao.getAllFavouriteListByUsername(u.getUsername());

        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        String genreId = (String) session.getAttribute("genreId2");

        if (genreId != null) {

            List<MovieTest> movieByGenres = movieService.getMoviesByGenre(genreId);


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

            GenreTest genre = genreDao.getGenreById(Integer.parseInt(genreId));

            model.addAttribute("genreName", genre.getName());

        }  else {
            toViewMoviesByGenreMovieIndex(model, session);
        }


    }



    /// Movie Index
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


    private void favouriteListForGeneralSearchOnNavBar(Model model, HttpSession session, User user) {

        FavoriteListDao favoriteListDao1 = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao1.getAllFavouriteListByUsername(user.getUsername());
        String query = (String) session.getAttribute("query1");

        List<MovieTest> movieBySearch = movieService.getMoviesBySearch(query);

        List<MovieTest> newMovieBySearch = new ArrayList<>();

        for (int i = 0; i < movieBySearch.size() - 2; i++) {

            if (movieBySearch.get(i).getBackdrop_path() != null) {
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

    private void favouriteListForMovieBySearch(Model model, HttpSession session, User user) {
        FavoriteListDao favoriteListDao1 = new FavouriteListDaoImpl("database.properties");


        ArrayList<FavoriteList> favoriteLists = favoriteListDao1.getAllFavouriteListByUsername(user.getUsername());

        String query = (String) session.getAttribute("query");

        List<MovieTest> movieBySearch = movieService.getMoviesBySearch(query);

        System.out.println(movieBySearch);

        List<MovieTest> newMovieBySearch = new ArrayList<>();

        for (int i = 0; i < movieBySearch.size(); i++) {
            if (movieBySearch.get(i).getBackdrop_path() != null) {
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




    /**
     * Getting the total amount of items in the cart for logged in user
     *
     * @param session holds the logged-in users information
     * @param model   holds attributes for the view
     */

    public void getTotalAmountOfItemsInCart(HttpSession session, Model model) {

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }

}
