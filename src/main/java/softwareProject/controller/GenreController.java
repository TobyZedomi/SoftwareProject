package softwareProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Genre;
import softwareProject.business.GenreTest;
import softwareProject.business.MovieTrailer;
import softwareProject.persistence.GenreDao;
import softwareProject.persistence.GenreDaoImpl;
import softwareProject.service.MovieService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class GenreController {

    @Autowired
    private MovieService movieService;

    /*
    @PostMapping("/viewAllGenres")
    public String viewAllGenres(Model model){
        GenreDao genreDao = new GenreDaoImpl("database.properties");
        ArrayList<Genre> genres = genreDao.getAllGenres();
        model.addAttribute("genres",genres);
        return "index";
    }

     */


    /**
     * View the genre from the movie db api
     * @param model holds the attributes for the view
     * @return the movie index page
     */

    @GetMapping("/viewGenre")
    public String viewGenre(Model model){


        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        return "movie_index";
    }

// view genre by id

    /**
     * Get the name of teh genre from the movie db api based on its ID
     * @param model holds the attributes for the view
     * @param id is the genre id being entered
     * @return the movie index page
     */
    @GetMapping("/getGenreName")
    public String genreName(Model model, @RequestParam(name = "id") String id){


        int genre_id = Integer.parseInt(id);

        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        for (int i = 0; i < genres.size();i++){

            if (genres.get(i).getId() == genre_id ){

                model.addAttribute("genreName", genres.get(i).getName());
                return "movie_index";
            }
        }

        return "error";
    }

}
