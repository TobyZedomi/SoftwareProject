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
import softwareProject.business.MovieProduct;
import softwareProject.persistence.MovieProductDao;
import softwareProject.persistence.MovieProductDaoImpl;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@Slf4j
public class MovieProductController {



    @PostMapping("/addMovieProduct")
    public String addMovieProduct( @RequestParam(name = "movie_name") String movie_name,
                            @RequestParam (name = "date_of_release") String date_of_release,
                            @RequestParam (name = "movie_info")String movie_info,
                            @RequestParam (name = "listPrice")String listPrice, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes
            ,Model model, HttpSession session) throws IOException {

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");


        LocalDate releaseDate = LocalDate.parse(date_of_release);
        Time length = Time.valueOf("00:00:00");
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

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);
            return "adminPanel_index";


        }else{

            message = "Movie added: "+newMovieProduct.getMovie_name();
            model.addAttribute("message", message);

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);
            log.info(message);

            return "adminPanel_index";
        }
    }


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

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);

        }else{

            message = "Movie Product " +movieProduct.getMovie_name()+ " was not deleted";
            model.addAttribute("message", message);
            log.info(message);

            List<MovieProduct> movieProducts = movieProductDao.getAllMovieProducts();
            model.addAttribute("movieProducts", movieProducts);

        }
        return "adminPanel_index";

    }

}
