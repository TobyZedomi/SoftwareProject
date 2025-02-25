package softwareProject.persistence;

import softwareProject.business.OrderItem;

import java.util.ArrayList;

public interface OrderItemDao {

    public int addOrderItem(OrderItem orderItem);

    public ArrayList<OrderItem> getAllOrderItems();
}
