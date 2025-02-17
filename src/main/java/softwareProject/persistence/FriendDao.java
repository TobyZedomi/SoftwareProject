package softwareProject.persistence;

public interface FriendDao {

    public int insertIntoFriend(String user,String friend);
    public int acceptRequest(String user,String friend);
    public int declineRequest(String user,String friend);
    public int getAllRequests(String user);
    public int getAllFriends(String user);
}
