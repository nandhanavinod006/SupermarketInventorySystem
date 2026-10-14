package dao;

import database.DBConnection;
import model.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Add a new product
    public void addProduct(Product p) throws SQLException {

        String sql = "INSERT INTO products " +
                "(product_name, category, brand, quantity, unit_price, expiry_date, minimum_stock) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getProductName());
            ps.setString(2, p.getCategory());
            ps.setString(3, p.getBrand());
            ps.setInt(4, p.getQuantity());
            ps.setDouble(5, p.getUnitPrice());

            if (p.getExpiryDate() == null) {
                ps.setNull(6, Types.DATE);
            } else {
                ps.setDate(6, Date.valueOf(p.getExpiryDate()));
            }

            ps.setInt(7, p.getMinimumStock());

            ps.executeUpdate();
        }
    }

    // Update a product
    public void updateProduct(Product p) throws SQLException {

        String sql = "UPDATE products SET " +
                "product_name=?, category=?, brand=?, quantity=?, " +
                "unit_price=?, expiry_date=?, minimum_stock=? " +
                "WHERE product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getProductName());
            ps.setString(2, p.getCategory());
            ps.setString(3, p.getBrand());
            ps.setInt(4, p.getQuantity());
            ps.setDouble(5, p.getUnitPrice());

            if (p.getExpiryDate() == null) {
                ps.setNull(6, Types.DATE);
            } else {
                ps.setDate(6, Date.valueOf(p.getExpiryDate()));
            }

            ps.setInt(7, p.getMinimumStock());
            ps.setInt(8, p.getProductId());

            ps.executeUpdate();
        }
    }

    // Delete a product
    public void deleteProduct(int id) throws SQLException {

        String sql = "DELETE FROM products WHERE product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Get all products
    public List<Product> getAllProducts() throws SQLException {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products ORDER BY product_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                products.add(getProduct(rs));
            }
        }

        return products;
    }

    // Search products
    public List<Product> searchProducts(String searchBy, String keyword) {

        List<Product> products = new ArrayList<>();

        String sql = "";

        if (searchBy.equals("Product ID")) {
            sql = "SELECT * FROM products WHERE product_id = ?";
        }
        else if (searchBy.equals("Product Name")) {
            sql = "SELECT * FROM products WHERE product_name LIKE ?";
        }
        else if (searchBy.equals("Category")) {
            sql = "SELECT * FROM products WHERE category LIKE ?";
        }
        else if (searchBy.equals("Brand")) {
            sql = "SELECT * FROM products WHERE brand LIKE ?";
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (searchBy.equals("Product ID")) {
                ps.setInt(1, Integer.parseInt(keyword));
            } else {
                ps.setString(1, "%" + keyword + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(getProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // Stock In / Stock Out
    public void recordStock(int productId, String type, int amount)
            throws SQLException {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0.");
        }

        String update;

        if (type.equals("IN")) {
            update = "UPDATE products " +
                    "SET quantity = quantity + ? " +
                    "WHERE product_id=?";
        } else {
            update = "UPDATE products " +
                    "SET quantity = quantity - ? " +
                    "WHERE product_id=? AND quantity >= ?";
        }

        String insert =
                "INSERT INTO stock_transactions " +
                "(product_id, transaction_type, quantity) " +
                "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps =
                    con.prepareStatement(update)) {

                ps.setInt(1, amount);
                ps.setInt(2, productId);

                if (type.equals("OUT")) {
                    ps.setInt(3, amount);
                }

                int result = ps.executeUpdate();

                if (result == 0) {
                    throw new IllegalArgumentException(
                            "Product not found or insufficient stock.");
                }
            }

            try (PreparedStatement ps =
                    con.prepareStatement(insert)) {

                ps.setInt(1, productId);
                ps.setString(2, type);
                ps.setInt(3, amount);

                ps.executeUpdate();
            }

            con.commit();
        }
    }

    // Convert database row into Product object
    private Product getProduct(ResultSet rs) throws SQLException {

        Date date = rs.getDate("expiry_date");

        LocalDate expiryDate = null;

        if (date != null) {
            expiryDate = date.toLocalDate();
        }

        return new Product(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                rs.getString("category"),
                rs.getString("brand"),
                rs.getInt("quantity"),
                rs.getDouble("unit_price"),
                expiryDate,
                rs.getInt("minimum_stock")
        );
    }
}