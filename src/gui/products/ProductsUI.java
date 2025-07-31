package gui.products;

import domain.models.User;
import gui.MainFrame;
import infrastructure.Database;
import infrastructure.mysql.JdbcProductRepository;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import repositories.ProductRepository;
import services.ProductService;

public class ProductsUI extends MainFrame {
    private final ProductRepository repo;
    private final ProductService service;

    public ProductsUI(User user) {
        super("Products", user);
        repo = new JdbcProductRepository(Database.getInstance());
        service = new ProductService(repo);

        String[] columns = {
            "Id", "Name",
            "Code", "Stock",
            "Tax Exc. Price", "Tax Inc. Price"
        };

        Object[][] data = service
            .getProducts()
            .stream()
            .map(product -> new Object[] {
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getStock(),
                product.getTaxExcPrice(),
                product.getTaxIncPrice()
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

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            this.dispose();
            ProductAddUI productAddUI = new ProductAddUI(service, user);
        });

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

    private JTable createTable(ProductService service, User user, DefaultTableModel model) {
        JTable table = new JTable(model);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int viewRow = table.getSelectedRow();
                    int modelRow = table.convertRowIndexToModel(viewRow);

                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);
                    parentFrame.dispose();
                    ProductUpdateUI productUpdateUI = new ProductUpdateUI(service, UUID.fromString(table.getValueAt(modelRow, 0).toString()), user);
                }
            }
        });

        return table;
    }
}
