
package ui;

import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Login extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, registerButton;

    private String loggedInUsername;
    private String loggedInRole;

    public Login() {
        setTitle("Supermarket Inventory Management System");
        setSize(500, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 36));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(title, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(16);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setPreferredSize(new Dimension(220, 38));

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(16);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setPreferredSize(new Dimension(220, 38));

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setPreferredSize(new Dimension(140, 42));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        registerButton = new JButton("Register Staff");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(160, 38));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        add(panel);
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(
                    this, "Please enter username and password.");
            Arrays.fill(password, '\0');
            return;
        }

        try {
            UserDAO dao = new UserDAO();

            if (dao.authenticate(username, password)) {
                String role = dao.getUserRole(username);

                if (role == null ||
                        !(role.equalsIgnoreCase("ADMIN")
                        || role.equalsIgnoreCase("STAFF"))) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid user role. Contact the administrator.");
                    return;
                }

                loggedInUsername = username;
                loggedInRole = role.toUpperCase();

                JOptionPane.showMessageDialog(
                        this,
                        "Login successful!\nRole: " + loggedInRole);

                dispose();

                // Pass the authenticated user's details to the dashboard.
                new Dashboard(loggedInUsername, loggedInRole);

            } else {
                JOptionPane.showMessageDialog(
                        this, "Invalid username or password.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this, "Login failed: " + ex.getMessage());
        } finally {
            Arrays.fill(password, '\0');
            passwordField.setText("");
        }
    }

    private void register() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a username and password to register.");
            Arrays.fill(password, '\0');
            return;
        }

        try {
            UserDAO dao = new UserDAO();

            String adminUsername = JOptionPane.showInputDialog(
                    this, "Enter administrator username:");

            if (adminUsername == null
                    || adminUsername.trim().isEmpty()) {
                return;
            }

            JPasswordField adminPasswordField = new JPasswordField();

            int result = JOptionPane.showConfirmDialog(
                    this,
                    adminPasswordField,
                    "Enter administrator password",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            char[] adminPassword = adminPasswordField.getPassword();

            try {
                String verifiedAdmin = adminUsername.trim();

                if (!dao.authenticate(verifiedAdmin, adminPassword)
                        || !"ADMIN".equalsIgnoreCase(
                                dao.getUserRole(verifiedAdmin))) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Administrator authentication failed.");
                    return;
                }
            } finally {
                Arrays.fill(adminPassword, '\0');
                adminPasswordField.setText("");
            }

            if (dao.registerUser(username, password)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Staff account registered successfully!");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed. Check the username and password.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. The username may already exist "
                    + "or the database may be unavailable.");
        } finally {
            Arrays.fill(password, '\0');
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}