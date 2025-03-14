package softwareProject.persistence;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import softwareProject.business.BillingAddress;
import softwareProject.business.Friends;
import softwareProject.business.User;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Repository
@Slf4j
public class UserDaoImpl extends MySQLDao implements UserDao {

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public UserDaoImpl(String databaseName){
        super(databaseName);
    }

    public UserDaoImpl(Connection conn){
        super(conn);
    }
    public UserDaoImpl(){
        super();
    }


    /**
     * Add a new user to the database
     * @param newUser is the user being added
     * @return 1 is user was added and -1 if not added
     */
    @Override
    public int registerUser(User newUser){
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
        try(PreparedStatement ps = conn.prepareStatement("insert into users values(?, ?, ?, ?, ?, ?, " +
                "?,?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, newUser.getUsername());
            ps.setString(2, newUser.getDisplayName());
            ps.setString(3, newUser.getEmail());
            ps.setString(4, hashPassword(newUser.getPassword()));
            ps.setDate(5, Date.valueOf(newUser.getDateOfBirth()));
            ps.setBoolean(6, newUser.isAdmin());
            ps.setTimestamp(7, Timestamp.valueOf(newUser.getCreatedAt()));
            ps.setString(8, newUser.getUser_image());

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
     * Get user based on username and password for the login system
     * @param username is the username being searched
     * @param password is the password being searched
     * @return the user if username and password match, if username or password doesn't match it will return null
     */
    @Override
    public User login(String username, String password){

        User user = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();



            if(rs.next()){

                user = mapRow(rs);
            }


        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }  finally {
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
                System.out.println("Exception occurred in the finally section of the method: " + e.getMessage());
            }
        }
        return user;
    }







    /**
     * Get a particular user based on the username
     * @param username is the user being searched
     * @return the user from that particular username
     */


    @Override
    public User findUserByUsername(String username){

        User user = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * FROM users where username = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);
            rs = ps.executeQuery();



            if(rs.next()){

                user = mapRow(rs);
            }


        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
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
                System.out.println("Exception occurred in the finally section of the method: " + e.getMessage());
            }
        }
        return user;
    }

    /**
     * Get the user of a particular email
     * @param email is the email being searched
     * @return the user that has the particular email
     */
    @Override
    public User findUserByThereEmail(String email){

        User user = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users where email = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, email);


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if(rs.next()){

                    user = mapRow(rs);
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
        return user;
    }

    /**
     * Get a list of user based on the username
     * @param username is the user being searched
     * @return the user from that particular username
     */
    @Override
    public ArrayList<User> findUserByUsername2(String username){

        ArrayList<User> requests = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users where username like ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username+"%");


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){

                    User u = mapRow(rs);
                    requests.add(u);
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
     * Will updated password linked with email
     * @param email the email of user
     * @param password the new bcrypt password
     * @return -1 if not complete, 1 if complete
     */
    @Override
    public int updatePassword(String email,String password){
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
        try(PreparedStatement ps = conn.prepareStatement("UPDATE users SET password = ? WHERE email = ?")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, hashPassword(password));
            ps.setString(2, email);

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




    private static int workload = 12;

    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);


        return(hashed_password);
    }

    /**
     * Updates the user imaged based on the logged-in users username
     * @param user the logged-in user
     * @param image the new imagine
     * @return -1 if not complete, 1 if complete
     */
    @Override
    public int updateUserImage(String user,String image){
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
        try(PreparedStatement ps = conn.prepareStatement("update users set user_image = ? where username = ?")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, image);
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
     * Updates the display name based on the logged-in users username
     * @param username the logged-in user
     * @param displayName the new display name
     * @return -1 if not complete, 1 if complete
     */
    @Override
    public int updateDisplayName(String username,String displayName){
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
        try(PreparedStatement ps = conn.prepareStatement("update users set displayName = ? where username = ?")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, displayName);
            ps.setString(2, username);

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
     * Search through each row in the user
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException is username and email isn't unique
     */
    private User mapRow(ResultSet rs)throws SQLException {

        User u = new User(

                rs.getString("username"),
                rs.getString("displayName"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getDate("dateOfBirth").toLocalDate(),
                rs.getBoolean("isAdmin"),
                rs.getTimestamp("createdAt").toLocalDateTime(),
                rs.getString("user_image")
        );
        return u;
    }

}
