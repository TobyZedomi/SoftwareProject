package softwareProject.persistence;

import softwareProject.business.BillingAddress;
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
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * FROM subscriptionplan where subscription_plan_id = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, subscriptionPlanID);
            rs = ps.executeQuery();



            if(rs.next()){

                subscriptionPlan = mapRow(rs);
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
