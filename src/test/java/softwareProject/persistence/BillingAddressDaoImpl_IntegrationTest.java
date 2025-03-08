package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.StreamingService;
import softwareProject.business.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BillingAddressDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Integration test to add a new BillingAddress
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addBillingAddress() throws SQLException {

        System.out.println("Integration test to add a new BillingAddress");


        BillingAddress tester = new BillingAddress(3, "Toby", "Toby A", "toby@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int result = billingAddressDao.addBillingAddress(tester);
        assertEquals(correctResult, result);

        BillingAddress inserted = billingAddressDao.getBillingAddressById(tester.getBilling_address_id());
        assertNotNull(inserted);

        assertBillingAddressEquals(tester, inserted);

    }

    /**
     * Integration test to add a new BillingAddress but Billing Address id already exist
     *
     * @throws SQLException something wrong in the database
     */
    @Test
    void addBillingAddressButBillingIdExist() throws SQLException {

        System.out.println("Integration test to add a new BillingAddress but billing address id already exist");


        BillingAddress tester = new BillingAddress(2, "Toby", "Toby A", "toby@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");
        int incorrectResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int result = billingAddressDao.addBillingAddress(tester);
        assertEquals(incorrectResult, result);

    }


    /**
     * Integration test but adding BillingAddress is null
     * @throws SQLException if something goes wrong with database
     */
    @Test
    void addBillingAddressButItsNull() throws SQLException {

        System.out.println("Add Billing Address but its null");

        BillingAddress tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            billingAddressDao.addBillingAddress(tester);
        });
    }

    /**
     * Integration test to get Billing Addresses
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllBillingAddress() throws SQLException {

        System.out.println("Test to get all BillingAddress");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);


        BillingAddress b1 = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");
        BillingAddress b2 = new BillingAddress(2, "James", "James M", "james@gmail.com", "90 Wheaton Hall", "Dublin", "Co.Louth", "A92 SQ3Q");

        ArrayList<BillingAddress> expectedResults = new ArrayList<>();
        expectedResults.add(b1);
        expectedResults.add(b2);

        ArrayList<BillingAddress> result = billingAddressDao.getAllBillingAddress();

        // test size

        assertEquals(expectedResults.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertBillingAddressEquals(expectedResults.get(i), result.get(i));
        }


    }

    /**
     * Integration test to Get BillingAddress by username
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getBillingAddressByUsername() throws SQLException {

        System.out.println("Test to get BillingAddress by username");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        BillingAddress b1 = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        BillingAddress result = billingAddressDao.getBillingAddressByUsername(b1.getUsername());

        //check if the billingAddresses are the same
        assertBillingAddressEquals(b1, result);

    }

    /**
     * Integration test to Get BillingAddress by username if username doesn't exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getBillingAddressByUsernameIfUsernameDoesntExist() throws SQLException {

        System.out.println("Test to get all BillingAddress if username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        BillingAddress b1 = new BillingAddress(1, "Ronaldo", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        BillingAddress result = billingAddressDao.getBillingAddressByUsername(b1.getUsername());

        //check if the billingAddresses are the same
        assertNotEquals(b1, result);

    }


    /**
     * Integration test to get billing address by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getBillingAddressById() throws SQLException {

        System.out.println("Test to get all BillingAddress by billingAddressId");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        BillingAddress b1 = new BillingAddress(1, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        BillingAddress result = billingAddressDao.getBillingAddressById(b1.getBilling_address_id());

        //check if the billingAddresses are the same
        assertBillingAddressEquals(b1, result);


    }


    /**
     * Integration test to get billing address by id but id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getBillingAddressByIdButIdDoesntExist() throws SQLException {

        System.out.println("Test to get all BillingAddress by billingAddressId but id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        BillingAddress b1 = new BillingAddress(111111, "Kate", "Kate A", "kate@gmail.com", "70 Vinland Road", "Dublin", "Co.Louth", "A92 QW2Q");

        BillingAddress result = billingAddressDao.getBillingAddressById(b1.getBilling_address_id());

        //check if the billingAddresses are the same
        assertNotEquals(b1, result);


    }

    /**
     * Test to update billing-address fullName
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressFullName() throws SQLException{


        System.out.println("Integration test to update billingAddress FullName");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 1;

        String fullName = "toby zedomi";
        int result = billingAddressDao.updateBillingAddressFullName(fullName, 2);

        assertEquals(correctResult,result);

       // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
       // assertEquals(fullName, updatedBillingAddress.getFullName());
    }


    /**
     * Update billing address fullName but billingAddressId doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressFullNameButIdDoesntExist() throws SQLException{

        System.out.println("Integration test to update billingAddress FullName but billingAddress id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 0;

        String fullName = "toby zedomi";
        int result = billingAddressDao.updateBillingAddressFullName(fullName, 2232);

        assertEquals(correctResult,result);

    }



    /**
     * Test to update billing-address email
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressEmail() throws SQLException{


        System.out.println("Integration test to update billingAddress Email");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 1;

        String email = "toby@gmail.com";
        int result = billingAddressDao.updateBillingAddressEmail(email, 2);

        assertEquals(correctResult,result);

        // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
        // assertEquals(fullName, updatedBillingAddress.getFullName());
    }

    /**
     * Test to update billing-address email but billing address id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressEmailButBillingAddressIdDoesntExist() throws SQLException{


        System.out.println("Integration test to update billingAddress Email but billing address id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 0;

        String email = "toby@gmail.com";
        int result = billingAddressDao.updateBillingAddressEmail(email, 2322);

        assertEquals(correctResult,result);

    }


    /**
     * Test to update billing-address address
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateAddressForBillingAddress() throws SQLException{


        System.out.println("Integration test to update billingAddress Address");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 1;

        String address = "forever street";
        int result = billingAddressDao.updateAddressForBillingAddress(address, 2);

        assertEquals(correctResult,result);

        // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
        // assertEquals(fullName, updatedBillingAddress.getFullName());
    }


    /**
     * Test to update billing-address address but billing address id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateAddressForBillingAddressButBillingAddressIdDoesntExist() throws SQLException{


        System.out.println("Integration test to update billingAddress Address but billingAddress id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 0;

        String address = "forever street";
        int result = billingAddressDao.updateAddressForBillingAddress(address, 23213);

        assertEquals(correctResult,result);

    }


    /**
     * Test to update billing-address city
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressCity() throws SQLException{


        System.out.println("Integration test to update billingAddress city");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 1;

        String city = "London";
        int result = billingAddressDao.updateBillingAddressCity(city, 2);

        assertEquals(correctResult,result);

        // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
        // assertEquals(fullName, updatedBillingAddress.getFullName());
    }



    /**
     * Test to update billing-address city but id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressCityButIdDoesntExist() throws SQLException{


        System.out.println("Integration test to update billingAddress city but id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 0;

        String city = "London";
        int result = billingAddressDao.updateBillingAddressCity(city, 22232);

        assertEquals(correctResult,result);

        // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
        // assertEquals(fullName, updatedBillingAddress.getFullName());
    }


    /**
     * Test to update billing-address county
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressCounty() throws SQLException{


        System.out.println("Integration test to update billingAddress county");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 1;

        String county = "Antrim";
        int result = billingAddressDao.updateBillingAddressCounty(county, 2);

        assertEquals(correctResult,result);

        // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
        // assertEquals(fullName, updatedBillingAddress.getFullName());
    }


    /**
     * Test to update billing-address county but id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressCountyButIdDoesntExist() throws SQLException{


        System.out.println("Integration test to update billingAddress county");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 0;

        String county = "Antrim";
        int result = billingAddressDao.updateBillingAddressCounty(county, 2321);

        assertEquals(correctResult,result);

        // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
        // assertEquals(fullName, updatedBillingAddress.getFullName());
    }



    /**
     * Test to update billing-address postcode
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressPostCode() throws SQLException{


        System.out.println("Integration test to update billingAddress postcode");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 1;

        String postcode = "A92 EWQW";
        int result = billingAddressDao.updateBillingAddressCounty(postcode, 2);

        assertEquals(correctResult,result);

        // BillingAddress updatedBillingAddress = billingAddressDao.getBillingAddressById(2);
        // assertEquals(fullName, updatedBillingAddress.getFullName());
    }


    /**
     * Test to update billing-address postcode but billingAddress doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateBillingAddressPostCodeButBillingAddressIdDoesntExist() throws SQLException{


        System.out.println("Integration test to update billingAddress postcode but id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        BillingAddressDao billingAddressDao = new BillingAddressDaoImpl(conn);

        int correctResult = 0;

        String postcode = "A92 EWQW";
        int result = billingAddressDao.updateBillingAddressCounty(postcode, 23214);

        assertEquals(correctResult,result);

    }




    /**
     * Check if two BillingAddress are the same
     *
     * @param b1 is the user being searched
     * @param b2 is the user being searched
     */

    private void assertBillingAddressEquals(BillingAddress b1, BillingAddress b2) {
        assertEquals(b1.getBilling_address_id(), b2.getBilling_address_id());
        assertEquals(b1.getUsername(), b2.getUsername());
        assertEquals(b1.getFullName(), b2.getFullName());
        assertEquals(b1.getEmail(), b2.getEmail());
        assertEquals(b1.getAddress(), b2.getAddress());
        assertEquals(b1.getCity(), b2.getCity());
        assertEquals(b1.getCounty(), b2.getCounty());
        assertEquals(b1.getPostcode(), b2.getPostcode());
    }

}