package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.ChatRoom;
import softwareProject.business.MovieDbByMovieId;
import softwareProject.business.MovieTrailer;
import softwareProject.persistence.ChatRoomDao;
import softwareProject.persistence.ChatRoomDaoImpl;
import softwareProject.service.MovieService;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ChatRoomController {

    @Autowired
    private MovieService movieService;
    @GetMapping("/getAllChatMessage")
    public String viewChatMessage( @RequestParam(name = "id") String id, HttpSession session, Model model){

        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl("database.properties");


        int movieId = Integer.parseInt(id);

        ArrayList<ChatRoom> chatRooms = chatRoomDao.getAllChatRoom();

        model.addAttribute("chatRooms", chatRooms);

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


}
