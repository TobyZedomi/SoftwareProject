package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartItemDaoImplTestMockTest {


    public CartItemDaoImplTestMockTest(){

    }

    /**
     * test to add Cart Item
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCartItem() throws SQLException {

        System.out.println("Mock test to add a CartItem");

        CartItem c1 = new CartItem(2, 1);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("CALL addIntoCartItem(?, ?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;


        // adding a new billingAddress
        CartItemDao cartItemDao = new CartItemDaoImpl(dbConn);
        int result = cartItemDao.addCartItem(c1);

        assertEquals(expected, result);

    }


    /**
     * Test to get all cart items by cart Id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllCartItemsByCartId() throws SQLException {

        System.out.println("Mock test to get All CartItems by cartId");

        CartItem c1 = new CartItem(1, 1);
        CartItem c2 = new CartItem(1, 2);
        CartItem c3 = new CartItem(1, 3);


        ArrayList<CartItem> expectedResults = new ArrayList<>();
        expectedResults.add(c1);
        expectedResults.add(c2);
        expectedResults.add(c3);


        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * FROM cart_items where cart_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, true, true, false);
        // Fill in the resultset
        when(rs.getInt("cart_id")).thenReturn(c1.getCart_id(), c2.getCart_id(), c3.getCart_id());
        when(rs.getInt("movie_id")).thenReturn(c1.getMovie_id(), c2.getMovie_id(), c3.getMovie_id());


        int numCartItemsInTable = 3;


        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        CartItemDao cartItemDao = new CartItemDaoImpl(dbConn);
        ArrayList<CartItem> result = cartItemDao.getAllCartItemsByCartId(c1.getCart_id());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numCartItemsInTable, result.size());

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);

        System.out.println("Results:");
        for(CartItem c: result){
            System.out.println(c);
        }

    }

    /**
     * test to delete cartItem by cartId
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void deleteCartItemByCartId() throws SQLException {

        System.out.println("Mock test to delete cart item by cart id");

        CartItem c1 = new CartItem(1, 1);


        // Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("DELETE from cart_items where cart_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);


        int expected = -1;

        // adding a new billingAddress
        CartItemDao cartItemDao = new CartItemDaoImpl(dbConn);
        int result = cartItemDao.deleteCartItemByCartId(c1.getCart_id());


        assertNotEquals(expected, result);

    }

    /**
     * Test to get total number of cart items based on cart id
     * @throws SQLException if something is wrong with the database
     */

/*
    @Test
    void totalNumberOfCartItems() throws SQLException {

        System.out.println("Mock test to get total number of cart items based on cartId");



        System.out.println("Mock test to get All CartItems by cartId");

        CartItem c1 = new CartItem(1, 1);
        CartItem c2 = new CartItem(1, 2);
        CartItem c3 = new CartItem(1, 3);


        ArrayList<CartItem> expectedResults = new ArrayList<>();
        expectedResults.add(c1);
        expectedResults.add(c2);
        expectedResults.add(c3);


        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT count(*) from cart_items WHERE cart_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        // fill in data

        when(ps.executeUpdate()).thenReturn(3);

        /*

        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, true, true, false);
        // Fill in the resultset
        when(rs.getInt("cart_id")).thenReturn(c1.getCart_id(), c2.getCart_id(), c3.getCart_id());
        when(rs.getInt("movie_id")).thenReturn(c1.getMovie_id(), c2.getMovie_id(), c3.getMovie_id());

         */

/*
        int numCartItemsInTable = 3;



        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        CartItemDao cartItemDao = new CartItemDaoImpl(dbConn);
        int result = cartItemDao.totalNumberOfCartItems(c1.getCart_id());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numCartItemsInTable, result);

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);



    }

 */



    /**
     * Mock test to delete cart item by CartId and Movie Id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void testDeleteCartItemByCartIdAndMovieId() throws SQLException {

        System.out.println("Mock test to delete cart item by cart id and movie id");

        CartItem c1 = new CartItem(1, 1);

        // Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("DELETE from cart_items where cart_id = ? and movie_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        CartItemDao cartItemDao = new CartItemDaoImpl(dbConn);
        int result = cartItemDao.deleteCartItemByCartIdAndMovieId(c1.getCart_id(), c1.getMovie_id());

        System.out.println(result);

        assertEquals(expected, result);
    }

    /**
     * Mock test to get cart by cart id and movie id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getCartItemByIdAndMovieId() throws SQLException {

        System.out.println("Mock test to get cart item by cart id and movie id");

        CartItem c1 = new CartItem(1, 1);

        // Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("SELECT * FROM cart_items where cart_id = ? AND movie_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 1 results in the resultset, so need true to be returned 1 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultset
        when(rs.getInt("cart_id")).thenReturn(c1.getCart_id());
        when(rs.getInt("movie_id")).thenReturn(c1.getMovie_id());



        // adding a new billingAddress
        CartItemDao cartItemDao = new CartItemDaoImpl(dbConn);
        CartItem result = cartItemDao.getCartItemByIdAndMovieId(c1.getCart_id(), c1.getMovie_id());

        System.out.println(result);

        assertEquals(c1, result);

    }
}