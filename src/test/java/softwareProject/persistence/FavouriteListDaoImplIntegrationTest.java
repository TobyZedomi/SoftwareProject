package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.CartItem;
import softwareProject.business.FavoriteList;
import softwareProject.business.ShopOrder;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FavouriteListDaoImplIntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /**
     * Adding to the favourite list
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addFavouriteList() throws SQLException {

        System.out.println("Integration test to add a new movie to the favourite list");

        FavoriteList tester = new FavoriteList("Toby", 4, "test", "test", "test", "test",5);
        int correctResult = 1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        int result = favouriteListDao.addFavouriteList(tester);
        assertEquals(correctResult, result);

        FavoriteList inserted = favouriteListDao.getFavouriteListByUsernameAndMovieId(tester.getUsername(), tester.getMovieDb_id());
        assertNotNull(inserted);

        assertEquals(tester, inserted);
    }

    /**
     * Test to add to favourite list but username doesnt exist
     *
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addFavouriteListButUsernameDoesntExist() throws SQLException {

        System.out.println("Integration test to add to favourite list but username doesnt exist");

        FavoriteList tester = new FavoriteList("Yellow", 4, "test", "test", "test", "test",4);

        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        int result = favouriteListDao.addFavouriteList(tester);
        assertEquals(correctResult, result);

    }


    /**
     * Integration test to add to favourite list but username and movie id already taken
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void addFavouriteListButUsernameAndMovieIdAlreadyTaken() throws SQLException {

        System.out.println("Integration test to add to favourite list but username and movie id already taken");

        FavoriteList tester = new FavoriteList("admin", 86331, "test", "test", "test","test",4);

        int correctResult = -1;

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        int result = favouriteListDao.addFavouriteList(tester);
        assertEquals(correctResult, result);

    }


    /**
     * Integration test but adding FavouriteList is null
     *
     * @throws SQLException if something goes wrong with database
     */
    @Test
    void addCartItemButItsNull() throws SQLException {

        System.out.println("Add to favourite list but its null");

        FavoriteList tester = null;


        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);


        assertThrows(NullPointerException.class, () -> {

            favouriteListDao.addFavouriteList(tester);
        });
    }


    /**
     * Integration test to get all favourite list by username
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getAllFavouriteListByUsername() throws SQLException {

        System.out.println("Test to Get all favourite list by username");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        ArrayList<FavoriteList> expected = new ArrayList<>();
        expected.add(new FavoriteList( "Toby", 1373723, "/ibF5XVxTzf1ayzZrmiJqgeQ39Qk.jpg", "War stories about family, ethics and honor include the true story of two U.S. Marines who in a span of six seconds, must stand their ground to stop a suicide truck bomb, a Navy Corpsman who attempts to hold on to his humanity, and a WW2 soldier who gets separated from his squad and is forced to re-evaluate his code.", "The Codes of War", "Action", 28));

        ArrayList<FavoriteList> result = favouriteListDao.getAllFavouriteListByUsername("Toby");

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertEquals(expected.get(i), result.get(i));
        }
    }


    @Test
    void getAllFavouriteListByUsernameButUsernameDoesntExist() throws SQLException {

        System.out.println("Test to Get all favourite list by username but username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        ArrayList<FavoriteList> expected = new ArrayList<>();
        expected.add(new FavoriteList( "Toby", 1373723, "/ibF5XVxTzf1ayzZrmiJqgeQ39Qk.jpg", "War stories about family, ethics and honor include the true story of two U.S. Marines who in a span of six seconds, must stand their ground to stop a suicide truck bomb, a Navy Corpsman who attempts to hold on to his humanity, and a WW2 soldier who gets separated from his squad and is forced to re-evaluate his code.", "The Codes of War", "Action", 28));

        ArrayList<FavoriteList> result = favouriteListDao.getAllFavouriteListByUsername("Yellow");

        // test size

        assertNotEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertEquals(expected.get(i), result.get(i));
        }
    }

    /**
     * Get all from favourite list
     * @throws SQLException
     */
    @Test
    void getAllFavouriteList() throws SQLException {

        System.out.println("Test to Get all favourite list by username");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        ArrayList<FavoriteList> expected = new ArrayList<>();
        expected.add(new FavoriteList("admin", 86331, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire", "Drama",18));
        expected.add(new FavoriteList("admin", 950387, "/2Nti3gYAX513wvhp8IiLL6ZDyOm.jpg", "Four misfits find themselves struggling with ordinary problems when they are suddenly pulled through a mysterious portal into the Overworld: a bizarre, cubic wonderland that thrives on imagination. To get back home, they'll have to master this world while embarking on a magical quest with an unexpected, expert crafter, Steve.", "A Minecraft Movie", "Family",10751));
        expected.add(new FavoriteList( "Toby", 1373723, "/ibF5XVxTzf1ayzZrmiJqgeQ39Qk.jpg", "War stories about family, ethics and honor include the true story of two U.S. Marines who in a span of six seconds, must stand their ground to stop a suicide truck bomb, a Navy Corpsman who attempts to hold on to his humanity, and a WW2 soldier who gets separated from his squad and is forced to re-evaluate his code.", "The Codes of War", "Action", 28));

        ArrayList<FavoriteList> result = favouriteListDao.getAllFavouriteList();

        // test size

        assertEquals(expected.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size();i++){
            assertEquals(expected.get(i), result.get(i));
        }
    }


    /**
     * Integration test to Delete from favourite list
     * @throws SQLException
     */
    @Test
    void deleteFromFavouriteList() throws SQLException {

        System.out.println("Integration test to delete cartItem by cart id and movie id ");


        FavoriteList tester = new FavoriteList("admin", 86331, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire","Drama",18);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        int result = favouriteListDao.deleteFroFavouriteList(tester.getUsername(), tester.getMovieDb_id());
        assertEquals(1, result);

        // try find the cart by its cart id and movie id to see it no longer exist
        FavoriteList favoriteList = favouriteListDao.getFavouriteListByUsernameAndMovieId(tester.getUsername(), tester.getMovieDb_id());

        assertNotEquals(tester, favoriteList);
    }

    /**
     * Integration test to Delete from favourite list but username doesnt exist
     * @throws SQLException
     */
    @Test
    void deleteFromFavouriteListButUsernameDoesntExist() throws SQLException {

        System.out.println("Integration test to delete cartItem but username doesnt exist ");


        FavoriteList tester = new FavoriteList("Yellow", 86331, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire","Drama",18);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        int result = favouriteListDao.deleteFroFavouriteList(tester.getUsername(), tester.getMovieDb_id());
        assertEquals(0, result);

        // try find the cart by its cart id and movie id to see it no longer exist
        FavoriteList favoriteList = favouriteListDao.getFavouriteListByUsernameAndMovieId(tester.getUsername(), tester.getMovieDb_id());

        assertNotEquals(tester, favoriteList);
    }

    /**
     * Integration test to Delete from favourite list but movieId doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void deleteFromFavouriteListButMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration test to delete from favourite list but movie id doesnt exist ");


        FavoriteList tester = new FavoriteList("admin", 86331344, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire","Drama",18);

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        int result = favouriteListDao.deleteFroFavouriteList(tester.getUsername(), tester.getMovieDb_id());
        assertEquals(0, result);

        // try find the cart by its cart id and movie id to see it no longer exist
        FavoriteList favoriteList = favouriteListDao.getFavouriteListByUsernameAndMovieId(tester.getUsername(), tester.getMovieDb_id());

        assertNotEquals(tester, favoriteList);
    }


    /**
     * Get favourite list based on username and movie id
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getFavouriteListByUsernameAndMovieId() throws SQLException {

        System.out.println("Integration Test to get FavouriteList by username and Movie id");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        FavoriteList tester = new FavoriteList("admin", 86331, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire","Drama",18);


        FavoriteList result = favouriteListDao.getFavouriteListByUsernameAndMovieId(tester.getUsername(), tester.getMovieDb_id());

        //check if the billingAddresses are the same
        assertEquals(tester, result);

    }


    /**
     * Get favourite list based on username and movie id but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getFavouriteListByUsernameAndMovieIdButUsernameDoesntExist() throws SQLException {

        System.out.println("Integration Test to get FavouriteList by username and Movie id but username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        FavoriteList tester = new FavoriteList("Yellow", 86331, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire","Drama",18);


        FavoriteList result = favouriteListDao.getFavouriteListByUsernameAndMovieId(tester.getUsername(), tester.getMovieDb_id());

        //check if the billingAddresses are the same
        assertNotEquals(tester, result);

    }


    /**
     * Get favourite list based on username and movie id but username doesnt exist
     * @throws SQLException if something goes wrong in the database
     */
    @Test
    void getFavouriteListByUsernameAndMovieIdButMovieIdDoesntExist() throws SQLException {

        System.out.println("Integration Test to get FavouriteList by username and Movie id but username doesnt exist");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        FavouriteListDaoImpl favouriteListDao = new FavouriteListDaoImpl(conn);

        FavoriteList tester = new FavoriteList("admin", 863313424, "/zbTaYrQzZaaEf1SZlv3RTZiUvZw.jpg", "In a social context deteriorated by a countrywide economic crisis, the life of several people will be turned upside down after they meet Cécile, a character who symbolizes desire.", "Desire","Drama",18);


        FavoriteList result = favouriteListDao.getFavouriteListByUsernameAndMovieId(tester.getUsername(), tester.getMovieDb_id());

        //check if the billingAddresses are the same
        assertNotEquals(tester, result);

    }
}