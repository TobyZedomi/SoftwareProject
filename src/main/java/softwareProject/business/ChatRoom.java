package softwareProject.business;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {


    private int chat_room_id;

    private String username;

    private String message;

    private LocalDateTime message_date;

    private String user_image;

    private int room_id;


}
