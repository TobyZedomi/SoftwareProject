package softwareProject.persistence;

import softwareProject.business.BillingAddress;
import softwareProject.business.MovieProduct;
import softwareProject.business.OrderItem;
import softwareProject.business.ShopOrder;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderItemDaoImpl extends MySQLDao implements OrderItemDao{

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public OrderItemDaoImpl(String databaseName){
        super(databaseName);
    }

    public OrderItemDaoImpl(Connection conn){
        super(conn);
    }
    public OrderItemDaoImpl(){
        super();
    }


    // add an orderItem

    /**
     * Add a order item
     * @param orderItem is the order item being added
     * @return 1if added and 0 if not added
     */

    @Override
    public int addOrderItem(OrderItem orderItem){
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
        try(PreparedStatement ps = conn.prepareStatement("insert into orderItem values(?, ?, ?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, orderItem.getOrder_items_id());
            ps.setDouble(2, orderItem.getPrice());
            ps.setInt(3, orderItem.getOrder_id());
            ps.setInt(4, orderItem.getMovie_id());

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


    // get all order items

    /**
     * Get all order items
     * @return an arraylist of all the order items
     */

    @Override
    public ArrayList<OrderItem> getAllOrderItems(){
        ArrayList<OrderItem> orderItems = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{

            con = getConnection();

            String query = "SELECT * from orderitem";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next()){

                    OrderItem orderItem = mapRow(rs);
                    orderItems.add(orderItem);
                }

        }catch (SQLException e) {
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

        return orderItems;
    }


    // get order item by id

    /**
     * Get order item by the id
     * @param id is the id being searched
     * @return the order item based on the id
     */
    @Override
    public OrderItem getOrderItemById(int id){

        OrderItem orderItem = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * FROM orderitem where order_items_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();



            if(rs.next()){

                orderItem = mapRow(rs);
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
        return orderItem;
    }

    /**
     * Search through each row in the orderItem
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private OrderItem mapRow(ResultSet rs)throws SQLException {

        OrderItem o = new OrderItem(

                rs.getInt("order_items_id"),
                rs.getDouble("price"),
                rs.getInt("order_id"),
                rs.getInt("movie_id")
        );
        return o;
    }


}
