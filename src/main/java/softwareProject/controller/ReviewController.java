package softwareProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import softwareProject.business.Review;
import softwareProject.persistence.ReviewDao;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewDao reviewDao;

    @GetMapping("/reviews")
    public String showReviews(Model model) {
        List<Review> reviews = reviewDao.getAllReviews();

          model.addAttribute("reviews", reviews);
        return "reviews";
    }

    @GetMapping("/review-form")
    public String showReviewForm(Model model) {

        model.addAttribute("review", new Review());
             return "review_form";
    }

    @PostMapping("/submit-review")
    public String submitReview(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("content") String content, Model model) {
        Review review = new Review();

        review.setName(name);
        review.setEmail(email);
        review.setContent(content);
        review.setCreatedAt(LocalDateTime.now());

        reviewDao.saveReview(review);

        model.addAttribute("message", "Thank you for your review!");
        return "redirect:/reviews";
    }
}
