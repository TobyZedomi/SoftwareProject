package softwareProject.persistence;

import softwareProject.business.Cart;
import softwareProject.business.CartItem;
import softwareProject.business.MovieProduct;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CartItemDaoImpl extends MySQLDao implements CartItemDao{


    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public CartItemDaoImpl(String databaseName){
        super(databaseName);
    }

    public CartItemDaoImpl(Connection conn){
        super(conn);
    }
    public CartItemDaoImpl(){
        super();
    }




    // add to cartItem

    @Override
    public int addCartItem(CartItem cartItem){
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
        try(PreparedStatement ps = conn.prepareStatement("CALL addIntoCartItem(?, ?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, cartItem.getCart_id());
            ps.setInt(2, cartItem.getMovie_id());

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


    // get all cart items by cart id

    @Override
    public ArrayList<CartItem> getAllCartItemsByCartId(int cartId){

        ArrayList<CartItem> cartItems = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{

            con = getConnection();

            String query = "SELECT * FROM cart_items where cart_id = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, cartId);
            rs = ps.executeQuery();

                while(rs.next()){
                    CartItem c = mapRow(rs);
                    cartItems.add(c);

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
        return cartItems;
    }


    // delete cartItem with particular cart_id and movie_id

    @Override
    public int deleteCartItemByCartIdAndMovieId(int cartId, int movieId){
        int rowsAffected = 0;
        Connection con = null;
        PreparedStatement ps = null;

        try{

            con = getConnection();

            String query = "DELETE from cart_items where cart_id = ? and movie_id = ?";

            ps = con.prepareStatement(query);
            ps.setInt(1,cartId);
            ps.setInt(2,movieId);

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


    // count the total number of cartItems

    @Override
    public int totalNumberOfCartItems(int cartId){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT count(*) from cart_items WHERE cart_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1,cartId);
            rs = ps.executeQuery();

            if(rs.next()){
                    return rs.getInt(1);
                }

        }catch (SQLException e) {
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

        return 0;
    }


    // delete cartItem by cartId

    @Override
    public int deleteCartItemByCartId(int cartId){
        int rowsAffected = 0;

        Connection con = null;
        PreparedStatement ps = null;


        try{

            con = getConnection();

            String query = "DELETE from cart_items where cart_id = ?";

            ps = con.prepareStatement(query);
            ps.setInt(1,cartId);
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


    // getCartItems By cartId and movieId

    @Override
    public CartItem getCartItemByIdAndMovieId(int cartId, int movieId){

        CartItem cartItem = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try  {

            con = getConnection();

            String query = "SELECT * FROM cart_items where cart_id = ? AND movie_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, cartId);
            ps.setInt(2, movieId);
            rs = ps.executeQuery();

            if(rs.next()){

                cartItem = mapRow(rs);
            }


        } catch (SQLException e) {
            System.out.println("Exception occured in the getCartItemByIdAndMovieId() method: " + e.getMessage());
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
        return cartItem;
    }



    /**
     * Search through each row in the cartItem
     * @param rs is the rating query being searched
     * @return the rating information
     * @throws SQLException is username
     */

    private CartItem mapRow(ResultSet rs)throws SQLException {

        CartItem c = new CartItem(
                rs.getInt("cart_id"),
                rs.getInt("movie_id")
        );
        return c;
    }
}
