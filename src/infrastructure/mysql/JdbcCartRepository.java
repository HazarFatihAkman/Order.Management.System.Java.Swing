package infrastructure.mysql;

import domain.models.Cart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import repositories.CartRepository;

public class JdbcCartRepository implements CartRepository {
    private final Connection conn;

    public JdbcCartRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Cart cart) {
        String sql = "INSERT INTO " + CartRepository.prefix + "(id, userId, products, price, isPaid) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cart.getId().toString());
            stmt.setString(2, cart.getUserId().toString());
            stmt.setString(3, cart.getProducts().toString());
            stmt.setDouble(4, cart.getPrice());
            stmt.setBoolean(5, cart.getIsPaid());

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
    public boolean update(Cart cart) {
        String sql = "UPDATE " + CartRepository.prefix + " SET products = ?, price = ?, isPaid = ? WHERE id = ? AND userId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cart.getProducts().toString());
            stmt.setDouble(2, cart.getPrice());
            stmt.setBoolean(3, cart.getIsPaid());
            stmt.setString(4, cart.getId().toString());
            stmt.setString(5, cart.getUserId().toString());

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
    public Cart getActiveCart(UUID userId) {
        String sql = "SELECT * FROM " + CartRepository.prefix + " WHERE userId = ? AND isPaid = false";
        Cart cart = new Cart();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cart = mapCart(rs);   
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cart;
    }

    @Override
    public Cart getCartById(UUID id) {
        Cart cart = new Cart();
        String sql = "SELECT * FROM " + CartRepository.prefix + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cart = mapCart(rs);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cart;
    }

    @Override
    public List<Cart> getCartsByUserId(UUID userId) {
        List<Cart> carts = new ArrayList<>();
        String sql = "SELECT * FROM " + CartRepository.prefix + " WHERE userId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                carts.add(mapCart(rs));
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return carts;
    }

    private List<UUID> mapProducts(String productStr) {
        return Arrays.stream(productStr.substring(1, productStr.length() - 1).split(","))
            .map(String::trim)
            .map(UUID::fromString)
            .collect(Collectors.toList());
    }

    private Cart mapCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();

        cart.setId(UUID.fromString(rs.getString("id")));
        cart.setUserId(UUID.fromString(rs.getString("userId")));
        cart.setProducts(mapProducts(rs.getString("products")));
        cart.setPrice(rs.getDouble("price"));
        cart.setIsPaid(rs.getBoolean("isPaid"));

        return cart;
    }
}
