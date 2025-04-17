package softwareProject.persistence;

import softwareProject.business.BillingAddress;
import softwareProject.business.CartItem;
import softwareProject.business.ChatRoom;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatRoomDaoImpl extends MySQLDao implements ChatRoomDao{

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public ChatRoomDaoImpl(String databaseName){
        super(databaseName);
    }

    public ChatRoomDaoImpl(Connection conn){
        super(conn);
    }
    public ChatRoomDaoImpl(){
        super();
    }


    /**
     * Add cart item
     * @param chatRoom is the chat room being added
     * @return 1 if added and 0 if not added
     */

    @Override
    public int addChatRoom(ChatRoom chatRoom){
        // DATABASE CODE
        //
        // Create variable to hold the result of the operation
        // Remember, where you are NOT doing a select, you will only ever get
        // a number indicating how many things were changed/affected
        int rowsAffected = 0;


        Connection conn = super.getConnection();

        // TRY to prepare a statement from the connection
        // When you are parameterizing the update, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try(PreparedStatement ps = conn.prepareStatement("insert into chat_room values(?, ?,?,?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, chatRoom.getChat_room_id());
            ps.setString(2, chatRoom.getUsername());
            ps.setString(3, chatRoom.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(chatRoom.getMessage_date()));
            ps.setString(5, chatRoom.getUser_image());

            // Execute the update and store how many rows were affected/changed
            // when inserting, this number indicates if the row was
            // added to the database (>0 means it was added)
            rowsAffected = ps.executeUpdate();
        }// Add an extra exception handling block for where there is already an entry
        // with the primary key specified
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            // Set the rowsAffected to -1, this can be used as a flag for the display section
            rowsAffected = -1;
        }catch(SQLException e){
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;
    }


    /**
     * Get all billing addresses
     * @return all billing addresses
     */

    @Override
    public ArrayList<ChatRoom> getAllChatRoom(){

        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            con = getConnection();

            String query = "Select * from chat_room";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next())
            {
                ChatRoom c = mapRow(rs);
                chatRooms.add(c);
            }
        }catch(SQLException e){
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the  method: " + e.getMessage());
            }
        }

        return chatRooms;
    }


    @Override
    public int deleteChatRoomMessageByTime(LocalDateTime message_date){
        int rowsAffected = 0;

        Connection con = null;
        PreparedStatement ps = null;


        try{

            con = getConnection();

            String query = "DELETE from chat_room where message_date = ?";

            ps = con.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.valueOf(message_date));
            rowsAffected = ps.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Exception occured in the updateProductName() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the updateProductName() method");
                e.getMessage();
            }
        }

        return rowsAffected;

    }





    /**
     * Search through each row in the cartItem
     * @param rs is the rating query being searched
     * @return the rating information
     * @throws SQLException is username
     */

    private ChatRoom mapRow(ResultSet rs)throws SQLException {

        ChatRoom c = new ChatRoom(
                rs.getInt("chat_room_id"),
                rs.getString("username"),
                rs.getString("message"),
                rs.getTimestamp("message_date").toLocalDateTime(),
                rs.getString("user_image")
        );
        return c;
    }


}
