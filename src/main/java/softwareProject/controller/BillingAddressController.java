package softwareProject.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.*;
import softwareProject.service.EmailSenderService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class BillingAddressController {


    @Autowired
    private EmailSenderService senderService;

    /**
     * Sending an email to users personal emai
     * @param userEmail is the users email that's its being sent to
     * @param subject is what the email is about
     * @param model holds attributes for the view
     * @param session is used to retrieve the users logged in details
     * @throws MessagingException if something goes wrong with sending a message
     * @throws IOException when file is nowhere to be found
     */
    public void sendMail(String userEmail, String subject, Model model, HttpSession session ) throws MessagingException, IOException {

        senderService.sendEmailMovie(userEmail, subject, model, session);
    }

    /**
     * The user enters there billing address and credit card information to make a purchase of a movie product on the website.
     * Once the purchase is made they are sent an email, there cart is cleared back to 0, there billing address is added to the database,
     * the purchase they made is added to the shop order and order items table
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
     * @param model holds attributes for the view
     * @param session holds the users logged in information
     * @return the payment confirmation page if everything is a success
     * @throws MessagingException if something goes wrong with sending a message
     * @throws IOException when file is nowhere to be found
     */

    @PostMapping("/addBillingAddress")
    public String addBillingAddress(
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
            Model model, HttpSession session) throws MessagingException, IOException {

        User u = (User) session.getAttribute("loggedInUser");

        // validation for billingAddress

        if (fullName.isBlank()){
            // message for error
            String message1 = "Full Name was left blank";
            model.addAttribute("message1",message1);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);
            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
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

            // method to get Cart information with movies and pricing

            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (email.isBlank()){
            // message for error
            String messageEmail = "Email was left blank";
            model.addAttribute("messageEmail",messageEmail);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (address.isBlank()){

            System.out.println("You must choose an address");
            // message for error
            String message2 = "Address was left blank";
            model.addAttribute("message2",message2);
            // method to get Cart information with movies and pricing

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (city.isBlank()){

            System.out.println("You must choose a city");
            // message for error
            String message3 = "City was left blank";
            model.addAttribute("message3",message3);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (county.isBlank()){

            System.out.println("You must choose a county");
            // message for error
            String message4 = "County was left blank";
            model.addAttribute("message4",message4);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (postcode.isBlank()){

            System.out.println("You must choose a postcode");
            // message for error
            String message5 = "Postcode was left blank";
            model.addAttribute("message5",message5);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        // validation for cardDetails

        if (cardName.isBlank()){
            System.out.println("Card Name was left blank");
            // message for error
            String message6 = "Card Name was left blank";
            model.addAttribute("message6",message6);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);
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

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }


        if (cardNumber.isBlank()){
            System.out.println("Card Number was left blank");
            // message for error
            String message7 = "Card Number was left blank";
            model.addAttribute("message7",message7);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
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

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }


        if (Month.isBlank()){

            System.out.println("You must choose a month");
            // message for error
            String message8 = "Month was left blank";
            model.addAttribute("message8",message8);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (Year.isBlank()){

            System.out.println("You must choose a year");
            // message for error
            String message9 = "Year was left blank";
            model.addAttribute("message9",message9);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
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

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);
            return "checkout_index";
        }



        // user to be added if billing User doesn't exist in billingAddress table

        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");

        // update email

        BillingAddress billingUser = new BillingAddress(0, u.getUsername(),fullName, email, address, city, county, postcode);
        billingAddressDao.addBillingAddress(billingUser);


        /// add to shop order for user using addShopOrder method
        addShopOrder(model, session);

        /// add orderItems

        addOrderItems(session);

        // sendEmail

        sendMail(u.getEmail(), "Purchased Digital Movies", model, session);

        //deleteCartItemByCartId
        deleteCartItemByCartId(session);

        // totalAmountInCartInNavBar

        getTotalAmountOfItemsInCart(session,model);

        return "confirmationPaymentPage";

    }

    /**
     * Hiolding the users data for the billing address nd credit card information
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
     * @param model holds teh data for the view on the checkout_index page
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


    ///////////////////////////////// billingAddressIfUserExist


    /**
     * If the billing address already exist it will redirect the user to this method instead and there billing address information will automatically update in the database if they decide to change any values,
     * so they aren't actually making a new billing address from scratch.
     * The user enters there billing address and credit card information to make a purchase of a movie product on the website.
     * Once the purchase is made they are sent an email, there cart is cleared back to 0, there billing address is added to the database,
     * the purchase they made is added to the shop order and order items table
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
     * @param model holds attributes for the view
     * @param session holds the users logged in information
     * @return the payment confirmation page if everything is a success
     * @throws MessagingException if something goes wrong with sending a message
     * @throws IOException when file is nowhere to be found
     */

    @PostMapping("/billingAddressUserBillAlreadyExist")
    public String billingAddressUserBillAlreadyExist(    @RequestParam(name="fullName") String fullName,
                                                         @RequestParam(name = "email") String email,
                                                         @RequestParam(name="address") String address,
                                                         @RequestParam(name="city") String city,
                                                         @RequestParam(name="county") String county,
                                                         @RequestParam(name="postcode") String postcode,
                                                         @RequestParam(name = "cardName") String cardName,
                                                         @RequestParam(name = "cardNumber") String cardNumber,
                                                         @RequestParam(name = "Month") String Month,
                                                         @RequestParam(name = "Year") String Year,
                                                         @RequestParam(name = "cvv") String cvv, Model model, HttpSession session) throws MessagingException, IOException {


        User u = (User) session.getAttribute("loggedInUser");

        // validation for billingAddress

        if (fullName.isBlank()){
            // message for error
            String message1 = "Full Name was left blank";
            model.addAttribute("message1",message1);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
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

            // method to get Cart information with movies and pricing

            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (email.isBlank()){
            // message for error
            String messageEmail = "Email was left blank";
            model.addAttribute("messageEmail",messageEmail);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (address.isBlank()){

            System.out.println("You must choose an address");
            // message for error
            String message2 = "Address was left blank";
            model.addAttribute("message2",message2);
            // method to get Cart information with movies and pricing

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (city.isBlank()){

            System.out.println("You must choose a city");
            // message for error
            String message3 = "City was left blank";
            model.addAttribute("message3",message3);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (county.isBlank()){

            System.out.println("You must choose a county");
            // message for error
            String message4 = "County was left blank";
            model.addAttribute("message4",message4);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (postcode.isBlank()){

            System.out.println("You must choose a postcode");
            // message for error
            String message5 = "Postcode was left blank";
            model.addAttribute("message5",message5);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        // validation for cardDetails

        if (cardName.isBlank()){
            System.out.println("Card Name was left blank");
            // message for error
            String message6 = "Card Name was left blank";
            model.addAttribute("message6",message6);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);
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

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }


        if (cardNumber.isBlank()){
            System.out.println("Card Number was left blank");
            // message for error
            String message7 = "Card Number was left blank";
            model.addAttribute("message7",message7);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
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

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }


        if (Month.isBlank()){

            System.out.println("You must choose a month");
            // message for error
            String message8 = "Month was left blank";
            model.addAttribute("message8",message8);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
        }

        if (Year.isBlank()){

            System.out.println("You must choose a year");
            // message for error
            String message9 = "Year was left blank";
            model.addAttribute("message9",message9);

            modelValidationBillingAddress(fullName, email, address, city, county, postcode, cardName, cardNumber, Month, Year, cvv, model);

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";
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

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            // method for total cart amount
            getTotalAmountOfItemsInCart(session, model);
            return "checkout_index";
        }



        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");


        // get all billingAddress to see if user already created a billing address

        ArrayList<BillingAddress> billingAddresses = billingAddressDao.getAllBillingAddress();

        for (int i = 0; i < billingAddresses.size();i++){


            if (billingAddresses.get(i).getUsername().equals(u.getUsername())) {


                // get billing address by username

                BillingAddress billingAddressByUsername = billingAddressDao.getBillingAddressByUsername(u.getUsername());
                model.addAttribute("billingAddressByUsername", billingAddressByUsername);
                session.setAttribute("billingAddressUser", billingAddressByUsername);

                // update billingAddress details

                billingAddressDao.updateBillingAddressFullName(fullName, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressEmail(email, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateAddressForBillingAddress(address, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressCity(city, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressCounty(county, billingAddressByUsername.getBilling_address_id());
                billingAddressDao.updateBillingAddressPostCode(postcode, billingAddressByUsername.getBilling_address_id());

                /// add to shop order for user using addShopOrder method
               addShopOrder(model, session);

               /// add orderItems

                addOrderItems(session);

                // send Email

                sendMail(u.getEmail(), "Purchased Digital Movies", model, session);

               // delete cartItem byCartId
                deleteCartItemByCartId(session);

                // totalAmountInCartInNavBar

                getTotalAmountOfItemsInCart(session,model);


                return "confirmationPaymentPage";

            }

        }


        return "error";

    }




    // method to get total price for users items bought

    /**
     * Getting the total price of the users products bought
     * @param model holds attributes for the view
     * @param session is used to retrieve the users logged in details
     * @return the total price of the users products bought
     */

    public double totalPrice(Model model, HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");


        ArrayList<CartItem> cartItems = cartItemDao.getAllCartItemsByCartId(cart.getCart_id());

        ArrayList<MovieProduct> movies = new ArrayList<>();

        for (int i = 0; i < cartItems.size();i++) {

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            movies.add(movieProductDao.getMovieById(cartItems.get(i).getMovie_id()));
            model.addAttribute("movies", movies);
        }

        // get total Price in cart

        double total = 0;

        for (int i = 0; i < movies.size();i++){

            total = total + movies.get(i).getListPrice();
        }

        return total;
    }


    /// add shop order to order table

    /**
     * Adding the users orders to the shop order table
     * @param model holds attributes for the view
     * @param session is used to retrieve the users logged in details
     */
    public void addShopOrder(Model model, HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl("database.properties");

        LocalDateTime order_date = LocalDateTime.now();

        // get totalPrice
        double total = totalPrice(model, session);

        // billingAddressByUsername
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");

        BillingAddress billingUser1 = billingAddressDao.getBillingAddressByUsername(u.getUsername());

        ShopOrder addShopOrder = new ShopOrder(0, u.getUsername(), billingUser1.getBilling_address_id(), order_date, total , "COMPLETE");

        shopOrderDao.addShopOrder(addShopOrder);


        /// getting the totalPrice for the most recent order in the shopOrder table

        double totalPriceShopOrder = 0;
        totalPriceShopOrder = addShopOrder.getTotal_price();
        model.addAttribute("totalPriceShopOrder", totalPriceShopOrder);

        // get the date of the most recent order
        LocalDateTime orderDate = addShopOrder.getOrder_date().toLocalDate().atStartOfDay();
        model.addAttribute("orderDate", orderDate);
    }


    // deleteCartByCartItem

    /**
     * Deleting teh users cart items by the the cart id
     * @param session is used to retrieve the users logged in details
     */
    public void deleteCartItemByCartId(HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        cartItemDao.deleteCartItemByCartId(cart.getCart_id());
    }


    /// add Order Items

    /**
     * Adds what the user bought to the order items table
     * @param session is used to retrieve the users logged in details
     */
    public void addOrderItems(HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        // gettingCartId from username
        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");


        // getting all cart Items from cartId
        ArrayList<CartItem> cartItems = cartItemDao.getAllCartItemsByCartId(cart.getCart_id());

        ArrayList<MovieProduct> movies = new ArrayList<>();


        for (int i = 0; i < cartItems.size();i++) {


            // getting movies by movies in cartItem Id
            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            movies.add(movieProductDao.getMovieById(cartItems.get(i).getMovie_id()));

            // getting orderId

            // adding to orderItems
            OrderItemDao orderItemDao = new OrderItemDaoImpl("database.properties");

            ShopOrderDao shopOrderDao = new ShopOrderDaoImpl("database.properties");

            ShopOrder shopOrder = shopOrderDao.getOrderWithTheHighestOrderIdByUsername(u.getUsername());

            OrderItem orderItem = new OrderItem(0, movies.get(i).getListPrice(), shopOrder.getOrder_id(), cartItems.get(i).getMovie_id());

            orderItemDao.addOrderItem(orderItem);


        }

    }



/*
    /// add What User Purchased to the email

    public String addOrderItemsOfWhatUserPurchasedToEmail(HttpSession session, Model model){

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        // gettingCartId from username
        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");


        // getting all cart Items from cartId
        ArrayList<CartItem> cartItems = cartItemDao.getAllCartItemsByCartId(cart.getCart_id());

        ArrayList<MovieProduct> movies = new ArrayList<>();


        String purchase;

        OrderItem orderItem;


        ArrayList<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < cartItems.size();i++) {


            // getting movies by movies in cartItem Id
            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            movies.add(movieProductDao.getMovieById(cartItems.get(i).getMovie_id()));

            ShopOrderDao shopOrderDao = new ShopOrderDaoImpl("database.properties");

            ShopOrder shopOrder = shopOrderDao.getOrderWithTheHighestOrderIdByUsername(u.getUsername());

             orderItem = new OrderItem(0, movies.get(i).getListPrice(), shopOrder.getOrder_id(), cartItems.get(i).getMovie_id());

             // create an arraylist of order items based on order items id above

            orderItems.add(orderItem);

        }

        System.out.println(orderItems);

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");;

        double total = 0;

        ArrayList<String> movieNames = new ArrayList<>();
        MovieProduct movieProduct;

        ArrayList<MovieProduct> movieProductArrayList = new ArrayList<>();

        for (int i = 0; i < orderItems.size();i++){

            movieProduct = movieProductDao.getMovieById(orderItems.get(i).getMovie_id());

            movieNames.add(movieProduct.getMovie_name());

            movieProductArrayList.add(movieProduct);

            total = total + movieProduct.getListPrice();
        }


        System.out.println(movieNames);

        model.addAttribute("movieProductArrayList", movieProductArrayList);
        model.addAttribute("total", total);


        purchase = "You have purchased the movies: " + movieNames+ " with a total price of " + total ;

        System.out.println(purchase);

        return purchase;

    }

 */


    // totalAmountInCartInNavBar

    /**
     * Getting the total amount of items for the users cart
     * @param model holds attributes for the view
     * @param session is used to retrieve the users logged in details
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



    // method to get Cart information with movies and pricing

    /**
     * Holds the users cart information with the pricing
     * @param model holds attributes for the view
     * @param session is used to retrieve the users logged in details
     */

    public void cartInformation(HttpSession session,Model model){

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");


        ArrayList<CartItem> cartItems = cartItemDao.getAllCartItemsByCartId(cart.getCart_id());

        ArrayList<MovieProduct> movies = new ArrayList<>();

        for (int i = 0; i < cartItems.size();i++) {

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            movies.add(movieProductDao.getMovieById(cartItems.get(i).getMovie_id()));
            model.addAttribute("movies", movies);
        }

        // get total Price in cart

        double total = 0;

        for (int i = 0; i < movies.size();i++){

            total = total + movies.get(i).getListPrice();
        }

        model.addAttribute("total", total);
    }

}
