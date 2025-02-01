package softwareProject.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softwareProject.business.User;
import softwareProject.persistence.UserDao;
import softwareProject.persistence.UserDaoImpl;
import org.springframework.ui.Model;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Controller
public class UserController {


    // register

    @PostMapping("registerUser")
    public String registerUser(
            @RequestParam(name="username") String username,
            @RequestParam(name="displayName") String displayName,
            @RequestParam(name="email") String email,
            @RequestParam(name="password") String password,
            @RequestParam(name="password2") String password2,
            @RequestParam(name="address") String address,
            @RequestParam(name="dateOfBirth") String dateOfBirth,
            Model model, HttpSession session) throws InvalidKeySpecException, NoSuchAlgorithmException {

        // VALIDATION


        // username validation

        if (username.isBlank()){
            String message = "Username was left blank";
            model.addAttribute("message", message);
            System.out.println("Username was left blank");
            return "user_indexSignUp";
        }

        Pattern usernameRegex = Pattern.compile("^[a-zA-Z]{3,25}$");
        Matcher match = usernameRegex.matcher(username);
        boolean matchfoundUsername = match.find();

        if (!matchfoundUsername){
            String message = "Username must be between 3-25 characters, letters only";
            model.addAttribute("message", message);
            System.out.println("Username must be between 3-25 characters, letters only");
          return "user_indexSignUp";
        }

        // displayName validation

        if (displayName.isBlank()){
            String message2 = "Display Name was left blank";
            model.addAttribute("message2", message2);

            System.out.println("Display Name was left blank");
            return "user_indexSignUp";
        }

        Pattern displayNameRegex = Pattern.compile("^[a-zA-Z0-9]{3,25}$");
        Matcher match2 = displayNameRegex.matcher(displayName);
        boolean matchfoundDisplayName= match2.find();

        if (!matchfoundDisplayName){
            String message2 = "Display Name must be between 3-25 characters and only letters and numbers";
            model.addAttribute("message2", message2);
            System.out.println("Display Name must be between 3-25 characters and only letters and numbers");
           return "user_indexSignUp";
        }

        // email validation

        if(email.isBlank()){
            String message3 = "Email was left blank";
            model.addAttribute("message3", message3);
            System.out.println("Email was left blank");
            return "user_indexSignUp";
        }

        // password validation

        if (password.isBlank()){
            String message4 = "Password was left blank";
            model.addAttribute("message4", message4);
            System.out.println("Password was left blank");

            return "user_indexSignUp";
        }

        Pattern passwordRegex = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,70}$");
        Matcher match1 = passwordRegex.matcher(password);
        boolean matchfoundPassword = match1.find();

        if (!matchfoundPassword){
            String message4 = "Password didnt have at least 7-70 characters, one uppercase letter, one lowercase letter, one number and one special character";
            model.addAttribute("message4", message4);
            System.out.println("Password must have at least 7 characters and maximum 70 characters, one uppercase letter, one lowercase letter and one number");
           return "user_indexSignUp";
        }

        if (password2.isBlank()){
            String message5 = "Confirm Password was left blank";
            model.addAttribute("message5", message5);
            System.out.println("Confirm password was left blank");
            return "user_indexSignUp";
        }

        if (!password.equals(password2)){
            String message5 = "Password and Confirm Password didnt match";
            model.addAttribute("message5", message5);
            System.out.println("Passwords dont match");
            return "user_indexSignUp";
        }

        // address validation

        if (address.isBlank()){
            String message6 = "Address was left blank";
            model.addAttribute("message6", message6);
            System.out.println("Address was left blank");
            return "user_indexSignUp";
        }

        // dateOfBirth validation

        if (dateOfBirth.isBlank()){
            String message7 = "Date Of Birth was left blank";
            model.addAttribute("message7", message7);
            System.out.println("Date of birth was left blank");
            return "user_indexSignUp";
        }

        LocalDate dob = LocalDate.parse(dateOfBirth);
        LocalDate today = LocalDate.now();

        if (!dob.isBefore(today.minusYears(12))){
            String message7 = "Date Of Birth has to be 12 years old or over";
            model.addAttribute("message7", message7);
            System.out.println("Date Of Birth has to be 12 years old or over");
            return "user_indexSignUp";
        }

        String view = "";
        UserDao userDao = new UserDaoImpl("database.properties");
        User u = new User(username, displayName, email, hashPassword(password), address, dob, false, LocalDateTime.now());
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

            System.out.println("Username or password was left blank");
            return "user_index";
        }

        UserDao userDao = new UserDaoImpl("database.properties");
        User user = userDao.login(username1, hashPassword(password1));


        if(user == null){
            String message = "No such username/password combination, try again....";
            model.addAttribute("message", message);
            log.info("Login failed with username {}", username1);
            return "loginFailed";
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
        if (session.getAttribute("loggedInUser") != null) {
            User u = (User) session.getAttribute("loggedInUser");


            if (session != null) {
                session.invalidate();
                log.info("User {} logged out of system", u.getUsername());
            }

            return "user_index";
        }

        return "error";
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
