package softwareProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String userIndex(){
        return "user_index";
    }

    @GetMapping("/user_indexSignUp")
    public String userIndexSignUp(){
        return "user_indexSignUp";
    }

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/movie_index")
    public String movieIndex(){
        return "movie_index";
    }

    @GetMapping("/logout_index")
    public String userIndex2(){
        return "logout_index";
    }


    @GetMapping("/registerSuccessUser")
    public String regIndex(){
        return "registerSuccessUser";
    }


}
