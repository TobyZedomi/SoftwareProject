package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.*;
import softwareProject.service.MovieService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
public class SubscriptionController {

    @Autowired
    private MovieService movieService;


    /**
     * Adds a yearly subscription for the user when new user is registered
     * @param session is the users details being held when registration is complete
     * @param model stores data
     * @return index page to go to the home page of the system if purchased subscription was a success or goes to the subscriptionFailed page if not a success
     */
    @PostMapping("addSubscriptionForUser")
    public String addSubscriptionForUser(HttpSession session,
                                         @RequestParam(name = "cardNumber") String cardNumber,
                                         @RequestParam(name = "Month") String Month,
                                         @RequestParam(name = "Year") String Year,
                                         @RequestParam(name = "cvv") String cvv,
                                              Model model) {

        String view = "";

        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl("database.properties");
        ArrayList<Subscription> allSubscriptions = subscriptionDao.getAllSubscriptions();


        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){

            modelCreditCard(cardNumber, Month, Year, cvv, model);

            System.out.println("Card Number must be a valid Visa credit card number");
            return  "purchaseSubscription";
        }


        if (Month.isBlank()){

            System.out.println("You must choose a month");

            modelCreditCard(cardNumber, Month, Year, cvv, model);
            return  "purchaseSubscription";
        }

        if (Year.isBlank()){

            System.out.println("You must choose a year");

            modelCreditCard(cardNumber, Month, Year, cvv, model);
            return  "purchaseSubscription";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){

            System.out.println("Cvv number must be 3 or 4 numbers long");

            modelCreditCard(cardNumber, Month, Year, cvv, model);
            return  "purchaseSubscription";
        }


        // session for user
        User user = (User) session.getAttribute("loggedInUser");

        // session for subscription to get the subscription id
        SubscriptionPlan subscriptionPlan = (SubscriptionPlan) session.getAttribute("subscriptionPicked");

        /*
        for (int i = 0; i < allSubscriptions.size(); i++) {

            if (allSubscriptions.get(i).getUsername().equals(user.getUsername())) {
                view = "subscriptionFailed";
            }
        }

         */

        LocalDateTime date = LocalDateTime.now();
        LocalDateTime endDate = date.plusYears(1);
        Subscription subscription = new Subscription(user.getUsername(),subscriptionPlan.getSubscription_plan_id(), LocalDateTime.now(), endDate);
        int added = subscriptionDao.addSubscription(subscription);

        if (added == -1) {

            view =  "subscriptionFailed";
        } else {
            log.info("User {} purchased subscription", user.getUsername());
            toViewMoviesFromMovieDbApi(model);
            view =  "registerSuccessUser";
        }

        return view;
    }

    private void toViewMoviesFromMovieDbApi(Model model) {
        List<MovieTest> movies = movieService.getMovies();
        model.addAttribute("movies", movies);

        for (int i = 0; i < movies.size(); i++) {

            List<MovieTrailer> trailers = movieService.getTrailer(movies.get(i).getId());
            model.addAttribute("trailers", trailers);
        }
    }

    private static void modelCreditCard(String cardNumber, String Month, String Year, String cvv, Model model) {
        model.addAttribute("cardNumber", cardNumber);
        model.addAttribute("Month", Month);
        model.addAttribute("Year", Year);
        model.addAttribute("cvv", cvv);
    }


/// subscription for if user registered but doesn't have a subscription

    /**
     * Adds a yearly subscription for the user when new user is registered
     * @param session is the users details being held when registration is complete
     * @param model stores data
     * @return index page to go to the home page of the system if purchased subscription was a success or goes to the subscriptionFailed page if not a success
     */
    @PostMapping("addSubscriptionForUserPart2")
    public String addSubscriptionForUserPart2(HttpSession session,
                                         @RequestParam(name = "cardNumber") String cardNumber,
                                         @RequestParam(name = "Month") String Month,
                                         @RequestParam(name = "Year") String Year,
                                         @RequestParam(name = "cvv") String cvv,
                                         Model model) {

        String view = "";

        // totalAmountOItems in basket
        getTotalAmountOfItemsInCart(session,model);


        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl("database.properties");
        ArrayList<Subscription> allSubscriptions = subscriptionDao.getAllSubscriptions();


        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){

            modelCreditCard(cardNumber, Month, Year, cvv, model);

            System.out.println("Card Number must be a valid Visa credit card number");
            return  "purchaseSubscriptionPart2";
        }


        if (Month.isBlank()){

            modelCreditCard(cardNumber, Month, Year, cvv, model);

            System.out.println("You must choose a month");
            return  "purchaseSubscriptionPart2";
        }

        if (Year.isBlank()){

            modelCreditCard(cardNumber, Month, Year, cvv, model);

            System.out.println("You must choose a year");
            return  "purchaseSubscriptionPart2";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){

            modelCreditCard(cardNumber, Month, Year, cvv, model);

            System.out.println("Cvv number must be 3 or 4 numbers long");
            return  "purchaseSubscriptionPart2";
        }


        // session for user
        User user = (User) session.getAttribute("loggedInUser");

        // session for subscription to get the subscription id
        SubscriptionPlan subscriptionPlan = (SubscriptionPlan) session.getAttribute("subscriptionPicked");

        /*

        for (int i = 0; i < allSubscriptions.size(); i++) {

            if (allSubscriptions.get(i).getUsername().equals(user.getUsername())) {

                Subscription subbedUser = subscriptionDao.getSubscriptionFromUsername(user.getUsername());
                session.setAttribute("subbedUser", subbedUser);
                return "subscriptionFailed";
            }
        }



         */

        LocalDateTime date = LocalDateTime.now();
        LocalDateTime endDate = date.plusYears(1);
        Subscription subscription = new Subscription(user.getUsername(),subscriptionPlan.getSubscription_plan_id(), LocalDateTime.now(), endDate);
        int added = subscriptionDao.addSubscription(subscription);

        if (added == -1) {

            view =  "subscriptionFailed";
        } else {
            log.info("User {} purchased subscription", user.getUsername());
            view =  "purchaseSubDetails";
        }

        return view;
    }



    // total amount of items in cart
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
