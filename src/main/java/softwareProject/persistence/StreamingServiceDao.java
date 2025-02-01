package softwareProject.persistence;

import softwareProject.business.StreamingService;
import java.util.List;

public interface StreamingServiceDao {
    List<StreamingService> getAllStreamingServices();
}
