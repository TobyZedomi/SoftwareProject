package softwareProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.GenreTest;
import softwareProject.business.MovieTest;
import softwareProject.business.MovieTrailer;
import softwareProject.service.MovieService;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private MovieService movieService;


    @GetMapping("/")
    public String userIndex(){
        return "user_index";
    }

    @GetMapping("/user_indexSignUp")
    public String userIndexSignUp(){
        return "user_indexSignUp";
    }

    // add these .add model from list of movies from movie db into index controller
    @GetMapping("/index")
    public String home(Model model){

        List<MovieTest> movies = movieService.getMovies();
        model.addAttribute("movies", movies);

        for (int i = 0; i < movies.size();i++) {

            List<MovieTrailer> trailers = movieService.getTrailer(movies.get(i).getId());
            model.addAttribute("trailers", trailers);
        }


        return "index";
    }

    @GetMapping("/movie_index")
    public String movieIndex(Model model){


        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        List<MovieTest> movieByGenres = movieService.getMoviesByGenre("878");
        model.addAttribute("movieByGenres", movieByGenres);


        for (int i = 0; i < movieByGenres.size();i++) {

            List<MovieTrailer> trailers = movieService.getTrailer(movieByGenres.get(i).getId());
            model.addAttribute("trailers", trailers);
        }

        return "movie_index";
    }

    @GetMapping("/logout_index")
    public String userIndex2(){
        return "logout_index";
    }


    @GetMapping("/registerSuccessUser")
    public String regIndex(){
        return "registerSuccessUser";
    }


    @GetMapping("/subscription_index")
    public String subscriptionIndex(){
        return "subscription_index";
    }


}
