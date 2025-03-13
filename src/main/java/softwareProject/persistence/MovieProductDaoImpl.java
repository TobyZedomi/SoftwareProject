package softwareProject.persistence;

import softwareProject.business.BillingAddress;
import softwareProject.business.CartItem;
import softwareProject.business.Movie;
import softwareProject.business.MovieProduct;

import java.sql.*;
import java.time.LocalDate;
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

           // String query = "SELECT * from movieProduct";
            String query = "CALL selectAllMovieProducts";
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



    // add Movie Product

    @Override
    public int addMovieProduct(MovieProduct movieProduct){
        // DATABASE CODE
        //
        // Create variable to hold the result of the operation
        // Remember, where you are NOT doing a select, you will only ever get
        // a number indicating how many things were changed/affected
        int rowsAffected = 0;


        Connection conn = super.getConnection();

        // TRY to prepare a statement from the connection
        // When you are parameterizing the update, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try(PreparedStatement ps = conn.prepareStatement("insert into movieProduct values(?, ?, ?, ?, ?, ?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, movieProduct.getMovie_id());
            ps.setString(2, movieProduct.getMovie_name());
            ps.setDate(3, Date.valueOf(movieProduct.getDate_of_release()));
            ps.setTime(4,movieProduct.getMovie_length());
            ps.setString(5, movieProduct.getMovie_info());
            ps.setString(6, movieProduct.getMovie_image());
            ps.setDouble(7, movieProduct.getListPrice());

            // Execute the update and store how many rows were affected/changed
            // when inserting, this number indicates if the row was
            // added to the database (>0 means it was added)
            rowsAffected = ps.executeUpdate();
        }// Add an extra exception handling block for where there is already an entry
        // with the primary key specified
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            // Set the rowsAffected to -1, this can be used as a flag for the display section
            rowsAffected = -1;
        }catch(SQLException e){
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;
    }



    @Override
    public int deleteMovieProductByMovieId(int movieId){
        int rowsAffected = 0;

        Connection con = null;
        PreparedStatement ps = null;


        try{

            con = getConnection();

            String query = "DELETE from movieProduct where movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setInt(1,movieId);
            rowsAffected = ps.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Exception occured in the updateProductName() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the updateProductName() method");
                e.getMessage();
            }
        }

        return rowsAffected;

    }


    // update movie name

    @Override
    public int updateMovieProductNameByMovieId(String movieName, int movieId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE movieProduct SET movie_name = ? WHERE movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, movieName);
            ps.setInt(2, movieId);

            rowsAffected = ps.executeUpdate();

        }catch (SQLException e) {
            System.out.println("Exception occured in the updateProductName() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the updateProductName() method");
                e.getMessage();
            }
        }

        return rowsAffected;
    }



    @Override
    public int updateMovieProductDateOfReleaseByMovieId(LocalDate dateOfRelease, int movieId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE movieProduct SET date_of_release = ? WHERE movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setDate(1, Date.valueOf(dateOfRelease));
            ps.setInt(2, movieId);

            rowsAffected = ps.executeUpdate();

        }catch (SQLException e) {
            System.out.println("Exception occured in the updateProductName() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the updateProductName() method");
                e.getMessage();
            }
        }

        return rowsAffected;
    }



    @Override
    public int updateMovieProductInfoByMovieId(String movieInfo, int movieId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE movieProduct SET movie_info = ? WHERE movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, movieInfo);
            ps.setInt(2, movieId);

            rowsAffected = ps.executeUpdate();

        }catch (SQLException e) {
            System.out.println("Exception occured in the updateProductName() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the updateProductName() method");
                e.getMessage();
            }
        }

        return rowsAffected;
    }


    @Override
    public int updateMovieProductPriceByMovieId(double listPrice, int movieId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE movieProduct SET listPrice = ? WHERE movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setDouble(1, listPrice);
            ps.setInt(2, movieId);

            rowsAffected = ps.executeUpdate();

        }catch (SQLException e) {
            System.out.println("Exception occured in the updateProductName() method: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                System.out.println("Exception occured in the finally section of the updateProductName() method");
                e.getMessage();
            }
        }

        return rowsAffected;
    }


// search for Movie Product
    @Override
    public ArrayList<MovieProduct> searchForMovieProductBYMovieName(String movieName){

        ArrayList<MovieProduct> movieProducts = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{

            con = getConnection();

            String query = "SELECT * FROM movieProduct WHERE movie_name LIKE '%' ? '%'";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, movieName);
            rs = ps.executeQuery();

            while(rs.next()){
                MovieProduct m = mapRow(rs);
                movieProducts.add(m);

            }


        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution" + e.getMessage());
        }finally {
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
        return movieProducts;
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
