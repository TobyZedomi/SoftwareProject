package softwareProject.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import softwareProject.business.*;
import softwareProject.persistence.FriendDao;
import softwareProject.persistence.FriendDaoImpl;
import softwareProject.persistence.*;
import softwareProject.service.EmailSenderService;
import softwareProject.service.MovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private MovieService movieService;


    @GetMapping("/")
    public String userIndex() {
        return "user_index";
    }

    @GetMapping("/user_indexSignUp")
    public String userIndexSignUp() {
        return "user_indexSignUp";
    }

    // add these .add model from list of movies from movie db into index controller
    @GetMapping("/index")
    public String home(HttpSession session, Model model) {


        if(session.getAttribute("loggedInUser") != null) {
            /// get total number of items in cart for user

            getTotalAmountOfItemsInCart(session, model);

            /// movie db get most popular movies

            List<MovieTest> movies = movieService.getMovies();

            // create new list to add the movies from the movie db into
            List<MovieTest> newMovie = new ArrayList<>();

            // loop through the movie db list and reduce the size by 2
            for (int i = 0; i < movies.size() - 2; i++) {

                // if any backdrop image is unavailable it will not add it to the new arraylist
                if (movies.get(i).getBackdrop_path() != null) {
                    // add the movies from the movie db into the new arraylist
                    newMovie.add(movies.get(i));
                    model.addAttribute("movies", newMovie);
                }
            }

            for (int i = 0; i < movies.size(); i++) {

                List<MovieTrailer> trailers = movieService.getTrailer(movies.get(i).getId());
                model.addAttribute("trailers", trailers);
            }


            return "index";
        }

        return "notValidUser";
    }

    @GetMapping("/movie_index")
    public String movieIndex(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            /// get total number of items in cart for user

            getTotalAmountOfItemsInCart(session, model);

            //

            List<GenreTest> genres = movieService.getGenres();
            model.addAttribute("genres", genres);

            List<MovieTest> movieByGenres = movieService.getMoviesByGenre("878");

            List<MovieTest> newMovie = new ArrayList<>();

            for (int i = 0; i < movieByGenres.size() - 2; i++) {

                if (movieByGenres.get(i).getBackdrop_path() != null) {
                    newMovie.add(movieByGenres.get(i));
                    model.addAttribute("movieByGenres", newMovie);
                }
            }

            // genre by id and get the name

            GenreDao genreDao = new GenreDaoImpl("database.properties");

            GenreTest genre = genreDao.getGenreById(878);

            model.addAttribute("genreName", genre.getName());


            for (int i = 0; i < movieByGenres.size(); i++) {

                List<MovieTrailer> trailers = movieService.getTrailer(movieByGenres.get(i).getId());
                model.addAttribute("trailers", trailers);
            }

            return "movie_index";
        }
        return "notValidUser";
    }

    @GetMapping("/logout_index")
    public String userIndex2() {
        return "logout_index";
    }


    @GetMapping("/registerSuccessUser")
    public String regIndex(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);

            List<MovieTest> movies = movieService.getMovies();

            // create new list to add the movies from the movie db into
            List<MovieTest> newMovie = new ArrayList<>();

            // loop through the movie db list and reduce the size by 2
            for (int i = 0; i < movies.size() - 2; i++) {

                // if any backdrop image is unavailable it will not add it to the new arraylist
                if (movies.get(i).getBackdrop_path() != null) {
                    // add the movies from the movie db into the new arraylist
                    newMovie.add(movies.get(i));
                    model.addAttribute("movies", newMovie);
                }
            }
            return "registerSuccessUser";
        }
        return "notValidUser";
    }


    @GetMapping("/subscription_index")
    public String subscriptionIndex(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);

            return "subscription_index";
        }

        return "notValidUser";
    }

    @GetMapping("/addFriends")
    public String addFriends(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);

            return "addFriends";

        }

        return "notValidUser";
    }

    @GetMapping("/friends")
    public String friends(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);

            User loggedInUser = (User) session.getAttribute("loggedInUser");
            FriendDao friendDao = new FriendDaoImpl("database.properties");
            UserDao userDao = new UserDaoImpl("database.properties");

            ArrayList<Friends> friends = friendDao.getAllFriends(loggedInUser.getUsername());
            ArrayList<User> details = new ArrayList<>();
            ArrayList<Friends> numberOfFriends = new ArrayList<>();
            if (friends.isEmpty()) {
                model.addAttribute("noFriends", "You currently have no friends");
            }

            for (int i = 0; i < friends.size(); i++) {
                String name;
                if (friends.get(i).getFriend1().equals(loggedInUser.getUsername())) {
                    name = friends.get(i).getFriend2();
                } else {
                    name = friends.get(i).getFriend1();
                }
                User friendUser = userDao.findUserByUsername(name);
                numberOfFriends = FriendController.getAllFriends(session, friendUser.getUsername(), model);
                if (name != null) {
                    details.add(friendUser);
                }
            }

            model.addAttribute("allFriends", details);
            model.addAttribute("numberOfFriends", numberOfFriends);
            model.addAttribute("total", numberOfFriends.size());

            return "friends";
        }

        return "notValidUser";
    }


    @GetMapping("/notifications")
    public String notifications(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);

            User loggedInUser = (User) session.getAttribute("loggedInUser");

            FriendDao friendDao = new FriendDaoImpl("database.properties");
            ArrayList<Friends> result = friendDao.getAllRequests(loggedInUser.getUsername());

            if (result.isEmpty()) {
                result = new ArrayList<>();
            }

            model.addAttribute("pendingRequests", result);
            return "notifications";

        }

        return "notValidUser";

    }

    @GetMapping("/forgot_password")
    public String forgotPasswordIndex() {
            return "forgot_password";
    }

    @GetMapping("/review_form")
    public String reviewIndex(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {
            getTotalAmountOfItemsInCart(session, model);
            return "review_form";
        }

        return "notValidUser";
    }

    @GetMapping("/writeReviewPage")
    public String writeReviewIndex() {
        return "writeReview";
    }



    @GetMapping("/store_index")
    public String storeIndex(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            /// get total number of items in cart for user

            getTotalAmountOfItemsInCart(session, model);

            ////

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);


            return "store_index";

        }

        return "notValidUser";
    }


    @GetMapping("/cart_index")
    public String cartIndex(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

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


            int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
            model.addAttribute("totalCartItems", totalCartItems);


            return "cart_index";

        }

        return "notValidUser";
    }

    @GetMapping("/checkout_index")
    public String checkout_index(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            /// get total number of items in cart for user

            getTotalAmountOfItemsInCart(session, model);

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

            for (int i = 0; i < cartItems.size(); i++) {

                MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

                movies.add(movieProductDao.getMovieById(cartItems.get(i).getMovie_id()));
                model.addAttribute("movies", movies);
            }

            // get total Price in cart

            double total = 0;

            for (int i = 0; i < movies.size(); i++) {

                total = total + movies.get(i).getListPrice();
            }

            model.addAttribute("total", total);


            /// get total number of items in cart for user

            getTotalAmountOfItemsInCart(session, model);

            return "checkout_index";

        }

        return "notValidUser";
    }


    @GetMapping("/confirmationPaymentPage")
    public String confirmationPaymentPage(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {
            getTotalAmountOfItemsInCart(session, model);
            return "confirmationPaymentPage";
        }

        return "notValidUser";
    }

    @GetMapping("/adminPanel_index")
    public String adminPanel_index(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);

            /// get total number of items in cart for user

            getTotalAmountOfItemsInCart(session, model);

            return "adminPanel_index";
        }

        return "notValidUser";
    }


    @GetMapping("/purchased_movies")
    public String purchasedMovies(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");

            ShopOrderDao shopOrderDao = new ShopOrderDaoImpl("database.properties");

            // get all shoporders by username
            ArrayList<ShopOrder> shopOrdersForUser = shopOrderDao.getAllShopOrdersByUsername(u.getUsername());


            // get all orders

            OrderItemDao orderItemDao = new OrderItemDaoImpl("database.properties");
            ArrayList<OrderItem> orderItems = orderItemDao.getAllOrderItems();

            ArrayList<MovieProduct> allMovieProducts = new ArrayList<>();

            for (int i = 0; i < shopOrdersForUser.size(); i++) {

                // session to see if shop user exist for html page
                session.setAttribute("shopOrderForUser", shopOrdersForUser.get(i).getUsername());

                for (int j = 0; j < orderItems.size(); j++) {

                    if (shopOrdersForUser.get(i).getOrder_id() == orderItems.get(j).getOrder_id()) {

                        // get by movie product in order table and put in arraylist of movie Products

                        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

                        movieProductDao.getMovieById(orderItems.get(j).getMovie_id());

                        allMovieProducts.add(movieProductDao.getMovieById(orderItems.get(j).getMovie_id()));

                    }
                }
            }

            // model to loop through arraylist
            model.addAttribute("allMovieProducts", allMovieProducts);

            // method to get total in cart
            getTotalAmountOfItemsInCart(session, model);

            return "purchased_movies";

        }

        return "notValidUser";
    }


    @GetMapping("/adminPanelStats")
    public String adminPanelStats(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {


            AuditCartItemDao auditCartItemDao = new AuditCartItemDaoImpl("database.properties");


            ArrayList<AuditCartItem2> auditCartItems = auditCartItemDao.getMovieIdsInDescOrderOfCount();

            System.out.println(auditCartItems);

            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");


            Map<MovieProduct, Integer> deletedCartItemsMap = new LinkedHashMap<>();

            MovieProduct movieProduct = null;

            for (int i = 0; i < auditCartItems.size(); i++) {

                movieProduct = movieProductDao.getMovieById(auditCartItems.get(i).getMovie_id());


                deletedCartItemsMap.put(movieProduct, auditCartItems.get(i).getCount());

            }

            model.addAttribute("deletedCartItemsMap", deletedCartItemsMap);
            System.out.println(movieProduct);


            // method to get total in cart
            getTotalAmountOfItemsInCart(session, model);

            return "adminPanelStats";

        }

        return "notValidUser";
    }

    @GetMapping("/noVideo")
    public String noVideosForMovie(HttpSession session, Model model) {

        if(session.getAttribute("loggedInUser") != null) {

            return "noVideo";
        }

        return "notValidUser";
    }

    @GetMapping("/purchaseSubscriptionPart2")
    public String purchasedSubPart2(HttpSession session, Model model){

        if(session.getAttribute("loggedInUser") != null) {

            /// get total number of items in cart for user

            getTotalAmountOfItemsInCart(session, model);

            User u = (User) session.getAttribute("loggedInUser");

            BillingAddressDao billingAddressDao = new BillingAddressDaoImpl("database.properties");

            BillingAddress billingAddressUser = billingAddressDao.getBillingAddressByUsername(u.getUsername());
            session.setAttribute("billingAddressUser", billingAddressUser);

            return "purchaseSubscriptionPart2";

        }

        return "notValidUser";
    }


    @GetMapping("/subscriptionConfirmPayment")
    public String subscriptionConfirmPayment(HttpSession session, Model model) {
        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);
            return "subscriptionConfirmPayment";
        }

        return "notValidUser";
    }


    @GetMapping("/reset_password")
    public String resetPassword(HttpSession session, Model model) {

            return "reset_password";
    }

    public void getTotalAmountOfItemsInCart(HttpSession session, Model model) {

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }

    @GetMapping("/userProfile")
    public String userProfile(HttpSession session, Model model){
        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);
            return "UserProfile";

        }

        return "notValidUser";
    }

    @GetMapping("/movieProductSearch")
    public String movieProductBySearch(HttpSession session, Model model){
        if(session.getAttribute("loggedInUser") != null) {

            getTotalAmountOfItemsInCart(session, model);

            return "movieProductSearch";

        }

        return "notValidUser";
    }

}
