package gui.tax;

import domain.models.Tax;
import domain.models.User;
import gui.MainFrame;
import gui.components.InputComponent;
import gui.components.NavButton;
import java.awt.*;
import java.util.UUID;
import javax.swing.*;
import services.TaxService;

public class TaxUpdateUI extends MainFrame {
    public TaxUpdateUI(TaxService service, UUID id, User user) {
        super("Update Tax / ", user);
        Tax tax = service.getTaxById(id);

        InputComponent nameInput = new InputComponent("Tax Name : ", 10);
        nameInput.getInput().setText(tax.getName());

        InputComponent rateInput = new InputComponent("Tax Rate : ", 10);
        rateInput.getInput().setText(Double.toString(tax.getRate()));

        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> {
            tax.setName(nameInput.getInput().getText());
            tax.setRate(Double.parseDouble(rateInput.getInput().getText()));
            service.update(tax);
            this.dispose();
            new TaxesUI(user);
        });

        JButton previousBtn = NavButton.createNav(this, "Previous Page", () -> new TaxesUI(user));

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
        panel.add(rateInput.getLabel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(rateInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(updateBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(previousBtn, gbc);
        add(panel);
    }
}
