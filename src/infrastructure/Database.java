package infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Database instance = null;
    private Connection conn = null;

    private static final String url = "jdbc:mysql://localhost:3306/Customer.Tracker";
    private static final String user = "admin";
    private static final String password = "admin123";
    private static final String createTableFormat = "CREATE TABLE IF NOT EXISTS %s ( %s );";

    public Database() {
        try {
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public static Connection getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new Database();
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return instance.getConnection();
    }

    public static void initDatabase() {
        try {
            Statement stmt = getInstance().createStatement();

            String taxsTable = String
                .format(createTableFormat,
                "taxes",
                    "id VARCHAR(36) NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL UNIQUE, " +
                    "rate DOUBLE NOT NULL");

            createTable("Tax", taxsTable, stmt);

            String productsTable = String
                .format(createTableFormat,
                    "products",
                    "id VARCHAR(36) NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL UNIQUE, " +
                    "taxExcPrice DOUBLE NOT NULL, " +
                    "taxIncPrice DOUBLE NOT NULL, " +
                    "taxId VARCHAR(36) NOT NULL, " +
                    "isDeleted BOOLEAN DEFAULT(false)");

            createTable("Product", productsTable, stmt);

            String cartsTable = String
                .format(createTableFormat, 
                    "carts",
                    "id VARCHAR(36) NOT NULL PRIMARY KEY, " +
                    "userId VARCHAR(36) NOT NULL, " +
                    "products TEXT, " + 
                    "price DOUBLE DEFAULT(0), " +
                    "isPaid BOOLEAN DEFAULT(false)");

            createTable("Cart", cartsTable, stmt);

            String usersTable = String
                .format(createTableFormat,
                        "users",
                        "id VARCHAR(36) NOT NULL PRIMARY KEY, " +
                        "fullName TEXT NOT NULL, " +
                        "email TEXT NOT NULL, " +
                        "password TEXT NOT NULL");

            createTable("Users", usersTable, stmt);

            String customersTable = String
                .format(createTableFormat,
                    "customers",
                    "id VARCHAR(36) NOT NULL PRIMARY KEY, " +
                    "fullName TEXT NOT NULL, ",
                    "phoneNumber VARCHAR(17) NOT NULL UNIQUE, " +
                    "address TEXT NOT NULL");

            createTable("Customers", customersTable, stmt);

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createTable(String tableName, String query, Statement stmt) throws SQLException {
            stmt.execute(query);
            System.out.println(tableName + " Table Created.");
    }
}
