package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Cart;
import softwareProject.business.User;
import softwareProject.persistence.*;

import java.util.ArrayList;

@Controller
@Slf4j
public class AdminController {

    @GetMapping("/updateUserToAdmin")
    public String updateUserToAdminName(HttpSession session, @RequestParam("username") String username, Model model){

        UserDao userDao = new UserDaoImpl("database.properties");

        getTotalAmountOfItemsInCart(session, model);

            userDao.updateToAdmin(username);


            getTotalAmountOfItemsInCart(session, model);
            model.addAttribute("message","User " +username+ " has been updated to admin ");


            ArrayList<User> users = userDao.getAllUsersThatAreNotAdmin();

            model.addAttribute("users", users);
            return "updateToAdmin";

    }



    public void getTotalAmountOfItemsInCart(HttpSession session,Model model){

        /// get total number of items in cart for user

        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");

        int totalCartItems = cartItemDao.totalNumberOfCartItems(cart.getCart_id());
        model.addAttribute("totalCartItems", totalCartItems);
    }


}
