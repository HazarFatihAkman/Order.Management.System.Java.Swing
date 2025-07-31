package gui.carts;

import domain.models.Cart;
import domain.models.Customer;
import domain.models.Order;
import domain.models.Product;
import domain.models.User;
import gui.MainFrame;
import gui.components.NavButton;
import infrastructure.Database;
import infrastructure.mysql.JdbcCustomerRepository;
import infrastructure.mysql.JdbcProductRepository;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import repositories.CustomerRepository;
import repositories.ProductRepository;
import services.CartService;
import services.CustomerService;
import services.ProductService;

public class CartUpdateUI extends MainFrame {
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final List<Customer> customers;

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final List<Product> products;
    private List<Product> addedProducts = new ArrayList<>();

    private Object[][] productsData;
    private DefaultTableModel productModels;
    private String[] productColumns = { "Id", "Name", "Code", "Stock", "Quantity", "Price" };

    private Cart cart = new Cart();
    private JLabel total;

    public CartUpdateUI(CartService service, UUID id, User user) {
        super("Cart Update", user);

        customerRepository = new JdbcCustomerRepository(Database.getInstance());
        customerService = new CustomerService(customerRepository);
        customers = customerService.getCustomers();

        productRepository = new JdbcProductRepository(Database.getInstance());
        productService = new ProductService(productRepository);

        products = productService
            .getProducts()
            .stream()
            .filter(i -> !i.getIsDeleted())
            .toList();

        cart = service.getCartById(id);

        addedProducts = products.stream()
            .filter(product -> cart.getProducts().stream()
                .anyMatch(i -> i.getProductId().equals(product.getId())))
            .collect(Collectors.toList());

        total = new JLabel(Double.toString(calcTotal()));

        productsData = addedProducts
            .stream()
            .map(product -> new Object[] {
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getStock(),
                cart.getProducts()
                    .stream()
                    .filter(i -> i.getProductId().equals(product.getId()))
                    .findFirst()
                    .get()
                    .getQuantity(),
                product.getTaxIncPrice()
            }).toArray(Object[][]::new);

        Customer cartCustomer = customerService.getCustomerById(cart.getCustomerId());
        JLabel customerFullNameLabel = new JLabel(cartCustomer.getFullName());

        JComboBox customerBox = new JComboBox<>(customers.toArray(new Customer[0]));
        customerBox.addActionListener(e -> {
            Customer customer = (Customer) customerBox.getSelectedItem();
            if (customer != null) {
                cart.setCustomer(customer);
                cart.setCustomerId(customer.getId());
            }
        });

        JComboBox productBox = new JComboBox<>(products.toArray(new Product[0]));
        productBox.addActionListener(e -> {
            Product product = (Product) productBox.getSelectedItem();
            Optional<Product> addedProductOptional = addedProducts
                .stream()
                .filter(i -> i.getId().equals(product.getId()))
                .findFirst();

            if (addedProductOptional.isPresent()) {
                Optional<Order> cartProduct = cart.getProducts()
                    .stream()
                    .filter(i -> i.getProductId().equals(addedProductOptional.get().getId()))
                    .findFirst();

                cartProduct.get().increaseQuantity();
            }
            else {
                Order selectredProduct = new Order(product.getId(), 1);
                cart.getProducts().add(selectredProduct);
                addedProducts.add(product);
            }
            product.setStock(product.getStock()-1);
            updateTable();
        });

        productModels = new DefaultTableModel(productsData, productColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable selectedProductsTable = viewSelectedProducts(productModels);
        JScrollPane productsScrollPane = new JScrollPane(selectedProductsTable);
        TableRowSorter<DefaultTableModel> productsRowSorter = new TableRowSorter<>(productModels);
        selectedProductsTable.setRowSorter(productsRowSorter);
        JTextField searchField = createSearch(productsRowSorter);

        JLabel removeProductLabel = new JLabel("Double Click to decrease quantity of product/remove product.");
        JLabel totalLabel = new JLabel("Total : ");

        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> {
            cart.setPrice(calcTotal());
            service.update(cart);
            addedProducts.forEach(i -> productService.update(i));
            this.dispose();
            new CartsUI(user);
        });

        JButton previousBtn = NavButton.createNav(this, "Previous Page", () -> new CartsUI(user));
        JButton paidBtn = new JButton("Paid");
        paidBtn.addActionListener(e -> {
            cart.setIsPaid(true);
            service.update(cart);
            this.dispose();
            new CartsUI(user);
        });

        // Positions
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(customerFullNameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(customerBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(productBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(productsScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(totalLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(total, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(updateBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(paidBtn, gbc);

        gbc.gridx = 2;
        gbc.gridy = 7;
        panel.add(previousBtn, gbc);
        add(panel);
    }

    private JTable viewSelectedProducts(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int viewRow = table.getSelectedRow();
                    int modelRow = table.convertRowIndexToModel(viewRow);

                    Optional<Order> removedSelectedProduct = cart.getProducts()
                        .stream()
                        .filter(i -> i.getProductId().equals(UUID.fromString(table.getValueAt(modelRow, 0).toString())))
                        .findFirst();

                    if (removedSelectedProduct.isPresent()) {
                        removedSelectedProduct.get().decreaseQuantity();

                        if (removedSelectedProduct.get().getQuantity() == 0) {
                            addedProducts.removeIf(i -> i.getId().equals(UUID.fromString(table.getValueAt(modelRow, 0).toString())));
                            cart.getProducts().removeIf(i -> i.getProductId().equals(UUID.fromString(table.getValueAt(modelRow, 0).toString())));
                        }
                        Optional<Product> productOptional = products
                            .stream()
                            .filter(i -> i.getId().equals(UUID.fromString(table.getValueAt(modelRow, 0).toString())))
                            .findFirst();
                        productOptional.get().setStock(productOptional.get().getStock()+1);
                    }
                    updateTable();
                }
            }
        });
        return table;
    }

    private void updateTable() {
        productModels.setRowCount(0);

        for (Product product : addedProducts) {
            productModels.addRow(new Object[] {
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getStock(),
                cart.getProducts()
                    .stream()
                    .filter(i -> i.getProductId().equals(product.getId()))
                    .findFirst()
                    .get()
                    .getQuantity(),
                product.getTaxIncPrice()
            });
        }

        total.setText(Double.toString(calcTotal()));
    }

    private double calcTotal() {
        return cart.getProducts()
            .stream()
            .mapToDouble(selectedProduct -> {
                Optional<Product> product = addedProducts.stream()
                    .filter(i -> i.getId().equals(selectedProduct.getProductId()))
                    .findFirst();

                return product.isPresent() ? product.get().getTaxIncPrice() * selectedProduct.getQuantity() : 0;
            })
            .sum();
    }
}
