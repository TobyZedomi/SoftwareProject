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
import softwareProject.service.EmailSenderService;
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


    @Autowired
    private EmailSenderService senderService;

    /**
     * Sends email top the user for there subscription purchased
     * @param userEmail is the users email that its being sent to
     * @param subject is the subject of what the email is about
     * @param body is what is in the email body
     */
    public void sendMail(String userEmail, String subject, String body ){

        senderService.sendEmail(userEmail, subject, body);
    }

    /**
     * User being able to purchase a subscription based on the subscription plan they chose upon registration. User will be sent email confirmation of purchase
     * @param session holds the logged in users details
     * @param fullName is the fullName being entered by the user
     * @param email is the email being entered by the user
     * @param address is the address being entered by the user
     * @param city is the city being entered by the user
     * @param county is the county being entered by the user
     * @param postcode is the postcode being entered by the user
     * @param cardName is the cardName being entered by the user
     * @param cardNumber is the cardNumber being entered by the user
     * @param Month is the Month being entered by the user
     * @param Year is the year being entered by the user
     * @param cvv is the cvv being entered by the user
     * @param model holds the attributes for the view
     * @return purchase subscription page if validation is wrong but if subscription is a success return the confirmation page
     */
    @PostMapping("addSubscriptionForUser")
    public String addSubscriptionForUser(HttpSession session,
                                         @RequestParam(name="fullName") String fullName,
                                         @RequestParam(name = "email") String email,
                                         @RequestParam(name="address") String address,
                                         @RequestParam(name="city") String city,
                                         @RequestParam(name="county") String county,
                                         @RequestParam(name="postcode") String postcode,
                                         @RequestParam(name = "cardName") String cardName,
                                         @RequestParam(name = "cardNumber") String cardNumber,
                                         @RequestParam(name = "Month") String Month,
                                         @RequestParam(name = "Year") String Year,
                                         @RequestParam(name = "cvv") String cvv,
                                              Model model) {

        String view = "";

        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl("database.properties");
        ArrayList<Subscription> allSubscriptions = subscriptionDao.getAllSubscriptions();


        String purchaseSubscription = validationForAddSubscription(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);
        if (purchaseSubscription != null) return purchaseSubscription;


        // session for user
        User user = (User) session.getAttribute("loggedInUser");

        // user to be added if billing User doesn't exist in billingAddress table

        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");

        // update email

        BillingAddress billingUser = new BillingAddress(0, user.getUsername(),fullName, email, address, city, county, postcode);
        billingAddressDao.addBillingAddress(billingUser);

        // session for subscription to get the subscription id
        SubscriptionPlan subscriptionPlan = (SubscriptionPlan) session.getAttribute("subscriptionPicked");

        LocalDateTime date = LocalDateTime.now();
        LocalDateTime endDate = date.plusYears(1);
        Subscription subscription = new Subscription(user.getUsername(),subscriptionPlan.getSubscription_plan_id(), LocalDateTime.now(), endDate);
        int added = subscriptionDao.addSubscription(subscription);

        model.addAttribute("subPrice", subscriptionPlan.getCost());
        model.addAttribute("orderSubDate", subscription.getSubscription_startDate());
        model.addAttribute("subName", subscriptionPlan.getSubscription_plan_name());

        String bodyEmail = subscriptionPlan.getSubscription_plan_name() + " for the price of: "+ "£"+ subscriptionPlan.getCost();

        sendMail(user.getEmail(), "Purchased Subscription",  bodyEmail);

        if (added == -1) {

            view =  "subscriptionFailed";
        } else {
            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session,model);
            log.info("User {} purchased subscription", user.getUsername());
            toViewMoviesFromMovieDbApi(model);
            view =  "subscriptionConfirmPayment2";
        }

        return view;
    }


    /**
     * Validation for the subscription
     * @param fullName is the fullName being entered by the user
     * @param email is the email being entered by the user
     * @param address is the address being entered by the user
     * @param city is the city being entered by the user
     * @param county is the county being entered by the user
     * @param postcode is the postcode being entered by the user
     * @param cardName is the cardName being entered by the user
     * @param cardNumber is the cardNumber being entered by the user
     * @param Month is the Month being entered by the user
     * @param Year is the year being entered by the user
     * @param cvv is the cvv being entered by the user
     * @param model the attributes for the view
     * @return purchase Subscription is validation is incorrect
     */
    private static String validationForAddSubscription(String fullName, String email, String address, String city, String county, String postcode, String cardName, String cardNumber, String Month, String Year, String cvv, Model model) {
        // validation for billingAddress

        if (fullName.isBlank()){
            // message for error
            String message1 = "Full Name was left blank";
            model.addAttribute("message1",message1);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        Pattern fullNameRegex = Pattern.compile("^[a-zA-Z]{3,25}.*[\\s\\.]*$");
        Matcher match12 = fullNameRegex.matcher(fullName);
        boolean matchfoundFullName = match12.find();

        if (!matchfoundFullName){

            System.out.println("Full Name must be between 3-25 characters, letters only");
            // message for error
            String message1 = "Full Name must be between 3-25 characters, letters only";
            model.addAttribute("message1",message1);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        if (email.isBlank()){
            // message for error
            String messageEmail = "Email was left blank";
            model.addAttribute("messageEmail",messageEmail);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);            // method to get Cart information with movies and pricing


            return "purchaseSubscription";
        }

        if (address.isBlank()){

            System.out.println("You must choose an address");
            // message for error
            String message2 = "Address was left blank";
            model.addAttribute("message2",message2);
            // method to get Cart information with movies and pricing

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        if (city.isBlank()){

            System.out.println("You must choose a city");
            // message for error
            String message3 = "City was left blank";
            model.addAttribute("message3",message3);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        if (county.isBlank()){

            System.out.println("You must choose a county");
            // message for error
            String message4 = "County was left blank";
            model.addAttribute("message4",message4);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        if (postcode.isBlank()){

            System.out.println("You must choose a postcode");
            // message for error
            String message5 = "Postcode was left blank";
            model.addAttribute("message5",message5);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        // validation for cardDetails

        if (cardName.isBlank()){
            System.out.println("Card Name was left blank");
            // message for error
            String message6 = "Card Name was left blank";
            model.addAttribute("message6",message6);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            return "purchaseSubscription";

        }

        Pattern cardNameRegex = Pattern.compile("^[a-zA-Z]{3,25}.*[\\s\\.]*$");
        Matcher match11 = cardNameRegex.matcher(cardName);
        boolean matchfoundCardName = match11.find();

        if (!matchfoundCardName){

            System.out.println("Card Name must be between 3-25 characters, letters only");
            // message for error
            String message6 = "Card Name must be between 3-25 characters, letters only";
            model.addAttribute("message6",message6);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }


        if (cardNumber.isBlank()){
            System.out.println("Card Number was left blank");
            // message for error
            String message7 = "Card Number was left blank";
            model.addAttribute("message7",message7);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){

            System.out.println("Card Number must be a valid Visa credit card number");
            // message for error
            String message7 = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message7",message7);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }


        if (Month.isBlank()){

            System.out.println("You must choose a month");
            // message for error
            String message8 = "Month was left blank";
            model.addAttribute("message8",message8);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }

        if (Year.isBlank()){

            System.out.println("You must choose a year");
            // message for error
            String message9 = "Year was left blank";
            model.addAttribute("message9",message9);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){

            System.out.println("Cvv number must be 3 or 4 numbers long");
            // message for error
            String message10 = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message10",message10);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            return "purchaseSubscription";
        }
        return null;
    }


    /**
     * To view the most popular movies for after the user registers
     * @param model holds the attributes for the view
     */
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


    /**
     * Model attribute names for the billing address
     * @param fullName is the fullName being entered by the user
     * @param email is the email being entered by the user
     * @param address is the address being entered by the user
     * @param city is the city being entered by the user
     * @param county is the county being entered by the user
     * @param postcode is the postcode being entered by the user
     * @param cardName is the cardName being entered by the user
     * @param cardNumber is the cardNumber being entered by the user
     * @param Month is the Month being entered by the user
     * @param Year is the year being entered by the user
     * @param cvv is the cvv being entered by the user
     * @param model holds the attributes for the view
     */
    private static void modelValidationBillingAddress(String fullName,String email, String address, String city, String county, String postcode, String cardName, String cardNumber, String Month, String Year, String cvv, Model model) {
        model.addAttribute("fullName", fullName);
        model.addAttribute("email", email);
        model.addAttribute("address", address);
        model.addAttribute("city", city);
        model.addAttribute("county", county);
        model.addAttribute("postcode", postcode);
        model.addAttribute("cardName", cardName);
        model.addAttribute("cardNumber", cardNumber);
        model.addAttribute("Month", Month);
        model.addAttribute("Year", Year);
        model.addAttribute("cvv", cvv);
    }


/// subscription for if user registered but doesn't have a subscription

    /**
     * User can purchase subscription if they decided to skip it upon registration. User will be sent email confirmation of purchase
     * @param session holds the logged in users details
     * @param fullName is the fullName being entered by the user
     * @param email is the email being entered by the user
     * @param address is the address being entered by the user
     * @param city is the city being entered by the user
     * @param county is the county being entered by the user
     * @param postcode is the postcode being entered by the user
     * @param cardName is the cardName being entered by the user
     * @param cardNumber is the cardNumber being entered by the user
     * @param Month is the Month being entered by the user
     * @param Year is the year being entered by the user
     * @param cvv is the cvv being entered by the user
     * @param model holds the attributes for the view
     * @return confirmation page once subscription is purchased
     */
    @PostMapping("addSubscriptionForUserPart2")
    public String addSubscriptionForUserPart2(HttpSession session,
                                              @RequestParam(name="fullName") String fullName,
                                              @RequestParam(name = "email") String email,
                                              @RequestParam(name="address") String address,
                                              @RequestParam(name="city") String city,
                                              @RequestParam(name="county") String county,
                                              @RequestParam(name="postcode") String postcode,
                                              @RequestParam(name = "cardName") String cardName,
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


        String purchaseSubscriptionPart2 = validationForPurchaseSubPart2(session, fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);
        if (purchaseSubscriptionPart2 != null) return purchaseSubscriptionPart2;


        // session for user
        User user = (User) session.getAttribute("loggedInUser");

        // user to be added if billing User doesn't exist in billingAddress table

        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");

        // update email

        BillingAddress billingUser = new BillingAddress(0, user.getUsername(),fullName, email, address, city, county, postcode);
        billingAddressDao.addBillingAddress(billingUser);

        // session for subscription to get the subscription id
        SubscriptionPlan subscriptionPlan = (SubscriptionPlan) session.getAttribute("subscriptionPicked");

        LocalDateTime date = LocalDateTime.now();
        LocalDateTime endDate = date.plusYears(1);
        Subscription subscription = new Subscription(user.getUsername(),subscriptionPlan.getSubscription_plan_id(), LocalDateTime.now(), endDate);
        int added = subscriptionDao.addSubscription(subscription);

        model.addAttribute("subPrice", subscriptionPlan.getCost());
        model.addAttribute("orderSubDate", subscription.getSubscription_startDate());
        model.addAttribute("subName", subscriptionPlan.getSubscription_plan_name());

        String bodyEmail = subscriptionPlan.getSubscription_plan_name() + " for the price of: "+ "£"+ subscriptionPlan.getCost();

        sendMail(user.getEmail(), "Purchased Subscription",  bodyEmail);

        if (added == -1) {

            view =  "subscriptionFailed";
        } else {
            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session,model);
            log.info("User {} purchased subscription", user.getUsername());
            view =  "subscriptionConfirmPayment";
        }

        return view;
    }

    private String validationForPurchaseSubPart2(HttpSession session, String fullName, String email, String address, String city, String county, String postcode, String cardName, String cardNumber, String Month, String Year, String cvv, Model model) {
        if (fullName.isBlank()){
            // message for error
            String message1 = "Full Name was left blank";
            model.addAttribute("message1",message1);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        Pattern fullNameRegex = Pattern.compile("^[a-zA-Z]{3,25}.*[\\s\\.]*$");
        Matcher match12 = fullNameRegex.matcher(fullName);
        boolean matchfoundFullName = match12.find();

        if (!matchfoundFullName){

            System.out.println("Full Name must be between 3-25 characters, letters only");
            // message for error
            String message1 = "Full Name must be between 3-25 characters, letters only";
            model.addAttribute("message1",message1);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        if (email.isBlank()){
            // message for error
            String messageEmail = "Email was left blank";
            model.addAttribute("messageEmail",messageEmail);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);            // method to get Cart information with movies and pricing

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        if (address.isBlank()){

            System.out.println("You must choose an address");
            // message for error
            String message2 = "Address was left blank";
            model.addAttribute("message2",message2);
            // method to get Cart information with movies and pricing

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        if (city.isBlank()){

            System.out.println("You must choose a city");
            // message for error
            String message3 = "City was left blank";
            model.addAttribute("message3",message3);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        if (county.isBlank()){

            System.out.println("You must choose a county");
            // message for error
            String message4 = "County was left blank";
            model.addAttribute("message4",message4);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        if (postcode.isBlank()){

            System.out.println("You must choose a postcode");
            // message for error
            String message5 = "Postcode was left blank";
            model.addAttribute("message5",message5);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        // validation for cardDetails

        if (cardName.isBlank()){
            System.out.println("Card Name was left blank");
            // message for error
            String message6 = "Card Name was left blank";
            model.addAttribute("message6",message6);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        Pattern cardNameRegex = Pattern.compile("^[a-zA-Z]{3,25}.*[\\s\\.]*$");
        Matcher match11 = cardNameRegex.matcher(cardName);
        boolean matchfoundCardName = match11.find();

        if (!matchfoundCardName){

            System.out.println("Card Name must be between 3-25 characters, letters only");
            // message for error
            String message6 = "Card Name must be between 3-25 characters, letters only";
            model.addAttribute("message6",message6);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }


        if (cardNumber.isBlank()){
            System.out.println("Card Number was left blank");
            // message for error
            String message7 = "Card Number was left blank";
            model.addAttribute("message7",message7);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){

            System.out.println("Card Number must be a valid Visa credit card number");
            // message for error
            String message7 = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message7",message7);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }


        if (Month.isBlank()){

            System.out.println("You must choose a month");
            // message for error
            String message8 = "Month was left blank";
            model.addAttribute("message8",message8);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);



            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }

        if (Year.isBlank()){

            System.out.println("You must choose a year");
            // message for error
            String message9 = "Year was left blank";
            model.addAttribute("message9",message9);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "purchaseSubscriptionPart2";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){

            System.out.println("Cvv number must be 3 or 4 numbers long");
            // message for error
            String message10 = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message10",message10);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);


            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);
            return "purchaseSubscriptionPart2";
        }
        return null;
    }


    ///// if billing address already exist to purchase subscription


    /**
     * If user already has a billing address with us they use this method that automatically updates there billing address
     * if they enter anything different. User will be sent confirmation email once subscription is purchased
     * @param session holds the logged in users details
     * @param fullName is the fullName being entered by the user
     * @param email is the email being entered by the user
     * @param address is the address being entered by the user
     * @param city is the city being entered by the user
     * @param county is the county being entered by the user
     * @param postcode is the postcode being entered by the user
     * @param cardName is the cardName being entered by the user
     * @param cardNumber is the cardNumber being entered by the user
     * @param Month is the Month being entered by the user
     * @param Year is the year being entered by the user
     * @param cvv is the cvv being entered by the user
     * @param model holds the attributes for the view
     * @return confirmation page once purchased
     */
    @PostMapping("addSubscriptionForUserPart2IfBillingExist")
    public String addSubscriptionForUserPart2IfBillingExist(HttpSession session,
                                              @RequestParam(name="fullName") String fullName,
                                              @RequestParam(name = "email") String email,
                                              @RequestParam(name="address") String address,
                                              @RequestParam(name="city") String city,
                                              @RequestParam(name="county") String county,
                                              @RequestParam(name="postcode") String postcode,
                                              @RequestParam(name = "cardName") String cardName,
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


        String purchaseSubscriptionPart2 = validationForPurchaseSubPart2(session, fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);
        if (purchaseSubscriptionPart2 != null) return purchaseSubscriptionPart2;


        // session for user
        User user = (User) session.getAttribute("loggedInUser");

        // session for subscription to get the subscription id
        SubscriptionPlan subscriptionPlan = (SubscriptionPlan) session.getAttribute("subscriptionPicked");




        // get all billingAddress to see if user already created a billing address

        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");


        ArrayList<BillingAddress> billingAddresses = billingAddressDao.getAllBillingAddress();

        for (int i = 0; i < billingAddresses.size();i++) {


            if (billingAddresses.get(i).getUsername().equals(user.getUsername())) {


                // get billing address by username

                BillingAddress billingAddressByUsername = billingAddressDao.getBillingAddressByUsername(user.getUsername());
                model.addAttribute("billingAddressByUsername", billingAddressByUsername);
                session.setAttribute("billingAddressUser", billingAddressByUsername);

                // update billingAddress details

                billingAddressDao.updateBillingAddressFullName(fullName, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressEmail(email, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateAddressForBillingAddress(address, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressCity(city, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressCounty(county, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressPostCode(postcode, billingAddressByUsername.getBilling_address_id());


        LocalDateTime date = LocalDateTime.now();
        LocalDateTime endDate = date.plusYears(1);
        Subscription subscription = new Subscription(user.getUsername(),subscriptionPlan.getSubscription_plan_id(), LocalDateTime.now(), endDate);
        int added = subscriptionDao.addSubscription(subscription);

                model.addAttribute("subPrice", subscriptionPlan.getCost());
                model.addAttribute("orderSubDate", subscription.getSubscription_startDate());
                model.addAttribute("subName", subscriptionPlan.getSubscription_plan_name());

                String bodyEmail = subscriptionPlan.getSubscription_plan_name() + " for the price of: "+ "£"+ subscriptionPlan.getCost();

                sendMail(user.getEmail(), "Purchased Subscription",  bodyEmail);

        if (added == -1) {

            log.info("User {} didnt purchase subscription", user.getUsername());
            view =  "subscriptionFailed";
        } else {
            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session,model);
            log.info("User {} purchased subscription", user.getUsername());
            view =  "subscriptionConfirmPayment";
        }


            }
        }

        return view;
    }





    // total amount of items in cart

    /**
     * Gets the total amount of items in the users cart
     * @param session holds the logged in users details
     * @param model holds the attributes for the view
     */
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
