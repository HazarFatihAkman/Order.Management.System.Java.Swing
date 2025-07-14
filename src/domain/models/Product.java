package domain.models;

import java.util.UUID;

public class Product {
    private UUID id;
    private String name;
    private String code;
    private int stock;
    private double taxExcPrice;
    private double taxIncPrice;
    private UUID taxId;
    private boolean isDeleted;

    public Product() {
        id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        if (id == null) {
            throw new NullPointerException("Id cannot be null.");
        }

        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.length() == 0 || name.isBlank() || name.isEmpty()) {
            throw new NullPointerException("Name cannot be null.");
        }

        this.name = name;
    }

    public void setTaxId(UUID taxId) {
        if (taxId == null) {
            throw new NullPointerException("Tax Id cannot be null.");
        }
        this.taxId = taxId;
    }

    public void setTaxExcPrice(double taxExcPrice) {
        if (taxExcPrice <= 0) {
            throw new NullPointerException("Price should be greater than 0.0");
        }

        this.taxExcPrice = taxExcPrice;
    }

    public void setTaxIncPrice(double taxRate) {
        if (taxRate <= 0) {
            throw new NullPointerException("Tax Rate should be greater than 0.0");
        }

        taxIncPrice = taxExcPrice * (taxRate / 100);
    }

    public void setIsDeleted(boolean isDeleted) {
        this. isDeleted = isDeleted;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTaxExcPrice() {
        return taxExcPrice;
    }

    public double getTaxIncPrice() {
        return taxIncPrice;
    }

    public UUID getTaxId() {
        return taxId;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }
}
