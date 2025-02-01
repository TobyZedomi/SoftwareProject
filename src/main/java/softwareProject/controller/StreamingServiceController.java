package softwareProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Movie;
import softwareProject.business.StreamingService;
import softwareProject.persistence.MovieDao;
import softwareProject.persistence.MovieDaoImpl;
import softwareProject.persistence.StreamingServiceDao;
import softwareProject.persistence.StreamingServiceDaoImpl;

import java.util.ArrayList;

@Controller
public class StreamingServiceController {

    @GetMapping("/getAllStreamingServicesForMovie")
    public String viewByGenre(@RequestParam(name = "movieID") String movieID, Model model){
        int movieID2 = Integer.parseInt(movieID);

        StreamingServiceDao streamingServiceDao = new StreamingServiceDaoImpl("database.properties");

        ArrayList<StreamingService> streamingServices = streamingServiceDao.getAllStreamingServicesByMovie(movieID2);
        model.addAttribute("streamingServices",streamingServices);

        return "streaming";

    }

}
