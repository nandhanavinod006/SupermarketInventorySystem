package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.ProductDAO;
import model.Product;

public class ProductSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> cmbSearchBy;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnClear;

    private JTable productTable;
    private DefaultTableModel tableModel;

    private ProductDAO productDAO;

    public ProductSearchFrame() {

        productDAO = new ProductDAO();

        setTitle("Product Search");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createGUI();
    }

    private void createGUI() {

        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT));

        searchPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Search Products"));

        searchPanel.add(
                new JLabel("Search By:"));

        String[] searchOptions = {
            "Product ID",
            "Product Name",
            "Category",
            "Brand"
        };

        cmbSearchBy =
                new JComboBox<String>(
                        searchOptions);

        searchPanel.add(cmbSearchBy);

        searchPanel.add(
                new JLabel("Search:"));

        txtSearch =
                new JTextField(25);

        searchPanel.add(txtSearch);

        btnSearch =
                new JButton("Search");

        btnClear =
                new JButton("Clear");

        searchPanel.add(btnSearch);
        searchPanel.add(btnClear);

        add(
                searchPanel,
                BorderLayout.NORTH);

        String[] columnNames = {
            "Product ID",
            "Product Name",
            "Category",
            "Brand",
            "Quantity",
            "Price",
            "Minimum Stock",
            "Expiry Date"
        };

        tableModel =
                new DefaultTableModel(
                        columnNames, 0) {

            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        productTable =
                new JTable(tableModel);

        productTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane =
                new JScrollPane(productTable);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Product Records"));

        add(
                scrollPane,
                BorderLayout.CENTER);

        btnSearch.addActionListener(
                e -> searchProducts());

        btnClear.addActionListener(
                e -> clearSearch());

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT));

        bottomPanel.add(
                new JLabel(
                        "Search by Product ID, Name, "
                        + "Category or Brand."));

        add(
                bottomPanel,
                BorderLayout.SOUTH);
    }

    private void searchProducts() {

        String keyword =
                txtSearch
                .getText()
                .trim();

        if (keyword.equals("")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a search value.");

            txtSearch.requestFocus();

            return;
        }

        String searchBy =
                cmbSearchBy
                .getSelectedItem()
                .toString();

        if (searchBy.equals("Product ID")) {

            try {

                Integer.parseInt(keyword);

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product ID must be a number.");

                txtSearch.requestFocus();

                return;
            }
        }

        List<Product> products =
                productDAO.searchProducts(
                        searchBy,
                        keyword);

        displayProducts(products);
    }

    private void displayProducts(
            List<Product> products) {

        tableModel.setRowCount(0);

        for (Product product : products) {

            Object[] row = {
                product.getProductId(),
                product.getProductName(),
                product.getCategory(),
                product.getBrand(),
                product.getQuantity(),
                product.getUnitPrice(),
                product.getMinimumStock(),
                product.getExpiryDate()
            };

            tableModel.addRow(row);
        }

        if (products.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No products found.");
        }
    }

    private void clearSearch() {

        txtSearch.setText("");

        tableModel.setRowCount(0);

        cmbSearchBy.setSelectedIndex(0);

        txtSearch.requestFocus();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                new Runnable() {

            public void run() {

                ProductSearchFrame frame =
                        new ProductSearchFrame();

                frame.setVisible(true);
            }
        });
    }
}