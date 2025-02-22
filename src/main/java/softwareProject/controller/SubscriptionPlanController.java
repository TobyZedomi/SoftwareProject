package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Cart;
import softwareProject.business.SubscriptionPlan;
import softwareProject.business.User;
import softwareProject.persistence.*;

@Slf4j
@Controller
public class SubscriptionPlanController {

    @GetMapping("SubPlanStandard")
    public String addSubscriptionForUser(HttpSession session,
                                        @RequestParam(name = "subscriptionPlanId") String subscriptionPlanId,
                                        Model model) {


        int subPlanId = Integer.parseInt(subscriptionPlanId);

        SubscriptionPlanDao subscriptionPlanDao = new SubscriptionPlanDaoImpl("database.properties");
        SubscriptionPlan subscriptionPlan = subscriptionPlanDao.getSubscriptionPlanById(subPlanId);

        model.addAttribute("subscriptionPlan",subscriptionPlan);
        session.setAttribute("subscriptionPicked", subscriptionPlan);
        return "purchaseSubscription";


    }


    @GetMapping("SubPlanStandardPart2")
    public String addSubscriptionForUserPart2(HttpSession session,
                                         @RequestParam(name = "subscriptionPlanId") String subscriptionPlanId,
                                         Model model) {


        int subPlanId = Integer.parseInt(subscriptionPlanId);

        // totalAmountOItems in basket
        getTotalAmountOfItemsInCart(session,model);


        SubscriptionPlanDao subscriptionPlanDao = new SubscriptionPlanDaoImpl("database.properties");
        SubscriptionPlan subscriptionPlan = subscriptionPlanDao.getSubscriptionPlanById(subPlanId);

        model.addAttribute("subscriptionPlan",subscriptionPlan);
        session.setAttribute("subscriptionPicked", subscriptionPlan);
        return "purchaseSubscriptionPart2";


    }


    // total amount of items in cart
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

