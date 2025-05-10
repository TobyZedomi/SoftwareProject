package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.CartItem;
import softwareProject.business.ChatRoom;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChatRoomDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Integration test to add to a chat room
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addChatRoom() throws SQLException {

        System.out.println("Integration test to add a chat room");

        ChatRoom tester = new ChatRoom(4,"Toby", "Hi", LocalDateTime.now(), "Default Image", 2);
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        int result = chatRoomDao.addChatRoom(tester);
        assertEquals(correctResult, result);

    }

    /**
     * Add a new chat room but username doesn't exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addChatRoomButUsernameDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new Chat Room but username doesnt exist");

        ChatRoom tester = new ChatRoom(4,"Tobyyy", "Hi", LocalDateTime.now(), "Default Image", 2);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        int result = chatRoomDao.addChatRoom(tester);
        assertEquals(correctResult, result);

    }

    /**
     * Add chat room but its null
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addChatRoomButItsNull() throws SQLException {

        System.out.println("Add chat room but its null");

        ChatRoom tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            chatRoomDao.addChatRoom(tester);
        });
    }


    /**
     * Test to get all chat room
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllChatRoom() throws SQLException {

        System.out.println("Test to Get all chat room");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        ArrayList<ChatRoom> expected = new ArrayList<>();
        expected.add(new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 86331));
        expected.add(new ChatRoom(2, "James", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 950387));
        expected.add(new ChatRoom(3, "James", "Hello", LocalDateTime.of(2025,12,16,0,0,0), "Default_Image", 950387));


        ArrayList<ChatRoom> result = chatRoomDao.getAllChatRoom();

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }


    /**
     * Delete chat room messages by room id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void deleteChatRoomMessageByRoomId() throws SQLException {

        System.out.println("Integration test to delete chat room messages by room id ");


       ChatRoom tester = new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 86331);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        int result = chatRoomDao.deleteChatRoomMessageByTime(tester.getRoom_id());
        assertEquals(1, result);

    }

    /**
     * Delete chat room messages by room id but room id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void deleteChatRoomMessageByRoomIdButRoomIdDoesntExist() throws SQLException {

        System.out.println("Integration test to delete chat room messages by room id but room id doesnt exist");


        ChatRoom tester = new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 863313434);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        int result = chatRoomDao.deleteChatRoomMessageByTime(tester.getRoom_id());
        assertEquals(0, result);

    }


    /**
     * Test to get all chat room by room id
     * @throws SQLException if something goes wrong in the database
     */

    @Test
    void getAllChatRoomByRoomId() throws SQLException {



        System.out.println("Test to Get all chat room by room id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        ArrayList<ChatRoom> expected = new ArrayList<>();
        expected.add(new ChatRoom(2, "James", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 950387));
        expected.add(new ChatRoom(3, "James", "Hello", LocalDateTime.of(2025,12,16,0,0,0), "Default_Image", 950387));


        ArrayList<ChatRoom> result = chatRoomDao.getAllChatRoomByRoomId(950387);

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

    }



    /**
     * Test to get all chat room by room id but room id doesn't exist
     * @throws SQLException if something goes wrong in the database
     */

    @Test
    void getAllChatRoomByRoomIdButRoomIdDoesntExist() throws SQLException {



        System.out.println("Test to Get all chat room by room id but room id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        ArrayList<ChatRoom> expected = new ArrayList<>();
        expected.add(new ChatRoom(2, "James", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 950387));
        expected.add(new ChatRoom(3, "James", "Hello", LocalDateTime.of(2025,12,16,0,0,0), "Default_Image", 950387));


        ArrayList<ChatRoom> result = chatRoomDao.getAllChatRoomByRoomId(1);

        // test size

        assertNotEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

    }

    /**
     * test to delete chat messages that are more than 5 minutes long
     * @throws SQLException
     */
    @Test
    void deleteChatRoomMessageByTimeMoreThan5Minutes() throws SQLException {

        System.out.println("Integration test to delete chat room messages by time more than 5 minutes ");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(conn);

        int result = chatRoomDao.deleteChatRoomMessageByTimeMoreThan5Minutes();
        assertEquals(2, result);

    }
}