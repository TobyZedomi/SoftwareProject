package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.MovieProduct;
import softwareProject.business.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemDaoImplTest_IntegrationTest {


    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * test to add order item
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addOrderItem() throws SQLException {

        System.out.println("Integration test to add a new OrderItem");


        OrderItem tester = new OrderItem(5, 5.99, 2, 2);
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);

        int result = orderItemDao.addOrderItem(tester);
        assertEquals(correctResult, result);

        OrderItem inserted = orderItemDao.getOrderItemById(tester.getOrder_items_id());
        assertNotNull(inserted);

        assertEquals(tester, inserted);

    }


    /**
     * test to add order item but order item id already exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addOrderItemButOrderItemIdAlreadyExist() throws SQLException {

        System.out.println("Integration test to add a new OrderItem but order item id already exist");


        OrderItem tester = new OrderItem(1, 5.99, 2, 2);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);

        int result = orderItemDao.addOrderItem(tester);
        assertEquals(correctResult, result);

    }

    /**
     * test to add order item but order id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addOrderItemButOrderIdDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new OrderItem but order id doesnt exist");


        OrderItem tester = new OrderItem(5, 5.99, 1232, 2);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);

        int result = orderItemDao.addOrderItem(tester);
        assertEquals(correctResult, result);

    }

    /**
     * test to add order item but movie id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addOrderItemButMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new OrderItem but movie id doesnt exist");


        OrderItem tester = new OrderItem(5, 5.99, 2, 221);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);

        int result = orderItemDao.addOrderItem(tester);
        assertEquals(correctResult, result);

    }

    /**
     * Add order item but its null
     * @throws SQLException if soemthing goes wrong in the database
     */
    @Test
    void addOrderItemButItsNull() throws SQLException {

        System.out.println("Add Order item but its null");

        OrderItem tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            orderItemDao.addOrderItem(tester);
        });
    }


    @Test
    void getAllOrderItems() throws SQLException {

        System.out.println("Integration test to Get All Movie Products");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);


       OrderItem o1 = new OrderItem(1, 8.99, 1, 14);
       OrderItem o2 = new OrderItem(2, 10.99, 1, 15);
       OrderItem o3 = new OrderItem(3, 8.99, 2, 14);
       OrderItem o4 = new OrderItem(4, 10.99, 2, 15);

       ArrayList<OrderItem> expectedResults = new ArrayList<>();
       expectedResults.add(o1);
       expectedResults.add(o2);
       expectedResults.add(o3);
       expectedResults.add(o4);

        ArrayList<OrderItem> result = orderItemDao.getAllOrderItems();

        // test size

        assertEquals(expectedResults.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedResults.get(i), result.get(i));
        }
    }

    /**
     * Get order items by order items id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getOrderItemsById() throws SQLException {

        System.out.println("Get order items by order items id");


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);

        OrderItem o1 = new OrderItem(1, 8.99, 1, 14);


        OrderItem result = orderItemDao.getOrderItemById(o1.getOrder_items_id());

        assertEquals(o1, result);

    }


    /**
     * Get order items by order items id but order items id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getOrderItemsByIdButOrderItemsIdDoesntExist() throws SQLException {

        System.out.println("Get order items by order items id but order items id doesnt exist");


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(conn);

        OrderItem o1 = new OrderItem(1121, 8.99, 1, 14);


        OrderItem result = orderItemDao.getOrderItemById(o1.getOrder_items_id());

        assertNotEquals(o1, result);

    }


}