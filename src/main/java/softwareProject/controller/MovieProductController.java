package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softwareProject.business.*;
import softwareProject.persistence.*;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MovieProductController {


    /**
     * The admin being able to add a new movie product to the store
     * @param movie_name is the name of the movie product to be added
     * @param date_of_release is the date of release of teh movie product to be added
     * @param movie_info is the movie information for the movie product to be added
     * @param listPrice is the listPrice of the movie product to be added
     * @param file is the file for the picture
     * @param redirectAttributes
     * @param session holds the logged-in users information
     * @param model holds information for the view
     * @return the admin panel page
     * @throws IOException when file is nowhere to be found
     */

    @PostMapping("/addMovieProduct")
    public String addMovieProduct( @RequestParam(name = "movie_name") String movie_name,
                            @RequestParam (name = "date_of_release") String date_of_release,
                            @RequestParam (name = "movie_info")String movie_info,
                            @RequestParam (name = "listPrice")String listPrice, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes
            ,Model model, HttpSession session) throws IOException {

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");


        LocalDate releaseDate = LocalDate.parse(date_of_release);
        Time length = Time.valueOf("02:30:00");
        double listPrice2 = Double.parseDouble(listPrice);

        //upload image
        String fileName = file.getOriginalFilename();

        file.transferTo(new File("C:\\Users\\tobyz\\IdeaProjects\\SoftwareProject\\src\\main\\resources\\static\\css\\images\\" + fileName));

        // add movie product

        MovieProduct newMovieProduct = new MovieProduct(0, movie_name, releaseDate, length, movie_info, fileName, listPrice2);

        int complete = movieProductDao.addMovieProduct(newMovieProduct);

        String message;
        if(complete == -1){
            message = "Movie failed to be added: " +newMovieProduct.getMovie_name();
            model.addAttribute("message",message);
            log.info(message);

            getAllMovieProducts(model, movieProductDao);

            getTotalAmountOfItemsInCart( session,model);

            return "adminPanel_index";


        }else{

            message = "Movie added: "+newMovieProduct.getMovie_name();
            model.addAttribute("message", message);

            getAllMovieProducts(model, movieProductDao);
            log.info(message);

            getTotalAmountOfItemsInCart( session,model);

            return "adminPanel_index";
        }
    }


    /**
     * Admin being able to delete a movie product from the store
     * @param session holds the logged-in users information
     * @param movieId is the mocvie being deleted
     * @param model hold the attributes for the view
     * @return
     */

    @GetMapping("/deleteMovieProduct")
    public String deleteMovieProduct(HttpSession session,
                                 @RequestParam(name = "movieId") String movieId, Model model) {


        int movieID2 = Integer.parseInt(movieId);

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

        MovieProduct movieProduct = movieProductDao.getMovieById(movieID2);

        int delete = movieProductDao.deleteMovieProductByMovieId(movieID2);



        String message;
        if (delete > 0){
            message = "Movie Product " +movieProduct.getMovie_name()+ " was deleted ";
            model.addAttribute("message", message);
            log.info(message);

            getAllMovieProducts(model, movieProductDao);

            getTotalAmountOfItemsInCart( session,model);

        }else{

            message = "Movie Product " +movieProduct.getMovie_name()+ " was not deleted";
            model.addAttribute("message", message);
            log.info(message);

            getAllMovieProducts(model, movieProductDao);

            getTotalAmountOfItemsInCart( session,model);
        }
        return "adminPanel_index";

    }


    /**
     * Getting the movie product by the movie id to updated
     * @param session holds the logged-in users information
     * @param movieId is the movie product being searched for
     * @param model holds the attributes for the view
     * @return adminUpdate page
     */
    @GetMapping("/getMovieProductById")
    public String getMovieProductById(HttpSession session,
                                     @RequestParam(name = "movieId") String movieId, Model model) {


        int movieID2 = Integer.parseInt(movieId);

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");


        MovieProduct movieProduct = movieProductDao.getMovieById(movieID2);

        model.addAttribute("movieProduct", movieProduct);

        getTotalAmountOfItemsInCart( session,model);

        return "adminUpdate";

    }


    /**
     * Admin can update the movie product in the store
     * @param movie_id is the movie id to be updated
     * @param movie_name is the movie name to be updated
     * @param date_of_release is the date of release to be updated
     * @param movie_info is teh movi einformation to be updated
     * @param listPrice is the lsit price to be updated
     * @param session holds the logged-in users information
     * @param model holds attributes for the view
     * @return the admin panel index page
     */
    @PostMapping("updateMovieProduct")
    public String updateMovieProduct(@RequestParam(name = "movie_id") String movie_id,
                                     @RequestParam(name = "movie_name") String movie_name,
                                     @RequestParam (name = "date_of_release") String date_of_release,
                                     @RequestParam (name = "movie_info")String movie_info,
                                     @RequestParam (name = "listPrice")String listPrice, HttpSession session, Model model){


        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

        int movieId2 = Integer.parseInt(movie_id);

        MovieProduct movieProduct = movieProductDao.getMovieById(movieId2);

        model.addAttribute("movieProduct", movieProduct);


        String messageUpdate;
        // update product name
         movieProductDao.updateMovieProductNameByMovieId(movie_name, movieId2);



        // update product dateOfRelease
        LocalDate releaseDate = LocalDate.parse(date_of_release);

        movieProductDao.updateMovieProductDateOfReleaseByMovieId(releaseDate, movieId2);


        // update movie info

       movieProductDao.updateMovieProductInfoByMovieId(movie_info, movieId2);


        // update listPrice

        double listPrice2 = Double.parseDouble(listPrice);

        movieProductDao.updateMovieProductPriceByMovieId(listPrice2, movieId2);


        // showing all movie Product

        getTotalAmountOfItemsInCart( session,model);


        messageUpdate = "Movie Product " +movieProduct.getMovie_name()+ " was updated";
        model.addAttribute("messageUpdate", messageUpdate);
        log.info(messageUpdate);

        getAllMovieProducts(model, movieProductDao);


        return "adminPanel_index";
    }


    /**
     * Getting movie products based on the name being searched
     * @param session holds the logged-in users information
     * @param model holds attributes for the view
     * @param query is what is being searched
     * @return movieProductSearch page
     */
    @GetMapping("/viewMovieProductBySearch")
    public String viewMovieProductBySearch(HttpSession session, Model model, @RequestParam(name = "query") String query){



        // totalAmountOItems in basket
        getTotalAmountOfItemsInCart(session,model);


        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

        ArrayList<MovieProduct> movieBySearch = movieProductDao.searchForMovieProductBYMovieName(query);


        if (movieBySearch.isEmpty()){
            String message = query + " Doesnt Exist, search again";
            model.addAttribute("messageSearch", message);


            return "movieProductSearch";
        }

        model.addAttribute("movieBySearch", movieBySearch);

        session.setAttribute("query", query);

        model.addAttribute("query", query);


        return "movieProductSearch";
    }

    /**
     * Get all the movie products for the store
     * @param model holds the attributes for the view
     * @param movieProductDao holds all teh movie products
     */
    private static void getAllMovieProducts(Model model, MovieProductDao movieProductDao) {
        List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
        model.addAttribute("movieProducts", movieProducts);
    }

    /**
     * Getting the total amount of items in the cart for logged in user
     * @param session holds the logged-in users information
     * @param model holds attributes for the view
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
