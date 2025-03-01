package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.*;

import java.time.LocalDateTime;
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

            // method to get Cart information with movies and pricing

            cartInformation(session,model);

            return "checkout_index";
        }

        if (address.isBlank()){
            String message = "You must choose an address";
            model.addAttribute("message", message);
            System.out.println("You must choose an address");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }

        if (city.isBlank()){
            String message = "You must choose a city";
            model.addAttribute("message", message);
            System.out.println("You must choose a city");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }

        if (county.isBlank()){
            String message = "You must choose a county";
            model.addAttribute("message", message);
            System.out.println("You must choose a county");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }

        if (postcode.isBlank()){
            String message = "You must choose a postcode";
            model.addAttribute("message", message);
            System.out.println("You must choose a postcode");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
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

            // method to get Cart information with movies and pricing
            cartInformation(session,model);

            return "checkout_index";
        }


        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){
            String message = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message", message);
            System.out.println("Card Number must be a valid Visa credit card number");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }


        if (Month.isBlank()){
            String message = "You must choose a month";
            model.addAttribute("message", message);
            System.out.println("You must choose a month");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }

        if (Year.isBlank()){
            String message = "You must choose a Year";
            model.addAttribute("message", message);
            System.out.println("You must choose a year");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){
            String message = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message", message);
            System.out.println("Cvv number must be 3 or 4 numbers long");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }



        // user to be added if billing User doesn't exist in billingAddress table

        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");


        BillingAddress billingUser = new BillingAddress(0, u.getUsername(),fullName, u.getEmail(), address, city, county, postcode);
        billingAddressDao.addBillingAddress(billingUser);


        /// add to shop order for user using addShopOrder method
        addShopOrder(model, session);

        /// add orderItems

        addOrderItems(session);

        //deleteCartItemByCartId
        deleteCartItemByCartId(session);

        // totalAmountInCartInNavBar

        getTotalAmountOfItemsInCart(session,model);

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
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }


        Pattern cardNumberRegex = Pattern.compile("^(?:4[0-9]{12}(?:[0-9]{3})?)$");
        Matcher match = cardNumberRegex.matcher(cardNumber);
        boolean matchfoundCardNumber = match.find();

        if (!matchfoundCardNumber){
            String message = "Card Number must be a valid Visa credit card number";
            model.addAttribute("message", message);
            System.out.println("Card Number must be a valid Visa credit card number");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }


        if (Month.isBlank()){
            String message = "You must choose a month";
            model.addAttribute("message", message);
            System.out.println("You must choose a month");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }

        if (Year.isBlank()){
            String message = "You must choose a Year";
            model.addAttribute("message", message);
            System.out.println("You must choose a year");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
            return "checkout_index";
        }


        Pattern cvvNumberRegex = Pattern.compile("^[0-9]{3,4}$");
        Matcher match1 = cvvNumberRegex.matcher(cvv);
        boolean matchfoundCvvNumber = match1.find();

        if (!matchfoundCvvNumber){
            String message = "Cvv number must be 3 or 4 numbers long";
            model.addAttribute("message", message);
            System.out.println("Cvv number must be 3 or 4 numbers long");
            // method to get Cart information with movies and pricing
            cartInformation(session,model);
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

               /// add orderItems

                addOrderItems(session);

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


    // deleteCartByCartItem

    public void deleteCartItemByCartId(HttpSession session){

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        cartItemDao.deleteCartItemByCartId(cart.getCart_id());
    }


    /// add Order Items

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


    // totalAmountInCartInNavBar

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
