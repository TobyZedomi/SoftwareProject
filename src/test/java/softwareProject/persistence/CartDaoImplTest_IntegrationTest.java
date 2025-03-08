package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.Cart;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartDaoImplTest_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Integration Test to add a new Cart
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCart() throws SQLException {
        System.out.println("Integration test to add a new Cart");


        Cart tester = new Cart(6, "Toby");
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);

        int result = cartDao.addCart(tester);
        assertEquals(correctResult, result);


        Cart inserted = cartDao.getCartById(tester.getCart_id());
        assertNotNull(inserted);


        assertCartEquals(tester, inserted);


    }

    /**
     * Add Cart But Username Doesn't Exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartButUsernameDoesntExist() throws SQLException {
        System.out.println("Integration test to add a new Cart but Username doesnt Exist");


        Cart tester = new Cart(6, "Ronaldo");
        int incorrectResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);

        int result = cartDao.addCart(tester);
        assertEquals(incorrectResult, result);

    }


    /**
     * Add Cart But cart id already exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartButIdAlreadyAdded() throws SQLException {
        System.out.println("Integration test to add a new Cart but Cart Id already Exist");


        Cart tester = new Cart(2, "Andrew");
        int incorrectResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);

        int result = cartDao.addCart(tester);
        assertEquals(incorrectResult, result);

    }


    /**
     * Add Cart but its null
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartButItsNull() throws SQLException {

        System.out.println("Integration test to Add Cart but its null");

        Cart tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            cartDao.addCart(tester);
        });
    }

    /**
     * Test to get all Carts
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllCarts() throws SQLException {

        System.out.println("Integration Test to get all Carts");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);


      Cart c1 = new Cart(1, "admin");
      Cart c2 = new Cart(2, "Andrew");
      Cart c3 = new Cart(3, "Toby");
      Cart c4 = new Cart(4, "Kate");
      Cart c5 = new Cart(5, "James");


        ArrayList<Cart> expectedResults = new ArrayList<>();
        expectedResults.add(c1);
        expectedResults.add(c2);
        expectedResults.add(c3);
        expectedResults.add(c4);
        expectedResults.add(c5);

        ArrayList<Cart> result = cartDao.getAllCarts();

        // test size

        assertEquals(expectedResults.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedResults.get(i), result.get(i));
        }

    }

    /**
     * Integration test to get Cart by username
     * @throws SQLException if something goes wrong in the database
     */

    @Test
    void getCartByUsername() throws SQLException {

        System.out.println("Integration Test to get Cart By Username");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);

        Cart c1 = new Cart(1, "admin");

        Cart result = cartDao.getCartByUsername(c1.getUsername());

        //check if the billingAddresses are the same
        assertCartEquals(c1, result);

    }

    /**
     * Integration test to get Cart by username but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getCartByUsernameButUsernameDoesntExist() throws SQLException {

        System.out.println("Integration Test to get Cart By Username but username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);

        Cart c1 = new Cart(1, "Ronaldo");

        Cart result = cartDao.getCartByUsername(c1.getUsername());

        //check if the billingAddresses are the same
        assertNotEquals(c1, result);
    }


    /**
     * Test to get Cart by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getCartById() throws SQLException {

        System.out.println("Integration Test to get Cart By Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);

        Cart c1 = new Cart(1, "admin");

        Cart result = cartDao.getCartById(c1.getCart_id());

        //check if the billingAddresses are the same
        assertCartEquals(c1, result);
    }

    /**
     * Test to get Cart By Id but Id doesn't exist
     * @throws SQLException if soemthing goes wrong in the database
     */
    @Test
    void getCartByIdButIdDoesntExist() throws SQLException {

        System.out.println("Integration Test to get Cart By Id but id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartDao cartDao = new CartDaoImpl(conn);

        Cart c1 = new Cart(112, "admin");

        Cart result = cartDao.getCartById(c1.getCart_id());

        //check if the billingAddresses are the same
        assertNotEquals(c1, result);
    }

    /**
     * Check if two Carts are the same
     *
     * @param c1 is the user being searched
     * @param c2 is the user being searched
     */

    private void assertCartEquals(Cart c1, Cart c2) {
        assertEquals(c1.getCart_id(), c2.getCart_id());
        assertEquals(c1.getUsername(), c2.getUsername());
    }

}