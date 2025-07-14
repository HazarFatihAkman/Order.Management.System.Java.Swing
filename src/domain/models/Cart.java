package domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
    private UUID id;
    private UUID userId;
    private List<UUID> products;
    private double price;
    private boolean isPaid;

    public Cart() {
        id = UUID.randomUUID();
        products = new ArrayList<>();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setProducts(List<UUID> products) {
        this.products = products;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<UUID> getProducts() {
        return products;
    }

    public double getPrice() {
        return price;
    }

    public boolean getIsPaid() {
        return isPaid;
    }
}
