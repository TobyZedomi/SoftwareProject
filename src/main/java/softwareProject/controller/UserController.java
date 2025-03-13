package softwareProject.controller;


import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softwareProject.business.Cart;
import softwareProject.business.MovieTest;
import softwareProject.business.User;
import softwareProject.persistence.*;
import org.springframework.ui.Model;
import softwareProject.service.EmailSenderService;
import softwareProject.service.MovieService;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static softwareProject.persistence.UserDaoImpl.hashPassword;


@Slf4j
@Controller
public class UserController {

    @Autowired
    private MovieService movieService;



    // register

    @PostMapping("registerUser")
    public String registerUser(
            @RequestParam(name="username") String username,
            @RequestParam(name="displayName") String displayName,
            @RequestParam(name="email") String email,
            @RequestParam(name="password") String password,
            @RequestParam(name="password2") String password2,
            @RequestParam(name="dateOfBirth") String dateOfBirth,
            Model model, HttpSession session) throws InvalidKeySpecException, NoSuchAlgorithmException {

        // VALIDATION


        // username validation

        if (username.isBlank()){
            String message = "Username was left blank";
            model.addAttribute("message", message);
            System.out.println("Username was left blank");

            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            return "user_indexSignUp";
        }

        Pattern usernameRegex = Pattern.compile("^[a-zA-Z]{3,25}$");
        Matcher match = usernameRegex.matcher(username);
        boolean matchfoundUsername = match.find();

        if (!matchfoundUsername){
            String message = "Username must be between 3-25 characters, letters only";
            model.addAttribute("message", message);
            System.out.println("Username must be between 3-25 characters, letters only");

            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);
            return "user_indexSignUp";
        }

        // displayName validation

        if (displayName.isBlank()){

            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Display Name was left blank");
            return "user_indexSignUp";
        }

        Pattern displayNameRegex = Pattern.compile("^[a-zA-Z0-9]{3,25}$");
        Matcher match2 = displayNameRegex.matcher(displayName);
        boolean matchfoundDisplayName= match2.find();

        if (!matchfoundDisplayName){
            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Display Name must be between 3-25 characters and only letters and numbers");
           return "user_indexSignUp";
        }

        // email validation

        if(email.isBlank()){
            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Email was left blank");
            return "user_indexSignUp";
        }

        // password validation

        if (password.isBlank()){
            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Password was left blank");

            return "user_indexSignUp";
        }

        Pattern passwordRegex = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,70}$");
        Matcher match1 = passwordRegex.matcher(password);
        boolean matchfoundPassword = match1.find();

        if (!matchfoundPassword){
            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Password must have at least 7 characters and maximum 70 characters, one uppercase letter, one lowercase letter and one number");
           return "user_indexSignUp";
        }

        if (password2.isBlank()){
            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Confirm password was left blank");
            return "user_indexSignUp";
        }

        if (!password.equals(password2)){
            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Passwords dont match");
            return "user_indexSignUp";
        }

        // dateOfBirth validation

        if (dateOfBirth.isBlank()){
            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Date of birth was left blank");
            return "user_indexSignUp";
        }

        LocalDate dob = LocalDate.parse(dateOfBirth);
        LocalDate today = LocalDate.now();

        if (!dob.isBefore(today.minusYears(12))){

            modelAttributeForRegister(username, displayName, email, password, password2, dateOfBirth, model);

            System.out.println("Date Of Birth has to be 12 years old or over");
            return "user_indexSignUp";
        }

        String view = "";
        String image = "DefaultUserImage.jpg";
        UserDao userDao = new UserDaoImpl("database.properties");

        User u = new User(username, displayName, email, password, dob, false, LocalDateTime.now(),image );
        int added = userDao.registerUser(u);
        if(added == 1){

            CartDao cartDao = new CartDaoImpl("database.properties");
            cartDao.addCart(new Cart(0,username));
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

    private static void modelAttributeForRegister(String username, String displayName, String email, String password, String password2, String dateOfBirth, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("displayName", displayName);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("password2", password2);
        model.addAttribute("dateOfBirth", dateOfBirth);
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
        User user = userDao.findUserByUsername(username1);


        if(user == null){
            String message = "No such username/password combination, try again....";
            model.addAttribute("message", message);
            log.info("Login failed with username {}", username1);
            return "loginFailed";
        }

        if (checkPassword(password1, user.getPassword()) == true && username1.equals(user.getUsername())) {

            session.setAttribute("loggedInUser", user);
            // between this line get list of movies from movie db
            // model .add attribute here for list of movies form movie db
            mostPopularMoviesMovieDbApi(model);
            log.info("User {} log into system", user.getUsername());

            // totalAmountOItems in basket
            getTotalAmountOfItemsInCart(session, model);

            return "loginSuccessful";
        }

        String message = "No such username/password combination, try again....";
        model.addAttribute("message", message);
        log.info("Login failed with username {}", username1);
        return "loginFailed";
    }

    private void mostPopularMoviesMovieDbApi(Model model) {
        List<MovieTest> movies = movieService.getMovies();

        // create new list to add the movies from the movie db into
        List<MovieTest> newMovie = new ArrayList<>();

        // loop through the movie db list and reduce the size by 2
        for (int i = 0; i < movies.size()-2;i++) {

            // if any backdrop image is unavailable it will not add it to the new arraylist
            if (movies.get(i).getBackdrop_path() != null) {
                // add the movies from the movie db into the new arraylist
                newMovie.add(movies.get(i));
                model.addAttribute("movies", newMovie);
            }
        }
    }


    ///logout

    /**
     * End the users session in the system
     * @param session is the session being searched to terminate
     * @return to user_index page to log in or register
     */

    @GetMapping("/logOut")
    public String logOut(HttpSession session, Model model){
        if (session.getAttribute("loggedInUser") != null) {
            User u = (User) session.getAttribute("loggedInUser");


            if (session != null) {
                session.invalidate();
                log.info("User {} logged out of system", u.getUsername());
            }

            String message = "You have logged out of the system "+ u.getUsername();
            model.addAttribute("messageLogOut", message);

            return "user_index";
        }

        return "notValidUser";
    }

    // totalAmount in Cart

    public void getTotalAmountOfItemsInCart(HttpSession session,Model model){

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }

    @PostMapping("/UserProfile")
    public String userProfile(HttpSession session, Model model){
        User u = (User) session.getAttribute("loggedInUser");

        model.addAttribute("User", u);
        return "UserProfile";
    }

    @PostMapping("/updateUserImage")
    public String updateUserImage(HttpSession session, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        String fileName = file.getOriginalFilename();
        file.transferTo(new File("C:\\Users\\andre\\Documents\\Year3\\WebPatterns\\SoftwareProject\\src\\main\\resources\\static\\css\\images" + fileName));

        UserDao userDao = new UserDaoImpl("database.properties");

        int complete = userDao.updateUserImage(loggedInUser.getUsername(),fileName);

        if(complete > 0){
            model.addAttribute("imageSuccess","Image has been updated");
            model.addAttribute("User", loggedInUser);
            return "UserProfile";
        }
        else{
            model.addAttribute("imageFailed", "Image failed to update");
            model.addAttribute("User", loggedInUser);
            return "UserProfile";
        }
    }

    @PostMapping("/updateDisplayName")
    public String updateDisplayName(HttpSession session, @RequestParam("name") String displayName, Model model){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        UserDao userDao = new UserDaoImpl("database.properties");

        int complete = userDao.updateDisplayName(loggedInUser.getUsername(),displayName);

        if(complete > 0){
            model.addAttribute("displayNameSuccess","Display name has been updated: "+displayName);
            model.addAttribute("User", loggedInUser);
            return "UserProfile";
        }
        else{
            model.addAttribute("displayNameFailed", "Display name failed to update");
            model.addAttribute("User", loggedInUser);
            return "UserProfile";
        }

    }


    @Autowired
    private EmailSenderService senderService;

    public void sendMail(String userEmail ) throws MessagingException, IOException {

        senderService.sendSetPasswordEmail(userEmail);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model, HttpSession session) throws MessagingException, IOException {
         forgetPassword(email,model, session);

         session.setAttribute("emailForUser", email);
         return "forgot_password";
    }


    public void forgetPassword(String email, Model model, HttpSession session) throws MessagingException, IOException {

        UserDao userDao = new UserDaoImpl("database.properties");

        String message;

        User user = userDao.findUserByThereEmail(email);

        if(user == null){

            message = "No user found with this email";
            model.addAttribute("noEmail",message);
            log.info("No user found with this email");
        }


        message = "Password has been sent";
        model.addAttribute("sentPassword", message);
        sendMail(email);


    }

    @GetMapping("/set-password")
    public String setPasswordR(@RequestParam(name="newPassword") String newPassword, Model model, HttpSession session) throws MessagingException, InvalidKeySpecException, NoSuchAlgorithmException {

        return setPassword( newPassword, model,session);
    }

    public String setPassword( String newPassword, Model model, HttpSession session) throws InvalidKeySpecException, NoSuchAlgorithmException {
        UserDao userDao = new UserDaoImpl("database.properties");

        Pattern passwordRegex = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,70}$");
        Matcher match1 = passwordRegex.matcher(newPassword);
        boolean matchfoundPassword = match1.find();

        if (!matchfoundPassword){
            model.addAttribute("incorrectPassword","Password must have at least 7 characters and maximum 70 characters, one uppercase letter, one lowercase letter and one number");
            log.info("Password must have at least 7 characters and maximum 70 characters, one uppercase letter, one lowercase letter and one number");
            return "reset_password";
        }

        if (newPassword.isBlank()){
            model.addAttribute("Password2","Re-enter password is left blank.Please try again");

            log.info("Confirm password was left blank");
            return "reset_password";
        }


        String emailUser = (String) session.getAttribute("emailForUser");

        int complete = userDao.updatePassword(emailUser,newPassword);

        if (complete>0) {
            return "reset_success";
        }else{
            return "reset_password";
        }
    }


    ////// bcrypt


    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return(password_verified);
    }


}
