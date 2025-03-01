package softwareProject.persistence;

import softwareProject.business.Cart;
import softwareProject.business.Subscription;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CartDaoImpl extends MySQLDao implements CartDao{


    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public CartDaoImpl(String databaseName){
        super(databaseName);
    }

    public CartDaoImpl(Connection conn){
        super(conn);
    }
    public CartDaoImpl(){
        super();
    }


    @Override
    public int addCart(Cart cart){
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
        try(PreparedStatement ps = conn.prepareStatement("insert into carts values(?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, cart.getCart_id());
            ps.setString(2, cart.getUsername());


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
    public ArrayList<Cart> getAllCarts(){

        ArrayList<Cart> carts = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{


            con = getConnection();

            String query = "Select * from carts";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next()){

                    Cart c = mapRow(rs);
                    carts.add(c);
                }

        }catch(SQLException e){
            System.out.println("Exception occured in the getAllCarts() method: " + e.getMessage());
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
                System.out.println("Exception occured in the finally section of the getAllProducts() method: " + e.getMessage());
            }
        }

        return carts;
    }


// GET cartId by username


    @Override
    public Cart getCartByUsername(String username){

        Cart cart = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try  {

            con = getConnection();

            String query = "SELECT * FROM carts where username = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if(rs.next()){

                    cart = mapRow(rs);
                }


        } catch (SQLException e) {
            System.out.println("Exception occured in the getCartByUsername() method: " + e.getMessage());
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
        return cart;
    }


    @Override
    public Cart getCartById(int id){

        Cart cart = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try  {

            con = getConnection();

            String query = "SELECT * FROM carts where cart_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if(rs.next()){

                cart = mapRow(rs);
            }


        } catch (SQLException e) {
            System.out.println("Exception occured in the getCartByIdAndUsername() method: " + e.getMessage());
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
        return cart;
    }


    /**
     * Search through each row in the cart
     * @param rs is the rating query being searched
     * @return the rating information
     * @throws SQLException is username
     */

    private Cart mapRow(ResultSet rs)throws SQLException {

        Cart c = new Cart(
                rs.getInt("cart_id"),
                rs.getString("username")
        );
        return c;
    }


}
