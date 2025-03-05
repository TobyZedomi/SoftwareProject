package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Friends;
import softwareProject.business.User;
import softwareProject.persistence.FriendDao;
import softwareProject.persistence.FriendDaoImpl;
import softwareProject.persistence.UserDao;
import softwareProject.persistence.UserDaoImpl;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class AddFriendController {

    @GetMapping("/searchForFriends")
    public String user(HttpSession session,@RequestParam(name="username") String username, Model model) {

        User u = (User) session.getAttribute("loggedInUser");
        UserDao userDao = new UserDaoImpl("database.properties");

        List<User> found = userDao.findUserByUsername2(username);
        ArrayList<Integer> numberOfFriends = new ArrayList<>();
        if (found != null) {
            for(User f : found){
                int number = FriendController.getNumberOfFriends(session,f.getUsername(),model);
                numberOfFriends.add(number);
            }
            model.addAttribute("username", found);
            model.addAttribute("numberOfFriends", numberOfFriends);
            log.info("User found" + username);
            return "addFriends";
        } else {
            log.info("User not found: " + username);
            model.addAttribute("error", "User not found");
            return "addFriends";
        }

    }
    @PostMapping("/addFriend")
    public String addFriend(HttpSession session, @RequestParam(name = "friendUsername") String friendUsername, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        UserDao userDao = new UserDaoImpl("database.properties");
        FriendDao friendDao = new FriendDaoImpl("database.properties");
        User friend = userDao.findUserByUsername(friendUsername);

        if (friendDao.getAFriend(loggedInUser.getUsername(), friendUsername)) {
            model.addAttribute("error", "You have already sent a request to this user or are friends with them");
            return "addFriends";
        }

        if (friend != null) {

            int result = friendDao.insertIntoFriend(loggedInUser.getUsername(), friendUsername);

            if (result > 0) {
                model.addAttribute("success", "Friend request has been sent");
            } else {
                model.addAttribute("error", "Unable to send friend request. Please try again");
            }
        }
        return "addFriends";
    }

    @PostMapping("/getAllFriendsForAddPage")
    public String getAllFriendsForAddPage(HttpSession session, @RequestParam(name = "friendUsername") String friendUsername, Model model) {

        FriendDao friendDao = new FriendDaoImpl("database.properties");

        ArrayList<Friends> friends = friendDao.getAllFriends(friendUsername);

        model.addAttribute("friends", friends);
        model.addAttribute("friendSize", friends.size());
        log.info("Number of friends: " + friends.size());

        return "addFriends";
    }
}
