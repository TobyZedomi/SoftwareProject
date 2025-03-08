package softwareProject.persistence;

import org.junit.jupiter.api.Test;
import softwareProject.business.AuditCartItem2;
import softwareProject.business.BillingAddress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditCartItemDaoImplMockTest {

    public AuditCartItemDaoImplMockTest(){

    }

    @Test
    void getMovieIdsInDescOrderOfCount() throws SQLException {


        System.out.println("Mock test to get all the movie id and count of movies deleted in the audit cart items table in descending order of count  ");
        /// expected Results

        AuditCartItem2 a1 = new AuditCartItem2(1, 2);
        AuditCartItem2 a2 = new AuditCartItem2(2, 1);

        ArrayList<AuditCartItem2> expectedResults = new ArrayList<>();
        expectedResults.add(a1);
        expectedResults.add(a2);

        /// Create Mock Objects

        Connection dbConn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        // Filling mock objects with appropriately dummy data
        when(dbConn.prepareStatement("SELECT movie_id, COUNT(*) as count FROM auditscartitems GROUP BY movie_id ORDER BY COUNT(*) DESC")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        // Want 2 results in the resultset, so need true to be returned 2 times
        when(rs.next()).thenReturn(true, true, false);
        // Fill in the resultset
        when(rs.getInt("movie_id")).thenReturn(a1.getMovie_id(), a1.getMovie_id());
        when(rs.getInt("count")).thenReturn(a1.getCount(), a2.getCount());

        int numAuditCartItems = 2;


        AuditCartItemDao auditCartItemDao = new AuditCartItemDaoImpl(dbConn);
        ArrayList<AuditCartItem2> result = auditCartItemDao.getMovieIdsInDescOrderOfCount();

        assertEquals(numAuditCartItems, result.size());


        assertEquals(expectedResults, result);


    }
}