package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.AuditCartItem;
import softwareProject.business.AuditCartItem2;
import softwareProject.business.BillingAddress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AuditCartItemDaoImpl_IntegrationTest {

    private MySQLDao connectionSource = new MySQLDao("database_test.properties");

    @Test
    void getMovieIdsInDescOrderOfCount() throws SQLException {

        System.out.println("Test to get all movie id and count in the AuditCartItem table inb descending order");

        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        AuditCartItemDao auditCartItemDao = new AuditCartItemDaoImpl(conn);


        AuditCartItem2 a1 = new AuditCartItem2(1, 3);
        AuditCartItem2 a2 = new AuditCartItem2(2, 2);
        AuditCartItem2 a3 = new AuditCartItem2(1, 1);

        ArrayList<AuditCartItem2> expectedResults = new ArrayList<>();
        expectedResults.add(a1);
        expectedResults.add(a2);
        expectedResults.add(a3);

        ArrayList<AuditCartItem2> result = auditCartItemDao.getMovieIdsInDescOrderOfCount();

        // test size

        assertEquals(expectedResults.size(), result.size());

        // test if all elements are the same

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedResults.get(i), result.get(i));
        }


    }






}