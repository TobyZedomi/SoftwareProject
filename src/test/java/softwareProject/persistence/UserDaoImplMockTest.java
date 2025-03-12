package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDaoImplMockTest {

    public UserDaoImplMockTest(){

    }

    /**
     * Mock test to add a new User
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void registerUser() throws SQLException {

        System.out.println("Mock test for adding a new User ");

        User tester = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into users values(?, ?, ?, ?, ?, ?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        UserDao userDao = new UserDaoImpl(dbConn);
        int result = userDao.registerUser(tester);

        assertEquals(expected, result);

    }

    /**
     * Mock test for login
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void login() throws SQLException {

        System.out.println("Mock test for login");


        System.out.println("Mock Test to get Billing Address by id");

        User expected = new User("admin", "adminUser123", "admin@gmail.com", "Admin123@", LocalDate.of(2003, 02, 16), true, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 1 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getString("username")).thenReturn(expected.getUsername());
        when(rs.getString("displayName")).thenReturn(expected.getDisplayName());
        when(rs.getString("email")).thenReturn(expected.getEmail());
        when(rs.getString("password")).thenReturn(expected.getPassword());
        when(rs.getDate("dateOfBirth").toLocalDate()).thenReturn(expected.getDateOfBirth());
        when(rs.getBoolean("isAdmin")).thenReturn(expected.isAdmin());
        when(rs.getTimestamp("createdAt").toLocalDateTime()).thenReturn(expected.getCreatedAt());

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        UserDao userDao = new UserDaoImpl(dbConn);
        User user = userDao.login(expected.getUsername(), expected.getPassword());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(expected, user);


    }

    /**
     * Mock test for findUser by username
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findUserByUsername() throws SQLException {

        System.out.println("Mock test for login");


        System.out.println("Mock Test to get Billing Address by id");

        User expected = new User("admin", "adminUser123", "admin@gmail.com", "Admin123@", LocalDate.of(2003, 02, 16), true, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * FROM users where username = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 1 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getString("username")).thenReturn(expected.getUsername());
        when(rs.getString("displayName")).thenReturn(expected.getDisplayName());
        when(rs.getString("email")).thenReturn(expected.getEmail());
        when(rs.getString("password")).thenReturn(expected.getPassword());
        when(rs.getDate("dateOfBirth").toLocalDate()).thenReturn(expected.getDateOfBirth());
        when(rs.getBoolean("isAdmin")).thenReturn(expected.isAdmin());
        when(rs.getTimestamp("createdAt").toLocalDateTime()).thenReturn(expected.getCreatedAt());

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        UserDao userDao = new UserDaoImpl(dbConn);
        User user = userDao.findUserByUsername(expected.getUsername());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(expected, user);

    }
}