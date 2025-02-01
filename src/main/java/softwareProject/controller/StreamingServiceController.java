package softwareProject.controller;

import lombok.extern.slf4j.Slf4j;
import softwareProject.business.StreamingService;
import softwareProject.persistence.StreamingServiceDao;
import softwareProject.persistence.StreamingServiceDaoImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class StreamingServiceController {

    @GetMapping("/viewStreamingServices")
    public String viewAllStreamingServices(Model model) {
        StreamingServiceDao serviceDao = new StreamingServiceDaoImpl("database.properties");
        List<StreamingService> services = serviceDao.getAllStreamingServices();
        model.addAttribute("services", services);
        return "streamin_services";
    }
}
