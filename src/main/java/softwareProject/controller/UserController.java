package softwareProject.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.User;
import softwareProject.persistence.UserDao;
import softwareProject.persistence.UserDaoImpl;
import org.springframework.ui.Model;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Base64;


@Slf4j
@Controller
public class UserController {


    // register

    @PostMapping("registerUser")
    public String registerUser(
            @RequestParam(name="username") String username,
            @RequestParam(name="email") String email,
            @RequestParam(name="password") String password,
            @RequestParam(name="password2") String password2,
            @RequestParam(name="address") String address,
            Model model, HttpSession session) throws InvalidKeySpecException, NoSuchAlgorithmException {

        // VALIDATION


        if(password.length() < 7){
            log.info("Registration failed with username {}", username);
            System.out.println("Password must be latest 7 characters");
        }
        else if(password.length() >= 30){
            log.info("Registration failed with username {}", username);
            System.out.println("Password cant be 30 characters or more");
        }
        else if (password != password2){
            log.info("Registration failed with username {}", username);
            System.out.println("Password doesnt match");
        }

        String view = "";
        UserDao userDao = new UserDaoImpl("database.properties");
        User u = new User(username,email, hashPassword(password), address, false);
        int added = userDao.registerUser(u);
        if(added == 1){
            view = "registerSuccess";
            model.addAttribute("registeredUser", u);
            session.setAttribute("loggedInUser", u);
            log.info("User {} registered", u.getUsername());
        }else{
            view = "registerFailed";
            log.info("Registration failed with username {}", username);
        }


        return view;
    }


    /**
     * This method is used to log in the user to the system and if the user doesn't have a subscription or subscription has expired they wont be able to login
     * @param username1 is the username being searched
     * @param password1 is the password being searched
     * @param model stores data of the message to display no such username/password
     * @param session holds the users information if the login is a success
     * @return loginFailed page if the login isn't successful, addSubscription page if the user doesn't have a subscription with us, renewSubscription page if user subscription is ended and needs a renewal or loginSuccessful page to the system if users credentials match
     * @throws InvalidKeySpecException if something goes wrong with hashing password
     * @throws NoSuchAlgorithmException if something goes wrong with hashing password
     */
    @PostMapping("/login")
    public String loginUser(
            @RequestParam(name="username1")String username1,
            @RequestParam(name="password1") String password1,
            Model model, HttpSession session) throws InvalidKeySpecException, NoSuchAlgorithmException {

        if(username1.isBlank() || password1.isBlank()){
           // String message = "You must enter a valid username and password to login";
            //model.addAttribute("message", message);

            log.info("Didnt enter any username or password");
            return "user_index";
        }

        UserDao userDao = new UserDaoImpl("database.properties");
        User user = userDao.login(username1, hashPassword(password1));


        if(user == null){
            String message = "No such username/password combination, try again....";
            model.addAttribute("message", message);
            log.info("Login failed with username {}", username1);
            return "user_index";
        }

        session.setAttribute("loggedInUser", user);
        log.info("User {} log into system", user.getUsername());
        return "loginSuccessful";
    }



    ///logout

    /**
     * End the users session in the system
     * @param session is the session being searched to terminate
     * @return to user_index page to log in or register
     */

    @GetMapping("/logOut")
    public String logOut(HttpSession session){

        if (session != null) {
            session.invalidate();
        }

        return "user_index";
    }


    // hash password

    /**
     * Hashing the password
     * @param password is being searched to hash
     * @return the hashed password
     * @throws InvalidKeySpecException if something goes wrong with hashing password
     * @throws NoSuchAlgorithmException if something goes wrong with hashing password
     */
    public String hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        char[]passwordChars = password.toCharArray();
        byte [] saltBytes = "NotSoSecretSalt".getBytes();
        int iterations = 65536;
        int keySize = 256;

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

        PBEKeySpec spec = new PBEKeySpec(passwordChars,saltBytes,iterations,keySize);

        SecretKey key = factory.generateSecret(spec);

        String keyAsString = Base64.getEncoder().encodeToString(key.getEncoded());

        return keyAsString;

    }
}
