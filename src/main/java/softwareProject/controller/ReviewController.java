package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import softwareProject.business.Review;
import softwareProject.business.User;
import softwareProject.persistence.ReviewDao;
import softwareProject.persistence.ReviewDaoImpl;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
public class ReviewController {

    @GetMapping("/writeReview")
    public String showReviewForm(@RequestParam("movieId") int movieId,
                                 @RequestParam("movieTitle") String movieTitle,
                                 Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("movieTitle", movieTitle);
        return "write_review";
    }

    @PostMapping("/submitReview")
    public String submitReview(HttpSession session,
                               @RequestParam("movieId") int movieId,
                               @RequestParam("movieTitle") String movieTitle,
                               @RequestParam("content") String content,
                               @RequestParam("rating") int rating) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        String username = loggedInUser.getUsername();

        Review review = new Review(username, movieId, movieTitle, content, rating, LocalDateTime.now());


        ReviewDao reviewDao = new ReviewDaoImpl();
        reviewDao.saveReview(review);

        return "redirect:/reviews";
    }

    @PostMapping("/updateReview")
    public String updateReview(HttpSession session,
                               @RequestParam("movieId") int movieId,
                               @RequestParam("content") String content,
                               @RequestParam("rating") int rating,
                               Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        String username = loggedInUser.getUsername();
        Review review = new Review(username, movieId, "", content, rating, LocalDateTime.now());


        ReviewDao reviewDao = new ReviewDaoImpl();
        int rowsAffected = reviewDao.updateReview(review);


        if (rowsAffected > 0) {
            return "redirect:/reviews";
        } else if (rowsAffected == -1) {
            model.addAttribute("error", "Update failed due to a database constraint.");
            return "error";
        } else {
            model.addAttribute("error", "No review was updated. Please try again.");
            return "error";
        }
    }


    @PostMapping("/deleteReview")
    public String deleteReview(HttpSession session,
                               @RequestParam("movieId") int movieId,
                               Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        String username = loggedInUser.getUsername();


        ReviewDao reviewDao = new ReviewDaoImpl();
        int rowsAffected = reviewDao.deleteReview(username, movieId);


        if (rowsAffected > 0) {
            return "redirect:/reviews";
        } else if (rowsAffected == -1) {
            model.addAttribute("error", "Cannot delete review because of a database constraint.");
            return "errorPage";
        } else {
            model.addAttribute("error", "Review deletion failed.");
            return "errorPage";
        }
    }


    @GetMapping("/userReviews")
    public String userReviews(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        String username = loggedInUser.getUsername();


        ReviewDao reviewDao = new ReviewDaoImpl();
        List<Review> reviews = reviewDao.getReviewsByUser(username);
        model.addAttribute("reviews", reviews);

        return "user_reviews";
    }

    @GetMapping("/reviews")
    public String showReviews(Model model) {

        ReviewDao reviewDao = new ReviewDaoImpl();
        List<Review> reviews = reviewDao.getAllReviews();
        model.addAttribute("reviews", reviews);

        return "reviews";
    }
}
