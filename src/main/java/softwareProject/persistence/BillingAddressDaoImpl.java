package softwareProject.persistence;

import softwareProject.business.BillingAddress;
import softwareProject.business.Cart;
import softwareProject.business.MovieProduct;
import softwareProject.business.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            con = getConnection();

            String query = "Select * from billing_address";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next())
            {
                BillingAddress b = mapRow(rs);
                billingAddresses.add(b);
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

        return billingAddresses;
    }

    // getBillingAddressByUsername


    @Override
    public BillingAddress getBillingAddressByUsername(String username){

        BillingAddress billingAddress = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * FROM billing_address where username = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, username);
            rs = ps.executeQuery();



                if(rs.next()){

                    billingAddress = mapRow(rs);
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
        return billingAddress;
    }

    // get BillingAddressById

    @Override
    public BillingAddress getBillingAddressById(int id){

        BillingAddress billingAddress = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{


            con = getConnection();

            String query = "SELECT * FROM billing_address where billing_address_id = ?";
            ps = con.prepareStatement(query);
            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, id);
            rs = ps.executeQuery();



            if(rs.next()){

                billingAddress = mapRow(rs);
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
        return billingAddress;
    }


    // update fullname

    @Override
    public int updateBillingAddressFullName(String fullName, int billingId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE billing_address SET fullName = ? WHERE billing_address_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, fullName);
            ps.setInt(2, billingId);

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
    public int updateBillingAddressEmail(String email, int billingId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE billing_address SET email = ? WHERE billing_address_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setInt(2, billingId);

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
    public int updateAddressForBillingAddress(String address, int billingId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE billing_address SET address = ? WHERE billing_address_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, address);
            ps.setInt(2, billingId);

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
    public int updateBillingAddressCity(String city, int billingId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE billing_address SET city = ? WHERE billing_address_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, city);
            ps.setInt(2, billingId);

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
    public int updateBillingAddressCounty(String county, int billingId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE billing_address SET county = ? WHERE billing_address_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, county);
            ps.setInt(2, billingId);

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
    public int updateBillingAddressPostCode(String postCode, int billingId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;

        try{
            con = getConnection();

            String query = "UPDATE billing_address SET postcode = ? WHERE billing_address_id = ?";

            ps = con.prepareStatement(query);
            ps.setString(1, postCode);
            ps.setInt(2, billingId);

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
