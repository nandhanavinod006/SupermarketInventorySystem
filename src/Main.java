import ui.ProductFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductFrame().setVisible(true));
    }
}
