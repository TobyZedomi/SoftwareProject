package softwareProject.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import softwareProject.business.*;
import softwareProject.persistence.*;

import java.time.LocalDateTime;


@Controller
@Slf4j
public class ChatController {

    private SimpMessagingTemplate template;

    @Autowired
    public ChatController(SimpMessagingTemplate template){
        this.template = template;
    }


    /**
     * Send a chat message to a particular room
     * @param room is the room id the message is being sent to
     * @param chatMessage is the chat message being sent
     */
    @MessageMapping("/chat.sendMessage/{room}")
    public void sendMessage(@DestinationVariable String room, @Payload ChatMessage chatMessage){

        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl("database.properties");

        int chatRoomId = Integer.parseInt(room);


        chatRoomDao.addChatRoom(new ChatRoom(0, chatMessage.getSender(), chatMessage.getContent(), LocalDateTime.now(), "DefaultUserImage.jpg", chatRoomId));

        this.template.convertAndSend("/topic/public/"+room, chatMessage);
    }


    /**
     * Add user to a particular chat room
     * @param room is the room id the user is being added to
     * @param chatMessage is the message seen when user enetrs the chat
     * @param headerAccessor is the users name being added in the websocket session
     */

    @MessageMapping("/chat.addUser/{room}")
    public void addUser(@DestinationVariable String room, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){

        //add username in web socket session
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());

        this.template.convertAndSend("/topic/public/"+room, chatMessage);
    }

}
