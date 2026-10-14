package ui;

import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductFrame extends JFrame {

    private final ProductDAO dao = new ProductDAO();

    private final JTextField id = new JTextField();
    private final JTextField name = new JTextField();
    private final JTextField category = new JTextField();
    private final JTextField brand = new JTextField();
    private final JTextField quantity = new JTextField();
    private final JTextField price = new JTextField();
    private final JTextField expiry = new JTextField();
    private final JTextField minimum = new JTextField();
    private final JTextField expiryDays = new JTextField();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Product", "Category", "Brand",
                    "Stock", "Price", "Expiry", "Min Stock", "Status"}, 0) {

        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    public ProductFrame() {

        setTitle("Supermarket Inventory Management");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();
        loadProducts();
    }

    private void buildUI() {

        setLayout(new BorderLayout(10, 10));

        // ---------- LEFT SIDE ----------

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ---------- PRODUCT DETAILS ----------

        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        productPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Product Details",
                0,
                0,
                new Font("Arial", Font.BOLD, 18)
        ));

        addProductField(productPanel, "Product ID", id);
        addProductField(productPanel, "Product Name", name);
        addProductField(productPanel, "Category", category);
        addProductField(productPanel, "Brand", brand);
        addProductField(productPanel, "Quantity", quantity);
        addProductField(productPanel, "Unit Price", price);
        addProductField(productPanel, "Expiry (YYYY-MM-DD)", expiry);
        addProductField(productPanel, "Minimum Stock", minimum);

        // ---------- BUTTONS ----------

        JButton add = createButton("Add Product");
        JButton update = createButton("Update Product");
        JButton delete = createButton("Delete Product");

        JButton clear = createButton("Clear");
        JButton stockIn = createButton("Stock In");
        JButton stockOut = createButton("Stock Out");

        JButton refresh = createButton("Refresh");
        JButton low = createButton("Low Stock");
        JButton exp = createButton("Expiry Check");

        add.addActionListener(e -> addProduct());
        update.addActionListener(e -> updateProduct());
        delete.addActionListener(e -> deleteProduct());

        clear.addActionListener(e -> clearFields());
        stockIn.addActionListener(e -> stockTransaction("IN"));
        stockOut.addActionListener(e -> stockTransaction("OUT"));

        refresh.addActionListener(e -> loadProducts());
        low.addActionListener(e -> showLowStock());
        exp.addActionListener(e -> expiryCheck());

        table.getSelectionModel().addListSelectionListener(
                e -> fillSelectedRow()
        );

        JPanel row1 = createButtonRow(add, update, delete);
        JPanel row2 = createButtonRow(clear, stockIn, stockOut);
        JPanel row3 = createButtonRow(refresh, low, exp);

        // ---------- EXPIRY MONITORING ----------

        JPanel expiryPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 8)
        );

        expiryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Expiry Monitoring",
                0,
                0,
                new Font("Arial", Font.BOLD, 18)
        ));

        JLabel expiryLabel = new JLabel(
                "Check products expiring within:"
        );

        expiryLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        expiryDays.setPreferredSize(
                new Dimension(70, 28)
        );

        expiryDays.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        JLabel daysLabel = new JLabel("days");

        daysLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        expiryPanel.add(expiryLabel);
        expiryPanel.add(expiryDays);
        expiryPanel.add(daysLabel);

        // Add everything to left side

        leftPanel.add(productPanel);
        leftPanel.add(row1);
        leftPanel.add(row2);
        leftPanel.add(row3);
        leftPanel.add(expiryPanel);

        leftPanel.setPreferredSize(
                new Dimension(520, 0)
        );

        // ---------- INVENTORY TABLE ----------

        table.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        table.setRowHeight(28);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(0, 32)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(
                new BorderLayout()
        );

        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Inventory",
                0,
                0,
                new Font("Arial", Font.BOLD, 18)
        ));

        tablePanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // Add left and right sides

        add(leftPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
    }

    // ---------- PRODUCT FIELD ----------

    private void addProductField(
            JPanel panel,
            String label,
            JTextField field) {

        JPanel row = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 5)
        );

        JLabel text = new JLabel(label);

        text.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        text.setPreferredSize(
                new Dimension(160, 28)
        );

        field.setPreferredSize(
                new Dimension(220, 28)
        );

        row.add(text);
        row.add(field);

        panel.add(row);
    }

    // ---------- BUTTON ROW ----------

    private JPanel createButtonRow(
            JButton b1,
            JButton b2,
            JButton b3) {

        JPanel row = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 6, 4)
        );

        row.add(b1);
        row.add(b2);
        row.add(b3);

        return row;
    }

    // ---------- BUTTON ----------

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        button.setPreferredSize(
                new Dimension(145, 34)
        );

        return button;
    }

    // ---------- READ PRODUCT ----------

    private Product readProduct() {

        if (name.getText().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Product name is required."
            );
        }

        int q = Integer.parseInt(
                quantity.getText().trim()
        );

        double pr = Double.parseDouble(
                price.getText().trim()
        );

        int min = Integer.parseInt(
                minimum.getText().trim()
        );

        if (q < 0 || pr < 0 || min < 0) {
            throw new IllegalArgumentException(
                    "Quantity, price and minimum stock cannot be negative."
            );
        }

        LocalDate date =
                expiry.getText().trim().isEmpty()
                        ? null
                        : LocalDate.parse(
                                expiry.getText().trim()
                        );

        int productId =
                id.getText().trim().isEmpty()
                        ? 0
                        : Integer.parseInt(
                                id.getText().trim()
                        );

        return new Product(
                productId,
                name.getText().trim(),
                category.getText().trim(),
                brand.getText().trim(),
                q,
                pr,
                date,
                min
        );
    }

    // ---------- ADD PRODUCT ----------

    private void addProduct() {

        try {

            dao.addProduct(readProduct());

            loadProducts();
            clearFields();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // ---------- UPDATE PRODUCT ----------

    private void updateProduct() {

        try {

            Product p = readProduct();

            if (p.getProductId() == 0) {
                throw new IllegalArgumentException(
                        "Select a product first."
                );
            }

            dao.updateProduct(p);

            loadProducts();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // ---------- DELETE PRODUCT ----------

    private void deleteProduct() {

        try {

            int productId = Integer.parseInt(
                    id.getText().trim()
            );

            dao.deleteProduct(productId);

            loadProducts();
            clearFields();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // ---------- STOCK IN / OUT ----------

    private void stockTransaction(String type) {

        try {

            int productId = Integer.parseInt(
                    id.getText().trim()
            );

            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter Stock " +
                            (type.equals("IN")
                                    ? "In"
                                    : "Out") +
                            " quantity:"
            );

            if (input == null) {
                return;
            }

            int amount = Integer.parseInt(
                    input.trim()
            );

            dao.recordStock(
                    productId,
                    type,
                    amount
            );

            loadProducts();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // ---------- LOAD PRODUCTS ----------

    private void loadProducts() {

        try {

            model.setRowCount(0);

            for (Product p : dao.getAllProducts()) {

                model.addRow(new Object[]{
                        p.getProductId(),
                        p.getProductName(),
                        p.getCategory(),
                        p.getBrand(),
                        p.getQuantity(),
                        p.getUnitPrice(),
                        p.getExpiryDate(),
                        p.getMinimumStock(),
                        status(p)
                });
            }

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // ---------- STATUS ----------

    private String status(Product p) {

        if (p.getQuantity() <= p.getMinimumStock()) {
            return "LOW STOCK";
        }

        if (p.getExpiryDate() != null &&
                p.getExpiryDate().isBefore(LocalDate.now())) {

            return "EXPIRED";
        }

        return "OK";
    }

    // ---------- LOW STOCK ----------

    private void showLowStock() {

        StringBuilder s =
                new StringBuilder(
                        "LOW STOCK PRODUCTS\n\n"
                );

        try {

            for (Product p : dao.getAllProducts()) {

                if (p.getQuantity() <=
                        p.getMinimumStock()) {

                    s.append(p.getProductName())
                            .append(" - Current: ")
                            .append(p.getQuantity())
                            .append(", Minimum: ")
                            .append(p.getMinimumStock())
                            .append('\n');
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    s.toString(),
                    "Low Stock Alert",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // ---------- EXPIRY CHECK ----------

    private void expiryCheck() {

        try {

            int days = Integer.parseInt(
                    expiryDays.getText().trim()
            );

            LocalDate today = LocalDate.now();
            LocalDate limit = today.plusDays(days);

            StringBuilder s =
                    new StringBuilder(
                            "EXPIRY CHECK\n\n"
                    );

            boolean found = false;

            for (Product p : dao.getAllProducts()) {

                if (p.getExpiryDate() != null &&
                        !p.getExpiryDate().isAfter(limit)) {

                    long d = ChronoUnit.DAYS.between(
                            today,
                            p.getExpiryDate()
                    );

                    s.append(p.getProductName())
                            .append(" - ")
                            .append(p.getExpiryDate())
                            .append(" (")
                            .append(d)
                            .append(" days)\n");

                    found = true;
                }
            }

            if (!found) {

                s.append(
                        "No products are expiring within "
                                + days
                                + " days."
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    s.toString(),
                    "Expiry Check",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // ---------- SELECT PRODUCT ----------

    private void fillSelectedRow() {

        int r = table.getSelectedRow();

        if (r < 0) {
            return;
        }

        id.setText(
                model.getValueAt(r, 0).toString()
        );

        name.setText(
                model.getValueAt(r, 1).toString()
        );

        category.setText(
                String.valueOf(
                        model.getValueAt(r, 2)
                )
        );

        brand.setText(
                String.valueOf(
                        model.getValueAt(r, 3)
                )
        );

        quantity.setText(
                model.getValueAt(r, 4).toString()
        );

        price.setText(
                model.getValueAt(r, 5).toString()
        );

        expiry.setText(
                model.getValueAt(r, 6) == null
                        ? ""
                        : model.getValueAt(r, 6).toString()
        );

        minimum.setText(
                model.getValueAt(r, 7).toString()
        );
    }

    // ---------- CLEAR ----------

    private void clearFields() {

        id.setText("");
        name.setText("");
        category.setText("");
        brand.setText("");
        quantity.setText("");
        price.setText("");
        expiry.setText("");
        minimum.setText("");
    }

    // ---------- ERROR ----------

    private void showError(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}