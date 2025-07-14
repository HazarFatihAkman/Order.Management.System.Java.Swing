package infrastructure.mysql;

import domain.models.Cart;
import domain.models.Customer;
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

public class JdbcCustomerRepository implements CustomerRepository {
    private final Connection conn;
    private final String selectAllQuery = "SELECT " +
                CustomerRepository.prefix + ".id AS id, " +
                CustomerRepository.prefix + ".fullName, " +
                CartRepository.prefix + ".id AS cartId, " +
                CartRepository.prefix + ".price, " +
                CartRepository.prefix + ".isPaid " +
            "FROM " + CustomerRepository.prefix +
            " LEFT JOIN " + CartRepository.prefix +
            " ON " + CustomerRepository.prefix + ".id = " + CartRepository.prefix + ".userId ";

    public JdbcCustomerRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Customer customer) {
        String sql = "INSERT INTO " + CustomerRepository.prefix + " (id, fullName, phoneNumber, address) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getId().toString());
            stmt.setString(2, customer.getFullName());
            stmt.setString(3, customer.getPhoneNumber());
            stmt.setString(4, customer.getAddress());

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
    public boolean update(Customer customer) {
        String sql = "UPDATE " + CustomerRepository.prefix + " SET fullName = ?, phoneNumber = ?, address = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhoneNumber());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getId().toString());

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
    public Customer getCustomerById(UUID id) {
        Customer customer = null;
        String sql = selectAllQuery + "WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (customer == null) {
                    customer = new Customer();
                    customer.setId(UUID.fromString(rs.getString("id")));
                    customer.setFullName(rs.getString("fullName"));
                }

                Cart cart = mapCart(rs);
                if (cart != null) {
                    customer.getCarts().add(cart);
                }
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        Customer customer = null;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllQuery)) {

            while (rs.next()) {
                if (customer == null && !customers.contains(UUID.fromString(rs.getString("id")))) {
                    customer = new Customer();
                    customer.setId(UUID.fromString(rs.getString("id")));
                    customer.setFullName(rs.getString("fullName"));
                }

                Cart cart = mapCart(rs);
                if (cart != null) {
                    customer.getCarts().add(cart);
                }

                customers.add(customer);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customers;
    }

    private Cart mapCart(ResultSet rs) throws SQLException {
        String cartId = rs.getString("cartId");
        if (cartId != null) {
            Cart cart = new Cart();
            cart.setId(UUID.fromString(cartId));
            cart.setPrice(rs.getDouble("price"));
            cart.setIsPaid(rs.getBoolean("isPaid"));
            return cart;
        }

        return null;
    }
}
