package domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private List<Cart> carts;

    public Customer() {
        carts = new ArrayList<>();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.length() == 0 || fullName.isBlank() || fullName.isEmpty()) {
            throw new NullPointerException("Full name cannot be null.");
        }

        this.fullName = fullName;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}
