
package dao;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserDAO {

    private static final int ITERATIONS = 120000;

    // Convert byte array to hexadecimal string
    private String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(String.format("%02x", b & 0xff));
        }

        return result.toString();
    }

    // Convert hexadecimal string to byte array
    private byte[] fromHex(String hex) {
        int length = hex.length();
        byte[] result = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            result[i / 2] = (byte) Integer.parseInt(
                    hex.substring(i, i + 2), 16
            );
        }

        return result;
    }

    // Generate a random salt for password hashing
    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        return toHex(salt);
    }

    // Hash password using PBKDF2
    private String hashPassword(char[] password, String salt)
            throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                password,
                fromHex(salt),
                ITERATIONS,
                256
        );

        try {
            byte[] hash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

            return toHex(hash);

        } finally {
            spec.clearPassword();
        }
    }

    // Register a new user
    public boolean registerUser(String username, char[] password)
            throws Exception {

        if (username == null || username.trim().isEmpty()
                || password == null || password.length == 0) {
            return false;
        }

        String salt = generateSalt();
        String hash = hashPassword(password, salt);

        String sql = "INSERT INTO users "
                + "(username, password_hash, password_salt) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, hash);
            ps.setString(3, salt);

            return ps.executeUpdate() == 1;
        }
    }

    // Authenticate an existing user
    public boolean authenticate(String username, char[] password)
            throws Exception {

        if (username == null || username.trim().isEmpty()
                || password == null || password.length == 0) {
            return false;
        }

        String sql = "SELECT password_hash, password_salt "
                + "FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return false;
                }

                String storedHash = rs.getString("password_hash");
                String salt = rs.getString("password_salt");

                String enteredHash = hashPassword(password, salt);

                return MessageDigest.isEqual(
                        fromHex(storedHash),
                        fromHex(enteredHash)
                );
            }
        }
    }

    // Get the role of a user
    public String getUserRole(String username) throws Exception {

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT role FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        }

        return null;
    }
}