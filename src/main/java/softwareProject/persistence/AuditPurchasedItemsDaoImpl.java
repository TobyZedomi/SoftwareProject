package softwareProject.persistence;

import softwareProject.business.AuditPurchasedItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditPurchasedItemsDaoImpl extends MySQLDao implements AuditPurchasedItemsDao {
    /**
     * get the database information from a particular database
     * @param databaseName is the database being searched
     */
    public AuditPurchasedItemsDaoImpl(String databaseName){
        super(databaseName);
    }

    public AuditPurchasedItemsDaoImpl(Connection conn){
        super(conn);
    }
    public AuditPurchasedItemsDaoImpl(){
        super();
    }
    @Override
    public Map<LocalDate, List<AuditPurchasedItems>> searchForPurchasesGroupedByMonth(LocalDateTime startDate, LocalDateTime endDate){

        Map<LocalDate, List<AuditPurchasedItems>> requests = new HashMap();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM auditPurchasedItems WHERE created_at BETWEEN ? AND ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, startDate.toString());
            ps.setString(2, endDate.toString());


            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()){
                    AuditPurchasedItems m = mapRow(rs);
                    LocalDate month = m.getCreated_at().withDayOfMonth(1).toLocalDate();

                    if (!requests.containsKey(month)) {
                        requests.put(month, new ArrayList<>());
                    }

                    requests.get(month).add(m);
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
        return requests;
    }

    private AuditPurchasedItems mapRow(ResultSet rs)throws SQLException {

        AuditPurchasedItems ap = new AuditPurchasedItems(
                rs.getInt("auditPurchasedItemsID"),
                rs.getString("username"),
                rs.getInt("order_id"),
                rs.getDouble("price"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        return ap;
    }
}
