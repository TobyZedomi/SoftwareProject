package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartDaoImplTestMockTest {


    public CartDaoImplTestMockTest(){

    }

    /**
     * Mock testing to add a cart
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addCart() throws SQLException {

        System.out.println("Mock test to add a Cart");

        Cart c1 = new Cart(6, "Kelly");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into carts values(?, " +
                "?)")).thenReturn(ps);

        int expected = 1;

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        // adding a new billingAddress
       CartDao cartDao = new CartDaoImpl(dbConn);
        int result = cartDao.addCart(c1);

        assertEquals(expected, result);


    }

    /**
     * Mock testing to get all Carts
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllCarts() throws SQLException {

        System.out.println("Mock testing to get all carts");

        Cart c1 = new Cart(1, "admin");
        Cart c2 = new Cart(2, "Andrew");
        Cart c3 = new Cart(3, "Toby");
        ArrayList<Cart> expectedResults = new ArrayList<>();
        expectedResults.add(c1);
        expectedResults.add(c2);
        expectedResults.add(c3);

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriately dummy data
        when(dbConn.prepareStatement("Select * from carts")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, true, true, false);


        when(rs.getInt("cart_id")).thenReturn(c1.getCart_id(), c2.getCart_id(), c3.getCart_id());
        when(rs.getString("username")).thenReturn(c1.getUsername(), c2.getUsername(), c3.getUsername());


        int numCartTable = 3;

        CartDao cartDao = new CartDaoImpl(dbConn);
        ArrayList<Cart> result = cartDao.getAllCarts();

        // check that the size is the same
        assertEquals(numCartTable, result.size());

        // check elements are the same
        assertEquals(expectedResults, result);

        /*
        System.out.println("Results:");
        for(Cart c: result){
            System.out.println(c);
        }
         */
    }


    /**
     * Mock testing to get cart by username
     * @throws SQLException if soemthing goes wrong in the datbase
     */
    @Test
    void getCartByUsername() throws SQLException {

        System.out.println("Mock testing to get Cart by Username");

        Cart c1 = new Cart(1, "admin");

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(dbConn.prepareStatement("SELECT * FROM carts where username = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 1 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getInt("cart_id")).thenReturn(c1.getCart_id());
        when(rs.getString("username")).thenReturn(c1.getUsername());


        CartDao cartDao = new CartDaoImpl(dbConn);
        Cart cart = cartDao.getCartByUsername(c1.getUsername());

        assertEquals(c1, cart);

    }


    /**
     * Mock testing to get Cart By Id and Username
     * @throws SQLException if something goes wrong in the database
     */


    @Test
    void getCartById() throws SQLException {

        System.out.println("Mock testing to get Cart by Id");

        Cart c1 = new Cart(1, "admin");

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(dbConn.prepareStatement("SELECT * FROM carts where cart_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 1 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getInt("cart_id")).thenReturn(c1.getCart_id());
        when(rs.getString("username")).thenReturn(c1.getUsername());

        CartDao cartDao = new CartDaoImpl(dbConn);
        Cart cart = cartDao.getCartById(c1.getCart_id());

        assertEquals(c1, cart);

    }


}