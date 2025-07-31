package domain.models;

import java.util.UUID;

public class Order {
    private UUID productId;
    private int quantity;

    public Order() { }

    public Order(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }
}
