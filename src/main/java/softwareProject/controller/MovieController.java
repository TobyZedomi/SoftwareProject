package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import softwareProject.business.Movie;
import softwareProject.persistence.MovieDao;
import softwareProject.persistence.MovieDaoImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Controller
@Slf4j
public class MovieController {

    @GetMapping("/viewByGenre")
    public String viewByGenre(@RequestParam(name = "genre") String genre, Model model){
        int genreID = Integer.parseInt(genre);
        MovieDao movieDao = new MovieDaoImpl("database.properties");
        ArrayList<Movie> movies = movieDao.findMovieByGenre(genreID);
        model.addAttribute("movies",movies);
        return "index";

            }

    @GetMapping("/viewMovies")
    public String viewMovies(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            MovieDao movieDao = new MovieDaoImpl("database.properties");
            List<Movie> movies = movieDao.getAllMovies();
            model.addAttribute("movies", movies);
            return "movies";
        }
        return "error";
    }

}
