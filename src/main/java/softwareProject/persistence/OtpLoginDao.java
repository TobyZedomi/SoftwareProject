package softwareProject.persistence;

import softwareProject.business.OtpLogin;
import softwareProject.business.ResetPasswordToken;

import java.sql.*;
import java.time.LocalDateTime;

public class OtpLoginDao extends MySQLDao implements OtpLoginDaoImpl{

    /**
     * get the database information from a particular database
     *
     * @param databaseName is the database being searched
     */
    public OtpLoginDao(String databaseName) {
        super(databaseName);
    }

    public OtpLoginDao(Connection conn) {
        super(conn);
    }

    public OtpLoginDao() {
        super();
    }

    @Override
    public int insertOTP(String email, int number, LocalDateTime date) {
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
        try (PreparedStatement ps = conn.prepareStatement("insert into otp_login (email, otp_number, expiry) values(?,?,?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, email);
            ps.setInt(2, number);
            ps.setTimestamp(3, Timestamp.valueOf(date));

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
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;
    }
    @Override
    public OtpLogin findOtp(String email) {
        // DATABASE CODE
        //
        // Create variable to hold the result of the operation
        // Remember, where you are NOT doing a select, you will only ever get
        // a number indicating how many things were changed/affected
        OtpLogin found = null;

        Connection conn = super.getConnection();

        // TRY to prepare a statement from the connection
        // When you are parameterizing the update, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM otp_login WHERE email = ?")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, email);
            // Execute the update and store how many rows were affected/changed
            // when inserting, this number indicates if the row was
            // added to the database (>0 means it was added)
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    found = mapRow(rs);
                }
            }
        }// Add an extra exception handling block for where there is already an entry
        // with the primary key specified
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Constraint Exception occurred: " + e.getMessage());
            // Set the rowsAffected to -1, this can be used as a flag for the display section
            found = null;
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return found;
    }
    @Override
    public int deleteByEmailOtp(String email) {
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
        try (PreparedStatement ps = conn.prepareStatement("delete from otp_login where email = ?")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, email);

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
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return rowsAffected;
    }


    private OtpLogin mapRow(ResultSet rs)throws SQLException {

        OtpLogin p = new OtpLogin(
                rs.getString("email"),
                rs.getInt("otp_number"),
                rs.getTimestamp("expiry").toLocalDateTime()
        );
        return p;
    }

}
