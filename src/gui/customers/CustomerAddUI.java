package gui.customers;

import domain.models.Customer;
import domain.models.User;
import gui.MainFrame;
import gui.components.InputComponent;
import gui.components.NavButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import services.CustomerService;

public class CustomerAddUI extends MainFrame {

    public CustomerAddUI(CustomerService service, User user) {
        super("Customer Add", user);

        InputComponent fullNameInput = new InputComponent("Full Name : ", 10);
        InputComponent phoneNumberInput = new InputComponent("Phone Number : ", 10);
        InputComponent addressInput = new InputComponent("Address : ", 10);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            Customer customer = new Customer();

            String fullName = fullNameInput.getInput().getText();
            String phoneNumber = phoneNumberInput.getInput().getText();
            String address = addressInput.getInput().getText();

            customer.setFullName(fullName);
            customer.setPhoneNumber(phoneNumber);
            customer.setAddress(address);

            service.create(customer);

            this.dispose();
            new CustomersUI(user);
        });

        JButton previousBtn = NavButton.createNav(this, "Previous Page", () -> new CustomersUI(user));

        // Positions
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(fullNameInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(fullNameInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(phoneNumberInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(phoneNumberInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(addressInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(addressInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(addBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(previousBtn, gbc);
        add(panel);
    }
}
