package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.OrderItem;
import softwareProject.business.ShopOrder;
import softwareProject.business.StreamingService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ShopOrderDaoImplTest_IntegrationTest {


    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    /**
     * Test to add a new Shop Order
     * @throws SQLException if soemthing goes wrong in the database
     */
    @Test
    void addShopOrder() throws SQLException {

        System.out.println("Integration test to add a new ShopOrder");


        ShopOrder tester = new ShopOrder(3, "Kate", 2, LocalDateTime.of(2024,12,24, 00,00,00 ), 19.98, "COMPLETE");

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        int result = shopOrderDao.addShopOrder(tester);
        assertEquals(correctResult, result);

        ShopOrder inserted = shopOrderDao.getShopOrderById(tester.getOrder_id());
        assertNotNull(inserted);

        assertEquals(tester, inserted);
    }

    /**
     * Test to add a new Shop Order but order id already exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addShopOrderButOrderIdAlreadyExist() throws SQLException {

        System.out.println("Integration test to add a new ShopOrder but order id already exist");


        ShopOrder tester = new ShopOrder(2, "Kate", 2, LocalDateTime.of(2024,12,24, 00,00,00 ), 19.98, "COMPLETE");

        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        int result = shopOrderDao.addShopOrder(tester);
        assertEquals(correctResult, result);

    }

    /**
     * Test to add a new Shop Order but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addShopOrderButUsernameDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new ShopOrder but username doesnt exist");


        ShopOrder tester = new ShopOrder(3, "Ronaldo", 2, LocalDateTime.of(2024,12,24, 00,00,00 ), 19.98, "COMPLETE");

        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        int result = shopOrderDao.addShopOrder(tester);
        assertEquals(correctResult, result);

    }


    /**
     * Test to add a new Shop Order but billing_address id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addShopOrderButBillingAddressIdDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new ShopOrder but  billing address id doesnt exist");


        ShopOrder tester = new ShopOrder(3, "Kate", 12322, LocalDateTime.of(2024,12,24, 00,00,00 ), 19.98, "COMPLETE");

        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        int result = shopOrderDao.addShopOrder(tester);
        assertEquals(correctResult, result);

    }


    @Test
    void addShopOrderButItsNull() throws SQLException {

        System.out.println("Add Shop Order but its null");

        ShopOrder tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            shopOrderDao.addShopOrder(tester);
        });
    }

    /**
     * Get Order with the highest order id based on username
     * @throws SQLException if something is wrong in the database
     */
    @Test
    void getOrderWithTheHighestOrderIdByUsername() throws SQLException {

        System.out.println("Get Order with the highest order id based on username");

        ShopOrder tester = new ShopOrder(1, "Kate", 1, LocalDateTime.of(2025,01,30, 00,00,00 ), 19.98, "COMPLETE");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        ShopOrder result = shopOrderDao.getOrderWithTheHighestOrderIdByUsername("Kate");

        assertEquals(tester, result);

    }


    /**
     * Get Order with the highest order id based on username but username doesn't exist
     * @throws SQLException if something is wrong in the database
     */
    @Test
    void getOrderWithTheHighestOrderIdByUsernameButUsernameDoesntExist() throws SQLException {

        System.out.println("Get Order with the highest order id based on username but username doesnt exist");

        ShopOrder tester = new ShopOrder(1, "Kate", 1, LocalDateTime.of(2025,01,30, 00,00,00 ), 19.98, "COMPLETE");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        ShopOrder result = shopOrderDao.getOrderWithTheHighestOrderIdByUsername("Ronaldo");

        assertNotEquals(tester, result);

    }


    /**
     * Get all shop orders by username but username doesn't exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllShopOrdersByUsername() throws SQLException {

        System.out.println("Test to Get all shop orders by username");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        ArrayList<ShopOrder> expected = new ArrayList<>();
        expected.add(new ShopOrder(1, "Kate", 1, LocalDateTime.of(2025,01,30, 00,00,00 ), 19.98, "COMPLETE"));

        ArrayList<ShopOrder> result = shopOrderDao.getAllShopOrdersByUsername("Kate");

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertEquals(expected.get(i), result.get(i));
        }

    }

    /**
     * Get all shop orders by username but username doesn't exist but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllShopOrdersByUsernameButUsernameDoesntExist() throws SQLException {

        System.out.println("Test to Get all shop orders by username but username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        ArrayList<ShopOrder> expected = new ArrayList<>();
        expected.add(new ShopOrder(1, "Kate", 1, LocalDateTime.of(2025,01,30, 00,00,00 ), 19.98, "COMPLETE"));

        ArrayList<ShopOrder> result = shopOrderDao.getAllShopOrdersByUsername("RONALDOO");

        // test size

        assertNotEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertNotEquals(expected.get(i), result.get(i));
        }

    }

    /**
     * Test to get shop order by order id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getShopOrderById() throws SQLException {

        System.out.println("Integration test to get shop order by order id");

        ShopOrder tester = new ShopOrder(1, "Kate", 1, LocalDateTime.of(2025,01,30, 00,00,00 ), 19.98, "COMPLETE");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        ShopOrder result = shopOrderDao.getShopOrderById(tester.getOrder_id());

        assertEquals(tester, result);
    }



    /**
     * Test to get shop order by order id if order id doesn't exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getShopOrderByIdButOrderIdDoesntExist() throws SQLException {

        System.out.println("Integration test to get shop order by order id");

        ShopOrder tester = new ShopOrder(12311, "Kate", 1, LocalDateTime.of(2025,01,30, 00,00,00 ), 19.98, "COMPLETE");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        ShopOrderDao shopOrderDao = new ShopOrderDaoImpl(conn);

        ShopOrder result = shopOrderDao.getShopOrderById(tester.getOrder_id());

        assertNotEquals(tester, result);
    }
}