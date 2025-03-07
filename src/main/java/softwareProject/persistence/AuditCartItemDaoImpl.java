package softwareProject.persistence;

import softwareProject.business.AuditCartItem;
import softwareProject.business.AuditCartItem2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AuditCartItemDaoImpl extends MySQLDao implements AuditCartItemDao {

    /**
     * get the database information from a particular database
     *
     * @param databaseName is the database being searched
     */
    public AuditCartItemDaoImpl(String databaseName) {
        super(databaseName);
    }

    public AuditCartItemDaoImpl(Connection conn) {
        super(conn);
    }

    public AuditCartItemDaoImpl() {
        super();
    }


    // arraylist of all movie ids and count by how they appear in descending order

    @Override
    public ArrayList<AuditCartItem2> getMovieIdsInDescOrderOfCount() {

        ArrayList<AuditCartItem2> auditCartItems = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();

            String query = "SELECT movie_id, COUNT(*) as count FROM auditscartitems GROUP BY movie_id ORDER BY COUNT(*) DESC";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                AuditCartItem2 a = mapRow2(rs);
                auditCartItems.add(a);
            }
        } catch (SQLException e) {
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

        return auditCartItems;
    }


    /**
     * Search through each row in the billingAddress
     *
     * @param rs is the query for user to be searched
     * @return the user information
     * @throws SQLException
     */
    private AuditCartItem mapRow(ResultSet rs) throws SQLException {

        AuditCartItem a = new AuditCartItem(

                rs.getInt("auditCartItemsID"),
                rs.getString("table_name"),
                rs.getString("transaction_name"),
                rs.getInt("movie_id"),
                rs.getTimestamp("transdate").toLocalDateTime()

        );
        return a;
    }


    private AuditCartItem2 mapRow2(ResultSet rs) throws SQLException {

        AuditCartItem2 a = new AuditCartItem2(

                rs.getInt("movie_id"),
                rs.getInt("count")

        );
        return a;
    }

}


