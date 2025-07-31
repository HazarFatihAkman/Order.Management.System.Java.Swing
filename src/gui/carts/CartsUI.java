package gui.carts;

import domain.models.User;
import gui.MainFrame;
import gui.components.NavButton;
import infrastructure.Database;
import infrastructure.mysql.JdbcCartRepository;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import repositories.CartRepository;
import services.CartService;

public class CartsUI extends MainFrame {
    private final CartRepository repo;
    private final CartService service;

    public CartsUI(User user) {
        super("Carts", user);

        repo = new JdbcCartRepository(Database.getInstance());
        service = new CartService(repo);

        String[] columns = {
            "Id",
            "Customer Name",
            "Customer Phone Number",
            "Customer Address",
            "Price",
            "Is Paid"
        };

        Object[][] data = service
            .getCarts()
            .stream()
            .map(cart -> new Object[] {
                cart.getId(),
                cart.getCustomer().getFullName(),
                cart.getCustomer().getPhoneNumber(),
                cart.getCustomer().getAddress(),
                cart.getPrice(),
                cart.getIsPaid()
            }).toArray(Object[][]::new);

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = createTable(service, user, model);
        JScrollPane scrollPane = new JScrollPane(table);
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);

        JTextField searchField = createSearch(rowSorter);
        JButton addBtn = NavButton.createNav(this, "Add", () -> new CartAddUI(service, user));

        // Positions
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(searchField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(informationLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        panel.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(backBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(addBtn, gbc);
        add(panel);
    }

    private JTable createTable(CartService service, User user, DefaultTableModel model) {
        JTable table = new JTable(model);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int viewRow = table.getSelectedRow();
                    int modelRow = table.convertRowIndexToModel(viewRow);

                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);
                    parentFrame.dispose();
                    CartUpdateUI cartUpdateUI = new CartUpdateUI(service, UUID.fromString(table.getValueAt(modelRow, 0).toString()), user);
                }
            }
        });

        return table;
    }
}
