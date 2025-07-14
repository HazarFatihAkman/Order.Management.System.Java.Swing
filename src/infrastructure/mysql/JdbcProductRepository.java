package infrastructure.mysql;

import domain.models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import repositories.ProductRepository;

public class JdbcProductRepository implements ProductRepository {
    private final Connection conn;

    public JdbcProductRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) {
        String sql = "INSERT INTO " + ProductRepository.prefix + " (id, name, taxExcPrice, taxIncPrice, taxId, isDeleted) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getId().toString());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getTaxExcPrice());
            stmt.setDouble(4, product.getTaxIncPrice());
            stmt.setString(5, product.getTaxId().toString());
            stmt.setBoolean(6, product.getIsDeleted());

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
    public boolean update(Product product) {
        String sql = "UPDATE " + ProductRepository.prefix + " SET name = ?, taxExcPrice = ?, taxIncPrice = ?, taxId = ?, isDeleted = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(3, product.getTaxExcPrice());
            stmt.setDouble(4, product.getTaxIncPrice());
            stmt.setString(5, product.getTaxId().toString());
            stmt.setBoolean(6, product.getIsDeleted());
            stmt.setString(7, product.getId().toString());
            
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
    public boolean markIsDeleted(UUID id) {
        String sql = "UPDATE " + ProductRepository.prefix + " SET isDeleted = false WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
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
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + ProductRepository.prefix;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }

    @Override
    public Product getProductById(UUID id) {
        String sql = "SELECT * FROM " + ProductRepository.prefix + " WHERE id = ?";
        Product product = new Product();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                product = mapProduct(rs);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return product;
    }

    @Override
    public Product getProductByName(String name) {
        Product product = new Product();
        String sql = "SELECT * FROM " + ProductRepository.prefix + " WHERE name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                product = mapProduct(rs);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return product;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(UUID.fromString(rs.getString("id")));
        product.setName(rs.getString("name"));
        product.setTaxExcPrice(rs.getDouble("taxExcPrice"));
        product.setTaxIncPrice(rs.getDouble("taxIncPrice"));
        product.setTaxId(UUID.fromString(rs.getString("taxId")));
        product.setIsDeleted(rs.getBoolean("isDeleted"));
        return product;
    }
}
