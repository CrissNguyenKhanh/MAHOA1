package BaiTap;

import javax.swing.*;

public class WelcomeForm extends JFrame {
    public WelcomeForm(String username) {
        setTitle("Welcome");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setBounds(20, 50, 200, 25);
        add(welcomeLabel);

        setVisible(true);
    }
}
