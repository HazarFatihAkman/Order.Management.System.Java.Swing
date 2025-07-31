package app;

import gui.LoginUI;
import infrastructure.Database;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        Database.initDatabase();
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
