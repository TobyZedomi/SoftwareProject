package softwareProject.persistence;

import softwareProject.business.StreamingService;

import java.util.ArrayList;

public interface StreamingServiceDao {
    public ArrayList<StreamingService> getAllStreamingServicesByMovie(int movieID);
}
