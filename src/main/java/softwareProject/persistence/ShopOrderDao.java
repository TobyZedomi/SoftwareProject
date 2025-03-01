package softwareProject.persistence;

import softwareProject.business.ShopOrder;

import java.util.ArrayList;

public interface ShopOrderDao {

    public int addShopOrder(ShopOrder shopOrder);

    public ShopOrder getOrderWithTheHighestOrderIdByUsername(String username);

    public ArrayList<ShopOrder> getAllShopOrdersByUsername(String username);

    public ShopOrder getShopOrderById(int id);
}
