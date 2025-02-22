package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.CartDao;
import softwareProject.persistence.CartDaoImpl;
import softwareProject.persistence.CartItemDao;
import softwareProject.persistence.CartItemDaoImpl;
import softwareProject.service.MovieService;

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
        model.addAttribute("movieByGenres", movieByGenres);

        return "movie_index";
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
