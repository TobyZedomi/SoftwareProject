package softwareProject.persistence;

import softwareProject.business.ResetPasswordToken;

import java.sql.*;
import java.time.LocalDateTime;

public class ResetPasswordDaoImpl extends MySQLDao implements ResetPasswordDao{

    /**
     * get the database information from a particular database
     *
     * @param databaseName is the database being searched
     */
    public ResetPasswordDaoImpl(String databaseName) {
        super(databaseName);
    }

    public ResetPasswordDaoImpl(Connection conn) {
        super(conn);
    }

    public ResetPasswordDaoImpl() {
        super();
    }

    @Override
    public int insertToken(String email, String token, LocalDateTime date) {
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
        try (PreparedStatement ps = conn.prepareStatement("insert into password_reset_tokens (email, token, expiry) values(?,?,?)")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, email);
            ps.setString(2, token);
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
    public ResetPasswordToken findToken(String token) {
        // DATABASE CODE
        //
        // Create variable to hold the result of the operation
        // Remember, where you are NOT doing a select, you will only ever get
        // a number indicating how many things were changed/affected
        ResetPasswordToken found = null;

        Connection conn = super.getConnection();

        // TRY to prepare a statement from the connection
        // When you are parameterizing the update, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM password_reset_tokens WHERE token = ?")) {
            // Fill in the blanks, i.e. parameterize the update
            ps.setString(1, token);
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
    public int deleteByEmail(String email) {
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
        try (PreparedStatement ps = conn.prepareStatement("delete from password_reset_tokens where email = ?")) {
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


    private ResetPasswordToken mapRow(ResultSet rs)throws SQLException {

        ResetPasswordToken p = new ResetPasswordToken(
                rs.getString("email"),
                rs.getString("token"),
                rs.getTimestamp("expiry").toLocalDateTime()
        );
        return p;
    }
}
