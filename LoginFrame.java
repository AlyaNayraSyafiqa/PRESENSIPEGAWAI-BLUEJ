import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login Admin");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (cekLogin(username, password)) {
                JOptionPane.showMessageDialog(this, "✅ Login berhasil!");
                dispose(); // Tutup login frame
                new Main(); // Buka menu utama
            } else {
                JOptionPane.showMessageDialog(this, "❌ Login gagal! Username/password salah.");
            }
        });

        add(panel);
        setVisible(true);
    }

    private boolean cekLogin(String username, String password) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM admin WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
