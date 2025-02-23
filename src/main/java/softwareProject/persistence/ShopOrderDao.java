package softwareProject.persistence;

import softwareProject.business.ShopOrder;

public interface ShopOrderDao {

    public int addShopOrder(ShopOrder shopOrder);

    public ShopOrder getOrderWithTheHighestOrderIdByUsername(String username);
}
