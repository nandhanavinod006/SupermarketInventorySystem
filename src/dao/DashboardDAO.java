
package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public int getTotalProducts() throws SQLException {
        return getCount("SELECT COUNT(*) FROM products");
    }

    public int getLowStockCount() throws SQLException {
        return getCount(
            "SELECT COUNT(*) FROM products " +
            "WHERE quantity <= minimum_stock"
        );
    }

    public int getExpiringSoonCount() throws SQLException {
        return getCount(
            "SELECT COUNT(*) FROM products " +
            "WHERE expiry_date >= CURDATE() " +
            "AND expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)"
        );
    }

    public int getTotalSuppliers() throws SQLException {
        return getCount("SELECT COUNT(*) FROM suppliers");
    }

    private int getCount(String sql) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}