package gui.components;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class InputComponent {
    private final JLabel label;
    private final JTextField input;

    public InputComponent(String labelName, int inputColumns) {
        label = new JLabel(labelName);
        input = new JTextField(inputColumns);
    }

    public JLabel getLabel() {
        return label;
    }

    public JTextField getInput() {
        return input;
    }
}
