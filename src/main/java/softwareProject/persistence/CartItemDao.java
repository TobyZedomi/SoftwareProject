package softwareProject.persistence;

import softwareProject.business.CartItem;

import java.util.ArrayList;

public interface CartItemDao {

    public int addCartItem(CartItem cartItem);

    public ArrayList<CartItem> getAllCartItemsByCartId(int cartId);

    public int deleteCartItemByCartIdAndMovieId(int cartId, int movieId);

    public int totalNumberOfCartItems(int cartId);

    public int deleteCartItemByCartId(int cartId);

    public CartItem getCartItemByIdAndMovieId(int cartId, int movieId);
}
