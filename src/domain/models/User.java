package domain.models;

import java.util.UUID;

public class User {
    private UUID id;
    private String fullName;
    private String email;
    private String password;

    public User() {
        id = UUID.randomUUID();
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
