package softwareProject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Friends;
import softwareProject.business.User;
import softwareProject.persistence.FriendDao;
import softwareProject.persistence.FriendDaoImpl;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class NotificationsController {

    @PostMapping("/pendingRequests")
    public String pendingRequests(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        FriendDao friendDao = new FriendDaoImpl("database.properties");
        ArrayList<Friends> result = friendDao.getAllRequests(loggedInUser.getUsername());

        if (result == null) {
            result = new ArrayList<>();
        }

        model.addAttribute("pendingRequests", result);
        return "notifications";


    }
    @PostMapping("/acceptFriendRequest")
    public String acceptFriendRequest(HttpSession session, @RequestParam(name = "fromUsername") String fromUsername, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        FriendDao friendDao = new FriendDaoImpl("database.properties");
        int result = friendDao.acceptRequest(loggedInUser.getUsername(),fromUsername);

        if (result > 0) {
            model.addAttribute("success", "Friend request accepted!");
            log.info("Friend request accepted between: "+loggedInUser.getUsername()+ " and " +fromUsername);
        } else {
            model.addAttribute("error", "Unable to accept friend request.");
            log.info("Friend request unable to be accepted between: "+loggedInUser.getUsername()+ " and " +fromUsername);
        }

        return "notifications";
    }

    @PostMapping("/declineFriendRequest")
    public String declineFriendRequest(HttpSession session, @RequestParam(name = "fromUsername") String fromUsername, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        FriendDao friendDao = new FriendDaoImpl("database.properties");
        int result = friendDao.declineRequest(loggedInUser.getUsername(),fromUsername);

        if (result > 0) {
            model.addAttribute("success", "Friend request declined!");
            log.info("Friend request declined between: "+loggedInUser.getUsername()+ " and " +fromUsername);
        } else {
            model.addAttribute("error", "Unable to delcine friend request.");
            log.info("Friend request unable to be decline between: "+loggedInUser.getUsername()+ " and " +fromUsername);
        }

        return "notifications";
    }



}
