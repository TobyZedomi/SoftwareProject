package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import softwareProject.business.*;
import softwareProject.persistence.FriendDao;
import softwareProject.persistence.FriendDaoImpl;
import softwareProject.persistence.*;
import softwareProject.service.MovieService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private MovieService movieService;


    @GetMapping("/")
    public String userIndex(){
        return "user_index";
    }

    @GetMapping("/user_indexSignUp")
    public String userIndexSignUp(){
        return "user_indexSignUp";
    }

    // add these .add model from list of movies from movie db into index controller
    @GetMapping("/index")
    public String home(HttpSession session,Model model){

        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session,model);

        ///

        List<MovieTest> movies = movieService.getMovies();
        model.addAttribute("movies", movies);

        for (int i = 0; i < movies.size();i++) {

            List<MovieTrailer> trailers = movieService.getTrailer(movies.get(i).getId());
            model.addAttribute("trailers", trailers);
        }


        return "index";
    }

    @GetMapping("/movie_index")
    public String movieIndex(HttpSession session, Model model){


        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session,model);

        //

        List<GenreTest> genres = movieService.getGenres();
        model.addAttribute("genres", genres);

        List<MovieTest> movieByGenres = movieService.getMoviesByGenre("878");
        model.addAttribute("movieByGenres", movieByGenres);


        for (int i = 0; i < movieByGenres.size();i++) {

            List<MovieTrailer> trailers = movieService.getTrailer(movieByGenres.get(i).getId());
            model.addAttribute("trailers", trailers);
        }

        return "movie_index";
    }

    @GetMapping("/logout_index")
    public String userIndex2(){
        return "logout_index";
    }


    @GetMapping("/registerSuccessUser")
    public String regIndex(Model model){
        List<MovieTest> movies = movieService.getMovies();
        model.addAttribute("movies", movies);
        return "registerSuccessUser";
    }


    @GetMapping("/subscription_index")
    public String subscriptionIndex(HttpSession session,Model model){

        getTotalAmountOfItemsInCart(session,model);

        return "subscription_index";
    }

    @GetMapping("/friends")
    public String friends(HttpSession session, Model model){
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        FriendDao friendDao = new FriendDaoImpl("database.properties");
        UserDao userDao = new UserDaoImpl("database.properties");

        ArrayList<Friends> friends = friendDao.getAllFriends(loggedInUser.getUsername());
        ArrayList<User> details = new ArrayList<>();

        for(int i = 0; i< friends.size();i++){
            String name;
            if (friends.get(i).getFriend1().equals(loggedInUser.getUsername())) {
                name = friends.get(i).getFriend2();
            } else {
                name = friends.get(i).getFriend1();
            }
            User friendUser = userDao.findUserByUsername(name);
            if(name != null){
                details.add(friendUser);
            }
        }

        model.addAttribute("Friends", details);

        return "friends";
    }

    @GetMapping("/notifications")
    public String notifications(HttpSession session, Model model){

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        FriendDao friendDao = new FriendDaoImpl("database.properties");
        ArrayList<Friends> result = friendDao.getAllRequests(loggedInUser.getUsername());

        if (result.isEmpty()) {
            result = new ArrayList<>();
        }

        model.addAttribute("pendingRequests", result);
        return "notifications";

    }

    @GetMapping("/forgot_password")
    public String forgotPasswordIndex(){ return "forgot_password"; }

    @GetMapping("/review_form")
    public String reviewIndex(){ return "review_form"; }


    @GetMapping("/store_index")
    public String storeIndex(HttpSession session, Model model){

        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session,model);

        ////

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

        List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
        model.addAttribute("movieProducts", movieProducts);



        return "store_index";
    }


    @GetMapping("/cart_index")
    public String cartIndex(HttpSession session, Model model){

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

            // get total Price

        double total = 0;

        for (int i = 0; i < movies.size();i++){

            total = total + movies.get(i).getListPrice();
        }

        model.addAttribute("total", total);


        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);


        return "cart_index";
    }

    @GetMapping("/checkout_index")
    public String checkout_index(HttpSession session, Model model){

        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session,model);

        // session for billng user

        User u = (User) session.getAttribute("loggedInUser");

        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");

        BillingAddress billingAddressUser = billingAddressDao.getBillingAddressByUsername(u.getUsername());
        session.setAttribute("billingAddressUser", billingAddressUser);

        // getting movies purchased in cart


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


        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session,model);

        return "checkout_index";
    }


    @GetMapping("/confirmationPaymentPage")
    public String confirmationPaymentPage(){
        return "confirmationPaymentPage";
    }

    @GetMapping("/adminPanel_index")
    public String adminPanel_index(HttpSession session, Model model){

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

        List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
        model.addAttribute("movieProducts", movieProducts);

        /// get total number of items in cart for user

        getTotalAmountOfItemsInCart(session,model);

        return "adminPanel_index";
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
