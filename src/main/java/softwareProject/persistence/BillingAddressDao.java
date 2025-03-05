package softwareProject.persistence;

import softwareProject.business.BillingAddress;

import java.util.ArrayList;

public interface BillingAddressDao {

    public int addBillingAddress(BillingAddress billingAddress);

    public ArrayList<BillingAddress> getAllBillingAddress();

    public BillingAddress getBillingAddressByUsername(String username);

    public BillingAddress getBillingAddressById(int id);

    public int updateBillingAddressFullName(String fullName, int billingId);

    public int updateAddressForBillingAddress(String address, int billingId);

    public int updateBillingAddressCity(String city, int billingId);

    public int updateBillingAddressCounty(String county, int billingId);

    public int updateBillingAddressPostCode(String postCode, int billingId);

    public int updateBillingAddressEmail(String email, int billingId);
}
