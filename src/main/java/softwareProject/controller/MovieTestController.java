package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import softwareProject.business.MovieTest;
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




}
