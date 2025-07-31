package infrastructure.mysql;

import domain.models.Tax;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import repositories.TaxRepository;

public class JdbcTaxRepository implements TaxRepository {
    private final Connection conn;

    public JdbcTaxRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Tax tax) {
        String sql = "INSERT INTO " + TaxRepository.prefix + " (id, name, rate) VALUES (?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tax.getId().toString());
            stmt.setString(2, tax.getName());
            stmt.setDouble(3, tax.getRate());

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
    public boolean update(Tax tax) {
        String sql = "UPDATE " + TaxRepository.prefix + " SET name = ?, rate = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tax.getName());
            stmt.setDouble(2, tax.getRate());
            stmt.setString(3, tax.getId().toString());

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
    public List<Tax> getTaxes() {
        List<Tax> taxes = null;
        String sql = "SELECT * FROM " + TaxRepository.prefix;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            taxes = new ArrayList<>();
            while (rs.next()) {
                taxes.add(mapTax(rs));
            }

            stmt.close();
            return taxes;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Tax getTaxByName(String name) {
        String sql = "SELECT * From " + TaxRepository.prefix + " WHERE name = ?";
        Tax tax = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tax = mapTax(rs);
            }

            stmt.close();
            return tax;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Tax getTaxById(UUID id) {
        String sql = "SELECT * From " + TaxRepository.prefix + " WHERE id = ?";
        Tax tax = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tax = mapTax(rs);
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tax;
    }

    private Tax mapTax(ResultSet rs) throws SQLException {
        Tax tax = new Tax();
        tax.setId(UUID.fromString(rs.getString("id")));
        tax.setName(rs.getString("name"));
        tax.setRate(rs.getDouble("rate"));
        return tax;
    }
}
