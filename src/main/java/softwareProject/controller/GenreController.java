package softwareProject.controller;

import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import softwareProject.business.Genre;
import softwareProject.persistence.GenreDao;
import softwareProject.persistence.GenreDaoImpl;

import java.util.ArrayList;

@Slf4j
@Controller
public class GenreController {
    @PostMapping("/viewAllGenres")
    public String viewAllGenres(Model model){
        GenreDao genreDao = new GenreDaoImpl("database.properties");
        ArrayList<Genre> genres = genreDao.getAllGenres();
        model.addAttribute("genres",genres);
        return "index";
    }
}
