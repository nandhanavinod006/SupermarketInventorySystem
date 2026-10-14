package dao;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StockDAO {

    // Stock IN
    public boolean addStockIn(int productId, int quantity) {

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return false;
        }

        String checkProduct =
                "SELECT product_id FROM products WHERE product_id = ?";

        String insertTransaction =
                "INSERT INTO stock_transactions " +
                "(product_id, transaction_type, quantity) " +
                "VALUES (?, 'IN', ?)";

        String updateProduct =
                "UPDATE products SET quantity = quantity + ? " +
                "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            // Check whether product exists
            PreparedStatement psCheck =
                    con.prepareStatement(checkProduct);

            psCheck.setInt(1, productId);

            ResultSet rs = psCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("Product not found!");
                con.rollback();
                return false;
            }

            // Record Stock IN
            PreparedStatement ps1 =
                    con.prepareStatement(insertTransaction);

            ps1.setInt(1, productId);
            ps1.setInt(2, quantity);
            ps1.executeUpdate();

            // Increase product quantity
            PreparedStatement ps2 =
                    con.prepareStatement(updateProduct);

            ps2.setInt(1, quantity);
            ps2.setInt(2, productId);
            ps2.executeUpdate();

            con.commit();

            System.out.println("Stock IN successful!");
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }


    // Stock OUT
    public boolean addStockOut(int productId, int quantity) {

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return false;
        }

        String checkStock =
                "SELECT quantity FROM products WHERE product_id = ?";

        String insertTransaction =
                "INSERT INTO stock_transactions " +
                "(product_id, transaction_type, quantity) " +
                "VALUES (?, 'OUT', ?)";

        String updateProduct =
                "UPDATE products SET quantity = quantity - ? " +
                "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            // Check available stock
            PreparedStatement psCheck =
                    con.prepareStatement(checkStock);

            psCheck.setInt(1, productId);

            ResultSet rs = psCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("Product not found!");
                con.rollback();
                return false;
            }

            int currentStock = rs.getInt("quantity");

            // Prevent negative stock
            if (quantity > currentStock) {
                System.out.println("Not enough stock!");
                con.rollback();
                return false;
            }

            // Record Stock OUT
            PreparedStatement ps1 =
                    con.prepareStatement(insertTransaction);

            ps1.setInt(1, productId);
            ps1.setInt(2, quantity);
            ps1.executeUpdate();

            // Decrease product quantity
            PreparedStatement ps2 =
                    con.prepareStatement(updateProduct);

            ps2.setInt(1, quantity);
            ps2.setInt(2, productId);
            ps2.executeUpdate();

            con.commit();

            System.out.println("Stock OUT successful!");
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}