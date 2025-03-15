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
        log.info("Going to the review form for movie: {}", movieTitle);
        return "write_review";
    }

    @PostMapping("/submitReview")
    public String submitReview(HttpSession session,
                               @RequestParam("movieId") int movieId,
                               @RequestParam("movieTitle") String movieTitle,
                               @RequestParam("content") String content,
                               @RequestParam("rating") int rating,
                               Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            log.info("User not logged in. Redirecting to login page.");
            return "login";
        }

        String username = loggedInUser.getUsername();

        // Validation
        if (content.isBlank()) {
            String message = "Review content cannot be empty.";
            model.addAttribute("message", message);
            log.info("Validation failed: {}", message);
            return "write_review";
        }

        if (rating < 1 || rating > 5) {
            String message = "Rating must be between 1 and 5.";
            model.addAttribute("message", message);
            log.info("Validation failed: {}", message);
            return "write_review";
        }

          Review review = new Review(username, movieId, movieTitle, content, rating, LocalDateTime.now());

        ReviewDao reviewDao = new ReviewDaoImpl();
         int result = reviewDao.saveReview(review);

        log.info("User '{}' attempted to submit a review for '{}'. Result: {}", username, movieTitle, result > 0 ? "Success" : "Failed");

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
            log.info("User not logged in. Redirecting to login page.");
            return "login";
        }

        String username = loggedInUser.getUsername();

        // Validation
        if (content.isBlank()) {
            String message = "Review content cannot be empty.";
            model.addAttribute("message", message);
            log.info("Validation failed:" + message);
            return "write_review";
        }

        if (rating < 1 || rating > 5) {
            String message = "Rating must be between 1 and 5.";
            model.addAttribute("message", message);
            log.info("Validation failed: " + message);
            return "write_review";
        }

        ReviewDao reviewDao = new ReviewDaoImpl();
        Review review = new Review(username, movieId, "", content, rating, LocalDateTime.now());

        int result = reviewDao.updateReview(review);
        log.info("User " + username + " attempted to update review for movie ID " + movieId + ". Result: " + (result > 0 ? "Success" : "Failed"));

        return "redirect:/reviews";
    }

    @PostMapping("/deleteReview")
    public String deleteReview(HttpSession session,
                               @RequestParam("movieId") int movieId,
                               Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            log.info("User not logged in. Redirecting to login page.");
            return "/login";
        }

        String username = loggedInUser.getUsername();
        ReviewDao reviewDao = new ReviewDaoImpl();
        int result = reviewDao.deleteReview(username, movieId);

        log.info("User " + username + " attempted to delete review for movie ID " + movieId + ". Result: " + (result > 0 ? "Success" : "Failed"));


        return "redirect:/reviews";
    }


    @GetMapping("/userReviews")
    public String userReviews(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "/login";
        }

        String username = loggedInUser.getUsername();


        ReviewDao reviewDao = new ReviewDaoImpl();
        List<Review> reviews = reviewDao.getReviewsByUsername(username);
        model.addAttribute("reviews", reviews);
        log.info("User " + username + " viewed their reviews. Total reviews: " + reviews.size());

        return "user_reviews";
    }

    @GetMapping("/reviews")
    public String showReviews(Model model) {

        ReviewDao reviewDao = new ReviewDaoImpl();
        List<Review> reviews = reviewDao.getAllReviews();
        model.addAttribute("reviews", reviews);

        log.info("Displaying all reviews. Total reviews: " + reviews.size());
        return "reviews";
    }
}
