package softwareProject.persistence;

import softwareProject.business.Subscription;

import java.util.ArrayList;

public interface SubscriptionDao {

    public int addSubscription(Subscription subscription);

    public Subscription getSubscriptionFromUsername(String username);

    public ArrayList<Subscription> getAllSubscriptions();
}
