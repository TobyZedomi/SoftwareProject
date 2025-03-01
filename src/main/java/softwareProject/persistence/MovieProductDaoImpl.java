package softwareProject.persistence;

import softwareProject.business.Movie;
import softwareProject.business.MovieProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MovieProductDaoImpl extends MySQLDao implements MovieProductDao {

    /**
     * get the database information from a particular database
     *
     * @param databaseName is the database being searched
     */
    public MovieProductDaoImpl(String databaseName) {
        super(databaseName);
    }

    public MovieProductDaoImpl(Connection conn) {
        super(conn);
    }

    public MovieProductDaoImpl() {
        super();
    }


    @Override
    public ArrayList<MovieProduct> getAllMovieProducts() {
        ArrayList<MovieProduct> movieProducts = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {


            con = getConnection();

            String query = "SELECT * from movieProduct";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                MovieProduct m = mapRow(rs);
                movieProducts.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Exception occured in the getAllProducts() method: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the getAllProducts() method: " + e.getMessage());
            }
        }

        return movieProducts;
    }


    @Override
    public MovieProduct getMovieById(int movieId) {

        MovieProduct movieProduct = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            con = getConnection();

            String query = "SELECT * FROM movieProduct where movie_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, movieId);
            rs = ps.executeQuery();


            if (rs.next()) {

                movieProduct = mapRow(rs);
            }

        } catch (SQLException e) {
            System.out.println("Exception occured in the getMovieById() method: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the getProductByCode() method: " + e.getMessage());
            }
        }
        return movieProduct;
    }


    /**
     * Search through each row in the movie
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private MovieProduct mapRow(ResultSet rs) throws SQLException {

        MovieProduct m = new MovieProduct(

                rs.getInt("movie_id"),
                rs.getString("movie_name"),
                rs.getDate("date_of_release").toLocalDate(),
                rs.getTime("movie_length"),
                rs.getString("movie_info"),
                rs.getString("movie_image"),
                rs.getDouble("listPrice")
        );
        return m;
    }
}
