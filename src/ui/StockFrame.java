package ui;

import dao.StockDAO;

import javax.swing.*;
import java.awt.*;

public class StockFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel productIdLabel;
    private JLabel quantityLabel;
    private JLabel typeLabel;

    private JTextField productIdField;
    private JTextField quantityField;

    private JComboBox<String> typeComboBox;
    private JButton updateButton;

    public StockFrame() {

        setTitle("Stock Management");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Keep the original Windows look and feel
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 247, 252));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 230)),
                        BorderFactory.createEmptyBorder(
                                25, 30, 25, 30)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel(
                "Stock Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 55, 85));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Product ID
        productIdLabel = new JLabel("Product ID:");
        productIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productIdLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(productIdLabel, gbc);

        productIdField = new JTextField(18);
        productIdField.setFont(new Font("Arial", Font.PLAIN, 15));
        productIdField.setPreferredSize(new Dimension(250, 38));

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(productIdField, gbc);

        // Quantity
        quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quantityLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(quantityLabel, gbc);

        quantityField = new JTextField(18);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 15));
        quantityField.setPreferredSize(new Dimension(250, 38));

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(quantityField, gbc);

        // Stock Type
        typeLabel = new JLabel("Stock Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        typeLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(typeLabel, gbc);

        typeComboBox = new JComboBox<>(
                new String[]{"IN", "OUT"});
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 15));
        typeComboBox.setPreferredSize(new Dimension(250, 38));

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(typeComboBox, gbc);

        // Update Stock button
        updateButton = new JButton("Update Stock");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setForeground(Color.BLACK);
        updateButton.setBackground(
                UIManager.getColor("Button.background"));
        updateButton.setFocusPainted(false);
        updateButton.setPreferredSize(new Dimension(190, 42));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 10, 5, 10);
        formPanel.add(updateButton, gbc);

        // Center the form inside the window
        mainPanel.add(formPanel);
        add(mainPanel);

        // Button action
        updateButton.addActionListener(e -> updateStock());

        setVisible(true);
    }

    private void updateStock() {

        try {
            String productText = productIdField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (productText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter Product ID and Quantity.");
                return;
            }

            int productId = Integer.parseInt(productText);
            int quantity = Integer.parseInt(quantityText);

            if (productId <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Product ID must be greater than 0.");
                return;
            }

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Quantity must be greater than 0.");
                return;
            }

            StockDAO stockDAO = new StockDAO();
            String type = (String) typeComboBox.getSelectedItem();

            boolean result;

            if ("IN".equals(type)) {
                result = stockDAO.addStockIn(productId, quantity);
            } else {
                result = stockDAO.addStockOut(productId, quantity);
            }

            if (result) {
                JOptionPane.showMessageDialog(
                        this, "Stock " + type + " successful!");

                productIdField.setText("");
                quantityField.setText("");
                typeComboBox.setSelectedIndex(0);

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Stock update failed. Check the Product ID and available stock.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Please enter valid numbers.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockFrame());
    }
}