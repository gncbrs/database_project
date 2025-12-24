package ui.theme;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class ModernTextField extends JTextField {

    public ModernTextField() {
        super();
        init();
    }

    public ModernTextField(String text) {
        super(text);
        init();
    }

    private void init() {
        setFont(Theme.STANDARD_FONT);
        setForeground(Theme.TEXT_COLOR);
        setBackground(Color.WHITE);
        setCaretColor(Theme.PRIMARY_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    // We could add more custom painting if needed, but standard with custom border
    // is usually fine.
}
