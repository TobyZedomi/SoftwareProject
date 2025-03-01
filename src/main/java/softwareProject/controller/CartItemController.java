package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.*;
import softwareProject.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartItemController {


    // add CartItem


    @GetMapping("/addCartItem")
    public String processRequest(HttpSession session,
                                 @RequestParam(name = "movieId") String movieId, Model model) {

        int movieID2 = Integer.parseInt(movieId);

        User u = (User) session.getAttribute("loggedInUser");

        // checking if user already purchased item
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl("database.properties");

        // get all shop Orders by username
        ArrayList<ShopOrder> shopOrdersByUser = shopOrderDao.getAllShopOrdersByUsername(u.getUsername());

        // loop through shop orders
        for (int i = 0; i < shopOrdersByUser.size(); i++) {

            OrderItemDao orderItemDao = new OrderItemDaoImpl("database.properties");

            // get all orders in the system
            ArrayList<OrderItem> orderItems = orderItemDao.getAllOrderItems();

            for (int j = 0; j < orderItems.size(); j++) {

                if (shopOrdersByUser.get(i).getOrder_id() == orderItems.get(j).getOrder_id()) {

                    MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

                    // get MovieProduct by movie id
                    MovieProduct movieProduct = movieProductDao.getMovieById(orderItems.get(j).getMovie_id());

                    if (movieProduct.getMovie_id() == movieID2) {
                        String message = "Movie has already been purchased by you";
                        model.addAttribute("message", message);

                        List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
                        model.addAttribute("movieProducts", movieProducts);

                        // totalAmountOItems in basket
                        getTotalAmountOfItemsInCart(session, model);
                        return "store_index";
                    }
                }

            }

        }

        // adding movie if not purchased

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int answer = cartItemDao.addCartItem(new CartItem(cart.getCart_id(), movieID2));

        System.out.println("Cart Id" + cart.getCart_id());

        if (answer == -1) {
            System.out.println(answer);
            String message = "Movie Product was not added to cart because you already have it in your cart";
            model.addAttribute("message", message);

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            return "store_index";
        } else {

            System.out.println(answer);
            String message = "Movie Product was added to cart";
            model.addAttribute("message", message);

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            return "store_index";

        }


    }


    // delete Cart Item

    @GetMapping("/deleteCartItem")
    public String deleteCartItem(HttpSession session,
                                 @RequestParam(name = "movieId") String movieId, Model model) {


        int movieID2 = Integer.parseInt(movieId);

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");


        ArrayList<CartItem> cartItems = cartItemDao.getAllCartItemsByCartId(cart.getCart_id());

        ArrayList<MovieProduct> movies = new ArrayList<>();

        for (int i = 0; i < cartItems.size(); i++) {

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            movies.add(movieProductDao.getMovieById(cartItems.get(i).getMovie_id()));
            model.addAttribute("movies", movies);
        }

        // get total Price

        double total = 0;

        for (int i = 0; i < movies.size(); i++) {

            total = total + movies.get(i).getListPrice();
        }

        model.addAttribute("total", total);

        // totalAmountOItems in basket
        getTotalAmountOfItemsInCart(session, model);


        // deleting cartItem

        cartItemDao.deleteCartItemByCartIdAndMovieId(cart.getCart_id(), movieID2);

        return "cart_index";
    }


    // cart count


    @GetMapping("/totalNumberOfCartItems")
    public String deleteCartItem(HttpSession session,
                                 Model model) {


        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);

        return "cart_index";
    }


    // total amount of items in cart
    public void getTotalAmountOfItemsInCart(HttpSession session, Model model) {

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }

}
