import infrastructure.Database;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            Database db = new Database();
            db.initDatabase();

            Connection conn = db.connect();
            conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
