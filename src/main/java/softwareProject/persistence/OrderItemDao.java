package softwareProject.persistence;

import softwareProject.business.OrderItem;

public interface OrderItemDao {

    public int addOrderItem(OrderItem orderItem);
}
