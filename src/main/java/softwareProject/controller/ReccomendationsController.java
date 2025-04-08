package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.CartDao;
import softwareProject.persistence.CartDaoImpl;
import softwareProject.persistence.CartItemDao;
import softwareProject.persistence.CartItemDaoImpl;
import softwareProject.service.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@Slf4j
public class ReccomendationsController {


    @Autowired
    private MovieService movieService;

    @GetMapping("/movieRecs")
    public String movieRecs(HttpSession session, Model model){

        if(session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");


          //  int id = Integer.parseInt(movieId);

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            int[] arr = new int[] {822119, 429617, 939243, 661539, 539972, 1084199, 1412113, 51482, 1357633, 1020414, 210577, 146233};
            System.out.print("Random number from the array = "+arr[new Random().nextInt(arr.length)]);

            List<MovieRecommendations> movieRecs = movieService.getRecommendations(arr[new Random().nextInt(arr.length)]);

            List<MovieRecommendations> newMovie = new ArrayList<>();

            for (int i = 0; i < 15; i++) {

                if (movieRecs.get(i).getBackdrop_path() != null) {
                    newMovie.add(movieRecs.get(i));
                    model.addAttribute("movieRecs",newMovie);
                }

            }



            return "movie_recs";

        }

        return "notValidUser";
    }



    public void getTotalAmountOfItemsInCart(HttpSession session,Model model){

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }

}
