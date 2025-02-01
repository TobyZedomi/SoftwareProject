package softwareProject.persistence;

import softwareProject.business.StreamingService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StreamingServiceDaoImpl extends MySQLDao implements StreamingServiceDao {

    public StreamingServiceDaoImpl(String databaseName) {
        super(databaseName);
    }

    @Override
    public List<StreamingService> getAllStreamingServices() {
        List<StreamingService> services = new ArrayList<>();
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM streamingservice")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StreamingService service = mapRow(rs);
                    services.add(service);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.freeConnection(conn);
        }
        return services;
    }

    private StreamingService mapRow(ResultSet rs) throws SQLException {
        StreamingService service = new StreamingService(
                rs.getInt("streaming_service_id"),
                rs.getString("movie_id"),
                rs.getString("streaming_service_name"),
                rs.getString("streaming_service_link"),
                rs.getDouble("cost")
        );
        return service;
    }
}
