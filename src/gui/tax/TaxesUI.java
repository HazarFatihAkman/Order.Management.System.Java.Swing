package gui.tax;

import domain.models.User;
import gui.MainFrame;
import gui.components.NavButton;
import infrastructure.Database;
import infrastructure.mysql.JdbcTaxRepository;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import repositories.TaxRepository;
import services.TaxService;

public class TaxesUI extends MainFrame {
    private final TaxRepository repo;
    private final TaxService service;

    public TaxesUI(User user) {
        super("Taxes", user);
        repo = new JdbcTaxRepository(Database.getInstance());
        service = new TaxService(repo);

        String[] columns = { "Id", "Name", "Rate" };

        Object[][] data = service
            .getTaxes()
            .stream()
            .map(tax -> new Object[] {
                tax.getId(),
                tax.getName(),
                tax.getRate()
            })
            .toArray(Object[][]::new);

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
        JButton addBtn = NavButton.createNav(this, "Add", () -> new TaxAddUI(service, user));

        // Positions
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

    private JTable createTable(TaxService service, User user, DefaultTableModel model) {
        JTable table = new JTable(model);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int viewRow = table.getSelectedRow();
                    int modelRow = table.convertRowIndexToModel(viewRow);

                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);
                    parentFrame.dispose();
                    TaxUpdateUI taxUpdateUI = new TaxUpdateUI(service, UUID.fromString(table.getValueAt(modelRow, 0).toString()), user);
                }
            }
        });

        return table;
    }
}
