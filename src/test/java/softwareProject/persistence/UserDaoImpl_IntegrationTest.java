package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.User;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class UserDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    /**
     * Test to register a user
     * @throws SQLException if something is wrong with database
     */
    @Test
    void registerUser() throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("Test to register a user");
        User tester = new User("Harry", "harry123", "harry@gmail.com", "password", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        int result = userDao.registerUser(tester);
        assertEquals(correctResult, result);

        User inserted = userDao.findUserByUsername(tester.getUsername());
        assertNotNull(inserted);

      //  assertUserEquals(tester, inserted);

    }

    /**
     * Test register but username already exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void registerUserButUsernameAlreadyExist() throws SQLException{
        System.out.println("test for register but username already exist");
        User tester = new User("Toby", "harry123", "harry@gmail.com", "passowrd", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        int incorrectResult = -1;
        int result = userDao.registerUser(tester);
        assertEquals(incorrectResult, result);

        conn.rollback();
    }


    @Test
    void registerUserButEmailAlreadyExist() throws SQLException{

        System.out.println("Test for register but email already exist");
        User tester = new User("Harry", "harry123", "toby@gmail.com", "passowrd", LocalDate.of(2002, 10, 10), false, LocalDateTime.of(2025,01,02,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        int incorrectResult = -1;
        int result = userDao.registerUser(tester);
        assertEquals(incorrectResult, result);

        conn.rollback();
    }


    /**
     * Test for Register a user but user is null
     * @throws SQLException if something wrong with database
     */
    @Test
    void registerUserButNull() throws SQLException {

        System.out.println("Test for Register a user but user is null");
        User tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            userDao.registerUser(tester);
        });
    }

// login test

    /**
     * Test for login if username and password is correct
     * @throws SQLException
     */
    @Test
    void login() throws SQLException {
        System.out.println("Test for login and if username and password is correct");

        User expected = new User("admin", "adminUser123", "admin@gmail.com", "$2a$12$x4EwpUD5VU.vJW1.xICz1OnEJqEMfdYx/ttl/Gi/JxljZAsguzqbi", LocalDate.of(2003, 02, 16), true, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.login(expected.getUsername(), expected.getPassword());

        assertEquals(expected, result);
    }

    /**
     * Test for login, if username doesn't exist but password is correct
     * @throws SQLException if soemthing goes wrong in database
     */
    @Test
    void loginIfUsernameDoesntExistButPasswordIsCorrect()throws SQLException{

        System.out.println("Test for login, if username doesnt exist but password is correct");
        User expected = new User("Ronaldo", "andrewGamer123", "andrew@gmail.com", "passwordDone123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.login(expected.getUsername(), expected.getPassword());

        assertNotEquals(expected, result);
    }

    /**
     * Test for login if username does exist but password is incorrect
     * @throws SQLException if something gopes wrong in the database
     */
    @Test
    void loginIfUsernameDoesExistButPasswordIsNotCorrect() throws SQLException{
        System.out.println("Test for login if username does exist but password is incorrect");
        User expected = new User("Andrew", "andrewGamer123", "andrew@gmail.com", "ronaldo123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.login(expected.getUsername(), expected.getPassword());

        assertNotEquals(expected, result);
    }

    /**
     * Test for if username is null but password is correct
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void loginUsernameISNullButPasswordIsCorrect() throws SQLException{
        System.out.println("Test for if username is null but password is correct");
        User expected = new User(null, "andrewGamer123", "andrew@gmail.com", "passwordDone123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.login(expected.getUsername(), expected.getPassword());

        assertNotEquals(expected, result);

    }

    /**
     * Test for login if username and password isn't correct
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void loginIfUsernameAndPasswordAreBothIncorrect() throws SQLException{

        System.out.println("Test for login if username and password isn't correct");

        User expected = new User("Luffy", "andrewGamer123", "andrew@gmail.com", "luffy123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.login(expected.getUsername(), expected.getPassword());

        assertNotEquals(expected, result);
    }

    // test for findUserByUsername

    /**
     * test for find user by username
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findUserByUsername() throws SQLException {
        System.out.println("test for find user by username");

        User expected = new User("Andrew", "andrewGamer123", "andrew@gmail.com", "$2a$12$x4EwpUD5VU.vJW1.xICz1OnEJqEMfdYx/ttl/Gi/JxljZAsguzqbi", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.findUserByUsername(expected.getUsername());

        assertUserEquals(expected, result);
    }

    /**
     * Test to find user by username but username doesn't exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findUserByUsernameButThereIsNoMatch() throws SQLException {
        System.out.println("Test to find user by username but username doesnt exist");
        User expected = new User("jojo", "andrewGamer123", "andrew@gmail.com", "passwordDone123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.findUserByUsername(expected.getUsername());

        assertNull(result);
    }

    /**
     * Test to find user by username but username is null
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findUserByUsernameButUserIsNull() throws SQLException{
        System.out.println("Test to find user by username but username is null");

        User expected = new User(null, "andrewGamer123", "andrew@gmail.com", "passwordDone123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.findUserByUsername(expected.getUsername());

        assertNull(result);

    }

    // test find by email

    /**
     * Test to find user by there email
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findUserByThereEmail() throws SQLException {
        System.out.println("Test to find user by there email");
        User expected = new User("Andrew", "andrewGamer123", "andrew@gmail.com", "passwordDone123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.findUserByThereEmail(expected.getEmail());

        assertEquals(expected,result);
    }

    /**
     * Test to find user by email but email doesn't exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findUserByEmailButEmailDoesNotExist() throws SQLException{
        System.out.println("Test to find user by email but email doesnt exist");
        User expected = new User("Andrew", "andrewGamer123", "a456@gmail.com", "passwordDone123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.findUserByThereEmail(expected.getEmail());

        assertNotEquals(expected,result);
    }

    /**
     * Test to find user by email but email is null
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void findUserByEmailButEmailIsNull() throws SQLException{
        System.out.println("Test to find user by email but email is null");

        User expected = new User("Andrew", "andrewGamer123", null, "passwordDone123@", LocalDate.of(2000, 12, 10), false, LocalDateTime.of(2025,01,30,0,0,0), "DefaultUserImage.jpg");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        UserDao userDao = new UserDaoImpl(conn);

        User result = userDao.findUserByThereEmail(expected.getEmail());

        assertNotEquals(expected,result);
    }

    /**
     * Check if two users are the same
     * @param u1 is the user being searched
     * @param u2 is the user being searched
     */

    private void assertUserEquals(User u1, User u2){
        assertEquals(u1.getUsername(), u2.getUsername());
        assertEquals(u1.getDisplayName(), u2.getDisplayName());
        assertEquals(u1.getEmail(), u2.getEmail());
        assertEquals(u1.getPassword(), u2.getPassword());
        assertEquals(u1.getDateOfBirth(), u2.getDateOfBirth());
        assertEquals(u1.isAdmin(), u2.isAdmin());
        assertEquals(u1.getCreatedAt(), u2.getCreatedAt());
    }
}