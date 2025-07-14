package infrastructure.mysql;

import domain.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import repositories.UserRepository;

public class JdbcUserRepository implements UserRepository {
    private final Connection conn;

    public JdbcUserRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO " + UserRepository.prefix + " (id, fullName, email, password) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());

            stmt.executeUpdate();
            stmt.close();
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE " + UserRepository.prefix + " SET fullName = ?, email = ?, password = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getId().toString());

            int effectedRows = stmt.executeUpdate();
            stmt.close();
            return effectedRows == 1;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public User getUserById(UUID id) {
        User user = new User();
        String sql = "SELECT * FROM " + UserRepository.prefix + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapUser(rs);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    public User login(String email, String password) {
        String sql = "SELECT * FROM " + UserRepository.prefix + " WHERE email = ? AND password = ?";
        User user = new User();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapUser(rs);
            }
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setFullName(rs.getString("fullName"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    }
}
