package gui;

import domain.models.User;
import gui.carts.CartsUI;
import gui.components.NavButton;
import gui.customers.CustomersUI;
import gui.products.ProductsUI;
import gui.tax.TaxesUI;
import java.awt.*;
import javax.swing.*;

public class DashboardUI extends MainFrame {
    public DashboardUI(User user) {
        super("Dashboard", user);

        JLabel userFullName = new JLabel("Welcome " + user.getFullName(), SwingConstants.CENTER);
        JButton taxesBtn = NavButton.createNav(this, "Taxes", () -> new TaxesUI(user));
        JButton productsBtn = NavButton.createNav(this, "Products", () -> new ProductsUI(user));
        JButton cartsBtn = NavButton.createNav(this, "Carts", () -> new CartsUI(user));
        JButton customersBtn = NavButton.createNav(this, "Customers", () -> new CustomersUI(user));

        // Positions
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        userFullName.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(userFullName, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(taxesBtn, gbc);

        gbc.gridx = 1;
        panel.add(productsBtn, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(customersBtn, gbc);

        gbc.gridx = 1;
        panel.add(cartsBtn, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(logoutBtn, gbc);

        gbc.gridx = 1;
        panel.add(quitBtn, gbc);

        add(panel);
    }
}
