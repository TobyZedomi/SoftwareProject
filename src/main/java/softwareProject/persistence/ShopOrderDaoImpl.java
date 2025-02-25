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
    @Override
    public ShopOrder getOrderWithTheHighestOrderIdByUsername(String username){

        ShopOrder shopOrder = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * from shop_order where username = ? GROUP BY order_id ORDER BY order_id DESC LIMIT 1")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);

            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if(rs.next()){

                    shopOrder = mapRow(rs);
                }

            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return shopOrder;
    }


    // get all shop orders by username

    @Override
    public ArrayList<ShopOrder> getAllShopOrdersByUsername(String username){

        ArrayList<ShopOrder> shopOrders = new ArrayList<>();

        Connection conn = super.getConnection();

        try(PreparedStatement ps = conn.prepareStatement("SELECT * from shop_order WHERE username = ?")){
            ps.setString(1, username);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){

                    ShopOrder s = mapRow(rs);
                    shopOrders.add(s);
                }
            }catch(SQLException e){
                System.out.println(LocalDateTime.now() + ": An SQLException  occurred while running the query" +
                        " or processing the result.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }catch(SQLException e){
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }

        return shopOrders;
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
