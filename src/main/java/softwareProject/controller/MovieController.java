package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import softwareProject.business.Movie;
import softwareProject.business.User;
import softwareProject.persistence.MovieDao;
import softwareProject.persistence.MovieDaoImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MovieController {

    @GetMapping("/viewByGenre")
    public String viewByGenre(@RequestParam(name = "genre") String genre, Model model){
        try {
            int genreID = Integer.parseInt(genre);
            MovieDao movieDao = new MovieDaoImpl("database.properties");
            ArrayList<Movie> movies = movieDao.findMovieByGenre(genreID);
            model.addAttribute("movies", movies);
        }catch(NumberFormatException e){
            log.info(String.valueOf(e));
        }
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

    @PostMapping("/addMovie")
    public String addMovie(@RequestParam String movie_name,
                           @RequestParam int genre_id,
                           @RequestParam int age_id,
                           @RequestParam String date_of_release,
                           @RequestParam String movie_length,
                           @RequestParam String movie_info,
                           @RequestParam String movie_image,Model model, HttpSession session){
        User u = (User)session.getAttribute("loggedInUser");

        LocalDate releaseDate = Date.valueOf(date_of_release).toLocalDate();
        Time length = java.sql.Time.valueOf(movie_length);
        Movie newMovie = new Movie(movie_name, genre_id, age_id, releaseDate, length, movie_info, movie_image);
        MovieDao movieDao = new MovieDaoImpl("database.properties");
        int complete = movieDao.insertNewMovie(newMovie);
        if(complete>0){
            String message = "Movie added: "+newMovie.getMovie_name();
            model.addAttribute("message", message);
            log.info(message);
        }else{
            String message = "Movie failed to be added";
            model.addAttribute("message",message);
            log.info(message);
        }
        return "movieUpdate";
    }
    @PostMapping("/deleteMovie")
    public String deleteMovie(@RequestParam String movie_name, Model model, HttpSession session){
        User u = (User)session.getAttribute("loggedInUser");
        MovieDao movieDao = new MovieDaoImpl("database.properties");
        Movie movie = movieDao.searchForMovieByName(movie_name);
        int complete = movieDao.deletingAMovie(movie.getMovie_id());
        if(complete>0){
            String message = "Movie deleted: "+movie_name;
            model.addAttribute("message", message);
            log.info(message);
        }else{
            String message = "Movie failed to delete";
            model.addAttribute("message",message);
            log.info(message);
        }
        return "movieUpdate";



    }

}
