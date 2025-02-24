package softwareProject.persistence;

import softwareProject.business.Friends;

import java.util.ArrayList;
import java.util.List;

public interface FriendDao {

    public int insertIntoFriend(String user,String friend);
    public int acceptRequest(String user,String friend);
    public int declineRequest(String user,String friend);
    public ArrayList<Friends> getAllRequests(String user);
    public int getNumberOfFriends(String user);
    public boolean getAFriend(String user, String friend);
    public ArrayList<Friends> getAllFriends(String user);
}
