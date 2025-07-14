package services;

import domain.models.User;
import domain.repositories.UserRepository;
import java.util.UUID;

public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User create(User user) {
        if (user == null) {
            System.out.println("User cannot be null.");
            return null;
        }

        if (repo.save(user)) {
            System.out.println("User saved.");
        }
        else {
            System.out.println("User unsaved.");
        }

        return user;
    }

    public User update(User user) {
        if (user == null) {
            System.out.println("Product cannot be null.");
            return null;
        }

        if (repo.getUserById(user.getId()) != null) {
            if (repo.update(user)) {
                System.out.println("User updated.");
            }
            else {
                System.out.println("User not updated.");
            }
        }

        return user;
    }

    public User getUserById(UUID id) {
        return repo.getUserById(id);
    }

    public User login(String email, String password) {
        return repo.login(email, password);
    }
}
