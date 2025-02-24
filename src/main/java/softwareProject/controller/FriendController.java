package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.User;
import softwareProject.persistence.FriendDao;
import softwareProject.persistence.FriendDaoImpl;
import softwareProject.persistence.UserDao;
import softwareProject.persistence.UserDaoImpl;
@Controller

public class FriendController {
    @PostMapping("/addFriend")
    public String addFriend(HttpSession session, @RequestParam(name = "friendUsername") String friendUsername, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        UserDao userDao = new UserDaoImpl("database.properties");
        FriendDao friendDao = new FriendDaoImpl("database.properties");
        User friend = userDao.findUserByUsername(friendUsername);

        if(friendDao.getAFriend(loggedInUser.getUsername(),friendUsername)){
            model.addAttribute("error","You have already sent a request to this user or are friends with them");
            return "friends";
        }

        if (friend != null) {

            int result = friendDao.insertIntoFriend(loggedInUser.getUsername(), friendUsername);

            if (result > 0) {
                model.addAttribute("success", "Friend request has been sent");
            } else{
                model.addAttribute("error", "Unable to send friend request. Please try again");
            }
        }
        return "friends";
    }
}
