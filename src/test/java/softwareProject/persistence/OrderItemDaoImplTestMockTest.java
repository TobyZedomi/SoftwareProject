package softwareProject.persistence;

import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import softwareProject.business.BillingAddress;
import softwareProject.business.Cart;
import softwareProject.business.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderItemDaoImplTestMockTest {

    public OrderItemDaoImplTestMockTest(){

    }

    /**
     * Mock test to add an order item
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addOrderItem() throws SQLException {

        System.out.println("Mock test to add an Order Item");

        OrderItem o1 = new OrderItem(1, 10,1,1);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into orderItem values(?, ?, ?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        OrderItemDao orderItemDao = new OrderItemDaoImpl(dbConn);
        int result = orderItemDao.addOrderItem(o1);

        assertEquals(expected, result);

    }

    /**
     * Mock test to get all order items
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllOrderItems() throws SQLException {

        System.out.println("Mock test to get all order items");

        OrderItem o1 = new OrderItem(1, 8.99, 1, 14);
        OrderItem o2 = new OrderItem(2, 10.99,1,15);
        OrderItem o3 = new OrderItem(3, 8.99, 2, 14);

        ArrayList<OrderItem> expectedResults = new ArrayList<>();
        expectedResults.add(o1);
        expectedResults.add(o2);
        expectedResults.add(o3);

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * from orderitem")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, true, true, false);

        // fill in results

        when(rs.getInt("order_items_id")).thenReturn(o1.getOrder_items_id(),o2.getOrder_items_id(), o3.getOrder_items_id());
        when(rs.getDouble("price")).thenReturn(o1.getPrice(), o2.getPrice(), o3.getPrice());
        when(rs.getInt("order_id")).thenReturn(o1.getOrder_id(), o2.getOrder_id(), o3.getOrder_id());


        int numOrderItemsTable = 3;

        OrderItemDao orderItemDao = new OrderItemDaoImpl(dbConn);
        ArrayList<OrderItem> result = orderItemDao.getAllOrderItems();


        assertEquals(numOrderItemsTable, result.size());

        assertEquals(expectedResults, result);

    }

    /**
     * Mock test to get order items by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getOrderItemById() throws SQLException {

        System.out.println("Mock testing to Get order item by id");

        OrderItem o1 = new OrderItem(1, 8.99, 1, 14);

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * FROM orderitem where order_items_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, false);

        // fill in results

        when(rs.getInt("order_items_id")).thenReturn(o1.getOrder_items_id());
        when(rs.getDouble("price")).thenReturn(o1.getPrice());
        when(rs.getInt("order_id")).thenReturn(o1.getOrder_id());


        OrderItemDao orderItemDao = new OrderItemDaoImpl(dbConn);
        OrderItem result = orderItemDao.getOrderItemById(o1.getOrder_items_id());


        assertEquals(o1,result);


    }
}