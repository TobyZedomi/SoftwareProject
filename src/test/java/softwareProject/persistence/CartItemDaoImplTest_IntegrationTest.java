package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.Cart;
import softwareProject.business.CartItem;
import softwareProject.business.StreamingService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartItemDaoImplTest_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    /**
     * Test to Add a new Cart Item
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartItem() throws SQLException {

        System.out.println("Integration test to add a new Cart Item");

        CartItem tester = new CartItem(4, 12);
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.addCartItem(tester);
        assertEquals(correctResult, result);

        CartItem inserted = cartItemDao.getCartItemByIdAndMovieId(tester.getCart_id(), tester.getMovie_id());
        assertNotNull(inserted);

        assertEquals(tester, inserted);

    }


    /**
     * Test to add Cart Item but Cart Id doesn't exist
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartItemButCartIdDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new Cart Item but cart id doesnt exist");

        CartItem tester = new CartItem(44, 12);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.addCartItem(tester);
        assertEquals(correctResult, result);

    }

    /**
     * Test to add Cart Item but Movie Id doesn't exist
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartItemButMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new Cart Item but cart id doesnt exist");

        CartItem tester = new CartItem(4, 121);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.addCartItem(tester);
        assertEquals(correctResult, result);

    }

    /**
     * Test to add Cart Item but Cart Id and Movie Id doesn't exist
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartItemButCartIdAndMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new Cart Item but cart id and movie id doesnt exist");

        CartItem tester = new CartItem(432, 121);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.addCartItem(tester);
        assertEquals(correctResult, result);

    }

    /**
     * Integration test but adding CarItem is null
     *
     * @throws SQLException if something goes wrong with database
     */
    @Test
    void addCartItemButItsNull() throws SQLException {

        System.out.println("Add Cart Item but its null");

        CartItem tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            cartItemDao.addCartItem(tester);
        });
    }

    /**
     * Test to get all cart items by cart id
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllCartItemsByCartId() throws SQLException {

        System.out.println("Test to Get cart items by cart id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        ArrayList<CartItem> expected = new ArrayList<>();
        expected.add(new CartItem(1, 1));
        expected.add(new CartItem(1, 2));

        ArrayList<CartItem> result = cartItemDao.getAllCartItemsByCartId(1);

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

    }


    /**
     * Test to get all cart items by cart id but cart id doesnt exist
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllCartItemsByCartIdButCartIdDoesntExist() throws SQLException {

        System.out.println("Test to Get cart items by cart id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        ArrayList<CartItem> expected = new ArrayList<>();
        expected.add(new CartItem(1, 1));
        expected.add(new CartItem(1, 2));

        ArrayList<CartItem> result = cartItemDao.getAllCartItemsByCartId(67);

        // test size

        assertNotEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertNotEquals(expected.get(i), result.get(i));
        }

    }

    /**
     * Test to delete cart by cart id and movie id
     *
     * @throws SQLException if something is wrong in the database
     */
    @Test
    void deleteCartItemByCartIdAndMovieId() throws SQLException {

        System.out.println("Integration test to delete cartItem by cart id and movie id ");

        CartItem tester = new CartItem(1, 1);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.deleteCartItemByCartIdAndMovieId(tester.getCart_id(), tester.getMovie_id());
        assertEquals(1, result);

        // try find the cart by its cart id and movie id to see it no longer exist
        CartItem cartItem = cartItemDao.getCartItemByIdAndMovieId(tester.getCart_id(), tester.getMovie_id());

        assertNotEquals(tester, cartItem);

    }


    /**
     * Test to delete cart by cart id and movie id but cart id doesn't exist
     *
     * @throws SQLException if something is wrong in the database
     */
    @Test
    void deleteCartItemByCartIdAndMovieIdButCartIdDoesntExist() throws SQLException {

        System.out.println("Integration test to delete cartItem by cart id and movie id but cartId doesnt exist ");

        CartItem tester = new CartItem(121, 1);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.deleteCartItemByCartIdAndMovieId(tester.getCart_id(), tester.getMovie_id());
        assertEquals(0, result);


    }


    /**
     * Test to delete cart by cart id and movie id but movie id doesn't exist
     *
     * @throws SQLException if something is wrong in the database
     */
    @Test
    void deleteCartItemByCartIdAndMovieIdButMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration test to delete cartItem by cart id and movie id but movieId doesnt exist ");

        CartItem tester = new CartItem(1, 111);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.deleteCartItemByCartIdAndMovieId(tester.getCart_id(), tester.getMovie_id());
        assertEquals(0, result);


    }


    /**
     * Test to delete cart by cart id and movie id but cart id and  movie id doesn't exist
     *
     * @throws SQLException if something is wrong in the database
     */
    @Test
    void deleteCartItemByCartIdAndMovieIdButCartIdAndMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration test to delete cartItem by cart id and movie id but cartId and movie id doesnt exist ");

        CartItem tester = new CartItem(111, 111);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.deleteCartItemByCartIdAndMovieId(tester.getCart_id(), tester.getMovie_id());
        assertEquals(0, result);


    }


    /**
     * Test to get total amount of CartItems by cart Id
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void totalNumberOfCartItemsByCartId() throws SQLException {

        System.out.println("Integration test to get total number of cart items");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int expectedResult = 2;

        int result = cartItemDao.totalNumberOfCartItems(1);

        assertEquals(expectedResult, result);

    }

    /**
     * Test to get total amount of CartItems by cart Id but cart id doesnt exist
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void totalNumberOfCartItemsByCartIdButCartIdDoesntExist() throws SQLException {

        System.out.println("Integration test to get total number of cart items");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int expectedResult = 2;

        int result = cartItemDao.totalNumberOfCartItems(1212);

        assertNotEquals(expectedResult, result);

    }


    /**
     * Test to delete Cart Item by cart Id
     *
     * @throws SQLException
     */
    @Test
    void deleteCartItemByCartId() throws SQLException {

        System.out.println("Integration test to delete cartItem by cart id ");

        CartItem tester = new CartItem(1, 1);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.deleteCartItemByCartId(tester.getCart_id());
        assertEquals(2, result);

        System.out.println(result);


        // try find the cart by its cart id and movie id to see it no longer exist
        CartItem cartItem = cartItemDao.getCartItemByIdAndMovieId(tester.getCart_id(), tester.getMovie_id());

        assertNotEquals(tester, cartItem);


    }

    /**
     * test to delete cart item by cart id but cart id doesnt exist
     *
     * @throws SQLException
     */
    @Test
    void deleteCartItemByCartIdButCartIdDoesntExist() throws SQLException {

        System.out.println("Integration test to delete cartItem by cart id but cart id doesnt ");

        CartItem tester = new CartItem(112, 1);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        int result = cartItemDao.deleteCartItemByCartId(tester.getCart_id());
        assertEquals(0, result);

    }

    /**
     * Test to get cart item by cart id and movie id
     * @throws SQLException
     */
    @Test
    void getCartItemByIdAndMovieId() throws SQLException {

        System.out.println("Integration Test to get CartItem by cartId and Movie id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        CartItem c1 = new CartItem(1, 1);

        CartItem result = cartItemDao.getCartItemByIdAndMovieId(c1.getCart_id(), c1.getMovie_id());

        //check if the billingAddresses are the same
        assertEquals(c1, result);

    }


    /**
     * Test to get cart item by cart id and movie id but cart id doesnt exist
     * @throws SQLException if something is wrong with the database
     */
    @Test
    void getCartItemByIdAndMovieIdButCartIdDoesntExist() throws SQLException {

        System.out.println("Integration Test to get CartItem by cartId and Movie id but cart id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        CartItem c1 = new CartItem(112, 1);

        CartItem result = cartItemDao.getCartItemByIdAndMovieId(c1.getCart_id(), c1.getMovie_id());

        //check if the billingAddresses are the same
        assertNotEquals(c1, result);

    }


    /**
     * Test to get cart item by cart id and movie id but movie id doesn't exist
     * @throws SQLException if something is wrong with the database
     */
    @Test
    void getCartItemByIdAndMovieIdButMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration Test to get CartItem by cartId and Movie id but movie id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        CartItem c1 = new CartItem(1, 1121);

        CartItem result = cartItemDao.getCartItemByIdAndMovieId(c1.getCart_id(), c1.getMovie_id());

        //check if the billingAddresses are the same
        assertNotEquals(c1, result);

    }

    /**
     * Test to get cart item by cart id and movie id but cart id and movie id doesn't exist
     * @throws SQLException if something is wrong with the database
     */
    @Test
    void getCartItemByIdAndMovieIdButCartIdAndMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration Test to get CartItem by cartId and Movie id but cartId and movie id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        CartItemDao cartItemDao = new CartItemDaoImpl(conn);

        CartItem c1 = new CartItem(1121, 1121);

        CartItem result = cartItemDao.getCartItemByIdAndMovieId(c1.getCart_id(), c1.getMovie_id());

        //check if the billingAddresses are the same
        assertNotEquals(c1, result);

    }
}