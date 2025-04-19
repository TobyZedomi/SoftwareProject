package softwareProject.persistence;

import softwareProject.business.ChatRoom;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ChatRoomDao {

    public int addChatRoom(ChatRoom chatRoom);

    public ArrayList<ChatRoom> getAllChatRoom();

    public int deleteChatRoomMessageByTime(int room_id);

    public ArrayList<ChatRoom> getAllChatRoomByRoomId(int roomId);

    public int deleteChatRoomMessageByTimeMoreThan5Minutes();
}
