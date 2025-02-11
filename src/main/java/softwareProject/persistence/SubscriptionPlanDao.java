package softwareProject.persistence;

import softwareProject.business.SubscriptionPlan;

public interface SubscriptionPlanDao {

    public SubscriptionPlan getSubscriptionPlanById(int subscriptionPlanID);
}
