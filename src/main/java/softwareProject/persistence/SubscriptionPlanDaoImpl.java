package softwareProject.persistence;

import softwareProject.business.SubscriptionPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionPlanDaoImpl extends MySQLDao implements SubscriptionPlanDao{


    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public SubscriptionPlanDaoImpl(String databaseName){
        super(databaseName);
    }

    public SubscriptionPlanDaoImpl(Connection conn){
        super(conn);
    }
    public SubscriptionPlanDaoImpl(){
        super();
    }



    /**
     * Get the subscriptionPlan from a particular subscriptionPlanID
     * @param subscriptionPlanID is the subscriptionPlan being searched
     * @return the subscription plan that matches the subscriptionPlanID
     */
    @Override
    public SubscriptionPlan getSubscriptionPlanById(int subscriptionPlanID){

        SubscriptionPlan subscriptionPlan = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM subscriptionplan where subscription_plan_id = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, subscriptionPlanID);

            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if(rs.next()){

                    subscriptionPlan = mapRow(rs);
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
        return subscriptionPlan;
    }




    /**
     * Search through each row in the subscription plan
     * @param rs is the query for subscriptions to be searched
     * @return the subscription information
     * @throws SQLException if something goes wrong in the database
     */
    private SubscriptionPlan mapRow(ResultSet rs)throws SQLException {

        SubscriptionPlan s = new SubscriptionPlan(
                rs.getInt("subscription_plan_id"),
                rs.getString("subscription_plan_name"),
                rs.getDouble("cost")
        );
        return s;
    }

}
