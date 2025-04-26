package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.CartItem;
import softwareProject.business.ChatRoom;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatRoomDaoImpl_MockTest {


    public ChatRoomDaoImpl_MockTest(){

    }

    /**
     * Mock test to add chat room
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addChatRoom() throws SQLException {

        System.out.println("Mock test to add a chat room");

        ChatRoom tester = new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 863313434);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into chat_room values(?, ?,?,?,?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;


        // adding a new billingAddress
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(dbConn);
        int result = chatRoomDao.addChatRoom(tester);

        assertEquals(expected, result);
    }


    /**
     * Mock test to get all chat room
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllChatRoom() throws SQLException {

        System.out.println("Mock test to get all chat room ");
        /// expected Results

        ChatRoom c1 = new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 86331);
        ChatRoom c2 = new ChatRoom(2, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 86331);

        ArrayList<ChatRoom> expectedResults = new ArrayList<>();
        expectedResults.add(c1);
        expectedResults.add(c2);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("Select * from chat_room")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultset, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getInt("chat_room_id")).thenReturn(c1.getChat_room_id(), c2.getChat_room_id());
        when(rs.getString("username")).thenReturn(c1.getUsername(), c2.getUsername());
        when(rs.getString("message")).thenReturn(c1.getMessage(), c2.getMessage());
        when(rs.getTimestamp("message_date")).thenReturn(Timestamp.valueOf(c1.getMessage_date()), Timestamp.valueOf(c2.getMessage_date()));
        when(rs.getString("user_image")).thenReturn(c1.getUser_image(), c2.getUser_image());
        when(rs.getInt("room_id")).thenReturn(c1.getRoom_id(), c2.getRoom_id());

        int numChatRoomId = 2;

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(dbConn);
        ArrayList<ChatRoom> result = chatRoomDao.getAllChatRoom();
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numChatRoomId, result.size());

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);


    }


    /**
     * Delete chat room messages by room_id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void deleteChatRoomMessageById() throws SQLException {


        System.out.println("Mock test to delete chat room messages by id");

        ChatRoom tester = new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 863313434);

        // Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("DELETE from chat_room where room_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(dbConn);
        int result = chatRoomDao.deleteChatRoomMessageByTime(tester.getRoom_id());

        System.out.println(result);

        assertEquals(expected, result);

    }

    /**
     * Mock test to delete all chat room by room id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllChatRoomByRoomId() throws SQLException {

        System.out.println("Mock test to get chat room by room id");

        ChatRoom c1 = new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 86331);
        ChatRoom c2 = new ChatRoom(2, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 86331);

        ArrayList<ChatRoom> expectedResults = new ArrayList<>();
        expectedResults.add(c1);
        expectedResults.add(c2);

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * FROM chat_room where room_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getInt("chat_room_id")).thenReturn(c1.getChat_room_id(), c2.getChat_room_id());
        when(rs.getString("username")).thenReturn(c1.getUsername(), c2.getUsername());
        when(rs.getString("message")).thenReturn(c1.getMessage(), c2.getMessage());
        when(rs.getTimestamp("message_date")).thenReturn(Timestamp.valueOf(c1.getMessage_date()), Timestamp.valueOf(c2.getMessage_date()));
        when(rs.getString("user_image")).thenReturn(c1.getUser_image(), c2.getUser_image());
        when(rs.getInt("room_id")).thenReturn(c1.getRoom_id(), c2.getRoom_id());


        int numCartItemsInTable = 2;


        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(dbConn);
        ArrayList<ChatRoom> result = chatRoomDao.getAllChatRoomByRoomId(86331);
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numCartItemsInTable, result.size());

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);


    }


    /**
     * Mock test to delete chat room messages by time more than 5 minutes
     * @throws SQLException if soemthing goes wrong in the database
     */
    @Test
    void deleteChatRoomMessageByTimeMoreThan5Minutes() throws SQLException {

        System.out.println(" Mock test to delete chat room messages by time more than 5 minutes");

        ChatRoom tester = new ChatRoom(1, "Kate", "Hello", LocalDateTime.of(2025,04,16,0,0,0), "Default_Image", 863313434);

        // Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("DELETE FROM chat_room WHERE message_date <= CURRENT_TIMESTAMP - INTERVAL 5 MINUTE")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl(dbConn);
        int result = chatRoomDao.deleteChatRoomMessageByTimeMoreThan5Minutes();

        System.out.println(result);

        assertEquals(expected, result);


    }
}