package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import softwareProject.business.Rating;
import softwareProject.business.User;
import softwareProject.persistence.RatingDao;
import softwareProject.persistence.RatingDaoImpl;

import java.util.List;

@Slf4j
@Controller
public class RatingController {

    @GetMapping("/highestRated")
    public String highestRatedMovies(HttpSession session, Model model) {

        if (session.getAttribute("loggedInUser") != null) {
            User u = (User) session.getAttribute("loggedInUser");


            RatingDao ratingDao = new RatingDaoImpl();
            List<Rating> highestRated = ratingDao.getHighestRatedMovies();

            model.addAttribute("ratings", highestRated);
            return "highest_rated_movies";
        }

        return "redirect:/login";
    }

    @GetMapping("/lowestRated")
    public String lowestRatedMovies(HttpSession session, Model model) {

        if (session.getAttribute("loggedInUser") != null) {
            User u = (User) session.getAttribute("loggedInUser");


            RatingDao ratingDao = new RatingDaoImpl();
            List<Rating> lowestRated = ratingDao.getLowestRatedMovies();

            model.addAttribute("ratings", lowestRated);
            return "lowest_rated_movies";
        }


        return "redirect:/login";
    }
}
