package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import softwareProject.business.*;
import softwareProject.persistence.*;

import java.time.LocalDateTime;


@Controller
@Slf4j
public class ChatController {

    private final HttpSession session;

    @Autowired
    public ChatController(HttpSession session) {
        this.session = session;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){

        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl("database.properties");

       // int chat_room_id = (int) session.getAttribute("id");


        chatRoomDao.addChatRoom(new ChatRoom(0, chatMessage.getSender(), chatMessage.getContent(), LocalDateTime.now(), "DefaultUserImage.jpg"));

        return chatMessage;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){


        //add username in web socket session
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }

}
