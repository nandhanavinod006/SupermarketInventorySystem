package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;

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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.ReportDAO;

public class ReportsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> cmbReportType;
    private JButton btnGenerate;
    private JButton btnClear;

    private JTable reportTable;
    private DefaultTableModel tableModel;

    private ReportDAO reportDAO;

    private final Font labelFont =
            new Font("Arial", Font.PLAIN, 16);

    private final Font fieldFont =
            new Font("Arial", Font.PLAIN, 15);

    private final Font buttonFont =
            new Font("Arial", Font.BOLD, 14);

    private final Font tableFont =
            new Font("Arial", Font.PLAIN, 14);

    private final String[] productColumns = {
        "Product ID",
        "Product Name",
        "Category",
        "Brand",
        "Quantity",
        "Price",
        "Minimum Stock",
        "Expiry Date"
    };

    private final String[] transactionColumns = {
        "Transaction ID",
        "Product ID",
        "Product Name",
        "Quantity",
        "Transaction Date"
    };

    public ReportsFrame() {

        reportDAO = new ReportDAO();

        setTitle("Inventory Reports");
        setSize(1200, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        createGUI();
    }

    private void createGUI() {

        setLayout(new BorderLayout(12, 12));

        // Report Selection Panel
        JPanel selectionPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.CENTER, 15, 15));

        selectionPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Report Selection"),
                        BorderFactory.createEmptyBorder(
                                8, 10, 8, 10)));

        JLabel lblReport = new JLabel("Select Report:");
        lblReport.setFont(labelFont);

        String[] reportOptions = {
            "Inventory Report",
            "Low-Stock Report",
            "Expiry Report",
            "Stock-In Report",
            "Stock-Out Report"
        };

        cmbReportType =
                new JComboBox<String>(reportOptions);

        cmbReportType.setFont(fieldFont);
        cmbReportType.setPreferredSize(
                new Dimension(240, 40));

        btnGenerate = createButton("Generate Report");
        btnClear = createButton("Clear");

        selectionPanel.add(lblReport);
        selectionPanel.add(cmbReportType);
        selectionPanel.add(btnGenerate);
        selectionPanel.add(btnClear);

        JPanel topWrapper = new JPanel(new BorderLayout());

        topWrapper.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 0, 15));

        topWrapper.add(
                selectionPanel,
                BorderLayout.CENTER);

        add(topWrapper, BorderLayout.NORTH);

        // Report Table
        tableModel = new DefaultTableModel(
                productColumns, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);

        reportTable.setFont(tableFont);
        reportTable.setRowHeight(30);
        reportTable.setAutoCreateRowSorter(true);

        JTableHeader header =
                reportTable.getTableHeader();

        header.setFont(
                new Font("Arial", Font.BOLD, 14));

        header.setPreferredSize(
                new Dimension(0, 38));

        JScrollPane scrollPane =
                new JScrollPane(reportTable);

        scrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Report Results"),
                        BorderFactory.createEmptyBorder(
                                5, 5, 5, 5)));

        JPanel tableWrapper = new JPanel(new BorderLayout());

        tableWrapper.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 15, 0, 15));

        tableWrapper.add(
                scrollPane,
                BorderLayout.CENTER);

        add(tableWrapper, BorderLayout.CENTER);

        // Bottom Status Message
        JLabel lblStatus = new JLabel(
                "Select a report type and click Generate Report.");

        lblStatus.setFont(
                new Font("Arial", Font.PLAIN, 13));

        lblStatus.setHorizontalAlignment(
                SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 10, 12, 10));

        bottomPanel.add(
                lblStatus,
                BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        btnGenerate.addActionListener(
                e -> generateReport());

        btnClear.addActionListener(
                e -> clearReport());
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(buttonFont);
        button.setPreferredSize(
                new Dimension(
                        text.equals("Generate Report") ? 165 : 100,
                        40));

        return button;
    }

    private void generateReport() {

        String reportType =
                cmbReportType.getSelectedItem().toString();

        tableModel.setRowCount(0);

        if (reportType.equals("Inventory Report")) {

            displayProductReport(
                    reportDAO.getInventoryReport());

        } else if (reportType.equals("Low-Stock Report")) {

            displayProductReport(
                    reportDAO.getLowStockReport());

        } else if (reportType.equals("Expiry Report")) {

            displayProductReport(
                    reportDAO.getExpiryReport());

        } else if (reportType.equals("Stock-In Report")) {

            displayTransactionReport(
                    reportDAO.getStockInReport());

        } else if (reportType.equals("Stock-Out Report")) {

            displayTransactionReport(
                    reportDAO.getStockOutReport());
        }

        if (tableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this, "No records found.");
        }
    }

    private void displayProductReport(
            List<Object[]> report) {

        tableModel.setColumnIdentifiers(productColumns);

        for (Object[] row : report) {
            tableModel.addRow(row);
        }
    }

    private void displayTransactionReport(
            List<Object[]> report) {

        tableModel.setColumnIdentifiers(transactionColumns);

        for (Object[] row : report) {
            tableModel.addRow(row);
        }
    }

    private void clearReport() {

        tableModel.setRowCount(0);
        cmbReportType.setSelectedIndex(0);
        tableModel.setColumnIdentifiers(productColumns);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ReportsFrame frame = new ReportsFrame();
            frame.setVisible(true);
        });
    }
}
