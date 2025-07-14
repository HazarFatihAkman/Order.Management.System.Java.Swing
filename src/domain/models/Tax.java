package domain.models;

import java.util.UUID;

public class Tax {
    private UUID id;
    private String name;
    private double rate;

    public Tax() {
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

    public void setRate(double rate) {
        if (rate <= 0) {
            throw new NullPointerException("Rate should be greater than 0.0");
        }
        this.rate = rate;
    }
    
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }
}
