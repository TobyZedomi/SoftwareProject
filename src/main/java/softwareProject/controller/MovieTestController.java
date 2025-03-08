package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
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

@Controller
public class MovieTestController {

    @Autowired
    private MovieService movieService;

    /*

    @GetMapping("/api/movie")
    public ResponseEntity<List<MovieTest>> getMovies(){
        List<MovieTest> movies = movieService.getMovies();

        if (movies.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

     */

    /*
    @GetMapping("/apiMovie")
    public String getMostPopularMovies( Model model, HttpSession session){
        // add these two lines into login page
        List<MovieTest> movies = movieService.getMovies();
        model.addAttribute("movies", movies);
        return "index";
    }

     */

@GetMapping("/movieTrailer")
    public String getMovieTrailer(Model model, @RequestParam(name = "id") String id, HttpSession session){

    int movieId = Integer.parseInt(id);

    // totalAmountOItems in basket
    getTotalAmountOfItemsInCart(session,model);


    List<MovieTrailer> trailers = movieService.getTrailer(movieId);

    if (trailers.isEmpty()){

        return "noVideo";
    }
    model.addAttribute("trailers", trailers);

    return "videos";
}


    @GetMapping("/viewMovieByGenre")
    public String viewMovieGenre(HttpSession session, Model model, @RequestParam(name = "id") int id){

        String genre_id = Integer.toString(id);

        // totalAmountOItems in basket
        getTotalAmountOfItemsInCart(session,model);


        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        List<MovieTest> movieByGenres = movieService.getMoviesByGenre(genre_id);

        List<MovieTest> newMovie = new ArrayList<>();

        for (int i = 0; i < movieByGenres.size() -2;i++) {

            if (movieByGenres.get(i).getBackdrop_path() != null) {
                newMovie.add(movieByGenres.get(i));
                model.addAttribute("movieByGenres", newMovie);
            }
        }

        GenreDao genreDao = new GenreDaoImpl("database.properties");

        GenreTest genre = genreDao.getGenreById(id);

        model.addAttribute("genreName", genre.getName());

        return "movie_index";
    }



    @GetMapping("/viewMovieBySearch")
    public String viewMovieBySearch(HttpSession session, Model model, @RequestParam(name = "query") String query){



        // totalAmountOItems in basket
        getTotalAmountOfItemsInCart(session,model);

        List<MovieTest> movieBySearch = movieService.getMoviesBySearch(query);

        List<MovieTest> newMovieBySearch = new ArrayList<>();

        for (int i = 0; i < movieBySearch.size() -2;i++){

            if (movieBySearch.get(i).getBackdrop_path() != null) {
                newMovieBySearch.add(movieBySearch.get(i));
                model.addAttribute("movieBySearch", newMovieBySearch);
            }
        }

        model.addAttribute("query", query);


        return "search_index";
    }






    // total amount of items in cart
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
