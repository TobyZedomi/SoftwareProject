package softwareProject.controller;



import softwareProject.business.User;
import softwareProject.persistence.UserDao;
import softwareProject.persistence.UserDaoImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FrontController {

    @GetMapping("/controller")
    public String processRequest(@RequestParam(name="username", required=false, defaultValue="Toby") String username, Model model) {


        UserDao userDao = new UserDaoImpl("database.properties");
        User u = userDao.findUserByUsername(username);
        model.addAttribute("user", u);
        System.out.println(u);

        return "greeting";
    }
}


