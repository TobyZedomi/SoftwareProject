package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.CartItem;
import softwareProject.business.ShopOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShopOrderDaoImplTestMockTest {


    public ShopOrderDaoImplTestMockTest(){

    }
    @Test
    void addShopOrder() throws SQLException {

        System.out.println("Mock test to add a Shop Order");

        ShopOrder s1 = new ShopOrder(1, "Toby", 1, LocalDateTime.of(2024,12,24, 00,00,00), 19.98, "COMPLETE");


        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into shop_order values(?, ?, ?, ?, ?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(dbConn) {
        };
        int result = shopOrderDao.addShopOrder(s1);

        assertEquals(expected, result);
    }

    @Test
    void getOrderWithTheHighestOrderIdByUsername() throws SQLException {

        ShopOrder s1 = new ShopOrder(1, "Toby", 1, LocalDateTime.of(2024,12,24, 00,00,00), 19.98, "COMPLETE");
        ShopOrder s2 = new ShopOrder(2, "Toby", 1, LocalDateTime.of(2024,12,25, 00,00,00), 19.98, "COMPLETE");
        ShopOrder s3 = new ShopOrder(3, "Toby", 1, LocalDateTime.of(2024,12,29, 00,00,00), 19.98, "COMPLETE");


        ArrayList<ShopOrder> expectedResults = new ArrayList<>();
        expectedResults.add(s1);
        expectedResults.add(s2);
        expectedResults.add(s3);

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * from shop_order where username = ? GROUP BY order_id ORDER BY order_id DESC LIMIT 1")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultset
        when(rs.getInt("order_id")).thenReturn(s1.getOrder_id(), s2.getOrder_id(), s3.getOrder_id());
        when(rs.getString("username")).thenReturn(s1.getUsername(), s2.getUsername(), s3.getUsername());
        when(rs.getInt("billing_address_id")).thenReturn(s1.getBilling_address_id(), s2.getBilling_address_id(), s3.getBilling_address_id());
        when(rs.getTimestamp("order_date").toLocalDateTime()).thenReturn(s1.getOrder_date(), s2.getOrder_date(), s3.getOrder_date());
        when(rs.getString("orderStatus")).thenReturn(s1.getOrder_status(), s2.getOrder_status(), s3.getOrder_status());

        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(dbConn);
        ShopOrder result = shopOrderDao.getOrderWithTheHighestOrderIdByUsername(s1.getUsername());


        assertEquals(s3, result);



    }

    @Test
    void getAllShopOrdersByUsername() throws SQLException {


        ShopOrder s1 = new ShopOrder(1, "Toby", 1, LocalDateTime.of(2024,12,24, 00,00,00), 19.98, "COMPLETE");
        ShopOrder s2 = new ShopOrder(2, "Toby", 1, LocalDateTime.of(2024,12,25, 00,00,00), 19.98, "COMPLETE");
        ShopOrder s3 = new ShopOrder(3, "Toby", 1, LocalDateTime.of(2024,12,29, 00,00,00), 19.98, "COMPLETE");


        ArrayList<ShopOrder> expectedResults = new ArrayList<>();
        expectedResults.add(s1);
        expectedResults.add(s2);
        expectedResults.add(s3);

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * from shop_order WHERE username = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultset
        when(rs.getInt("order_id")).thenReturn(s1.getOrder_id(), s2.getOrder_id(), s3.getOrder_id());
        when(rs.getString("username")).thenReturn(s1.getUsername(), s2.getUsername(), s3.getUsername());
        when(rs.getInt("billing_address_id")).thenReturn(s1.getBilling_address_id(), s2.getBilling_address_id(), s3.getBilling_address_id());
        when(rs.getTimestamp("order_date").toLocalDateTime()).thenReturn(s1.getOrder_date(), s2.getOrder_date(), s3.getOrder_date());
        when(rs.getString("orderStatus")).thenReturn(s1.getOrder_status(), s2.getOrder_status(), s3.getOrder_status());

        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(dbConn);
        ArrayList<ShopOrder> result = shopOrderDao.getAllShopOrdersByUsername(s1.getUsername());


        assertEquals(expectedResults, result);


    }

    @Test
    void getShopOrderById() throws SQLException {


        ShopOrder s1 = new ShopOrder(1, "Toby", 1, LocalDateTime.of(2024,12,24, 00,00,00), 19.98, "COMPLETE");



        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * FROM shop_order where order_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultset
        when(rs.getInt("order_id")).thenReturn(s1.getOrder_id());
        when(rs.getString("username")).thenReturn(s1.getUsername());
        when(rs.getInt("billing_address_id")).thenReturn(s1.getBilling_address_id());
        when(rs.getTimestamp("order_date").toLocalDateTime()).thenReturn(s1.getOrder_date());
        when(rs.getString("orderStatus")).thenReturn(s1.getOrder_status());

        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(dbConn);
        ShopOrder result = shopOrderDao.getShopOrderById(s1.getOrder_id());


        assertEquals(s1, result);

    }
}