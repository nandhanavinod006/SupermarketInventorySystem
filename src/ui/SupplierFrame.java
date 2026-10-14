package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.SupplierDAO;
import model.Supplier;

public class SupplierFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtSupplierId;
    private JTextField txtSupplierName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtSearch;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnSearch;

    private JTable supplierTable;
    private DefaultTableModel tableModel;

    private SupplierDAO supplierDAO;

    private final Font labelFont =
            new Font("Arial", Font.PLAIN, 15);

    private final Font fieldFont =
            new Font("Arial", Font.PLAIN, 15);

    private final Font buttonFont =
            new Font("Arial", Font.BOLD, 14);

    public SupplierFrame() {

        supplierDAO = new SupplierDAO();

        setTitle("Supplier Management");
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        createGUI();
        loadSuppliers();
    }

    private void createGUI() {

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel =
                new JPanel(new GridLayout(5, 2, 12, 10));

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Supplier Details"),
                        BorderFactory.createEmptyBorder(
                                10, 10, 10, 10)));

        formPanel.setPreferredSize(
                new Dimension(1000, 245));

        JLabel[] labels = {
            new JLabel("Supplier ID:"),
            new JLabel("Supplier Name:"),
            new JLabel("Phone:"),
            new JLabel("Email:"),
            new JLabel("Address:")
        };

        for (JLabel label : labels) {
            label.setFont(labelFont);
        }

        txtSupplierId = createTextField();
        txtSupplierId.setEditable(false);

        txtSupplierName = createTextField();
        txtPhone = createTextField();
        txtEmail = createTextField();
        txtAddress = createTextField();

        formPanel.add(labels[0]);
        formPanel.add(txtSupplierId);
        formPanel.add(labels[1]);
        formPanel.add(txtSupplierName);
        formPanel.add(labels[2]);
        formPanel.add(txtPhone);
        formPanel.add(labels[3]);
        formPanel.add(txtEmail);
        formPanel.add(labels[4]);
        formPanel.add(txtAddress);

        JPanel buttonPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT, 10, 8));

        btnAdd = createButton("Add");
        btnUpdate = createButton("Update");
        btnDelete = createButton("Delete");
        btnClear = createButton("Clear");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel searchPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT, 10, 8));

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(labelFont);

        txtSearch = createTextField();
        txtSearch.setPreferredSize(
                new Dimension(220, 36));

        btnSearch = createButton("Search");

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel actionPanel =
                new JPanel(new BorderLayout());

        actionPanel.add(
                buttonPanel, BorderLayout.WEST);

        actionPanel.add(
                searchPanel, BorderLayout.EAST);

        JPanel topPanel =
                new JPanel(new BorderLayout(8, 8));

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 12, 5, 12));

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {
            "ID", "Supplier Name", "Phone",
            "Email", "Address"
        };

        tableModel = new DefaultTableModel(
                columnNames, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        supplierTable = new JTable(tableModel);
        supplierTable.setFont(
                new Font("Arial", Font.PLAIN, 14));
        supplierTable.setRowHeight(30);
        supplierTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        supplierTable.setAutoCreateRowSorter(true);

        JTableHeader header =
                supplierTable.getTableHeader();

        header.setFont(
                new Font("Arial", Font.BOLD, 14));

        header.setPreferredSize(
                new Dimension(0, 35));

        JScrollPane scrollPane =
                new JScrollPane(supplierTable);

        scrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Supplier Records"),
                        BorderFactory.createEmptyBorder(
                                5, 5, 5, 5)));

        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchSuppliers());
        txtSearch.addActionListener(e -> searchSuppliers());

        supplierTable.getSelectionModel()
                .addListSelectionListener(
                        new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting()) {
                    return;
                }

                int selectedRow =
                        supplierTable.getSelectedRow();

                if (selectedRow < 0) {
                    return;
                }

                int modelRow =
                        supplierTable.convertRowIndexToModel(
                                selectedRow);

                txtSupplierId.setText(
                        tableModel.getValueAt(
                                modelRow, 0).toString());

                txtSupplierName.setText(
                        tableModel.getValueAt(
                                modelRow, 1).toString());

                txtPhone.setText(
                        tableModel.getValueAt(
                                modelRow, 2).toString());

                txtEmail.setText(
                        tableModel.getValueAt(
                                modelRow, 3).toString());

                txtAddress.setText(
                        tableModel.getValueAt(
                                modelRow, 4).toString());
            }
        });
    }

    private JTextField createTextField() {

        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setPreferredSize(
                new Dimension(250, 36));

        return field;
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setPreferredSize(
                new Dimension(105, 38));

        return button;
    }

    private void addSupplier() {

        if (!validateFields()) {
            return;
        }

        Supplier supplier = new Supplier();

        supplier.setSupplierName(
                txtSupplierName.getText().trim());
        supplier.setPhone(txtPhone.getText().trim());
        supplier.setEmail(txtEmail.getText().trim());
        supplier.setAddress(txtAddress.getText().trim());

        if (supplierDAO.addSupplier(supplier)) {

            JOptionPane.showMessageDialog(
                    this, "Supplier added successfully.");

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add supplier.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSupplier() {

        if (txtSupplierId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a supplier from the table.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        Supplier supplier = new Supplier();

        supplier.setSupplierId(
                Integer.parseInt(
                        txtSupplierId.getText().trim()));

        supplier.setSupplierName(
                txtSupplierName.getText().trim());
        supplier.setPhone(txtPhone.getText().trim());
        supplier.setEmail(txtEmail.getText().trim());
        supplier.setAddress(txtAddress.getText().trim());

        if (supplierDAO.updateSupplier(supplier)) {

            JOptionPane.showMessageDialog(
                    this, "Supplier updated successfully.");

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update supplier.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSupplier() {

        if (txtSupplierId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a supplier from the table.");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this supplier?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        int supplierId =
                Integer.parseInt(
                        txtSupplierId.getText().trim());

        if (supplierDAO.deleteSupplier(supplierId)) {

            JOptionPane.showMessageDialog(
                    this, "Supplier deleted successfully.");

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete supplier.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchSuppliers() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadSuppliers();
            return;
        }

        List<Supplier> suppliers =
                supplierDAO.searchSuppliers(keyword);

        displaySuppliers(suppliers);
    }

    private void loadSuppliers() {

        List<Supplier> suppliers =
                supplierDAO.getAllSuppliers();

        displaySuppliers(suppliers);
    }

    private void displaySuppliers(
            List<Supplier> suppliers) {

        tableModel.setRowCount(0);

        for (Supplier supplier : suppliers) {

            Object[] row = {
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress()
            };

            tableModel.addRow(row);
        }
    }

    private void clearFields() {

        txtSupplierId.setText("");
        txtSupplierName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");

        supplierTable.clearSelection();
        loadSuppliers();
    }

    private boolean validateFields() {

        if (txtSupplierName.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Supplier name is required.");

            txtSupplierName.requestFocus();
            return false;
        }

        return true;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            SupplierFrame frame = new SupplierFrame();
            frame.setVisible(true);
        });
    }
}
