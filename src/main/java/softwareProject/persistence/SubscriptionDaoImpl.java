package softwareProject.persistence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Flow;

import softwareProject.business.BillingAddress;
import softwareProject.business.Subscription;
import softwareProject.business.SubscriptionPlan;

public class SubscriptionDaoImpl extends MySQLDao implements SubscriptionDao {


    public SubscriptionDaoImpl(String databaseName){
        super(databaseName);
    }

    public SubscriptionDaoImpl(Connection conn){
        super(conn);
    }
    public SubscriptionDaoImpl(){
        super();
    }


    // add subscription


// add subscription

    /**
     * Add a subscription to the database
     * @param subscription is the subscription being added
     * @return 1 if subscription was added and -1 if not added
     */
    @Override
    public int addSubscription(Subscription subscription){
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
        try(PreparedStatement ps = conn.prepareStatement("INSERT INTO subscription(username, subscription_plan_id, subscription_startDate, subscription_endDate) VALUES ( ?, ?, ?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, subscription.getUsername());
            ps.setInt(2, subscription.getSubscription_plan_id());
            ps.setTimestamp(3, Timestamp.valueOf(subscription.getSubscription_startDate()));
            ps.setTimestamp(4, Timestamp.valueOf(subscription.getSubscription_endDate()));

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


    /// get username from username in subscription

    /**
     * get the subscription for the user based on the username
     * @param username is the username being searched
     * @return the user subscription based on username if username doesn't match it will return null
     */

    @Override
    public Subscription getSubscriptionFromUsername(String username){

        Subscription subUser = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * from subscription WHERE username = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);
            rs = ps.executeQuery();



            if(rs.next()){

                subUser = mapRow(rs);
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
        return subUser;
    }




    // get all subscriptions

    /**
     * get all subscriptions in the database
     * @return an arraylist of all subscriptions
     */
    @Override
    public ArrayList<Subscription> getAllSubscriptions(){

        ArrayList<Subscription> subscriptions = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            con = getConnection();

            String query = "SELECT * from subscription";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next())
            {
                Subscription s = mapRow(rs);
                subscriptions.add(s);
            }
        }catch(SQLException e){
            System.out.println(LocalDateTime.now() + ": An SQLException  occurred while preparing the SQL " +
                    "statement.");
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
                System.out.println("Exception occured in the finally section of the  method: " + e.getMessage());
            }
        }

        return subscriptions;
    }


    /**
     * Search through each row in the subscription
     * @param rs is the query for subscriptions to be searched
     * @return the subscription information
     * @throws SQLException if something goes wrong in the database
     */
    private Subscription mapRow(ResultSet rs)throws SQLException {

        Subscription s = new Subscription(
                rs.getString("username"),
                rs.getInt("subscription_plan_id"),
                rs.getTimestamp("subscription_startDate").toLocalDateTime(),
                rs.getTimestamp("subscription_endDate").toLocalDateTime()
        );
        return s;
    }

}
