package gui.products;

import domain.models.Product;
import domain.models.Tax;
import domain.models.User;
import gui.MainFrame;
import gui.components.InputComponent;
import gui.components.NavButton;
import infrastructure.Database;
import infrastructure.mysql.JdbcTaxRepository;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import repositories.TaxRepository;
import services.ProductService;
import services.TaxService;

public class ProductAddUI extends MainFrame {
    private final TaxRepository taxRepo;
    private final TaxService taxService;

    public ProductAddUI(ProductService service, User user) {
        super("Product Add", user);

        Product product = new Product();
        Tax tax = new Tax();
        taxRepo = new JdbcTaxRepository(Database.getInstance());
        taxService = new TaxService(taxRepo);

        JLabel taxIncPrice = new JLabel();
        InputComponent nameInput = new InputComponent("Product Name : ", 10);
        InputComponent codeInput = new InputComponent("Product Code : ", 10);
        InputComponent stockInput = new InputComponent("Product Stock : ", 10);
        InputComponent taxExcPriceInput = new InputComponent("Tax Exc. Price : ", 10);

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

                    if (tax != null && tax.getRate() > 0) {
                        product.calcTaxIncPrice(tax.getRate());
                        taxIncPrice.setText(Double.toString(product.getTaxIncPrice()));
                    }
                });

                debounceTimer.setRepeats(false);
                return debounceTimer;
            }
        });

        List<Tax> taxes = taxService.getTaxes();
        JComboBox<Tax> taxBox = new JComboBox<>(taxes.toArray(new Tax[0]));
        taxBox.addActionListener(e -> {
            Tax selectedTax = (Tax) taxBox.getSelectedItem();

            if (selectedTax != null) {
                tax.setId(selectedTax.getId());
                tax.setName(selectedTax.getName());
                tax.setRate(selectedTax.getRate());
                product.calcTaxIncPrice(tax.getRate());
                taxIncPrice.setText(Double.toString(product.getTaxIncPrice()));
            }
        });


        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            product.setName(nameInput.getInput().getText());
            product.setCode(codeInput.getInput().getText());
            product.setStock(Integer.parseInt(stockInput.getInput().getText()));
            product.setTaxId(tax.getId());
            product.setTaxExcPrice(Integer.parseInt(taxExcPriceInput.getInput().getText()));
            product.calcTaxIncPrice(tax.getRate());

            service.create(product);
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
        gbc.gridy = 4;
        panel.add(taxBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(addBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(previousBtn, gbc);
        add(panel);
    }
    
}
