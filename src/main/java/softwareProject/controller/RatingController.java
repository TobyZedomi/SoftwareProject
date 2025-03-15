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
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            log.info("User" +loggedInUser.getUsername() + "is viewing the highest-rated movies.");

            RatingDao ratingDao = new RatingDaoImpl();
            List<Rating> highestRated = ratingDao.getHighestRatedMovies();

            log.info("Got" +highestRated.size() + "highest-rated movies from the database.");

            model.addAttribute("ratings", highestRated);
            return "highest_rated_movies";
        }

        log.info("You do not have access to see highest-rated movies. Redirecting to login.");
        return "redirect:/login";
    }

    @GetMapping("/lowestRated")
    public String lowestRatedMovies(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            log.info("User '{}' is viewing the lowest-rated movies.", loggedInUser.getUsername());

            RatingDao ratingDao = new RatingDaoImpl();
            List<Rating> lowestRated = ratingDao.getLowestRatedMovies();

            log.info("The user got  {} lowest-rated movies from the database.", lowestRated.size());

            model.addAttribute("ratings", lowestRated);
            return "lowest_rated_movies";
        }

        log.info("You do not have access to see lowest-rated movies. Redirecting to login.");
        return "redirect:/login";
    }
}
