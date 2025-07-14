package repositories;

import domain.models.User;
import java.util.UUID;

public interface UserRepository {
    String prefix = "users";

    boolean save(User user);
    boolean update(User user);
    User getUserById(UUID id);
    User login(String email, String password);
}
