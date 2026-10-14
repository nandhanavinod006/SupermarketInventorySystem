
package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import dao.DashboardDAO;

public class Dashboard extends JFrame {

    private String username;
    private String role;

    JButton productButton;
    JButton supplierButton;
    JButton stockButton;
    JButton reportsButton;
    JButton logoutButton;

    JLabel totalProducts;
    JLabel lowStock;
    JLabel expiringSoon;
    JLabel totalSuppliers;

    // Keep compatibility with existing code that uses new Dashboard()
    public Dashboard() {
        this("admin", "ADMIN");
    }

    // Dashboard for the logged-in user
    public Dashboard(String username, String role) {

        this.username = username;
        this.role = role == null ? "" : role.toUpperCase();

        setTitle("Supermarket Inventory Management System - Dashboard");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(Box.createVerticalStrut(20));

        // Heading
        JLabel title = new JLabel("INVENTORY MANAGEMENT DASHBOARD");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);

        JLabel userLabel = new JLabel(
                "Logged in as: " + username + " (" + this.role + ")"
        );
        userLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(userLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Information cards
        JPanel cardsPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 10)
        );

        JPanel productPanel = createCard("Total Products");
        totalProducts = createNumberLabel();
        productPanel.add(totalProducts);

        JPanel lowStockPanel = createCard("Low Stock");
        lowStock = createNumberLabel();
        lowStockPanel.add(lowStock);

        JPanel expiryPanel = createCard("Expiring Soon");
        expiringSoon = createNumberLabel();
        expiryPanel.add(expiringSoon);

        JPanel supplierPanel = createCard("Total Suppliers");
        totalSuppliers = createNumberLabel();
        supplierPanel.add(totalSuppliers);

        cardsPanel.add(productPanel);
        cardsPanel.add(lowStockPanel);
        cardsPanel.add(expiryPanel);
        cardsPanel.add(supplierPanel);

        mainPanel.add(cardsPanel);

        // Load counts from the database
        loadDashboardCounts();

        mainPanel.add(Box.createVerticalStrut(25));

        // Navigation buttons
        JPanel navigationPanel = new JPanel(
                new GridLayout(2, 2, 25, 20)
        );

        productButton = new JButton("Product Management");
        supplierButton = new JButton("Supplier Management");
        stockButton = new JButton("Stock Management");
        reportsButton = new JButton("Reports");

        productButton.setFont(new Font("Arial", Font.PLAIN, 15));
        supplierButton.setFont(new Font("Arial", Font.PLAIN, 15));
        stockButton.setFont(new Font("Arial", Font.PLAIN, 15));
        reportsButton.setFont(new Font("Arial", Font.PLAIN, 15));

        navigationPanel.add(productButton);
        navigationPanel.add(supplierButton);
        navigationPanel.add(stockButton);
        navigationPanel.add(reportsButton);

        navigationPanel.setPreferredSize(new Dimension(500, 120));
        navigationPanel.setMaximumSize(new Dimension(500, 120));

        mainPanel.add(navigationPanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 15));
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setMaximumSize(new Dimension(150, 40));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(logoutButton);
        mainPanel.add(Box.createVerticalGlue());

        // Button actions
        productButton.addActionListener(e -> {
            if (isAdmin()) {
                new ProductFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Staff cannot access product management."
                );
            }
        });

        supplierButton.addActionListener(e -> {
            if (isAdmin()) {
                new SupplierFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Only administrators can manage suppliers."
                );
            }
        });

        stockButton.addActionListener(
                e -> new StockFrame().setVisible(true)
        );

        reportsButton.addActionListener(
                e -> new ReportsFrame().setVisible(true)
        );

        // Apply role-based button access
        applyRolePermissions();

        // Logout
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new Login();
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    private void applyRolePermissions() {
        // Disable restricted features for staff
        productButton.setEnabled(isAdmin());
        supplierButton.setEnabled(isAdmin());

        if (!isAdmin()) {
            productButton.setToolTipText(
                    "Available to administrators only"
            );
            supplierButton.setToolTipText(
                    "Available to administrators only"
            );
        }
    }

    // Load summary counts from MySQL
    private void loadDashboardCounts() {
        try {
            DashboardDAO dao = new DashboardDAO();

            totalProducts.setText(
                    String.valueOf(dao.getTotalProducts())
            );

            lowStock.setText(
                    String.valueOf(dao.getLowStockCount())
            );

            expiringSoon.setText(
                    String.valueOf(dao.getExpiringSoonCount())
            );

            totalSuppliers.setText(
                    String.valueOf(dao.getTotalSuppliers())
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load dashboard counts:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Creates an information card
    private JPanel createCard(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(160, 100));

        panel.setBorder(
                BorderFactory.createLineBorder(Color.BLACK)
        );

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(15));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    // Creates number label for dashboard cards
    private JLabel createNumberLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Arial", Font.BOLD, 25));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }
}