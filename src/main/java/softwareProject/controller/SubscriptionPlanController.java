package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.Cart;
import softwareProject.business.Subscription;
import softwareProject.business.SubscriptionPlan;
import softwareProject.business.User;
import softwareProject.persistence.*;

import java.util.ArrayList;

@Slf4j
@Controller
public class SubscriptionPlanController {

    /**
     * Choosing subscription plan
     * @param session holds the logged in users details
     * @param subscriptionPlanId is the subscription plan being picked
     * @param model holds the attributes for the view
     * @return
     */

    @GetMapping("SubPlanStandard")
    public String addSubscriptionForUser(HttpSession session,
                                        @RequestParam(name = "subscriptionPlanId") String subscriptionPlanId,
                                        Model model) {


        int subPlanId = Integer.parseInt(subscriptionPlanId);

        SubscriptionPlanDao subscriptionPlanDao = new SubscriptionPlanDaoImpl("database.properties");
        SubscriptionPlan subscriptionPlan = subscriptionPlanDao.getSubscriptionPlanById(subPlanId);

        model.addAttribute("subscriptionPlan",subscriptionPlan);
        session.setAttribute("subscriptionPicked", subscriptionPlan);

        getTotalAmountOfItemsInCart(session,model);
        return "purchaseSubscription";


    }


    /**
     * Choosing susbcription plan if they skipped it upon registration. If they already have s a subscription they will be notified of this
     * @param session holds the logged in users details
     * @param subscriptionPlanId is the subscription plan to be picked
     * @param model holds the attributes for the view
     * @return subscription index if already purchased but go to checkout page for subscription if they choose to purchase subscription
     */

    @GetMapping("SubPlanStandardPart2")
    public String addSubscriptionForUserPart2(HttpSession session,
                                         @RequestParam(name = "subscriptionPlanId") String subscriptionPlanId,
                                         Model model) {


        int subPlanId = Integer.parseInt(subscriptionPlanId);

        // totalAmountOItems in basket
        getTotalAmountOfItemsInCart(session,model);

        User user = (User) session.getAttribute("loggedInUser");

        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl("database.properties");

        ArrayList<Subscription> subscriptions = subscriptionDao.getAllSubscriptions();

        for (int i = 0; i < subscriptions.size();i++){

            if (subscriptions.get(i).getUsername().equals(user.getUsername())){

                String message = "You already have a subscription with us";
                model.addAttribute("message",message);

                log.info("User {} already has a subscription with us", user.getUsername());

                return "subscription_index";
            }


        }


        SubscriptionPlanDao subscriptionPlanDao = new SubscriptionPlanDaoImpl("database.properties");
        SubscriptionPlan subscriptionPlan = subscriptionPlanDao.getSubscriptionPlanById(subPlanId);

        model.addAttribute("subscriptionPlan",subscriptionPlan);
        session.setAttribute("subscriptionPicked", subscriptionPlan);
        return "purchaseSubscriptionPart2";


    }


    /**
     * Gets the total amount of items in the cart
     * @param session holds the logged in users details
     * @param model holds the attributes for the view
     */

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

