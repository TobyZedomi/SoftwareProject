package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Movie;
import softwareProject.persistence.MovieDao;
import softwareProject.persistence.MovieDaoImpl;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

@Controller
public class MovieController {

    @RequestMapping("/viewByGenre")
    public String viewByGenre(@RequestParam(name = "genre") String genre, Model model){
        int genreID = Integer.parseInt(genre);
        MovieDao movieDao = new MovieDaoImpl("database.properties");
        ArrayList<Movie> movies = movieDao.findMovieByGenre(genreID);
        model.addAttribute("movies",movies);
        return "movies";

            }

}
