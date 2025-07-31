package gui.products;

import domain.models.Product;
import domain.models.Tax;
import domain.models.User;
import gui.MainFrame;
import gui.components.InputComponent;
import gui.components.NavButton;
import infrastructure.Database;
import infrastructure.mysql.JdbcTaxRepository;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import repositories.TaxRepository;
import services.ProductService;
import services.TaxService;

public class ProductUpdateUI extends MainFrame {
    private final TaxRepository taxRepo;
    private final TaxService taxService;
    private final Product product;

    public ProductUpdateUI(ProductService service, UUID id, User user) {
        super("Update Product" , user);

        taxRepo = new JdbcTaxRepository(Database.getInstance());
        taxService = new TaxService(taxRepo);
        product = service.getProductById(id);

        JLabel taxIncPrice = new JLabel(Double.toString(product.getTaxIncPrice()));
        InputComponent nameInput = new InputComponent("Product Name : ", 10);
        nameInput.getInput().setText(product.getName());

        InputComponent codeInput = new InputComponent("Product Code : ", 10);
        codeInput.getInput().setText(product.getName());

        InputComponent stockInput = new InputComponent("Product Stock : ", 10);
        stockInput.getInput().setText(Integer.toString(product.getStock()));

        List<Tax> taxes = taxService.getTaxes();
        InputComponent taxExcPriceInput = new InputComponent("Tax Exc. Price : ", 10);
        taxExcPriceInput.getInput().setText(Double.toString(product.getTaxExcPrice()));

        taxExcPriceInput.getInput().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { onChange().start(); }
            @Override
            public void removeUpdate(DocumentEvent e) { onChange().start(); }
            @Override
            public void changedUpdate(DocumentEvent e) { onChange().start(); }

            private Timer onChange() {
                Timer debounceTimer = new Timer(1000, e -> {
                    double value = Double.parseDouble(taxExcPriceInput.getInput().getText());
                    product.setTaxExcPrice(value);

                    Optional<Tax> matchedTax = taxes
                        .stream()
                        .filter(i -> i.getId().equals(product.getTaxId()))
                        .findFirst();

                    matchedTax.ifPresent(tax -> {
                        product.calcTaxIncPrice(tax.getRate());
                        taxIncPrice.setText(Double.toString(product.getTaxIncPrice()));
                    });

                    taxIncPrice.setText(Double.toString(product.getTaxIncPrice()));
                });

                debounceTimer.setRepeats(false);
                return debounceTimer;
            }
        });

        JComboBox<Tax> taxBox = new JComboBox<>(taxes.toArray(new Tax[0]));
        taxBox.addActionListener(e -> {
            Tax selectedTax = (Tax) taxBox.getSelectedItem();

            if (selectedTax != null) {
                product.setTaxId(selectedTax.getId());
                product.calcTaxIncPrice(selectedTax.getRate());
                taxIncPrice.setText(Double.toString(product.getTaxIncPrice()));
            }
        });

        JButton deleteBtn = new JButton(product.getIsDeleted() ? "Enable" : "Disable");
        deleteBtn.addActionListener(e -> {
            if (!product.getIsDeleted()) {
                service.markIsDeleted(id);
            }
            else {
                service.restore(id);
            }

            this.dispose();
            new ProductsUI(user);
        });

        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> { 
            product.setName(nameInput.getInput().getText());
            product.setCode(codeInput.getInput().getText());
            product.setStock(Integer.parseInt(stockInput.getInput().getText()));
            product.setTaxExcPrice(Double.parseDouble(taxExcPriceInput.getInput().getText()));

            service.update(product);
            this.dispose();
            new ProductsUI(user);
        });

        JButton previousBtn = NavButton.createNav(this, "Previous Page", () -> new ProductsUI(user));

        // Positions
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(codeInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(codeInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(stockInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(stockInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(taxExcPriceInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(taxExcPriceInput.getInput(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        panel.add(taxIncPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(taxBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(updateBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(deleteBtn, gbc);

        gbc.gridx = 2;
        gbc.gridy = 6;
        panel.add(previousBtn, gbc);

        add(panel);
    }
}
