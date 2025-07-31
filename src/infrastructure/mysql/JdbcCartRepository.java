package infrastructure.mysql;

import domain.models.Cart;
import domain.models.Customer;
import extension.SelectedProductsExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import repositories.CartRepository;
import repositories.CustomerRepository;

public class JdbcCartRepository implements CartRepository {
    private final Connection conn;
    private final String selectAllQuery = "SELECT " +
            CartRepository.prefix + ".id AS id, " +
            CartRepository.prefix + ".customerId, " +
            CartRepository.prefix + ".products, " +
            CartRepository.prefix + ".price, " +
            CartRepository.prefix + ".isPaid, " +
            CustomerRepository.prefix + ".fullName, " +
            CustomerRepository.prefix + ".phoneNumber, " +
            CustomerRepository.prefix + ".address " +
        "From " + CartRepository.prefix +
        " LEFT JOIN " + CustomerRepository.prefix +
        " ON " + CartRepository.prefix + ".customerId = " + CustomerRepository.prefix + ".id";

    public JdbcCartRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Cart cart) {
        String sql = "INSERT INTO " + CartRepository.prefix + "(id, customerId, products, price, isPaid) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cart.getId().toString());
            stmt.setString(2, cart.getCustomerId().toString());
            stmt.setString(3, SelectedProductsExtension.toJson(cart.getProducts()));
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
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Cart cart) {
        String sql = "UPDATE " + CartRepository.prefix + " SET products = ?, price = ?, isPaid = ? WHERE " + CartRepository.prefix + ".id = ? AND customerId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, SelectedProductsExtension.toJson(cart.getProducts()));
            stmt.setDouble(2, cart.getPrice());
            stmt.setBoolean(3, cart.getIsPaid());
            stmt.setString(4, cart.getId().toString());
            stmt.setString(5, cart.getCustomerId().toString());

            int effectedRows = stmt.executeUpdate();
            stmt.close();
            return effectedRows == 1;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Cart> getCarts() {
        String sql = selectAllQuery;
        List<Cart> carts = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cart cart = mapCart(rs);
                cart.setCustomer(mapCustomer(rs));
                carts.add(cart);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return carts;
    }

    @Override
    public Cart getActiveCart(UUID customerId) {
        String sql = selectAllQuery + " WHERE customerId = ? AND isPaid = false";
        Cart cart = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cart = mapCart(rs);
                cart.setCustomer(mapCustomer(rs));
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return cart;
    }

    @Override
    public Cart getCartById(UUID id) {
        Cart cart = null;
        String sql = selectAllQuery + " WHERE " + CartRepository.prefix + ".id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cart = mapCart(rs);
                cart.setCustomer(mapCustomer(rs));
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return cart;
    }

    @Override
    public List<Cart> getCartsByCustomerId(UUID customerId) {
        List<Cart> carts = new ArrayList<>();
        String sql = selectAllQuery + " WHERE customerId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cart newCart = mapCart(rs);
                newCart.setCustomer(mapCustomer(rs));
                carts.add(newCart);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return carts;
    }

    private Cart mapCart(ResultSet rs) throws SQLException, Exception {
        Cart cart = new Cart();

        cart.setId(UUID.fromString(rs.getString("id")));
        cart.setCustomerId(UUID.fromString(rs.getString("customerId")));
        cart.setProducts(SelectedProductsExtension.fromJson(rs.getString("products")));
        cart.setPrice(rs.getDouble("price"));
        cart.setIsPaid(rs.getBoolean("isPaid"));

        return cart;
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();

        customer.setId(UUID.fromString(rs.getString("customerId")));
        customer.setFullName(rs.getString("fullName"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        customer.setAddress(rs.getString("address"));
        return customer;
    }
}
