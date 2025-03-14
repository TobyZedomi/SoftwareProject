package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.Movie;
import softwareProject.business.MovieProduct;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MovieProductDaoImplTest_IntegrationTest {


    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Test to get All Movie Products
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllMovieProducts() throws SQLException {
        System.out.println("Integration test to Get All Movie Products");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);


        ArrayList<MovieProduct> expectedResults = getMovieProducts();

        ArrayList<MovieProduct> result = movieProductDao.getAllMovieProducts();

        // test size

        assertEquals(expectedResults.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedResults.get(i), result.get(i));
        }


    }


    /**
     * Test to get movie by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getMovieById() throws SQLException {

        System.out.println("Integration Test to get Movie By Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);


        MovieProduct expectedResult = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2004,07, 16), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        MovieProduct result = movieProductDao.getMovieById(expectedResult.getMovie_id());

        assertEquals(expectedResult, result);
    }

    /**
     * Test to get movie by id but id doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getMovieByIdButIdDoesntExist() throws SQLException {

        System.out.println("Integration Test to get Movie By Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);


        MovieProduct expectedResult = new MovieProduct(1121, "Spider-Man-2", LocalDate.of(2004,07, 16), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);

        MovieProduct result = movieProductDao.getMovieById(expectedResult.getMovie_id());

        assertNotEquals(expectedResult, result);
    }


    /**
     * Integration test to add a new Movie Product
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addMovieProduct() throws SQLException {

        System.out.println("Integration test to add a new Movie Product");


        MovieProduct tester = new MovieProduct(16, "Spider-Man-5", LocalDate.of(2022, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int result = movieProductDao.addMovieProduct(tester);
        assertEquals(correctResult, result);

        MovieProduct inserted = movieProductDao.getMovieById(tester.getMovie_id());
        assertNotNull(inserted);

        assertEquals(tester, inserted);

    }


    /**
     * Integration test but Movie Id is the same
     * @throws SQLException if something is wrong in the database
     */
    @Test
    void addMovieProductButIdIsTheSame() throws SQLException {

        System.out.println("Integration test to add a new Movie Product but the id already exist");


        MovieProduct tester = new MovieProduct(1, "Spider-Man-5", LocalDate.of(2022, 2,21), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);
        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int result = movieProductDao.addMovieProduct(tester);
        assertEquals(correctResult, result);

    }


    /**
     * Integration test for adding Movie Product but it is null
     * @throws SQLException if something goes wrong with database
     */
    @Test
    void addMovieProductButItsNull() throws SQLException {

        System.out.println("Add Movie Product but its null");

        MovieProduct tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

           movieProductDao.addMovieProduct(tester);
        });
    }


    /**
     * Test to update movieProduct movie name
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductNameById() throws SQLException{


        System.out.println("Integration test to update movie product name by Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
      MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 1;

        String movieName = "Spiderman No Way Home";
        int result = movieProductDao.updateMovieProductNameByMovieId(movieName, 1);

        assertEquals(correctResult,result);

        /*
        MovieProduct updatedMovieProduct = movieProductDao.getMovieById(1);
        assertEquals(movieName, updatedMovieProduct.getMovie_name());

         conn.rollback();

         */
    }


    /**
     * Update Movie Product Name but MovieId doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductNameButIdDoesntExist() throws SQLException{

        System.out.println("Integration test to update movie Product Name but movie id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 0;

        String fullName = "toby zedomi";
        int result = movieProductDao.updateMovieProductNameByMovieId(fullName, 2232);

        assertEquals(correctResult,result);

    }



    /**
     * Test to update movieProduct date of release
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductDateOfReleaseById() throws SQLException{


        System.out.println("Integration test to update movie product date of release by Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 1;

        LocalDate date = LocalDate.of(2022, 12, 22);
        int result = movieProductDao.updateMovieProductDateOfReleaseByMovieId(date, 1);

        assertEquals(correctResult,result);

        /*
        MovieProduct updatedMovieProduct = movieProductDao.getMovieById(1);
        assertEquals(movieName, updatedMovieProduct.getMovie_name());

         conn.rollback();

         */
    }


    /**
     * Update Movie Product Date of release but MovieId doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductDateOfReleaseButIdDoesntExist() throws SQLException{

        System.out.println("Integration test to update movie Product date of release but movie id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 0;

        LocalDate date = LocalDate.of(2022, 12, 22);
        int result = movieProductDao.updateMovieProductDateOfReleaseByMovieId(date, 2232);

        assertEquals(correctResult,result);

    }


    /**
     * Test to update movieProduct information by id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductInfoById() throws SQLException{


        System.out.println("Integration test to update movie product information by Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 1;

        String information = "Spider-man is cool";
        int result = movieProductDao.updateMovieProductInfoByMovieId(information, 1);

        assertEquals(correctResult,result);

        /*
        MovieProduct updatedMovieProduct = movieProductDao.getMovieById(1);
        assertEquals(movieName, updatedMovieProduct.getMovie_name());

         conn.rollback();

         */
    }


    /**
     * Update Movie Product information but MovieId doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductInformationButIdDoesntExist() throws SQLException{

        System.out.println("Integration test to update movie Product information but movie id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 0;

        String information = "Spider-man is cool";
        int result = movieProductDao.updateMovieProductInfoByMovieId(information, 2232);

        assertEquals(correctResult,result);

    }


    /**
     * Test to update movie product list price by Id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductListPriceById() throws SQLException{


        System.out.println("Integration test to update movie product list price by Id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 1;

        double price  = 90;
        int result = movieProductDao.updateMovieProductPriceByMovieId(price, 1);

        assertEquals(correctResult,result);

        /*
        MovieProduct updatedMovieProduct = movieProductDao.getMovieById(1);
        assertEquals(movieName, updatedMovieProduct.getMovie_name());

         conn.rollback();

         */
    }


    /**
     * Update Movie Product list price but MovieId doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void  updateMovieProductListPriceButIdDoesntExist() throws SQLException{

        System.out.println("Integration test to update movie Product list Price but movie id doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);

        int correctResult = 0;

        double price  = 90;
        int result = movieProductDao.updateMovieProductPriceByMovieId(price, 2232);

        assertEquals(correctResult,result);

    }


    /**
     * Search for movie product based on entering movie name
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void searchForMovieProductBYMovieName() throws SQLException {


        System.out.println("Search for movie product based on movie name");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);


        MovieProduct m1 = new MovieProduct(9, "Limitless", LocalDate.of(2002, 03,25), Time.valueOf("01:45:00"), "Eddie Morra doesnt have a bright future as he loses his job but he discovers an untested drug that improves his decisions", "limitless.jpg", 15.99);

        ArrayList<MovieProduct> expectedResults = new ArrayList<>();
        expectedResults.add(m1);

        String query = "ss";

        ArrayList<MovieProduct> result = movieProductDao.searchForMovieProductBYMovieName(query);

        assertEquals(expectedResults, result);

        // checking the size is the same

        int size = expectedResults.size();

        assertEquals(size, result.size());

    }


    /**
     * Search for movie product based on entering movie name but no result
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void searchForMovieProductBYMovieNameButNoResultBasedOnEnteredValue() throws SQLException {


        System.out.println("Search for movie product based on movie name but no result based on movie name entered ");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        MovieProductDao movieProductDao = new MovieProductDaoImpl(conn);


        MovieProduct m1 = new MovieProduct(9, "Limitless", LocalDate.of(2002, 03,25), Time.valueOf("01:45:00"), "Eddie Morra doesnt have a bright future as he loses his job but he discovers an untested drug that improves his decisions", "limitless.jpg", 15.99);

        ArrayList<MovieProduct> expectedResults = new ArrayList<>();
        expectedResults.add(m1);

        String query = "ssgfvdv";

        ArrayList<MovieProduct> result = movieProductDao.searchForMovieProductBYMovieName(query);

        assertNotEquals(expectedResults, result);

    }


    /**
     * Get all Movie products
     * @return arraylist of all movie products
     */

    private static ArrayList<MovieProduct> getMovieProducts() {
        MovieProduct m1 = new MovieProduct(1, "Spider-Man-2", LocalDate.of(2004,07, 16), Time.valueOf("02:07:00"), "Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus", "spiderman-2.jpg", 10.99);
        MovieProduct m2 = new MovieProduct(2, "The Dark Knight", LocalDate.of(2008,07,28), Time.valueOf("02:32:00"), "Batman must face Joker as he wants to destroy and control Gotham City. Batman struggles to face joker before its to late", "batman.jpg", 12.99);
        MovieProduct m3 = new MovieProduct(3, "Inception", LocalDate.of(2010,07,16), Time.valueOf("02:27:00"), "Cobb enters the dreams of people to steal information and wants to achieve an impossible task", "inception.jpg", 8.99);
        MovieProduct m4 = new MovieProduct(4, "Dont Look Up", LocalDate.of(2021, 12, 10), Time.valueOf("02:46:00"), "Two astorometers try there best to tell the entire world that a comet is going to destroy earth", "dontlookup.jpg", 10.99);
        MovieProduct m5 = new MovieProduct(5, "Friday", LocalDate.of(1995,04,26),Time.valueOf("01:37:00"), "Two unemployed slackers are in debt to a gangster", "friday.jpg", 7.99);
        MovieProduct m6 = new MovieProduct(6, "Yes Man", LocalDate.of(2008,12,17), Time.valueOf("01:44:00"), "Carl is a lonely man who meets a friend that convinces him to say yes to everything for a whole entire year", "yesman.jpg", 5.99);
        MovieProduct m7 = new MovieProduct(7, "Interstellar", LocalDate.of(2014,11,07), Time.valueOf("02:55:00"), "Earth is going to become unlivable in the future so an exNasa pilot and his team is tasked to research other planets for humans", "interstellar.jpg", 5.99 );
        MovieProduct m8 = new MovieProduct(8, "The Matrix", LocalDate.of(1999,06,11), Time.valueOf("02:16:00"), "Neo a computer hacker has always questioned reality and his questioning was answered as the truth was revealed", "thematix.jpg", 12.99);
        MovieProduct m9 = new MovieProduct(9, "Limitless", LocalDate.of(2011,03,25), Time.valueOf("01:45:00"), "Eddie Morra doesnt have a bright future as he loses his job but he discovers an untested drug that improves his decisions", "limitless.jpg", 5.99);
        MovieProduct m10 = new MovieProduct(10, "Se7en", LocalDate.of(1995,9,22), Time.valueOf("02:10:00"), "Detectives Somerset and Millis pair up together to solve murders", "seven.webp", 15.99);
        MovieProduct m11 = new MovieProduct(11, "Old", LocalDate.of(2021, 07, 23), Time.valueOf("02:49:00"), "A vacation that turns into a nightmare, as a couple is stuck on a beach with others that make them age rapidly", "old.jpg", 5.99);
        MovieProduct m12 = new MovieProduct(12, "The Prestige", LocalDate.of(2006,11,10), Time.valueOf("02:10:00"), "Two friends became bitter rivals, as they are in constant competition with each other to achieve fame", "theprestige.jpg", 15.99 );
        MovieProduct m13 = new MovieProduct(13, "Spider-Man Into The Spider-Verse", LocalDate.of(2018,10,14), Time.valueOf("02:41:00"), "Miles Morales becomes Spider-man but he soon meets alternate versions of himself and gets in a battle to save the multiverse", "intospiderman.jpg", 8.99);
        MovieProduct m14 = new MovieProduct(14, "Kurokos Basketball Last Game", LocalDate.of(2017,03,18), Time.valueOf("01:31:00"), "Japan team faces the USA basketball team", "kuroko.jpg", 8.99);
        MovieProduct m15 = new MovieProduct(15, "The Last", LocalDate.of(2014,03,18), Time.valueOf("01:52:00"), "A couple years after the ninja war, Naruto must stop Toneri Otsutuski", "thelast.jpg", 10.99);


        ArrayList<MovieProduct> expectedResults = new ArrayList<>();
        expectedResults.add(m1);
        expectedResults.add(m2);
        expectedResults.add(m3);
        expectedResults.add(m4);
        expectedResults.add(m5);
        expectedResults.add(m6);
        expectedResults.add(m7);
        expectedResults.add(m8);
        expectedResults.add(m9);
        expectedResults.add(m10);
        expectedResults.add(m11);
        expectedResults.add(m12);
        expectedResults.add(m13);
        expectedResults.add(m14);
        expectedResults.add(m15);
        return expectedResults;
    }
}