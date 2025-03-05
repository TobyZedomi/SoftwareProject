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

@Slf4j
@Controller
public class FriendController {

    public static int getNumberOfFriends(HttpSession session, @RequestParam(name = "friendUsername") String friendUsername, Model model){

        FriendDao friendDao = new FriendDaoImpl("database.properties");

        int numberOfFriends = friendDao.getNumberOfFriends(friendUsername);

        model.addAttribute("numberOfFriends", numberOfFriends);
        log.info("Number of friends: " + numberOfFriends);

        return numberOfFriends;
    }

    public static ArrayList<Friends> getAllFriends(HttpSession session, @RequestParam(name = "friendUsername") String friendUsername, Model model){

        FriendDao friendDao = new FriendDaoImpl("database.properties");

        ArrayList<Friends> friends = friendDao.getAllFriends(friendUsername);

        log.info("Number of friends: " + friends.size());

        return friends;
    }

    @GetMapping("/searchForYourFriends")
    public String searchForYourFriend(HttpSession session,@RequestParam(name= "friendUsername") String friendUsername, Model model){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        FriendDao friendDao = new FriendDaoImpl("database.properties");
        UserDao userDao = new UserDaoImpl("database.properties");

        ArrayList<Friends> friends = friendDao.searchForFriends(loggedInUser.getUsername(),friendUsername);
        ArrayList<Friends> allFriends = new ArrayList<>();
        ArrayList<User> details = new ArrayList<>();

        if(friends.isEmpty()){
            model.addAttribute("Empty", "Sorry you have no friends with this username");
        }

        for(int i = 0; i< friends.size();i++){
            String name;
            if (friends.get(i).getFriend1().equals(loggedInUser.getUsername())) {
                name = friends.get(i).getFriend2();
            } else {
                name = friends.get(i).getFriend1();
            }
            User friendUser = userDao.findUserByUsername(name);
            allFriends = getAllFriends(session,friendUser.getUsername(),model);
            if(name != null){
                details.add(friendUser);
            }
        }

        model.addAttribute("total", allFriends.size());
        model.addAttribute("friends", details);

        return "friends";
    }

    @PostMapping("/deleteAFriend")
    public String deleteAFriend(HttpSession session,@RequestParam(name= "friendUsername") String friendUsername, Model model){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        FriendDao friendDao = new FriendDaoImpl("database.properties");

        int deleted = friendDao.deleteAFriend(loggedInUser.getUsername(),friendUsername);

        if(deleted > 0 ){
            model.addAttribute("deleted", "You are no longer friends with "+friendUsername);
            log.info("You are no longer friends with "+friendUsername);
        }else{
            model.addAttribute("failedDelete","Failed to remove "+friendUsername);
            log.info("Failed to remove "+friendUsername);
        }

        return "friends";
    }


}
