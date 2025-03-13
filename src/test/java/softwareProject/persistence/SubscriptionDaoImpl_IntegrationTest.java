package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.MovieProduct;
import softwareProject.business.Subscription;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Integration to add a new subscription
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addSubscription() throws SQLException {

        System.out.println("Integration test to add a new Subscription");


        Subscription tester = new Subscription( "admin", 1,  LocalDateTime.of(2022, 2,21, 0, 0, 0), LocalDateTime.of(2023, 2,21, 0, 0, 0));

        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(conn);

        int result = subscriptionDao.addSubscription(tester);
        assertEquals(correctResult, result);

        Subscription inserted = subscriptionDao.getSubscriptionFromUsername(tester.getUsername());
        assertNotNull(inserted);

        assertEquals(tester, inserted);
    }


    /**
     * Integration to add a new subscription but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addSubscriptionButUsernameDoesntExist() throws SQLException {

        System.out.println("Integration test to add a new Subscription but username doesnt exist");

        Subscription tester = new Subscription( "messi", 1,  LocalDateTime.of(2022, 2,21, 0, 0, 0), LocalDateTime.of(2023, 2,21, 0, 0, 0));

        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(conn);

        int result = subscriptionDao.addSubscription(tester);
        assertEquals(correctResult, result);

    }


    /**
     * Integration test but adding Subscription is null
     * @throws SQLException if something goes wrong with database
     */
    @Test
    void addSubscriptionButItsNull() throws SQLException {

        System.out.println("Add Subscription but its null");

        Subscription tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            subscriptionDao.addSubscription(tester);
        });
    }

    /**
     * Test to get subscription from username
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getSubscriptionFromUsername() throws SQLException {

        System.out.println("Test to get Subscription from username");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(conn);

        Subscription s1 = new Subscription("Toby", 1,  LocalDateTime.of(2025, 01,30, 0, 0, 0), LocalDateTime.of(2026, 01,30, 0, 0, 0));

        Subscription result = subscriptionDao.getSubscriptionFromUsername(s1.getUsername());

        assertEquals(s1, result);
    }


    /**
     * Test to get subscription from username but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getSubscriptionFromUsernameButUsernameDoesntExist() throws SQLException {

        System.out.println("Test to get Subscription from username but username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(conn);

        Subscription s1 = new Subscription("HarryRed", 1,  LocalDateTime.of(2025, 01,20, 0, 0, 0), LocalDateTime.of(2026, 01,30, 0, 0, 0));

        Subscription result = subscriptionDao.getSubscriptionFromUsername(s1.getUsername());

        assertNotEquals(s1, result);
    }


    /**
     * Test to get all subscriptions
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllSubscriptions() throws SQLException {

        System.out.println("Test to get all Subscriptions");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(conn);

        Subscription s1 = new Subscription("Toby", 1,  LocalDateTime.of(2025, 01,30, 0, 0, 0), LocalDateTime.of(2026, 01,30, 0, 0, 0));
        Subscription s2 = new Subscription("Andrew", 2,  LocalDateTime.of(2025, 11,30, 0, 0, 0), LocalDateTime.of(2026, 11,30, 0, 0, 0));
        Subscription s3 = new Subscription("James", 3,  LocalDateTime.of(2025, 12,12, 0, 0, 0), LocalDateTime.of(2026, 12,12, 0, 0, 0));



        ArrayList<Subscription> expectedResults = new ArrayList<>();
        expectedResults.add(s1);
        expectedResults.add(s2);
        expectedResults.add(s3);

        ArrayList<Subscription> result = subscriptionDao.getAllSubscriptions();

        // test size

        assertEquals(expectedResults.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedResults.get(i), result.get(i));
        }

    }
}