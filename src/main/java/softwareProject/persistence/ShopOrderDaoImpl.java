package softwareProject.persistence;

import softwareProject.business.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ShopOrderDaoImpl extends MySQLDao implements ShopOrderDao{


    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public ShopOrderDaoImpl(String databaseName){
        super(databaseName);
    }

    public ShopOrderDaoImpl(Connection conn){
        super(conn);
    }
    public ShopOrderDaoImpl(){
        super();
    }




    /// add to shopOrder

    /**
     * Add a shop order
     * @param shopOrder is the shop order being added
     * @return 1 added and 0 if not added
     */

    @Override
    public int addShopOrder(ShopOrder shopOrder){
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
        try(PreparedStatement ps = conn.prepareStatement("insert into shop_order values(?, ?, ?, ?, ?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, shopOrder.getOrder_id());
            ps.setString(2, shopOrder.getUsername());
            ps.setInt(3, shopOrder.getBilling_address_id());
            ps.setTimestamp(4, Timestamp.valueOf(shopOrder.getOrder_date()));
            ps.setDouble(5, shopOrder.getTotal_price());
            ps.setString(6, shopOrder.getOrder_status());

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


// get top order with the highest order Id by Username

    /**
     * Get the shop order with the highest order id based on the username
     * @param username is the username being searched
     * @return the shop order based on the username
     */
    @Override
    public ShopOrder getOrderWithTheHighestOrderIdByUsername(String username){

        ShopOrder shopOrder = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = getConnection();

            String query = "SELECT * from shop_order where username = ? GROUP BY order_id ORDER BY order_id DESC LIMIT 1";
            ps = con.prepareStatement(query);

            ps.setString(1, username);
            rs = ps.executeQuery();



                if(rs.next()){

                    shopOrder = mapRow(rs);
                }


        } catch (SQLException e) {
            System.out.println("Exception occured in the getOrderWithTheHighestOrderIdByUsername() method: " + e.getMessage());
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
        return shopOrder;
    }


    // get all shop orders by username

    /**
     * Get all shop orders based on the username
     * @param username is the username being searched
     * @return the list of shop orders by username
     */
    @Override
    public ArrayList<ShopOrder> getAllShopOrdersByUsername(String username){

        ArrayList<ShopOrder> shopOrders = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{

            con = getConnection();

            String query = "SELECT * from shop_order WHERE username = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();


            while(rs.next()){

                    ShopOrder s = mapRow(rs);
                    shopOrders.add(s);
                }

        }catch (SQLException e) {
            System.out.println("Exception occured in the getAllShopOrdersByUsername() method: " + e.getMessage());
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
                System.out.println("Exception occured in the finally section of the getProductsWithGreaterQuantity() method: " + e.getMessage());
            }
        }

        return shopOrders;
    }

// get shop order by order id

    @Override
    public ShopOrder getShopOrderById(int id){

        ShopOrder shopOrder = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{

            con = getConnection();

            String query = "SELECT * FROM shop_order where order_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();



            if(rs.next()){

                shopOrder = mapRow(rs);
            }


        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
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
                System.out.println("Exception occurred in the finally section of the method: " + e.getMessage());
            }
        }
        return shopOrder;
    }



    /**
     * Search through each row in the shopOrder
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private ShopOrder mapRow(ResultSet rs)throws SQLException {

        ShopOrder s = new ShopOrder(

                rs.getInt("order_id"),
                rs.getString("username"),
                rs.getInt("billing_address_id"),
                rs.getTimestamp("order_date").toLocalDateTime(),
                rs.getDouble("total_price"),
                rs.getString("order_status")
        );
        return s;
    }

}
