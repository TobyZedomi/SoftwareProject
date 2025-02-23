package softwareProject.persistence;

import softwareProject.business.CartItem;
import softwareProject.business.MovieProduct;

import java.util.ArrayList;

public interface CartItemDao {

    public int addCartItem(CartItem cartItem);

    public ArrayList<CartItem> getAllCartItemsByCartId(int cartId);

    public int deleteCartItem(int cartId, int movieId);

    public int totalNumberOfCartItems(int cartId);

    public int deleteCartItem(int cartId);
}
