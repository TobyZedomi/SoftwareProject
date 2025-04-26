package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import softwareProject.business.User;
import softwareProject.persistence.*;
import softwareProject.business.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class GiftController {

    private final GiftedMovieDao giftedMovieDao = new GiftedMovieDaoImpl();
    private final FriendDao friendDao = new FriendDaoImpl();
    private final CartDao cartDao = new CartDaoImpl();

    @PostMapping("/purchaseMovie")
    public String purchaseMovie(@RequestParam int movieId,
                                @RequestParam int quantity,
                                @RequestParam(required = false) String recipient,
                                HttpSession session,
                                Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        String username = user.getUsername();

        for (int i = 0; i < quantity; i++) {
            if (recipient != null && !recipient.isEmpty() && !recipient.equals(username)) {
                if (friendDao.getAFriend(username, recipient)) {
                    giftedMovieDao.giftMovie(username, recipient, movieId);
                } else {
                    model.addAttribute("error", "You can only gift movies to your friends.");
                    return "purchase_movie";
                }
            } else {
                Cart cart = new Cart();
                cart.setUsername(username);
                cart.setMovieId(movieId);
                cartDao.addCart(cart);
            }
        }

        return "redirect:/store_index";
    }

    @GetMapping("/giftedMovies")
    public String viewGifts(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        String username = user.getUsername();
        model.addAttribute("gifts", giftedMovieDao.getGiftsReceived(username));
        return "gifted_movies";
    }
}