package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class BillingAddressController {

    @PostMapping("/addBillingAddress")
    public String addBillingAddress(
            @RequestParam(name="fullName") String fullName,
            @RequestParam(name="address") String address,
            @RequestParam(name="city") String city,
            @RequestParam(name="county") String county,
            @RequestParam(name="postcode") String postcode,
            @RequestParam(name = "cardName") String cardName,
            @RequestParam(name = "cardNumber") String cardNumber,
            @RequestParam(name = "Month") String Month,
            @RequestParam(name = "Year") String Year,
            @RequestParam(name = "cvv") String cvv,
            Model model, HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        // validation for billingAddress

        Pattern fullNameRegex = Pattern.compile("^[a-zA-Z]{3,25}.*[\\s\\.]*$");
        Matcher match12 = fullNameRegex.matcher(fullName);
        boolean matchfoundFullName = match12.find();

        if (!matchfoundFullName){
            String message = "Full Name must be between 3-25 characters, letters only";
            model.addAttribute("message", message);
            System.out.println("Full Name must be between 3-25 characters, letters only");
            return "checkout_index";
        }

        // validation for cardDetails

        Pattern cardNameRegex = Pattern.compile("^[a-zA-Z]{3,25}.*[\\s\\.]*$");
        Matcher match11 = cardNameRegex.matcher(cardName);
        boolean matchfoundCardName = match11.find();

        if (!matchfoundCardName){
            String message = "Card Name must be between 3-25 characters, letters only";
            model.addAttribute("message", message);
            System.out.println("Card Name must be between 3-25 characters, letters only");
            return "checkout_index";
        }


        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){
            String message = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message", message);
            System.out.println("Card Number must be a valid Visa credit card number");
            return "checkout_index";
        }


        if (Month.isBlank()){
            String message = "You must choose a month";
            model.addAttribute("message", message);
            System.out.println("You must choose a month");
            return "checkout_index";
        }

        if (Year.isBlank()){
            String message = "You must choose a Year";
            model.addAttribute("message", message);
            System.out.println("You must choose a year");
            return "checkout_index";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){
            String message = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message", message);
            System.out.println("Cvv number must be 3 or 4 numbers long");
            return "checkout_index";
        }



        // user to be added if billing User doesn't exist in billingAddress table

        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");


        BillingAddress billingUser = new BillingAddress(0, u.getUsername(),fullName, u.getEmail(), address, city, county, postcode);
        billingAddressDao.addBillingAddress(billingUser);


        /// add to shop order for user using addShopOrder method
        addShopOrder(model, session);

        //deleteCartItemByCartId
        deleteCartItemByCartId(session);

        return "confirmationPaymentPage";

    }


    /// billingAddressIfUserExist

    @PostMapping("/billingAddressUserBillAlreadyExist")
    public String billingAddressUserBillAlreadyExist(  @RequestParam(name = "cardName") String cardName,
                                                       @RequestParam(name = "cardNumber") String cardNumber,
                                                       @RequestParam(name = "Month") String Month,
                                                       @RequestParam(name = "Year") String Year,
                                                       @RequestParam(name = "cvv") String cvv, Model model, HttpSession session){


        User u = (User) session.getAttribute("loggedInUser");

        // validation for cardDetails

        Pattern cardNameRegex = Pattern.compile("^[a-zA-Z]{3,25}.*[\\s\\.]*$");
        Matcher match11 = cardNameRegex.matcher(cardName);
        boolean matchfoundCardName = match11.find();

        if (!matchfoundCardName){
            String message = "Card Name must be between 3-25 characters, letters only";
            model.addAttribute("message", message);
            System.out.println("Card Name must be between 3-25 characters, letters only");
            return "checkout_index";
        }


        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){
            String message = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message", message);
            System.out.println("Card Number must be a valid Visa credit card number");
            return "checkout_index";
        }


        if (Month.isBlank()){
            String message = "You must choose a month";
            model.addAttribute("message", message);
            System.out.println("You must choose a month");
            return "checkout_index";
        }

        if (Year.isBlank()){
            String message = "You must choose a Year";
            model.addAttribute("message", message);
            System.out.println("You must choose a year");
            return "checkout_index";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){
            String message = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message", message);
            System.out.println("Cvv number must be 3 or 4 numbers long");
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

                /// add to shop order for user using addShopOrder method
               addShopOrder(model, session);

               // delete cartItem byCartId
                deleteCartItemByCartId(session);


                return "confirmationPaymentPage";

            }

        }


        return "error";

    }




    // method to get total price for users items bought

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


    public void deleteCartItemByCartId(HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        cartItemDao.deleteCartItem(cart.getCart_id());
    }



}
