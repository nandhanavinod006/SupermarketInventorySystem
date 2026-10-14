package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import model.Supplier;

public class SupplierDAO {

    public boolean addSupplier(Supplier supplier) {

        String sql = "INSERT INTO suppliers "
                   + "(supplier_name, phone, email, address) "
                   + "VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, supplier.getSupplierName());
            statement.setString(2, supplier.getPhone());
            statement.setString(3, supplier.getEmail());
            statement.setString(4, supplier.getAddress());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Supplier> getAllSuppliers() {

        List<Supplier> suppliers =
                new ArrayList<Supplier>();

        String sql = "SELECT * FROM suppliers";

        try {
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Supplier supplier =
                        new Supplier();

                supplier.setSupplierId(
                        resultSet.getInt("supplier_id"));

                supplier.setSupplierName(
                        resultSet.getString("supplier_name"));

                supplier.setPhone(
                        resultSet.getString("phone"));

                supplier.setEmail(
                        resultSet.getString("email"));

                supplier.setAddress(
                        resultSet.getString("address"));

                suppliers.add(supplier);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    public boolean updateSupplier(Supplier supplier) {

        String sql = "UPDATE suppliers SET "
                   + "supplier_name = ?, "
                   + "phone = ?, "
                   + "email = ?, "
                   + "address = ? "
                   + "WHERE supplier_id = ?";

        try {
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(
                    1, supplier.getSupplierName());

            statement.setString(
                    2, supplier.getPhone());

            statement.setString(
                    3, supplier.getEmail());

            statement.setString(
                    4, supplier.getAddress());

            statement.setInt(
                    5, supplier.getSupplierId());

            int rows =
                    statement.executeUpdate();

            statement.close();
            connection.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSupplier(int supplierId) {

        String sql =
                "DELETE FROM suppliers WHERE supplier_id = ?";

        try {
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, supplierId);

            int rows =
                    statement.executeUpdate();

            statement.close();
            connection.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Supplier> searchSuppliers(String keyword) {

        List<Supplier> suppliers =
                new ArrayList<Supplier>();

        String sql =
                "SELECT * FROM suppliers "
                + "WHERE supplier_name LIKE ? "
                + "OR phone LIKE ? "
                + "OR email LIKE ?";

        try {
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            String searchText =
                    "%" + keyword + "%";

            statement.setString(
                    1, searchText);

            statement.setString(
                    2, searchText);

            statement.setString(
                    3, searchText);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Supplier supplier =
                        new Supplier();

                supplier.setSupplierId(
                        resultSet.getInt("supplier_id"));

                supplier.setSupplierName(
                        resultSet.getString("supplier_name"));

                supplier.setPhone(
                        resultSet.getString("phone"));

                supplier.setEmail(
                        resultSet.getString("email"));

                supplier.setAddress(
                        resultSet.getString("address"));

                suppliers.add(supplier);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return suppliers;
    }
}