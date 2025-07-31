package gui.tax;

import domain.models.Tax;
import domain.models.User;
import gui.MainFrame;
import gui.components.InputComponent;
import gui.components.NavButton;
import java.awt.*;
import javax.swing.*;
import services.TaxService;

public class TaxAddUI extends MainFrame {
    public TaxAddUI(TaxService service, User user) {
        super("Add Tax", user);

        InputComponent nameInput = new InputComponent("Tax Name : ", 10);
        InputComponent rateInput = new InputComponent("Tax Rate : ", 10);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            Tax tax = new Tax();
            String nameStr = nameInput.getInput().getText();
            double rateDouble = Double.parseDouble(rateInput.getInput().getText());

            tax.setName(nameStr);
            tax.setRate(rateDouble);
            service.create(tax);

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
        panel.add(addBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(previousBtn, gbc);
        add(panel);
    }
}
