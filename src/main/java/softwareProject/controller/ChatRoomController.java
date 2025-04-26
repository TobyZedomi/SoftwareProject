package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softwareProject.business.*;
import softwareProject.persistence.*;
import softwareProject.service.MovieService;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ChatRoomController {

    @Autowired
    private MovieService movieService;
    @GetMapping("/getAllChatMessage")
    public String viewChatMessage(@RequestParam(name = "id") String id, HttpSession session, Model model){

        if (session.getAttribute("loggedInUser") != null) {

            User u = (User) session.getAttribute("loggedInUser");


            ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl("database.properties");

            // delete chat messages that are more than 5 minutes
            chatRoomDao.deleteChatRoomMessageByTimeMoreThan5Minutes();


            int movieId = Integer.parseInt(id);

            //session.setAttribute("chat_room_id", movieId);
            model.addAttribute("chat_room_id", id);

            ArrayList<ChatRoom> chatRooms = chatRoomDao.getAllChatRoomByRoomId(movieId);

            model.addAttribute("chatRooms", chatRooms);

            List<MovieTrailer> trailers = movieService.getTrailer(movieId);

            if (trailers.isEmpty()) {

                return "noVideo";
            }
            model.addAttribute("trailers", trailers);

            MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieId);
            model.addAttribute("movieName", movieDbByMovieId.getTitle());

            System.out.println(chatRooms);

            getTotalAmountOfItemsInCart(session, model);

            log.info("User {} clicked to watch movie videos on {}and access ChatRoom", u.getUsername(), movieDbByMovieId.getTitle());


            return "videos";

        }

        return "notValidUser";
    }

/*

    @PostMapping("/addMessageToChat")
    public String addMovieProduct(@RequestParam(name = "message") String message
            , Model model, HttpSession session) {


        User u = (User) session.getAttribute("loggedInUser");

        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl("database.properties");


        int chat_room_id = (int) session.getAttribute("chat_room_id");

        chatRoomDao.addChatRoom(new ChatRoom(chat_room_id, u.getUsername(), message, LocalDateTime.now(), "DefaultUserImage.jpg"));


        List<MovieTrailer> trailers = movieService.getTrailer(chat_room_id);

        if (trailers.isEmpty()) {

            return "noVideo";
        }
        model.addAttribute("trailers", trailers);

        MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(chat_room_id);
        model.addAttribute("movieName", movieDbByMovieId.getTitle());

        return "videos";
    }

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
