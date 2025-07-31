package domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
    private UUID id;
    private UUID customerId;
    private List<Order> products; // id | quantity
    private double price;
    private boolean isPaid;
    private Customer customer;

    public Cart() {
        id = UUID.randomUUID();
        products = new ArrayList<>();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public List<Order> getProducts() {
        return products;
    }

    public double getPrice() {
        return price;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public Customer getCustomer() {
        return customer;
    }
}
