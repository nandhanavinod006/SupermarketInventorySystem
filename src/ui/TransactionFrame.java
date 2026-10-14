package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;

public class TransactionFrame extends JFrame {

    JTable transactionTable;
    DefaultTableModel tableModel;

    public TransactionFrame() {

        setTitle("Stock Transaction History");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {
            "Transaction ID",
            "Product ID",
            "Type",
            "Quantity",
            "Date"
        };

        tableModel = new DefaultTableModel(columns, 0);

        transactionTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(transactionTable);

        add(scrollPane);

        loadTransactions();

        setVisible(true);
    }

    private void loadTransactions() {

        String sql =
                "SELECT transaction_id, product_id, " +
                "transaction_type, quantity, transaction_date " +
                "FROM stock_transactions " +
                "ORDER BY transaction_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int transactionId =
                        rs.getInt("transaction_id");

                int productId =
                        rs.getInt("product_id");

                String type =
                        rs.getString("transaction_type");

                int quantity =
                        rs.getInt("quantity");

                String date =
                        rs.getString("transaction_date");

                tableModel.addRow(new Object[]{
                    transactionId,
                    productId,
                    type,
                    quantity,
                    date
                });
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load transactions."
            );
        }
    }

    public static void main(String[] args) {

        new TransactionFrame();
    }
}