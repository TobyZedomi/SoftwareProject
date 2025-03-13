package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.Subscription;
import softwareProject.business.SubscriptionPlan;

import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionPlanDaoImplMockTest {


    /**
     * Test to get Subscription plan by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getSubscriptionPlanById() throws SQLException {

        System.out.println("Mock testing to get Billing Address by username");

        SubscriptionPlan expected = new SubscriptionPlan( 1, "Standard Plan", 9.99);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * FROM subscriptionplan where subscription_plan_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getInt("subscription_plan_id")).thenReturn(expected.getSubscription_plan_id());
        when(rs.getString("subscription_plan_name")).thenReturn(expected.getSubscription_plan_name());
        when(rs.getDouble("cost")).thenReturn(expected.getCost());


        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        SubscriptionPlanDao subscriptionPlanDao = new SubscriptionPlanDaoImpl(dbConn);
        SubscriptionPlan subscriptionPlan = subscriptionPlanDao.getSubscriptionPlanById(expected.getSubscription_plan_id());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(expected, subscriptionPlan);
    }
}