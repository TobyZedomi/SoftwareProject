package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import softwareProject.business.BillingAddress;
import softwareProject.business.CartItem;
import softwareProject.business.Movie;
import softwareProject.business.MovieProduct;

import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MovieProductDaoImplTestMockTesting {

    public MovieProductDaoImplTestMockTesting(){

    }


    /**
     * Mock Testing to get all Movie Products
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllMovieProducts() throws SQLException {

        System.out.println("Mock testing to get all Movie Products");

        MovieProduct m1 = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);
        MovieProduct m2 = new MovieProduct(2, "The Dark Knight", LocalDate.of(2008, 7,28), Time.valueOf("02:17:00"), "Batman must face Joker as he wants to destroy and control Gotham City. Batman struggles to face joker before its to late", "batman.jpg", 12.99 );
        MovieProduct m3 = new MovieProduct(3, "Inception", LocalDate.of(2010, 7,16),  Time.valueOf("02:17:00"), "Cobb enters the dreams of people to steal information and wants to achieve an impossible task", "inception.jpg", 8.99);

        ArrayList<MovieProduct> expectedResults = new ArrayList<>();
        expectedResults.add(m1);
        expectedResults.add(m2);
        expectedResults.add(m3);

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("CALL selectAllMovieProducts")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true, true, true, false);


        when(rs.getInt("movie_id")).thenReturn(m1.getMovie_id(), m2.getMovie_id(), m3.getMovie_id());
        when(rs.getString("movie_name")).thenReturn(m1.getMovie_name(), m2.getMovie_name(), m3.getMovie_name());
        when(rs.getDate("date_of_release")).thenReturn(Date.valueOf(m1.getDate_of_release()), Date.valueOf(m2.getDate_of_release()), Date.valueOf(m3.getDate_of_release()));
        when(rs.getTime("movie_length")).thenReturn(m1.getMovie_length(), m2.getMovie_length(), m3.getMovie_length());
        when(rs.getString("movie_info")).thenReturn(m1.getMovie_info(), m2.getMovie_info(), m3.getMovie_info());
        when(rs.getString("movie_image")).thenReturn(m1.getMovie_image(), m2.getMovie_image(), m3.getMovie_image());
        when(rs.getDouble("listPrice")).thenReturn(m1.getListPrice(), m2.getListPrice(), m3.getListPrice());


        int numMovieProductInTable = 3;

        MovieProductDao movieProductDao = new MovieProductDaoImpl(dbConn);
        ArrayList<MovieProduct> result = movieProductDao.getAllMovieProducts();


        // check the size is the same
        assertEquals(numMovieProductInTable, result.size());


        assertEquals(expectedResults, result);

        System.out.println("Results:");
        for(MovieProduct m: result){
            System.out.println(m);
        }

    }

    /**
     * Mock testing to Get MovieProduct by Id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getMovieById() throws SQLException {

        System.out.println("Mock test to get Movie By movie Id");

        MovieProduct m1 = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        // Create mock objects
        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Fill mock objects with appropriatel dummy data
        when(dbConn.prepareStatement("SELECT * FROM movieProduct where movie_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        // Want 3 results in the resultset, so need true to be returned 3 times
        when(rs.next()).thenReturn(true,  false);


        when(rs.getInt("movie_id")).thenReturn(m1.getMovie_id());
        when(rs.getString("movie_name")).thenReturn(m1.getMovie_name());
        when(rs.getDate("date_of_release")).thenReturn(Date.valueOf(m1.getDate_of_release()));
        when(rs.getTime("movie_length")).thenReturn(m1.getMovie_length());
        when(rs.getString("movie_info")).thenReturn(m1.getMovie_info());
        when(rs.getString("movie_image")).thenReturn(m1.getMovie_image());
        when(rs.getDouble("listPrice")).thenReturn(m1.getListPrice());

        MovieProductDao movieProductDao = new MovieProductDaoImpl(dbConn);
        MovieProduct result = movieProductDao.getMovieById(m1.getMovie_id());

        assertEquals(m1, result);

    }



    /**
     * Mock testing to add Movie Product
     * @throws SQLException if anything goes wrong in the database
     */
    @Test
    void addMovieProduct() throws SQLException {

        System.out.println("Adding Movie Product mock Test");

        MovieProduct adding = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into movieProduct values(?, ?, ?, ?, ?, ?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        MovieProductDao movieProductDao = new MovieProductDaoImpl(dbConn);
        int result = movieProductDao.addMovieProduct(adding);


        assertEquals(expected, result);
    }

    /**
     * Mock test to delete movie product by id
     * @throws SQLException
     */
    @Test
    void deleteMovieProductByMovieId() throws SQLException {

        System.out.println("Delete Movie Product by movie ID");

        MovieProduct m1 = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        // Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("DELETE from movieProduct where movie_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;


        MovieProductDao movieProductDao = new MovieProductDaoImpl(dbConn);
        int result = movieProductDao.deleteMovieProductByMovieId(m1.getMovie_id());


        assertEquals(expected, result);
    }


    /**
     * Mock test to update movie product name by id
     * @throws SQLException if something goes wrong in the database
     */

    @Test
    void updateMovieProductNameByMovieId() throws SQLException {

        System.out.println("Mock Test to update Movie Product Name by Id");

        MovieProduct update = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        String movieName = "Spider-man No Way Home";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement(  "UPDATE movieProduct SET movie_name = ? WHERE movie_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        MovieProductDao movieProductDao= new MovieProductDaoImpl(dbConn);
        int result = movieProductDao.updateMovieProductNameByMovieId(movieName, update.getMovie_id());


        assertEquals(expectedResult, result);
    }


    /**
     * Mock test to update movie product date of release by id
     * @throws SQLException if something goes wrong in the database
     */

    @Test
    void updateMovieProductDateOfReleaseByMovieId() throws SQLException {

        System.out.println("Mock Test to update Movie Product Date of release by Id");

        MovieProduct update = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        LocalDate date = LocalDate.of(2022, 12, 22);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement(  "UPDATE movieProduct SET date_of_release = ? WHERE movie_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;

        MovieProductDao movieProductDao= new MovieProductDaoImpl(dbConn);
        int result = movieProductDao.updateMovieProductDateOfReleaseByMovieId(date, update.getMovie_id());


        assertEquals(expectedResult, result);
    }



    /**
     * Mock test to update movie product information by id
     * @throws SQLException if something goes wrong in the database
     */

    @Test
    void updateMovieProductInfoByMovieId() throws SQLException {

        System.out.println("Mock Test to update Movie Product Date information by Id");

        MovieProduct update = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

       String movieProductInfo = "Spiderier is cool";

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement(  "UPDATE movieProduct SET movie_info = ? WHERE movie_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;


        MovieProductDao movieProductDao= new MovieProductDaoImpl(dbConn);
        int result = movieProductDao.updateMovieProductInfoByMovieId(movieProductInfo, update.getMovie_id());

        assertEquals(expectedResult, result);
    }


    /**
     * Mock test to update movie product information by id
     * @throws SQLException if something goes wrong in the database
     */

    @Test
    void updateMovieProductPriceByMovieId() throws SQLException {

        System.out.println("Mock Test to update Movie Product List Price  by Id");

        MovieProduct update = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2002, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        Double movieProductPrice = 100.0;

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);



        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement(  "UPDATE movieProduct SET listPrice = ? WHERE movie_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expectedResult = 1;


        MovieProductDao movieProductDao= new MovieProductDaoImpl(dbConn);
        int result = movieProductDao.updateMovieProductPriceByMovieId(movieProductPrice, update.getMovie_id());

        assertEquals(expectedResult, result);
    }




}