package gui.customers;

import domain.models.Customer;
import domain.models.User;
import gui.MainFrame;
import gui.components.InputComponent;
import gui.components.NavButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.UUID;
import javax.swing.JButton;
import services.CustomerService;

public class CustomerUpdateUI extends MainFrame {
    private final Customer customer;
    public CustomerUpdateUI(CustomerService service, UUID id, User user) {
        super("Customer Update", user);

        customer = service.getCustomerById(id);

        InputComponent fullNameInput = new InputComponent("Full Name : ", 10);
        fullNameInput.getInput().setText(customer.getFullName());

        InputComponent phoneNumberInput = new InputComponent("Phone Number : ", 10);
        phoneNumberInput.getInput().setText(customer.getPhoneNumber());

        InputComponent addressInput = new InputComponent("Address : ", 10);
        addressInput.getInput().setText(customer.getAddress());

        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> {
            customer.setFullName(fullNameInput.getInput().getText());
            customer.setPhoneNumber(phoneNumberInput.getInput().getText());
            customer.setAddress(addressInput.getInput().getText());

            service.update(customer);

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
        panel.add(updateBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(previousBtn, gbc);
        add(panel);
    }
}
