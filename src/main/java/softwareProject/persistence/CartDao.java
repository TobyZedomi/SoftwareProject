package softwareProject.persistence;

import softwareProject.business.Cart;

import java.util.ArrayList;

public interface CartDao {

    public int addCart(Cart cart);

    public ArrayList<Cart> getAllCarts();

    public Cart getCartByUsername(String username);
}
