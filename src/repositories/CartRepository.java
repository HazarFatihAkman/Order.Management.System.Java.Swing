package repositories;

import domain.models.Cart;
import java.util.List;
import java.util.UUID;

public interface CartRepository {
    String prefix = "carts";

    boolean save(Cart cart);
    boolean update(Cart cart);
    List<Cart> getCarts();
    Cart getActiveCart(UUID userId);
    Cart getCartById(UUID id);
    List<Cart> getCartsByCustomerId(UUID customerId);
}
