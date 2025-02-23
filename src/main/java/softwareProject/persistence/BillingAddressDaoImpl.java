package softwareProject.persistence;

import softwareProject.business.BillingAddress;
import softwareProject.business.Cart;
import softwareProject.business.MovieProduct;
import softwareProject.business.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BillingAddressDaoImpl extends MySQLDao implements BillingAddressDao{

    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public BillingAddressDaoImpl(String databaseName){
        super(databaseName);
    }

    public BillingAddressDaoImpl(Connection conn){
        super(conn);
    }
    public BillingAddressDaoImpl(){
        super();
    }


    /// add to billingAddress


    @Override
    public int addBillingAddress(BillingAddress billingAddress){
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
        try(PreparedStatement ps = conn.prepareStatement("insert into billing_address values(?, ?, ?, ?, ?, ?,?, " +
                "?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setInt(1, billingAddress.getBilling_address_id());
            ps.setString(2, billingAddress.getUsername());
            ps.setString(3, billingAddress.getFullName());
            ps.setString(4, billingAddress.getEmail());
            ps.setString(5, billingAddress.getAddress());
            ps.setString(6, billingAddress.getCity());
            ps.setString(7, billingAddress.getCounty());
            ps.setString(8, billingAddress.getPostcode());

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


    // get all billingAddress

    @Override
    public ArrayList<BillingAddress> getAllBillingAddress(){

        ArrayList<BillingAddress> billingAddresses = new ArrayList<>();

        Connection conn = super.getConnection();

        try(PreparedStatement ps = conn.prepareStatement("SELECT * from billing_address")){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){

                    BillingAddress b = mapRow(rs);
                    billingAddresses.add(b);
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

        return billingAddresses;
    }


    // getBillingAddressByUsername


    @Override
    public BillingAddress getBillingAddressByUsername(String username){

        BillingAddress billingAddress = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM billing_address where username = ? ")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);

            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if(rs.next()){

                    billingAddress = mapRow(rs);
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
        return billingAddress;
    }


    /**
     * Search through each row in the billingAddress
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private BillingAddress mapRow(ResultSet rs)throws SQLException {

        BillingAddress b = new BillingAddress(

                rs.getInt("billing_address_id"),
                rs.getString("username"),
                rs.getString("fullName"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("county"),
                rs.getString("postcode")

        );
        return b;
    }

}
