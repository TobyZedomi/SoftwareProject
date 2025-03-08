package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import softwareProject.business.BillingAddress;
import softwareProject.business.StreamingService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



class BillingAddressDaoImplMockTest {

    public BillingAddressDaoImplMockTest()
    {
    }

    /**
     * Adding a billing Address
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void addBillingAddress() throws SQLException {

        System.out.println("Adding Billing Address mock Test");

        BillingAddress adding = new BillingAddress(3, "Toby", "Toby A","toby@gmail.com","70 Vinland Road","Dublin", "Co.Louth", "A92 QW2Q" );

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into billing_address values(?, ?, ?, ?, ?, ?,?, " + "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        int result = billingAddressDao.addBillingAddress(adding);

        System.out.println(result);


        assertEquals(expected, result);
    }

    /**
     * Mock test to get all Billing Address
     * @throws SQLException if anything happens in the database
     */
    @Test
    void getAllBillingAddress() throws SQLException {

        System.out.println("Mock test to get all Billing Address ");
        /// expected Results

        BillingAddress b1 = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");
        BillingAddress b2 = new BillingAddress(2, "James", "James M", "james@gmail.com", "90 Wheaton Hall", "Dublin", "Co.Louth", "A92 QW2Q");

        ArrayList<BillingAddress> expectedResults = new ArrayList<>();
        expectedResults.add(b1);
        expectedResults.add(b2);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("Select * from billing_address")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultset, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getInt("billing_address_id")).thenReturn(b1.getBilling_address_id(), b2.getBilling_address_id());
        when(rs.getString("username")).thenReturn(b1.getUsername(), b2.getUsername());
        when(rs.getString("fullName")).thenReturn(b1.getFullName(), b2.getFullName());
        when(rs.getString("email")).thenReturn(b1.getEmail(), b2.getEmail());
        when(rs.getString("address")).thenReturn(b1.getAddress(), b2.getAddress());
        when(rs.getString("city")).thenReturn(b1.getCity(), b2.getCity());
        when(rs.getString("county")).thenReturn(b1.getCounty(), b2.getCounty());
        when(rs.getString("postcode")).thenReturn(b1.getPostcode(), b2.getPostcode());

        int numBillingAddressInTable = 2;

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        ArrayList<BillingAddress> result = billingAddressDao.getAllBillingAddress();
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numBillingAddressInTable, result.size());

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);

        /*
        System.out.println("Results:");
        for(BillingAddress b: result){
            System.out.println(b);
        }

         */

    }

    /**
     * Mock test to Get Billing Address By Id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getBillingAddressById() throws SQLException {


        System.out.println("Mock Test to get Billing Address by id");

        BillingAddress expected = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * FROM billing_address where billing_address_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 1 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getInt("billing_address_id")).thenReturn(expected.getBilling_address_id());
        when(rs.getString("username")).thenReturn(expected.getUsername());
        when(rs.getString("fullName")).thenReturn(expected.getFullName());
        when(rs.getString("email")).thenReturn(expected.getEmail());
        when(rs.getString("address")).thenReturn(expected.getAddress());
        when(rs.getString("city")).thenReturn(expected.getCity());
        when(rs.getString("county")).thenReturn(expected.getCounty());
        when(rs.getString("postcode")).thenReturn(expected.getPostcode());

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        BillingAddress user = billingAddressDao.getBillingAddressById(expected.getBilling_address_id());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(expected, user);

       // System.out.println(user);

    }

    /**
     * Mock testing to get Billing Address by username
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getBillingAddressByUsername() throws SQLException {

        System.out.println("Mock testing to get Billing Address by username");

        BillingAddress expected = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * FROM billing_address where username = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getInt("billing_address_id")).thenReturn(expected.getBilling_address_id());
        when(rs.getString("username")).thenReturn(expected.getUsername());
        when(rs.getString("fullName")).thenReturn(expected.getFullName());
        when(rs.getString("email")).thenReturn(expected.getEmail());
        when(rs.getString("address")).thenReturn(expected.getAddress());
        when(rs.getString("city")).thenReturn(expected.getCity());
        when(rs.getString("county")).thenReturn(expected.getCounty());
        when(rs.getString("postcode")).thenReturn(expected.getPostcode());

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        BillingAddress user = billingAddressDao.getBillingAddressByUsername(expected.getUsername());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(expected.getBilling_address_id(), user.getBilling_address_id());

       // System.out.println(user);

    }


    /**
     * Mock test to update billing address fullName by billing address id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void updateBillingAddressFullName() throws SQLException {


        System.out.println("Mock Test to update billing address fullName");

        BillingAddress update = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        String fullName = "Toby";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement( "UPDATE billing_address SET fullName = ? WHERE billing_address_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        // adding a new billingAddress
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        int result = billingAddressDao.updateBillingAddressFullName(fullName, update.getBilling_address_id());


        assertEquals(expectedResult, result);

    }



    /**
     * Mock test to update billing address email by billing address id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void updateBillingAddressEmail() throws SQLException {


        System.out.println("Mock Test to update billing address email");

        BillingAddress update = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        String email = "toby@gmail.com";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement( "UPDATE billing_address SET email = ? WHERE billing_address_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        // adding a new billingAddress
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        int result = billingAddressDao.updateBillingAddressEmail(email, update.getBilling_address_id());


        assertEquals(expectedResult, result);

    }



    /**
     * Mock test to update billing address address by billing address id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateAddressForBillingAddress() throws SQLException {


        System.out.println("Mock Test to update billing address address");

        BillingAddress update = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        String address = "house road";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement( "UPDATE billing_address SET address = ? WHERE billing_address_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        // adding a new billingAddress
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        int result = billingAddressDao.updateAddressForBillingAddress(address, update.getBilling_address_id());


        assertEquals(expectedResult, result);

    }


    /**
     * Mock test to update billing address email by billing address id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressCity() throws SQLException {


        System.out.println("Mock Test to update billing address city");

        BillingAddress update = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        String city = "London";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement( "UPDATE billing_address SET city = ? WHERE billing_address_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        // adding a new billingAddress
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        int result = billingAddressDao.updateBillingAddressCity(city, update.getBilling_address_id());


        assertEquals(expectedResult, result);

    }


    /**
     * Mock test to update billing address county by billing address id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressCounty() throws SQLException {


        System.out.println("Mock Test to update billing address county");

        BillingAddress update = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        String county = "Antrim";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement( "UPDATE billing_address SET county = ? WHERE billing_address_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        // adding a new billingAddress
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        int result = billingAddressDao.updateBillingAddressCounty(county, update.getBilling_address_id());


        assertEquals(expectedResult, result);

    }


    /**
     * Mock test to update billing address postcode by billing address id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void   updateBillingAddressPostCode() throws SQLException {


        System.out.println("Mock Test to update billing address postcode");

        BillingAddress update = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        String postCode = "A92 EWQQ";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement( "UPDATE billing_address SET postcode = ? WHERE billing_address_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        // adding a new billingAddress
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(dbConn);
        int result = billingAddressDao.updateBillingAddressPostCode(postCode, update.getBilling_address_id());


        assertEquals(expectedResult, result);

    }

}