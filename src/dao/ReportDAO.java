package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;

public class ReportDAO {

    public List<Object[]> getInventoryReport() {

        List<Object[]> report =
                new ArrayList<Object[]>();

        String sql =
                "SELECT product_id, product_name, category, "
                + "brand, quantity, unit_price, "
                + "minimum_stock, expiry_date "
                + "FROM products";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Object[] row = {
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getString("category"),
                    resultSet.getString("brand"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getInt("minimum_stock"),
                    resultSet.getString("expiry_date")
                };

                report.add(row);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return report;
    }

    public List<Object[]> getLowStockReport() {

        List<Object[]> report =
                new ArrayList<Object[]>();

        String sql =
                "SELECT product_id, product_name, category, "
                + "brand, quantity, unit_price, "
                + "minimum_stock, expiry_date "
                + "FROM products "
                + "WHERE quantity <= minimum_stock";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Object[] row = {
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getString("category"),
                    resultSet.getString("brand"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getInt("minimum_stock"),
                    resultSet.getString("expiry_date")
                };

                report.add(row);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return report;
    }

    public List<Object[]> getExpiryReport() {

        List<Object[]> report =
                new ArrayList<Object[]>();

        String sql =
                "SELECT product_id, product_name, category, "
                + "brand, quantity, unit_price, "
                + "minimum_stock, expiry_date "
                + "FROM products "
                + "ORDER BY expiry_date";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Object[] row = {
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getString("category"),
                    resultSet.getString("brand"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getInt("minimum_stock"),
                    resultSet.getString("expiry_date")
                };

                report.add(row);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return report;
    }

    public List<Object[]> getStockInReport() {

        List<Object[]> report =
                new ArrayList<Object[]>();

        String sql =
                "SELECT st.transaction_id, "
                + "st.product_id, "
                + "p.product_name, "
                + "st.quantity, "
                + "st.transaction_date "
                + "FROM stock_transactions st "
                + "JOIN products p "
                + "ON st.product_id = p.product_id "
                + "WHERE st.transaction_type = 'IN' "
                + "ORDER BY st.transaction_date DESC";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Object[] row = {
                    resultSet.getInt("transaction_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("transaction_date")
                };

                report.add(row);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return report;
    }

    public List<Object[]> getStockOutReport() {

        List<Object[]> report =
                new ArrayList<Object[]>();

        String sql =
                "SELECT st.transaction_id, "
                + "st.product_id, "
                + "p.product_name, "
                + "st.quantity, "
                + "st.transaction_date "
                + "FROM stock_transactions st "
                + "JOIN products p "
                + "ON st.product_id = p.product_id "
                + "WHERE st.transaction_type = 'OUT' "
                + "ORDER BY st.transaction_date DESC";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Object[] row = {
                    resultSet.getInt("transaction_id"),
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("transaction_date")
                };

                report.add(row);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return report;
    }
}