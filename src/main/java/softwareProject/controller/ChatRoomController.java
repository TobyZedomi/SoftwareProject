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
import softwareProject.persistence.ChatRoomDao;
import softwareProject.persistence.ChatRoomDaoImpl;
import softwareProject.persistence.MovieProductDao;
import softwareProject.persistence.MovieProductDaoImpl;
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

        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl("database.properties");


        int movieId = Integer.parseInt(id);

        //session.setAttribute("chat_room_id", movieId);
        model.addAttribute("chat_room_id", id);

        ArrayList<ChatRoom> chatRooms = chatRoomDao.getAllChatRoomByRoomId(movieId);

        ArrayList<ChatRoom> chatRoomArrayList = new ArrayList<>();

        for (int i = 0;  i < chatRooms.size();i++){

            if (chatRooms.get(i).getMessage_date().toLocalTime().isBefore(LocalTime.from(LocalDateTime.now().minusMinutes(5)))){

                chatRoomDao.deleteChatRoomMessageByTime(chatRooms.get(i).getMessage_date());

                return "videos";
            }

            chatRoomArrayList.add(chatRooms.get(i));
            System.out.println(chatRoomArrayList);
        }

        model.addAttribute("chatRooms", chatRoomArrayList);

        List<MovieTrailer> trailers = movieService.getTrailer(movieId);

        if (trailers.isEmpty()) {

            return "noVideo";
        }
        model.addAttribute("trailers", trailers);

        MovieDbByMovieId movieDbByMovieId = movieService.getMoviesByMovieId(movieId);
        model.addAttribute("movieName", movieDbByMovieId.getTitle());

        System.out.println(chatRooms);


        return "videos";
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







}
