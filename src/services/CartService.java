package services;

import domain.models.Cart;
import java.util.List;
import java.util.UUID;
import repositories.CartRepository;

public class CartService {
    private final CartRepository repo;

    public CartService(CartRepository repo) {
        this.repo = repo;
    }

    public Cart create(Cart cart) {
        if (cart == null) {
            System.out.println("Cart cannot be null.");
            return null;
        }

        if (repo.getActiveCart(cart.getCustomerId()) != null) {
            System.out.println("Active Cart already exists");
            return cart;
        }

        if (repo.save(cart)) {
            System.out.println("Cart saved.");
        }
        else {
            System.out.println("Cart unsaved.");
        }

        return cart;
    }

    public Cart update(Cart cart) {
        if (cart == null) {
            System.out.println("Cart cannot be null.");
            return null;
        }

        if (repo.getCartById(cart.getId()) != null) {
            if (repo.update(cart)) {
                System.out.println("Cart updated.");
            }
            else {
                System.out.println("Cart not updated.");
            }
        }

        return cart;
    }

    public List<Cart> getCarts() {
        return repo.getCarts();
    }

    public Cart getActiveCart(UUID userId) {
        return repo.getActiveCart(userId);
    }

    public Cart getCartById(UUID id) {
        return repo.getCartById(id);
    }

    public List<Cart> getCartsByUserId(UUID userId) {
        return repo.getCartsByCustomerId(userId);
    }
}
