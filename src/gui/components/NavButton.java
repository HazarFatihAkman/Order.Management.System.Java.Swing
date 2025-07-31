package gui.components;

import javax.swing.JButton;

import gui.MainFrame;

public class NavButton {
    public static JButton createNav(MainFrame self, String title, Runnable action) {
        JButton btn = new JButton(title);
        btn.addActionListener(e -> {
            self.dispose();
            action.run();
        });

        return btn;
    }
}
