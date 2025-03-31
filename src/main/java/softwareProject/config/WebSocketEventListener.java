package softwareProject.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import softwareProject.business.ChatMessage;
import softwareProject.business.MessageType;
import softwareProject.business.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {


    private final SimpMessageSendingOperations messageTemplate;
@EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){


    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String username = (String) headerAccessor.getSessionAttributes().get("username");

    if (username != null){

        log.info("User disconnected: {} ", username);
        var chatMessage = ChatMessage.builder()
                .type(MessageType.LEAVE)
                .sender(username)
                .build();
        messageTemplate.convertAndSend("/topic/public", chatMessage);
    }


    }


}
