package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.BillingAddress;
import softwareProject.business.FavoriteList;
import softwareProject.business.MovieProduct;
import softwareProject.business.Subscription;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FavouriteListDaoImplMockTest {


    public FavouriteListDaoImplMockTest(){

    }
    @Test
    void addFavouriteList() throws SQLException {

        System.out.println("Adding Favourite List mock Test");

        FavoriteList tester = new FavoriteList("admin", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);


        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("insert into favouriteList values(?, ?, ?, ?, " +
                "?)")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;

        // adding a new billingAddress
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(dbConn);
        int result = favouriteListDao.addFavouriteList(tester);

        System.out.println(result);


        assertEquals(expected, result);
    }


    /**
     * Mock test to get all favourite list by username
     * @throws SQLException
     */
    @Test
    void getAllFavouriteListByUsername() throws SQLException {


        System.out.println("Mock testing to get FavouriteList by username");

        FavoriteList f1 = new FavoriteList("admin", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire");
        FavoriteList f2 = new FavoriteList("admin", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire");

        ArrayList<FavoriteList> expectedResults = new ArrayList<>();
        expectedResults.add(f1);
        expectedResults.add(f2);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("SELECT * FROM favouritelist where username = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultset, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getString("username")).thenReturn(f1.getUsername(), f2.getUsername());
        when(rs.getInt("movieDb_id")).thenReturn(f1.getMovieDb_id(), f2.getMovieDb_id());
        when(rs.getString("backdrop_path")).thenReturn(f1.getBackdrop_path(), f2.getBackdrop_path());
        when(rs.getString("overview")).thenReturn(f1.getOverview(), f2.getOverview());
        when(rs.getString("title")).thenReturn(f1.getTitle(), f2.getTitle());


        int numInFavListTable = 2;

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl(dbConn);
        ArrayList<FavoriteList> result = favoriteListDao.getAllFavouriteListByUsername("admin");
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numInFavListTable, result.size());

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);

    }


    /**
     * Mock test to get all favourite list
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllFavouriteList() throws SQLException {

        System.out.println("Mock test to get all FavouriteList ");
        /// expected Results

        FavoriteList f1 = new FavoriteList("admin", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire");
        FavoriteList f2 = new FavoriteList("Toby", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire");

        ArrayList<FavoriteList> expectedResults = new ArrayList<>();
        expectedResults.add(f1);
        expectedResults.add(f2);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("Select * from favouritelist")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultset, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getString("username")).thenReturn(f1.getUsername(), f2.getUsername());
        when(rs.getInt("movieDb_id")).thenReturn(f1.getMovieDb_id(), f2.getMovieDb_id());
        when(rs.getString("backdrop_path")).thenReturn(f1.getBackdrop_path(), f2.getBackdrop_path());
        when(rs.getString("overview")).thenReturn(f1.getOverview(), f2.getOverview());
        when(rs.getString("title")).thenReturn(f1.getTitle(), f2.getTitle());

        int numInFavListTable = 2;

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl(dbConn);
        ArrayList<FavoriteList> result = favoriteListDao.getAllFavouriteList();
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(numInFavListTable, result.size());

        // An alternative approach is to use the arraylist of Products we created
        // as expected results at the start
        // If this equals the arraylist we got back from our method being tested,
        // then the method worked as expected
        assertEquals(expectedResults, result);
    }

    @Test
    void deleteFromFavouriteList() throws SQLException {

        System.out.println("Delete from the favourite list");

        FavoriteList f1 = new FavoriteList("admin", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire");

        // Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("DELETE from favouritelist where username = ? and movieDb_id = ?")).thenReturn(ps);

        // fill in data

        when(ps.executeUpdate()).thenReturn(1);

        int expected = 1;


        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl(dbConn);
        int result = favoriteListDao.deleteFroFavouriteList(f1.getUsername(), f1.getMovieDb_id());


        assertEquals(expected, result);

    }

    @Test
    void getFavouriteListByUsernameAndMovieId() throws SQLException {

        System.out.println("Mock testing to get FavouriteList by username and movie id");

        FavoriteList f1 = new FavoriteList("admin", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire");

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);



        when(dbConn.prepareStatement("SELECT * FROM favouritelist where username = ? and movieDb_id = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultSet, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, false);
        // Fill in the resultSet
        when(rs.getString("username")).thenReturn(f1.getUsername());
        when(rs.getInt("movieDb_id")).thenReturn(f1.getMovieDb_id());
        when(rs.getString("backdrop_path")).thenReturn(f1.getBackdrop_path());
        when(rs.getString("overview")).thenReturn(f1.getOverview());
        when(rs.getString("title")).thenReturn(f1.getTitle());

        // Supply the DAO with the mock connection, which has been filled with all
        // required mock information for the query to run
        FavoriteListDao favoriteListDao = new FavouriteListDaoImpl(dbConn);
        FavoriteList favoriteList = favoriteListDao.getFavouriteListByUsernameAndMovieId(f1.getUsername(), f1.getMovieDb_id());
        // Check that the number of entries retrieved matches the (known) number
        // of entries in the supplied dummy data
        assertEquals(f1, favoriteList);
    }
}