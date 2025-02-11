package softwareProject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softwareProject.business.SubscriptionPlan;
import softwareProject.persistence.SubscriptionPlanDao;
import softwareProject.persistence.SubscriptionPlanDaoImpl;

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




    }

