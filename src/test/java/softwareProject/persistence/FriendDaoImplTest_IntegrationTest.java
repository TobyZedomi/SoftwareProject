package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.Friends;
import softwareProject.business.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FriendDaoImplTest_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    /**
     * Testing sending a friend request.
     * @throws SQLException if something is wrong with database
     */
    @Test
    void insertIntoFriend() throws SQLException {
        System.out.println("Test to send a friend request");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);

        FriendDao friendDao = new FriendDaoImpl(conn);

        int result = friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());
        assertEquals(correctResult, result);

        boolean found = false;
        ArrayList<Friends> getRequests = friendDao.getAllRequests(tester2.getUsername());
        for(int i = 0; i<getRequests.size();i++){
            if(getRequests.get(i).getFriend1().equalsIgnoreCase(tester1.getUsername())){
                found=true;
            }
        }
        assertTrue(found,"Request not found in database");

    }
    /**
     * Testing sending a friend request duplicate.
     * @throws SQLException if something is wrong with database
     */
    @Test
    void insertIntoFriendDuplicate() throws SQLException {
        System.out.println("Test to send a friend request duplicate");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);

        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());
        int result = friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());

        assertEquals(correctResult, result);


    }
    /**
     * Accept a friend request test.
     * @throws SQLException if something is wrong with database
     */
    @Test
    void acceptRequest() throws SQLException {
        System.out.println("Test to accept a friend request");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);

        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());

        int result = friendDao.acceptRequest(tester2.getUsername(),tester1.getUsername());
        assertEquals(correctResult, result);


        ArrayList<Friends> searchFriend = friendDao.searchForFriends(tester2.getUsername(), tester1.getUsername());
        boolean found = false;
        for(int i =0; i<searchFriend.size();i++){
            if(searchFriend.get(i).getFriend1().equals(tester1.getUsername())){
                found=true;
            }
        }
        assertTrue(found,"Friend not found in database");
    }

    /**
     * Decline a friend request test.
     * @throws SQLException if something is wrong with database
     */
    @Test
    void declineRequest() throws SQLException {
        System.out.println("Test to decline a friend request");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);

        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());

        int result = friendDao.declineRequest(tester2.getUsername(),tester1.getUsername());
        assertEquals(correctResult, result);


        ArrayList<Friends> searchFriend = friendDao.searchForFriends(tester2.getUsername(), tester1.getUsername());
        boolean found = false;
        for(int i =0; i<searchFriend.size();i++){
            if(searchFriend.get(i).getFriend1().equals(tester1.getUsername())){
                found=true;
            }
        }
        assertFalse(found,"Friend was found in database");
    }

    /**
     * Test for get all requests
     * @throws SQLException if something is wrong with database
     */
    @Test
    void getAllRequests() throws SQLException {
        System.out.println("Test to get all friend requests");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester3 = new User("John", "John123", "John@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 2;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);
        userDao.registerUser(tester3);


        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());
        friendDao.insertIntoFriend(tester3.getUsername(),tester2.getUsername());


        ArrayList<Friends> getAllFriends = friendDao.getAllRequests(tester2.getUsername());
        assertEquals(correctResult,getAllFriends.size());
    }

    /**
     * Test for get number of friends
     * @throws SQLException if something is wrong with database
     */
    @Test
    void getNumberOfFriends() throws SQLException {
        System.out.println("Test to get all friend");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester3 = new User("John", "John123", "John@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 2;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);
        userDao.registerUser(tester3);


        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());
        friendDao.insertIntoFriend(tester1.getUsername(),tester3.getUsername());

        friendDao.acceptRequest(tester2.getUsername(),tester1.getUsername());
        friendDao.acceptRequest(tester3.getUsername(),tester1.getUsername());

        int getAllFriends = friendDao.getNumberOfFriends(tester1.getUsername());
        assertEquals(correctResult,getAllFriends);
    }

    /**
     * Test for get a friend
     * @throws SQLException if something is wrong with database
     */
    @Test
    void getAFriend() throws SQLException {
        System.out.println("Test to get a friend");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);

        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());

        int result = friendDao.acceptRequest(tester2.getUsername(),tester1.getUsername());
        assertEquals(correctResult, result);


        boolean friend = friendDao.getAFriend(tester2.getUsername(), tester1.getUsername());

        assertTrue(friend,"Friend was not found in database");
    }

    /**
     * Test for get all friends
     * @throws SQLException if something is wrong with database
     */
    @Test
    void getAllFriends() throws SQLException {
        System.out.println("Test to get all friend");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester3 = new User("John", "John123", "John@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 2;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);
        userDao.registerUser(tester3);


        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());
        friendDao.insertIntoFriend(tester1.getUsername(),tester3.getUsername());

        friendDao.acceptRequest(tester2.getUsername(),tester1.getUsername());
        friendDao.acceptRequest(tester3.getUsername(),tester1.getUsername());

        ArrayList<Friends> getAllFriends = friendDao.getAllFriends(tester1.getUsername());
        assertEquals(correctResult,getAllFriends.size());
    }

    /**
     * Test search for friends
     * @throws SQLException if something is wrong with database
     */
    @Test
    void searchForFriends() throws SQLException {
        System.out.println("Test to search for friends");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester3 = new User("John", "John123", "John@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);
        userDao.registerUser(tester3);


        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());
        friendDao.insertIntoFriend(tester1.getUsername(),tester3.getUsername());

        friendDao.acceptRequest(tester2.getUsername(),tester1.getUsername());
        friendDao.acceptRequest(tester3.getUsername(),tester1.getUsername());

        ArrayList<Friends> searchFriend = friendDao.searchForFriends(tester1.getUsername(),tester2.getUsername());
        assertEquals(correctResult,searchFriend.size());

        boolean found = false;
        for(int i =0; i<searchFriend.size();i++){
            if(searchFriend.get(i).getFriend1().equals(tester1.getUsername())){
                found=true;
            }
        }
        assertTrue(found,"Friend was found in database");
    }

    /**
     * Test to delete a friend
     * @throws SQLException if something is wrong with database
     */
    @Test
    void deleteAFriend() throws SQLException {
        System.out.println("Test to delete a friend");
        User tester1 = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");
        User tester2 = new User("Alex", "Alex123", "Alex@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);

        UserDao userDao = new UserDaoImpl(conn);

        userDao.registerUser(tester1);
        userDao.registerUser(tester2);

        FriendDao friendDao = new FriendDaoImpl(conn);

        friendDao.insertIntoFriend(tester1.getUsername(),tester2.getUsername());

        int result = friendDao.acceptRequest(tester2.getUsername(),tester1.getUsername());
        assertEquals(correctResult, result);

        int complete = friendDao.deleteAFriend(tester1.getUsername(), tester2.getUsername());
        assertEquals(correctResult,complete);
    }
}