package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.Subscription;
import softwareProject.business.SubscriptionPlan;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionPlanDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Test to get Subscription Plan by Id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getSubscriptionPlanById() throws SQLException {


        System.out.println("Test to get SubscriptionPlan from Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionPlanDao subscriptionPlanDao = new SubscriptionPlanDaoImpl(conn);

        SubscriptionPlan expected = new SubscriptionPlan( 1, "Standard Plan", 9.99);

        SubscriptionPlan result = subscriptionPlanDao.getSubscriptionPlanById(expected.getSubscription_plan_id());

        assertEquals(expected, result);

    }


    /**
     * Test to get Subscription Plan by Id but Id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getSubscriptionPlanByIdButIdDoesntExist() throws SQLException {


        System.out.println("Test to get SubscriptionPlan from Id butId doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        SubscriptionPlanDao subscriptionPlanDao = new SubscriptionPlanDaoImpl(conn);

        SubscriptionPlan expected = new SubscriptionPlan( 1123, "Standard Plan", 9.99);

        SubscriptionPlan result = subscriptionPlanDao.getSubscriptionPlanById(expected.getSubscription_plan_id());

        assertNotEquals(expected, result);

    }
}