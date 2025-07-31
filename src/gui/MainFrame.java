package gui;

import domain.models.User;
import gui.components.NavButton;
import java.awt.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
public class MainFrame extends JFrame {
    protected GridBagConstraints gbc;
    protected JButton backBtn;
    protected final JButton quitBtn;
    protected final JButton logoutBtn;
    protected final JPanel panel = new JPanel(new GridBagLayout());
    protected final JLabel informationLabel = new JLabel("Double click on the row for editing.");

    public MainFrame(String title, User user) {
        setTitle(title);
        setSize(1280, 720);
        setBackground(Color.LIGHT_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        quitBtn = NavButton.createNav(this, "Quit", () -> System.exit(0));
        logoutBtn = NavButton.createNav(this, "Logout", () -> new LoginUI());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (user != null) {
            backBtn = NavButton.createNav(this, "Back to Dashboard", () -> new DashboardUI(user));
        }

        setVisible(true);
    }

    public JTextField createSearch(TableRowSorter<DefaultTableModel> rowSorter) {
        JTextField search = new JTextField(15);
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = search.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                }
                else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
                }
            }
        });

        return search;
    }
}
