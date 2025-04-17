package softwareProject.persistence;

import softwareProject.business.ChatRoom;

import java.util.ArrayList;

public interface ChatRoomDao {

    public int addChatRoom(ChatRoom chatRoom);

    public ArrayList<ChatRoom> getAllChatRoom();
}
