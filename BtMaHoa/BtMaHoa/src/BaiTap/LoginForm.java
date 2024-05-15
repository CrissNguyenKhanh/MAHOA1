package BaiTap;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 20, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 160, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 60, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 160, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 100, 80, 25);
        add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(190, 100, 90, 25);
        add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        setVisible(true);
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Mã hoá mật khẩu trước khi lưu vào cơ sở dữ liệu
        String hashedPassword = hashPassword(password);

        // Lưu thông tin đăng ký vào cơ sở dữ liệu
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=UserDB;user=sa;password=12345;encrypt=true;trustServerCertificate=true");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO [user] (username, password) VALUES (?, ?)");

            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration successful!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registration failed!");
        }
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Mã hoá mật khẩu để so sánh với mật khẩu đã lưu trong cơ sở dữ liệu
        String hashedPassword = hashPassword(password);

        // Kiểm tra đăng nhập từ cơ sở dữ liệu
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=UserDB;user=sa;password=12345;encrypt=true;trustServerCertificate=true");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM [user] WHERE username = ? AND password = ?");

            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                // Hiển thị giao diện welcome ở đây
                new WelcomeForm(username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed!");
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
