package softwareProject.persistence;

import softwareProject.business.BillingAddress;

import java.util.ArrayList;

public interface BillingAddressDao {

    public int addBillingAddress(BillingAddress billingAddress);

    public ArrayList<BillingAddress> getAllBillingAddress();

    public BillingAddress getBillingAddressByUsername(String username);
}
