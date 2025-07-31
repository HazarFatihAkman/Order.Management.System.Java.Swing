package gui;

import domain.models.User;
import gui.components.InputComponent;
import infrastructure.Database;
import infrastructure.mysql.JdbcUserRepository;
import java.awt.*;
import javax.swing.*;
import repositories.UserRepository;
import services.UserService;

public class LoginUI extends MainFrame {
    public LoginUI() {
        super("Login", null);

        UserRepository repo = new JdbcUserRepository(Database.getInstance());
        UserService userService = new UserService(repo);
        InputComponent emailInput = new InputComponent("Email : ", 15);
        JLabel passwordLabel = new JLabel("Password : ");
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> {
            String email = emailInput.getInput().getText();
            String password = new String(passwordField.getPassword());
            User user = userService.login(email, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful.");
                this.dispose();
                DashboardUI dashboardUI = new DashboardUI(user);
            }
            else {
                JOptionPane.showMessageDialog(this, "Login failed.");
            }

            emailInput.getInput().setText("");
            passwordField.setText("");
        });

        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailInput.getLabel(), gbc);

        gbc.gridx = 1;
        panel.add(emailInput.getInput(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(loginBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(quitBtn, gbc);

        add(panel);
    }
}
