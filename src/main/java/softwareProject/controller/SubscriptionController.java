package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Cart;
import softwareProject.business.Subscription;
import softwareProject.business.SubscriptionPlan;
import softwareProject.business.User;
import softwareProject.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
public class SubscriptionController {


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
            String message = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message", message);
            System.out.println("Card Number must be a valid Visa credit card number");
            return  "purchaseSubscription";
        }


        if (Month.isBlank()){
            String message = "You must choose a month";
            model.addAttribute("message", message);
            System.out.println("You must choose a month");
            return  "purchaseSubscription";
        }

        if (Year.isBlank()){
            String message = "You must choose a Year";
            model.addAttribute("message", message);
            System.out.println("You must choose a year");
            return  "purchaseSubscription";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){
            String message = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message", message);
            System.out.println("Cvv number must be 3 or 4 numbers long");
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
            view =  "registerSuccessUser";
        }

        return view;
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
            String message = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message", message);
            System.out.println("Card Number must be a valid Visa credit card number");
            return  "purchaseSubscriptionPart2";
        }


        if (Month.isBlank()){
            String message = "You must choose a month";
            model.addAttribute("message", message);
            System.out.println("You must choose a month");
            return  "purchaseSubscriptionPart2";
        }

        if (Year.isBlank()){
            String message = "You must choose a Year";
            model.addAttribute("message", message);
            System.out.println("You must choose a year");
            return  "purchaseSubscriptionPart2";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){
            String message = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message", message);
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
