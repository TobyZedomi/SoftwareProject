package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.MovieProduct;
import softwareProject.business.Subscription;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionDaoImplMockTest {

    public SubscriptionDaoImplMockTest(){

    }

    @Test
    void addSubscription() throws SQLException {



        System.out.println("Adding Subscription mock Test");

        Subscription adding = new Subscription( "Toby", 1,  LocalDateTime.of(2022, 2,21, 0, 0, 0), LocalDateTime.of(2023, 2,21, 0, 0, 0));

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("INSERT INTO subscription(username, subscription_plan_id, subscription_startDate, subscription_endDate) VALUES ( ?, ?, ?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(dbConn);
        int result = subscriptionDao.addSubscription(adding);


        assertEquals(expected, result);
    }

    @Test
    void getSubscriptionFromUsername() throws SQLException {

        System.out.println("Mock testing to get Billing Address by username");

        Subscription expected = new Subscription( "Toby", 1,  LocalDateTime.of(2022, 2,21, 0, 0, 0), LocalDateTime.of(2023, 2,21, 0, 0, 0));

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * from subscription WHERE username = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getString("username")).thenReturn(expected.getUsername());
        when(rs.getInt("subscription_plan_id")).thenReturn(expected.getSubscription_plan_id());
        when(rs.getTimestamp("subscription_startDate")).thenReturn(Timestamp.valueOf(expected.getSubscription_startDate()));
        when(rs.getTimestamp("subscription_endDate")).thenReturn(Timestamp.valueOf(expected.getSubscription_endDate()));


        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(dbConn);
        Subscription subscription = subscriptionDao.getSubscriptionFromUsername(expected.getUsername());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(expected, subscription);


    }

    /**
     * Mock test to get all subscriptions
     * @throws SQLException when there's something wrong in the database
     */
    @Test
    void getAllSubscriptions() throws SQLException {


        System.out.println("Mock test to get all Subscriptions ");
        /// expected Results

        Subscription s1 = new Subscription( "Toby", 1,  LocalDateTime.of(2022, 2,21, 0, 0, 0), LocalDateTime.of(2023, 2,21, 0, 0, 0));
        Subscription s2 = new Subscription( "Toby", 2,  LocalDateTime.of(2022, 3,21, 0, 0, 0), LocalDateTime.of(2023, 3,21, 0, 0, 0));

        ArrayList<Subscription> expectedResults = new ArrayList<>();
        expectedResults.add(s1);
        expectedResults.add(s2);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("SELECT * from subscription")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultset, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getString("username")).thenReturn(s1.getUsername(), s2.getUsername());
        when(rs.getInt("subscription_plan_id")).thenReturn(s1.getSubscription_plan_id(), s2.getSubscription_plan_id());
        when(rs.getTimestamp("subscription_startDate")).thenReturn(Timestamp.valueOf(s1.getSubscription_startDate()), Timestamp.valueOf(s2.getSubscription_startDate()));
        when(rs.getTimestamp("subscription_endDate")).thenReturn(Timestamp.valueOf(s1.getSubscription_endDate()), Timestamp.valueOf(s2.getSubscription_endDate()));

        int numSubscriptionTable = 2;


        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl(dbConn);
        ArrayList<Subscription> result = subscriptionDao.getAllSubscriptions();

        assertEquals(numSubscriptionTable, result.size());


        assertEquals(expectedResults, result);


    }
}