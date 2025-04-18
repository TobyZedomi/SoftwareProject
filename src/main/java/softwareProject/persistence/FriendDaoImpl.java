package softwareProject.persistence;

import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.stereotype.Repository;
import softwareProject.business.Friends;
import softwareProject.business.Genre;
import softwareProject.business.Movie;
import softwareProject.business.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FriendDaoImpl extends MySQLDao implements FriendDao{

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public FriendDaoImpl(String databaseName){
        super(databaseName);
    }

    public FriendDaoImpl(Connection conn){
        super(conn);
    }
    public FriendDaoImpl(){
        super();
    }

    /**
     * Insert into the friend table, but they will not be friends until the friend request is accepted.
     * @param user the logged-in user
     * @param friend the user you are sending friend request to.
     * @return -1 if not complete, 1 if completed.
     */
    @Override
    public int insertIntoFriend(String user,String friend){
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
        try(PreparedStatement ps = conn.prepareStatement("insert into friends values(?, ?,false)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, user );
            ps.setString(2, friend);

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
     * Updates the friend table by changing the request to true as they are now friends.
     * @param user the logged-in user
     * @param friend the user you received the friend request from.
     * @return -1 if not complete, 1 if completed.
     */
    @Override
    public int acceptRequest(String user,String friend){
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
        try(PreparedStatement ps = conn.prepareStatement("update friends set request = true where friend1 = ? and friend2 = ?")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, friend);
            ps.setString(2, user);

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
     * Deletes from the friend table as the request is declined.
     * @param user the logged-in user
     * @param friend the user you received the friend request from.
     * @return -1 if not complete, 1 if completed.
     */
    @Override
    public int declineRequest(String user,String friend){
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
        try(PreparedStatement ps = conn.prepareStatement("delete from friends where friend1 = ? and friend2 = ? and request = false")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, friend);
            ps.setString(2, user);

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
     * Gets all the friends requests you have received by selecting all from friends where you are friend2(the receiver of the request) and
     * request is false;
     * @param user the logged-in user
     * @return Arraylist of friends
     */
    @Override
    public ArrayList<Friends> getAllRequests(String user){

        ArrayList<Friends> requests = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("select * from friends where friend2 = ? and request = false")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, user);


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){

                    Friends m = mapRow(rs);
                    requests.add(m);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return requests;
    }
    /**
     * Gets all the friends requests you have received by using a count selecting all from friends where you are friend2(the receiver of the request) and
     * request is false;
     * @param user the logged-in user
     * @return number of friends(int)
     */
    @Override
    public int getNumberOfFriends(String user){

        Connection conn = super.getConnection();

        try(PreparedStatement ps = conn.prepareStatement("Select count(*) from friends where (friend1 = ? or friend2 = ?) and request = true")){
            ps.setString(1,user);
            ps.setString(2,user);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }catch(SQLException e){
                System.out.println(LocalDateTime.now() + ": An SQLException  occurred while running the query" +
                        " or processing the result.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }catch(SQLException e){
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * This is used to check if you are friends with this user already. The user itself will never come into any interaction with this.
     * @param user logged-in user
     * @param friend the friend we are looking for.
     * @return boolean true if the user is found. false if it is not.
     */
    @Override
    public boolean getAFriend(String user, String friend){
        // DATABASE CODE
        //
        // Create variable to hold the result of the operation
        // Remember, where you are NOT doing a select, you will only ever get
        // a number indicating how many things were changed/affected
        boolean complete = false;


        Connection conn = super.getConnection();

        // TRY to prepare a statement from the connection
        // When you are parameterizing the update, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try(PreparedStatement ps = conn.prepareStatement("Select * from friends where (friend1 = ? and friend2 = ? or friend2 = ? and friend1 = ?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, user);
            ps.setString(2,friend);
            ps.setString(3, user);
            ps.setString(4,friend);
            // Execute the update and store how many rows were affected/changed
            // when inserting, this number indicates if the row was
            // added to the database (>0 means it was added)
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    complete = true;
                }
            }
        }// Add an extra exception handling block for where there is already an entry
        // with the primary key specified
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            // Set the rowsAffected to -1, this can be used as a flag for the display section
            complete = false;
        }catch(SQLException e){
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return complete;
    }

    /**
     * This will get a list of your friends
     * @param user logged-in user
     * @return Arraylist of friends
     */
    @Override
    public ArrayList<Friends> getAllFriends(String user){

        ArrayList<Friends> requests = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("Select * from friends where (friend1 = ? or friend2 = ?) and request = true")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, user);
            ps.setString(2, user);


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){

                    Friends m = mapRow(rs);
                    requests.add(m);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return requests;
    }

    /**
     * Be able to search for friends. You will be able to put in a letter or a few, and it will show you multiple users
     * related to this letter or letters. To do this we used %;
     * @param user the logged-in user
     * @param friend the friend we are searching for.
     * @return Arraylist of friends
     */
    @Override
    public ArrayList<Friends> searchForFriends(String user,String friend){

        ArrayList<Friends> requests = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("Select * from friends where (friend1 = ? and friend2 like ? or friend2 = ? and friend1 like ?) and request = true")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, user);
            ps.setString(2, friend);
            ps.setString(3, user);
            ps.setString(4, friend);


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){

                    Friends m = mapRow(rs);
                    requests.add(m);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return requests;
    }

    /**
     * Deletes a friend from the friend table.
     * @param user logged-in user
     * @param friend the friend we are removing
     * @return -1 if not complete, 1 if complete
     */
    @Override
    public int deleteAFriend(String user,String friend){
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
        try(PreparedStatement ps = conn.prepareStatement("delete from friends where (friend1 = ? and friend2 = ? || friend2 = ? and friend1 = ? ) and request = true")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, friend);
            ps.setString(2, user);
            ps.setString(3, friend);
            ps.setString(4, user);

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

    private Friends mapRow(ResultSet rs)throws SQLException {

        Friends f = new Friends(
                rs.getString("friend1"),
                rs.getString("friend2"),
                rs.getBoolean("request")
        );
        return f;
    }

}
